package com.gzyslczx.yslc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.gzyslczx.yslc.adapters.option.ManageOptionListAdapter;
import com.gzyslczx.yslc.databinding.ActivityManagerOptionBinding;
import com.gzyslczx.yslc.events.GuBbChangeOptionStateEvent;
import com.gzyslczx.yslc.events.OptionDragEvent;
import com.gzyslczx.yslc.events.OptionSetTopEvent;
import com.gzyslczx.yslc.modes.response.ResMyOptionObj;
import com.gzyslczx.yslc.presenter.OptionPresenter;
import com.gzyslczx.yslc.tools.DragSwipeListTool;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.OnItemTouchCallbackListener;
import com.gzyslczx.yslc.tools.TransStatusTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ManagerOptionActivity extends BaseActivity<ActivityManagerOptionBinding> implements View.OnClickListener, OnItemClickListener,
        OnItemChildClickListener {

    private final String TAG = "ManageOptionAct";
    private OptionPresenter mPresenter;
    private ManageOptionListAdapter mListAdapter;
    public static final String ListKey = "OptionList";
    private List<ResMyOptionObj> dataList;
    private ItemTouchHelper itemTouchHelper;
    private DragSwipeListTool dragSwipeListTool;
    private int topPosition = 0;
    private HashSet<ResMyOptionObj> mDragSet = new HashSet<ResMyOptionObj>();
    private HashSet<ResMyOptionObj> mSelectSet = new HashSet<ResMyOptionObj>();
    private AlertDialog mDialog;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityManagerOptionBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.white));
        TransStatusTool.setStatusBarLightMode(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        dataList = (List<ResMyOptionObj>) getIntent().getSerializableExtra(ListKey);
        mViewBinding.ManageOpList.setLayoutManager(new LinearLayoutManager(this));
        mListAdapter = new ManageOptionListAdapter(R.layout.manage_option_list_item, dataList);
        mListAdapter.setOnItemClickListener(this::onItemClick);
        mListAdapter.setOnItemChildClickListener(this::onItemChildClick);
        mViewBinding.ManageOpList.setAdapter(mListAdapter);
        SetupDragAction();
        SetupBackClick();
        SetAllSelectListener();
        mViewBinding.ManageOpFinish.setOnClickListener(this::onClick);
        mViewBinding.ManageOpDelete.setOnClickListener(this::onClick);
        mPresenter = new OptionPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /*
     * 回退点击
     * */
    private void SetupBackClick() {
        mViewBinding.ManageOpToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /*
     * 设置拖动监听
     * */
    private void SetupDragAction(){
        dragSwipeListTool = new DragSwipeListTool(new OnItemTouchCallbackListener() {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPos = viewHolder.getAdapterPosition();
                int toPos = target.getAdapterPosition();
                if ((fromPos<0) || (fromPos==toPos) || (toPos>mListAdapter.getData().size()-1)){
                    return false;
                }
                mListAdapter.notifyItemMoved(fromPos, toPos);
                Collections.swap(mListAdapter.getData(), fromPos, toPos);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void onFinishMove(int EndPos) {
                if (EndPos==0){
                    mListAdapter.getData().get(0).setTop(true);
                    mListAdapter.getData().get(1).setTop(false);
                }
                Log.d("MyOptionFrag", String.format("拖动了:%s", new Gson().toJson(mListAdapter.getData().get(EndPos))));
                mDragSet.add(mListAdapter.getData().get(EndPos));
            }
        });
        dragSwipeListTool.setCanSwipe(false);
        itemTouchHelper = new ItemTouchHelper(dragSwipeListTool);
        itemTouchHelper.attachToRecyclerView(mViewBinding.ManageOpList);
    }

    /*
     * 设置全选点击监听
     * */
    private void SetAllSelectListener(){
        mViewBinding.ManageOpAllCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    for (int i=0; i<dataList.size(); i++){
                        dataList.get(i).setSelected(true);
                        mListAdapter.notifyItemChanged(i);
                        mSelectSet.add(dataList.get(i));
                    }
                    mViewBinding.ManageOpSelectedNum.setText(String.valueOf(mListAdapter.getData().size()));
                }else {
                    for (int i=0; i<dataList.size(); i++){
                        dataList.get(i).setSelected(false);
                        mListAdapter.notifyItemChanged(i);
                        mSelectSet.remove(dataList.get(i));
                    }
                    mViewBinding.ManageOpSelectedNum.setText("0");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ManageOpFinish){
            //点击完成
            if (mDragSet.size()>0) {
                List<String> codes = new ArrayList<String>();
                List<Integer> pos = new ArrayList<Integer>();
                for (ResMyOptionObj obj : mDragSet) {
                    codes.add(obj.getStockCode());
                    pos.add(mListAdapter.getItemPosition(obj));
                }
                if (mDialog==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(false);
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mPresenter.RequestDragMyOption(ManagerOptionActivity.this, codes, pos, ManagerOptionActivity.this);
                            dialogInterface.dismiss();
                            mViewBinding.ManageLoad.setVisibility(View.VISIBLE);
                        }
                    });
                    mDialog = builder.create();
                }
                mDialog.setMessage("确定保存当前设置吗");
                mDialog.show();
            }else {
                finish();
            }
        }
        if (view.getId() == R.id.ManageOpDelete){
            int num = Integer.parseInt(mViewBinding.ManageOpSelectedNum.getText().toString());
            if (num > 0){
                String[] codes = new String[num];
                int i =0;
                for (ResMyOptionObj obj : mSelectSet){
                    codes[i] = obj.getStockCode();
                    i++;
                }
                if (mDialog==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(false);
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NormalActionTool.DeleteOptionAction(ManagerOptionActivity.this, null, ManagerOptionActivity.this,
                                    codes, TAG);
                            dialogInterface.dismiss();
                            mViewBinding.ManageLoad.setVisibility(View.VISIBLE);
                        }
                    });
                    mDialog = builder.create();
                }
                mDialog.setMessage("确定删除所选项吗");
                mDialog.show();
            }
        }
    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        Log.d(TAG, "点击了置顶");
        if (view.getId() == R.id.ManageOpSetTopImg && position>0) {
            mPresenter.RequestSetTopMyOption(this, mListAdapter.getData().get(position).getStockCode(), null, this);
            topPosition = position;
        }
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        boolean state = mListAdapter.getData().get(position).ChangeSelect();
        mListAdapter.notifyItemChanged(position);
        if (state){
            //选上
            mSelectSet.add(mListAdapter.getData().get(position));
            int num = Integer.parseInt(mViewBinding.ManageOpSelectedNum.getText().toString());
            mViewBinding.ManageOpSelectedNum.setText(String.valueOf(num+1));
        }else {
            //取消
            mSelectSet.remove(mListAdapter.getData().get(position));
            int num = Integer.parseInt(mViewBinding.ManageOpSelectedNum.getText().toString());
            mViewBinding.ManageOpSelectedNum.setText(String.valueOf(num-1));
        }
    }

    /*
    *接收置顶返回结果
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnOptionSetTopEvent(OptionSetTopEvent event){
        Log.d(TAG, "接收到置顶返回结果");
        if (event.isFlag()){
            mListAdapter.notifyItemMoved(topPosition, 0);
            mListAdapter.getData().get(topPosition).setTop(true);
            mListAdapter.getData().get(0).setTop(false);
            Collections.sort(mListAdapter.getData());
            mListAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(this, event.getError(), Toast.LENGTH_SHORT).show();
        }
        mViewBinding.ManageLoad.setVisibility(View.GONE);
    }
    /*
     *接收拖动返回结果
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnOptionDragEvent(OptionDragEvent event){
        Log.d(TAG, "接收到拖动返回结果:"+new Gson().toJson(event));
        if (event.isFlag()){
            mViewBinding.ManageLoad.setVisibility(View.GONE);
            mDragSet.clear();
        }else {
            Toast.makeText(this, event.getError(), Toast.LENGTH_SHORT).show();
            mViewBinding.ManageLoad.setVisibility(View.GONE);
        }
    }
    /*
     *接收删除返回结果
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnOptionDeleteEvent(GuBbChangeOptionStateEvent event){
        Log.d(TAG, "接收到删除返回结果"+new Gson().toJson(event));
        if (event.isFlag() && !event.isAddOption()){
            for (ResMyOptionObj obj : mSelectSet) {
                int position = mListAdapter.getItemPosition(obj);
                mListAdapter.getData().remove(obj);
                mListAdapter.notifyItemRemoved(position);
                if (mDragSet.contains(obj)) {
                    mDragSet.remove(obj);
                }
            }
            mSelectSet.clear();
            mViewBinding.ManageOpSelectedNum.setText("0");
            mViewBinding.ManageOpAllCheck.setChecked(false);
            mViewBinding.ManageLoad.setVisibility(View.GONE);
        }else if (!event.isFlag() && !event.isAddOption()){
            Toast.makeText(this, event.getError(), Toast.LENGTH_SHORT).show();
            mViewBinding.ManageLoad.setVisibility(View.GONE);
        }
    }


}
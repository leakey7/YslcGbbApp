package com.gzyslczx.yslc;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.gzyslczx.yslc.adapters.fundtong.ConceptFundListAdapter;
import com.gzyslczx.yslc.adapters.fundtong.ConceptWordListAdapter;
import com.gzyslczx.yslc.adapters.fundtong.bean.ConceptSelectData;
import com.gzyslczx.yslc.databinding.ActivityConceptSelectorBinding;
import com.gzyslczx.yslc.events.FundConceptSelectListEvent;
import com.gzyslczx.yslc.events.NoticeConceptUpdateEvent;
import com.gzyslczx.yslc.presenter.FundConceptSelectPres;
import com.gzyslczx.yslc.tools.TransStatusTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class ConceptSelectorActivity extends BaseActivity<ActivityConceptSelectorBinding> implements OnItemChildClickListener, OnItemClickListener {

    private final String TAG = "ConceptSelectAct";
    private FundConceptSelectPres mPresenter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private ConceptFundListAdapter mListAdapter;
    private ConceptWordListAdapter mWordAdapter;
    private int LastWordPos=-1;
    private boolean isClickWord=false;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityConceptSelectorBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.white));
        TransStatusTool.setStatusBarLightMode(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        gridLayoutManager = new GridLayoutManager(this, 3);
        mViewBinding.CSelectorDataList.setLayoutManager(gridLayoutManager);
        mListAdapter = new ConceptFundListAdapter(R.layout.concept_select_left_head_item, new ArrayList<ConceptSelectData>());
        mListAdapter.setOnItemChildClickListener(this::onItemChildClick);
        mViewBinding.CSelectorDataList.setAdapter(mListAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        mViewBinding.CSelectorKeyList.setLayoutManager(linearLayoutManager);
        mWordAdapter = new ConceptWordListAdapter(R.layout.concept_select_word_item);
        mWordAdapter.setOnItemClickListener(this::onItemClick);
        mViewBinding.CSelectorKeyList.setAdapter(mWordAdapter);
        SetupBackClicked();
        SetupScrollListener();
        SetupConfirmClicked();
        mPresenter = new FundConceptSelectPres();
        mPresenter.RequestConceptSelectList(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /*
     * 回退点击
     * */
    private void SetupBackClicked(){
        mViewBinding.CSelectorToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        Log.d(TAG, String.format("选中了：%d;信息：%s", position, new Gson().toJson(mListAdapter.getData().get(position))));
        if (!mListAdapter.getData().get(position).isHeader()) {
            boolean select = mListAdapter.getData().get(position).isSelect();
            if (select) {
                LastWordPos = -1;
                mListAdapter.getData().get(position).setSelect(false);
                mViewBinding.CSelectorToolConfirm.setBackgroundColor(ActivityCompat.getColor(this, R.color.gray_eee));
                mViewBinding.CSelectorToolConfirm.setTag("un");
            } else {
                mListAdapter.getData().get(position).setSelect(true);
                mViewBinding.CSelectorToolConfirm.setBackgroundColor(ActivityCompat.getColor(this, R.color.orange_FF753E));
                mViewBinding.CSelectorToolConfirm.setTag("is");
                if (LastWordPos > 0) {
                    mListAdapter.getData().get(LastWordPos).setSelect(false);
                }
                LastWordPos = position;
            }
            mListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        isClickWord = true;
        mWordAdapter.SetSelectWord(mWordAdapter.getData().get(position));
        mWordAdapter.notifyDataSetChanged();
        for (int i=0; i<mListAdapter.getData().size(); i++){
            if (mListAdapter.getData().get(i).isHeader()){
                if (mListAdapter.getData().get(i).getWord().equals(mWordAdapter.getData().get(position))) {
                    mViewBinding.CSelectorDataList.scrollToPosition(i);
                    break;
                }
            }
        }
    }

    /*
     * 设置滑动监听
     * */
    private void SetupScrollListener(){
        mViewBinding.CSelectorDataList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isClickWord){
                    isClickWord = false;
                }else {
                    int pos = gridLayoutManager.findFirstVisibleItemPosition();
                    mWordAdapter.SetSelectWord(mListAdapter.getData().get(pos).getWord());
                    mWordAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /*
     * 设置确定点击
     * */
    private void SetupConfirmClicked(){
        mViewBinding.CSelectorToolConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewBinding.CSelectorToolConfirm.getTag().equals("is")){
                    if (LastWordPos>0){
                        NoticeConceptUpdateEvent event = new NoticeConceptUpdateEvent(mListAdapter.getData().get(LastWordPos).getCode());
                        EventBus.getDefault().post(event);
                        finish();
                    }
                }else {
                    Toast.makeText(ConceptSelectorActivity.this, "请选择概念基金", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
    * 接收筛选表
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnConceptSelectEvent(FundConceptSelectListEvent event){
        if (event.isFlag()){
            mWordAdapter.SetSelectWord(event.getWordList().get(0));
            mWordAdapter.setList(event.getWordList());
            mListAdapter.setList(event.getDataList());
        }else {
            Toast.makeText(this, event.getError(), Toast.LENGTH_SHORT).show();
        }
    }

}
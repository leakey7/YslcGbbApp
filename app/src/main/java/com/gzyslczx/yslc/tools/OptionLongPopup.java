package com.gzyslczx.yslc.tools;

import android.view.View;
import android.widget.PopupWindow;

import com.gzyslczx.yslc.databinding.LongClickOptionBinding;

public class OptionLongPopup extends PopupWindow {

    private LongClickOptionBinding mViewBinding;
    private int SelectPosition = -1;
    Action action = Action.NORMAL;
    private OnOptionClickedAction clickedAction;

    public enum Action {
        DELETED,
        SETTOP,
        SETMANAGER,
        NORMAL,
    }

    public OptionLongPopup(View contentView, int width, int height) {
        super(contentView, width, height);
        mViewBinding = LongClickOptionBinding.bind(contentView);
        setContentView(mViewBinding.getRoot());
        setFocusable(true);
        setOutsideTouchable(true);
        SetupDeleteOptionClick();
        SetupTopOptionClick();
        SetupManagerOptionClick();
    }

    public void setClickedAction(OnOptionClickedAction clickedAction) {
        this.clickedAction = clickedAction;
    }

    /*
     * 设置删除自选点击
     * */
    private void SetupDeleteOptionClick() {
        mViewBinding.OptionDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickedAction != null && SelectPosition >= 0) {
                    clickedAction.OnSelectOptionAction(Action.DELETED, SelectPosition);
                }
                dismiss();
            }
        });
    }

    /*
     * 设置置顶点击
     * */
    private void SetupTopOptionClick() {
        mViewBinding.OptionSetTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickedAction != null && SelectPosition > 0) {
                    clickedAction.OnSelectOptionAction(Action.SETTOP, SelectPosition);
                }
                dismiss();
            }
        });
    }

    /*
     * 设置编辑管理点击
     * */
    private void SetupManagerOptionClick() {
        mViewBinding.OptionSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickedAction != null && SelectPosition >= 0) {
                    clickedAction.OnSelectOptionAction(Action.SETMANAGER, SelectPosition);
                }
                dismiss();
            }
        });
    }

    public void setSelectPosition(int selectPosition) {
        SelectPosition = selectPosition;
    }

    public int getSelectPosition() {
        return SelectPosition;
    }

    public void SetNormalAction() {
        this.action = Action.NORMAL;
    }

}

package com.gzyslczx.youruismapp.presenters;

import android.content.Context;

import com.gzyslczx.youruismapp.BaseActivity;
import com.gzyslczx.youruismapp.events.UpdateYRTokenEvent;
import com.gzyslczx.youruismapp.fragments.BaseFragment;
import com.gzyslczx.youruismapp.tools.BaseTool;
import com.gzyslczx.youruismapp.tools.NetWork.YrNetWorkTool;
import com.gzyslczx.youruismapp.tools.SPTool;
import com.gzyslczx.youruismapp.tools.TimeTool;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BasePresenter extends BaseTool {

    /*
     * Activity中检查友睿权限
     * */
    public boolean CheckYRTokenOnActivity(BaseActivity baseActivity, String tag) {
        if (SPTool.init(baseActivity).GetInfo(SPTool.SpToken).equals(SPTool.TAG)) {
            //Token为空
            PrintLogE(tag, "友睿Token为空");
            return false;
        } else {
            //Token非空,检查时限
            if (CheckTokenTime(baseActivity)){
                //无效,申请更新Token
                PrintLogE(tag, "友睿Token失效");
                return false;
            }else {
                //有效，无需更新Token
                PrintLogE(tag, "友睿Token有效");
                EventBus.getDefault().post(new UpdateYRTokenEvent(true, SPTool.init(baseActivity).GetInfo(SPTool.SpToken)));
                return true;
            }
        }
    }

    /*
     * Fragment中检查友睿权限
     * */
    public boolean CheckYRTokenOnFragment(BaseFragment baseFragment, String tag) {
        if (SPTool.init(baseFragment.getContext()).GetInfo(SPTool.SpToken).equals(SPTool.TAG)) {
            //未存储Token
            PrintLogE(tag, "友睿Token未空");
            return false;
        } else {
            //已存储Token,检查时限
            if (CheckTokenTime(baseFragment.getContext())){
                //时限无效,更新Token
                PrintLogE(tag, "友睿Token失效");
                return false;
            }else {
                //时限有效，无需更新Token
                PrintLogE(tag, "友睿Token有效");
                return true;
            }
        }
    }

    /*
     * 检查友睿Token时限
     * */
    private boolean CheckTokenTime(Context context) {
        try {
            return new Date().after(new SimpleDateFormat(TimeTool.Ymd).parse(SPTool.init(context).GetInfo(SPTool.SpTokenTime)));
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
    }

}

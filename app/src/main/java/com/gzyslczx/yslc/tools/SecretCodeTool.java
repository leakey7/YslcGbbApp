package com.gzyslczx.yslc.tools;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.gzyslczx.yslc.BigVideoActivity;
import com.gzyslczx.yslc.FundTongActivity;
import com.gzyslczx.yslc.HomeActivity;
import com.gzyslczx.yslc.KLineActivity;
import com.gzyslczx.yslc.LabelArtActivity;
import com.gzyslczx.yslc.LabelSelfActivity;
import com.gzyslczx.yslc.SpecialSupActivity;
import com.gzyslczx.yslc.SpecialSupDetailActivity;
import com.gzyslczx.yslc.TeacherAllActivity;
import com.gzyslczx.yslc.TeacherArtActivity;
import com.gzyslczx.yslc.WebActivity;
import com.gzyslczx.yslc.events.GuBbHomeChangePageEvent;
import com.gzyslczx.yslc.events.GuBbVipPushEvent;

import org.greenrobot.eventbus.EventBus;

public class SecretCodeTool {

    public static Intent AnalysisSecretCode(Context context, String SecretCode){
        Intent intent=null;
        if (!TextUtils.isEmpty(SecretCode)){
            if (SecretCode.equals("cheats/")){
                //K线秘籍
                intent = new Intent(context, KLineActivity.class);
                return intent;
            }
            if ((SecretCode.substring(0, SecretCode.indexOf('/'))).equals("prog")){
                //栏目
                intent = new Intent(context, LabelSelfActivity.class);
                intent.putExtra(LabelSelfActivity.LNameKey, SecretCode.substring(SecretCode.indexOf('/')+1));
                return intent;
            }
            if (SecretCode.substring(0, SecretCode.indexOf("/")).equals("arti")){
                //栏目文章
                intent = new Intent(context, LabelArtActivity.class);
                intent.putExtra(LabelArtActivity.LabelArtKey, SecretCode.substring(SecretCode.indexOf("/")+1));
                return intent;
            }
            if (SecretCode.substring(0, SecretCode.indexOf("/")).equals("teacherAll")){
                //名师专栏
                intent = new Intent(context, TeacherAllActivity.class);
                return intent;
            }
            if (SecretCode.equals("/jjt")){
                //基金通
                intent = new Intent(context, FundTongActivity.class);
                return intent;

            }
            if (SecretCode.substring(0, 4).equals("http")){
                //WEB页
                intent = new Intent(context, WebActivity.class);
                intent.putExtra(WebActivity.WebKey, SecretCode);
                return intent;
            }
            if (SecretCode.substring(0, SecretCode.indexOf("/")).equals("shipin")){
                //宽屏视频
                intent = new Intent(context, BigVideoActivity.class);
                intent.putExtra(BigVideoActivity.BidVideoKey, SecretCode.substring(SecretCode.indexOf("/")+1));
                return intent;
            }
            if (SecretCode.substring(0, SecretCode.indexOf("/")).equals("tarti")){
                //名师文章
                intent = new Intent(context, TeacherArtActivity.class);
                intent.putExtra(TeacherArtActivity.TeacherArtKey, SecretCode.substring(SecretCode.indexOf("/")+1));
                return intent;
            }
            if (SecretCode.substring(0, SecretCode.indexOf("/")).equals("web")){
                //WEB页
                intent = new Intent(context, WebActivity.class);
                intent.putExtra(WebActivity.WebKey, SecretCode.substring(SecretCode.indexOf("/")+1));
                return intent;
            }
            if (SecretCode.substring(0, SecretCode.indexOf("/")).equals("VIP")){
                //VIP页
                intent = new Intent(context, HomeActivity.class);
                GuBbHomeChangePageEvent event = new GuBbHomeChangePageEvent(2);
                EventBus.getDefault().post(event);
                GuBbVipPushEvent vipPushEvent = new GuBbVipPushEvent(SecretCode.substring(SecretCode.indexOf('/')+1));
                EventBus.getDefault().postSticky(vipPushEvent);
                return intent;
            }
            if (SecretCode.substring(0, SecretCode.indexOf("/")).equals("SupMain")){
                //盘前特供首页
                intent = new Intent(context, SpecialSupActivity.class);
                intent.putExtra(SpecialSupActivity.SupportDate, SecretCode.substring(SecretCode.indexOf("/")+1));
                return intent;
            }
            if (SecretCode.substring(0, SecretCode.indexOf("/")).equals("SupDetail")){
                //盘前特供详情
                intent = new Intent(context, SpecialSupDetailActivity.class);
                intent.putExtra(SpecialSupDetailActivity.SpecialSupKey, SecretCode.substring(SecretCode.indexOf("/")+1));
                return intent;
            }
        }
        return intent;
    }

}

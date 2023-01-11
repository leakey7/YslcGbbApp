package com.gzyslczx.youruismapp.tools.NetWork;

import android.util.Log;

import com.gzyslczx.youruismapp.BaseActivity;
import com.gzyslczx.youruismapp.fragments.BaseFragment;
import com.gzyslczx.youruismapp.requestes.TokenReqBody;
import com.gzyslczx.youruismapp.responses.TokenRes;
import com.gzyslczx.youruismapp.tools.BaseTool;
import com.gzyslczx.youruismapp.tools.SPTool;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class YrNetWorkTool extends BaseTool {

    private final String TAG = "YRNetWorkTool";

    private final String YrAppKey = "YR183340513110001";
    private final String YrAppSecret ="83976ebf-175a-4c02-b658-7861c1ea6310";

    private static volatile YrNetWorkTool yrNetWorkTool;
    private YrNetWorkUnit yrNetWorkUnit;

    private YrNetWorkTool() {
        PrintLogD(TAG, "初始化YrNetWorkTool");
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(YrNetWorkPath.YrPath)
                .build();
        yrNetWorkUnit = retrofit.create(YrNetWorkUnit.class);
    }

    public static YrNetWorkTool Wake(){
        if (yrNetWorkTool==null){
            synchronized (YrNetWorkTool.class){
                if (yrNetWorkTool==null){
                    yrNetWorkTool = new YrNetWorkTool();
                }
            }
        }
        return yrNetWorkTool;
    }

    /*
    * 请求友睿Token
    * */
    public void ReqToken(BaseActivity baseActivity, BaseFragment baseFragment){
        Observable<TokenRes> observable = yrNetWorkUnit.ReqToken(new TokenReqBody(YrAppKey, YrAppSecret, 1));
        observable = AddRetryExtraAll(observable, baseActivity, baseFragment, TAG);
        observable.subscribe(new Consumer<TokenRes>() {
            @Override
            public void accept(TokenRes tokenRes) throws Throwable {
                if (tokenRes.getCode()==0){
                    PrintLogD(TAG, "获取友睿Token成功");
                    if (baseActivity!=null){
                        SPTool.init(baseActivity).SaveInfo(SPTool.SpToken, tokenRes.getToken());
                        SPTool.init(baseActivity).SaveInfo(SPTool.SpTokenTime, new SimpleDateFormat("yyyyMMdd").format(new Date()));
                    }else if (baseFragment!=null){
                        SPTool.init(baseFragment.getContext()).SaveInfo(SPTool.SpToken, tokenRes.getToken());
                    }
                }else {
                    PrintLogD(TAG, String.format("获取友睿Token失败：%s", tokenRes.getMsg()));
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                PrintLogD(TAG, String.format("获取友睿Token错误：%s", throwable.getMessage()));
            }
        });
    }



}

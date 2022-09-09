package com.gzyslczx.yslc.presenter;

import static com.gzyslczx.yslc.tools.ConnTool.AddRetryReq;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.gzyslczx.yslc.modes.ConnMode;
import com.gzyslczx.yslc.modes.request.ReqCancelUser;
import com.gzyslczx.yslc.modes.request.ReqChangeNickName;
import com.gzyslczx.yslc.modes.request.ReqDragOption;
import com.gzyslczx.yslc.modes.request.ReqFlash;
import com.gzyslczx.yslc.modes.request.ReqJustId;
import com.gzyslczx.yslc.modes.request.ReqJustIdPhone;
import com.gzyslczx.yslc.modes.request.ReqJustKeywordPhone;
import com.gzyslczx.yslc.modes.request.ReqJustNidUserid;
import com.gzyslczx.yslc.modes.request.ReqJustPhone;
import com.gzyslczx.yslc.modes.request.ReqJustPhoneDate;
import com.gzyslczx.yslc.modes.request.ReqJustPhoneSId;
import com.gzyslczx.yslc.modes.request.ReqJustPhoneStockCode;
import com.gzyslczx.yslc.modes.request.ReqJustPlateform;
import com.gzyslczx.yslc.modes.request.ReqJustUserid;
import com.gzyslczx.yslc.modes.request.ReqJustUseridCurrentPageSize;
import com.gzyslczx.yslc.modes.request.ReqKLineLearnPraise;
import com.gzyslczx.yslc.modes.request.ReqKLineList;
import com.gzyslczx.yslc.modes.request.ReqLabelSelf;
import com.gzyslczx.yslc.modes.request.ReqLoginByCode;
import com.gzyslczx.yslc.modes.request.ReqMainReco;
import com.gzyslczx.yslc.modes.request.ReqMsgInfoList;
import com.gzyslczx.yslc.modes.request.ReqPhonePlatform;
import com.gzyslczx.yslc.modes.request.ReqPhoneWithSCodes;
import com.gzyslczx.yslc.modes.request.ReqPushUpdate;
import com.gzyslczx.yslc.modes.request.ReqScrollSmallVideo;
import com.gzyslczx.yslc.modes.request.ReqSuggest;
import com.gzyslczx.yslc.modes.request.ReqTSelf;
import com.gzyslczx.yslc.modes.request.ReqTSelfLivingList;
import com.gzyslczx.yslc.modes.response.ResAdv;
import com.gzyslczx.yslc.modes.response.ResAppSet;
import com.gzyslczx.yslc.modes.response.ResChangeNickName;
import com.gzyslczx.yslc.modes.response.ResDefaultOption;
import com.gzyslczx.yslc.modes.response.ResFlash;
import com.gzyslczx.yslc.modes.response.ResJustIntObj;
import com.gzyslczx.yslc.modes.response.ResJustStrObj;
import com.gzyslczx.yslc.modes.response.ResKLineDetails;
import com.gzyslczx.yslc.modes.response.ResKLineList;
import com.gzyslczx.yslc.modes.response.ResKLineType;
import com.gzyslczx.yslc.modes.response.ResLabelArt;
import com.gzyslczx.yslc.modes.response.ResLabelSelf;
import com.gzyslczx.yslc.modes.response.ResLogin;
import com.gzyslczx.yslc.modes.response.ResMainFinancingBuy;
import com.gzyslczx.yslc.modes.response.ResMainInstitutionBuy;
import com.gzyslczx.yslc.modes.response.ResMainNorthBuy;
import com.gzyslczx.yslc.modes.response.ResMainReco;
import com.gzyslczx.yslc.modes.response.ResMainRiskRadar;
import com.gzyslczx.yslc.modes.response.ResMainSup;
import com.gzyslczx.yslc.modes.response.ResMainTeacherLiving;
import com.gzyslczx.yslc.modes.response.ResMineInfo;
import com.gzyslczx.yslc.modes.response.ResMsgBox;
import com.gzyslczx.yslc.modes.response.ResMsgInfoList;
import com.gzyslczx.yslc.modes.response.ResMyFocus;
import com.gzyslczx.yslc.modes.response.ResMyFocusInfoList;
import com.gzyslczx.yslc.modes.response.ResMyOption;
import com.gzyslczx.yslc.modes.response.ResNewVersion;
import com.gzyslczx.yslc.modes.response.ResScrollSmallVideo;
import com.gzyslczx.yslc.modes.response.ResSearchArticleMore;
import com.gzyslczx.yslc.modes.response.ResSearchFundMore;
import com.gzyslczx.yslc.modes.response.ResSearchHis;
import com.gzyslczx.yslc.modes.response.ResSearchNor;
import com.gzyslczx.yslc.modes.response.ResSearchStockMore;
import com.gzyslczx.yslc.modes.response.ResSpecialSupDetail;
import com.gzyslczx.yslc.modes.response.ResSpecialSupport;
import com.gzyslczx.yslc.modes.response.ResTSelf;
import com.gzyslczx.yslc.modes.response.ResTSelfLivingList;
import com.gzyslczx.yslc.modes.response.ResTeacherAll;
import com.gzyslczx.yslc.modes.response.ResTeacherArt;
import com.gzyslczx.yslc.modes.response.ResTeacherDyn;
import com.gzyslczx.yslc.modes.response.ResToken;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.yourui.RequestApi;
import com.yourui.sdk.message.use.Stock;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GuBbBasePresenter {

    private static volatile GuBbBasePresenter mBase;
    private ConnMode mReqBase;

    private GuBbBasePresenter() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(ConnPath.GuBbMain)
                .build();
        mReqBase = retrofit.create(ConnMode.class);
    }

    public static GuBbBasePresenter Create() {
        if (mBase == null) {
            synchronized (GuBbBasePresenter.class) {
                if (mBase == null) {
                    mBase = new GuBbBasePresenter();
                }
            }
        }
        return mBase;
    }

    /*
    * 请求Token
    * */
    public Observable<ResToken> RequestGuBbToken(String TAG){
        Observable<ResToken> observable = mReqBase.requestToken();
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
    * 请求股扒扒广告图标
    * */
    public Observable<ResAdv> RequestGuBbAdv(Context context, int id, String TAG){
        //1是首页栏目图标 2战绩榜 3为栏目页广告 4为股扒扒图标 5为开机图标 6为服务默认广告 7为首页顶部滚动广告 8为首页直播图片 9为消息类型
        ReqJustId req = new ReqJustId(id);
        Observable<ResAdv> observable = mReqBase.requestGuBbAdv(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
    * 请求首页盘前特供
    * */
    public Observable<ResMainSup> RequestMainSpecialSupport(Context context, String TAG){
        Observable<ResMainSup> observable = mReqBase.requestMainSup(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken));
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求首页名师动态
     * */
    public Observable<ResTeacherDyn> RequestMainTeacherDyn(Context context, String TAG){
        ReqJustUserid req;
        String uid = SpTool.Instance(context).GetInfo(SpTool.GuBbUserID);
        if (uid.equals(SpTool.SpDefault)){
            req = new ReqJustUserid("");
        }else {
            req = new ReqJustUserid(uid);
        }
        Observable<ResTeacherDyn> observable = mReqBase.requestMainTDyn(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
    * 请求首页名师直播
    * */
    public Observable<ResMainTeacherLiving> RequestMainTeacherLiving(Context context, String TAG){
        Observable<ResMainTeacherLiving> observable = mReqBase.requestMainTLiving(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken));
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求首页我的关注
     * */
    public Observable<ResMyFocus> RequestMainMyFocus(Context context, String TAG){
        ReqJustUserid req;
        String uid = SpTool.Instance(context).GetInfo(SpTool.GuBbUserID);
        if (uid.equals(SpTool.SpDefault)){
            req= new ReqJustUserid("");
        }else {
            req= new ReqJustUserid(uid);
        }
        Observable<ResMyFocus> observable = mReqBase.requestMainMyFocus(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求首页更多关注
     * */
    public Observable<ResMyFocus> RequestMainMoreFocus(Context context, String TAG){
        ReqJustUserid req;
        String uid = SpTool.Instance(context).GetInfo(SpTool.GuBbUserID);
        if (uid.equals(SpTool.SpDefault)){
            req= new ReqJustUserid("");
        }else {
            req= new ReqJustUserid(uid);
        }
        Observable<ResMyFocus> observable = mReqBase.requestMainMoreFocus(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求首页更多关注
     * */
    public Observable<ResJustIntObj> RequestFocusAction(Context context, String Nid, String TAG){
        ReqJustNidUserid req;
        String uid = SpTool.Instance(context).GetInfo(SpTool.GuBbUserID);
        if (uid.equals(SpTool.SpDefault)){
            req= new ReqJustNidUserid(Nid, "");
        }else {
            req= new ReqJustNidUserid(Nid, uid);
        }
        Observable<ResJustIntObj> observable = mReqBase.requestFocusAction(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求一键登录
     * */
    public Observable<ResLogin> RequestOneKeyLogin(Context context, String token, String TAG){
        Log.d(TAG, "极光ID="+JPushInterface.getRegistrationID(context));
        ReqPhonePlatform req = new ReqPhonePlatform(token, JPushInterface.getRegistrationID(context));
        Observable<ResLogin> observable = mReqBase.requestOneKeyLogin(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
    * 请求登录验证码
    * */
    public Observable<ResJustStrObj> RequestLoginCode(Context context, String phone, String TAG){
        ReqJustPhone req = new ReqJustPhone(phone);
        Observable<ResJustStrObj> observable = mReqBase.requestLoginCode(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求验证码登录
     * */
    public Observable<ResLogin> RequestLoginByCode(Context context, String phone, String code, String TAG){
        Log.d(TAG, "极光ID="+JPushInterface.getRegistrationID(context));
        ReqLoginByCode req = new ReqLoginByCode(phone,code, JPushInterface.getRegistrationID(context));
        Observable<ResLogin> observable = mReqBase.requestLoginByCode(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求推荐、视频和小视频列表
     * type：(1为推荐 2为视频 3为小视频)
     * */
    public Observable<ResMainReco> RequestMainRecoAndVideo(Context context, int currentPage, int type, String TAG){
        String uid=null;
        if (SpTool.Instance(context).GetInfo(SpTool.GuBbUserID).equals(SpTool.SpDefault)){
            uid = "";
        }else {
            uid = SpTool.Instance(context).GetInfo(SpTool.GuBbUserID);
        }
        ReqMainReco req = new ReqMainReco(6, currentPage, type, uid);
        Observable<ResMainReco> observable = mReqBase.requestMainRecoAndVideo(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求栏目专页
     * */
    public Observable<ResLabelSelf> RequestLabelSelf(Context context, int currentPage, String labelName, String TAG){
        String uid=null;
        if (SpTool.Instance(context).GetInfo(SpTool.GuBbUserID).equals(SpTool.SpDefault)){
            uid = "";
        }else {
            uid = SpTool.Instance(context).GetInfo(SpTool.GuBbUserID);
        }
        ReqLabelSelf req = new ReqLabelSelf(8, currentPage, uid, labelName);
        Observable<ResLabelSelf> observable = mReqBase.requestLabelSelf(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
    * 请求点赞
    * */
    public Observable<ResJustIntObj> RequestNormalPraise(Context context, String nid, String TAG){
        String uid=null;
        if (SpTool.Instance(context).GetInfo(SpTool.GuBbUserID).equals(SpTool.SpDefault)){
            uid = "";
        }else {
            uid = SpTool.Instance(context).GetInfo(SpTool.GuBbUserID);
        }
        ReqJustNidUserid req = new ReqJustNidUserid(nid, uid);
        Observable<ResJustIntObj> observable = mReqBase.requestPraise(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求首页北向资金买入
     * */
    public Observable<ResMainNorthBuy> RequestMainNorthBuy(Context context, String TAG){
        Observable<ResMainNorthBuy> observable = mReqBase.requestMainNorthBuy(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken));
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求首页机构买入
     * */
    public Observable<ResMainInstitutionBuy> RequestMainInstitutionsBuy(Context context, String TAG){
        Observable<ResMainInstitutionBuy> observable = mReqBase.requestInstitutionsBuy(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken));
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求首页融资买入
     * */
    public Observable<ResMainFinancingBuy> RequestMainFinancingBuy(Context context, String TAG){
        Observable<ResMainFinancingBuy> observable = mReqBase.requestFinancingBuy(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken));
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求首页风险雷达
     * */
    public Observable<ResMainRiskRadar> RequestMainRiskRadar(Context context, String TAG){
        Observable<ResMainRiskRadar> observable = mReqBase.requestRiskRadar(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken));
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求名师个人页
     * */
    public Observable<ResTSelf> RequestTeacherSelf(Context context, int type, int currentPage, String teacherId, String TAG){
        String uid=null;
        if (SpTool.Instance(context).GetInfo(SpTool.GuBbUserID).equals(SpTool.SpDefault)){
            uid = "";
        }else {
            uid = SpTool.Instance(context).GetInfo(SpTool.GuBbUserID);
        }
        ReqTSelf req = new ReqTSelf(3, currentPage, type, teacherId, uid);
        Observable<ResTSelf> observable = mReqBase.requestTeacherSelf(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求名师个人页直播列
     * */
    public Observable<ResTSelfLivingList> RequestTeacherSelfLivingList(Context context, int currentPage, String teacherId, String TAG){
        String phone=null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqTSelfLivingList req = new ReqTSelfLivingList(5, currentPage, teacherId, phone);
        Observable<ResTSelfLivingList> observable = mReqBase.requestTeacherSelfLivingList(
                ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求栏目资讯详情
     * */
    public Observable<ResLabelArt> RequestLabelArtDetail(Context context, String Nid, String TAG){
        String uid=null;
        if (SpTool.Instance(context).GetInfo(SpTool.GuBbUserID).equals(SpTool.SpDefault)){
            uid = "";
        }else {
            uid = SpTool.Instance(context).GetInfo(SpTool.GuBbUserID);
        }
        ReqJustNidUserid req = new ReqJustNidUserid(Nid, uid);
        Observable<ResLabelArt> observable = mReqBase.requestLabelArtDetail(
                ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求名师资讯详情
     * */
    public Observable<ResTeacherArt> RequestTeacherArtDetail(Context context, String Nid, String TAG){
        String uid=null;
        if (SpTool.Instance(context).GetInfo(SpTool.GuBbUserID).equals(SpTool.SpDefault)){
            uid = "";
        }else {
            uid = SpTool.Instance(context).GetInfo(SpTool.GuBbUserID);
        }
        ReqJustNidUserid req = new ReqJustNidUserid(Nid, uid);
        Observable<ResTeacherArt> observable = mReqBase.requestTeacherArtDetail(
                ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求K线秘籍类型
     * */
    public Observable<ResKLineType> RequestKLineType(Context context, String TAG){
        Observable<ResKLineType> observable = mReqBase.requestKLineType(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken));
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求K线秘籍资讯列表
     * */
    public Observable<ResKLineList> RequestKLineArtList(Context context, int Cid, int CurrentPage, String TAG){
        String phone=null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqKLineList req = new ReqKLineList(phone, Cid, 5, CurrentPage);
        Observable<ResKLineList> observable = mReqBase.requestKLineArtList(
                ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求K线秘籍资讯列表
     * */
    public Observable<ResKLineList> RequestKLineVideoList(Context context, String TAG){
        String phone=null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqJustPhone req = new ReqJustPhone(phone);
        Observable<ResKLineList> observable = mReqBase.requestKLineVideoList(
                ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求K线秘籍学习或点赞
     * */
    public Observable<ResJustIntObj> RequestKLineLearnPraise(Context context, int id, int state, int typeInfo, String TAG){
        String phone=null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqKLineLearnPraise req = new ReqKLineLearnPraise(phone, id, state, typeInfo);
        Observable<ResJustIntObj> observable = mReqBase.requestKLineLearnPraise(
                ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求K线秘籍视频详情
     * */
    public Observable<ResKLineDetails> RequestKLineVideoDetail(Context context, int id, String TAG){
        String phone=null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqJustIdPhone req = new ReqJustIdPhone(id, phone);
        Observable<ResKLineDetails> observable = mReqBase.requestKLineVideoDetails(
                ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求K线秘籍文章详情
     * */
    public Observable<ResKLineDetails> RequestKLineArticleDetail(Context context, int id, String TAG){
        String phone=null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqJustIdPhone req = new ReqJustIdPhone(id, phone);
        Observable<ResKLineDetails> observable = mReqBase.requestKLineArtDetails(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求首页越声快讯
     * */
    public Observable<ResFlash> RequestFlashList(Context context, int currentPage, int type, String TAG) {
        ReqFlash req = new ReqFlash(currentPage, type);
        Log.d(TAG, new Gson().toJson(req));
        Observable<ResFlash> observable
                = mReqBase.requestFlashList(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 请求滑动小视频列表
     * */
    public Observable<ResScrollSmallVideo> RequestScrollSmallVideoList(Context context, String nid, int currentPage, String tag){
        String uid=null;
        if (SpTool.Instance(context).GetInfo(SpTool.GuBbUserID).equals(SpTool.SpDefault)){
            uid = "";
        }else {
            uid = SpTool.Instance(context).GetInfo(SpTool.GuBbUserID);
        }
        ReqScrollSmallVideo req = new ReqScrollSmallVideo(uid, nid, 6, currentPage);
        Observable<ResScrollSmallVideo> observable = mReqBase.requestSmallScrollVideoList(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, tag);
        return  observable;
    }

    /*
     * 请求滑动小视频列表
     * */
    public Observable<ResTeacherAll> RequestTeacherAllList(Context context, String tag){
        String uid=null;
        if (SpTool.Instance(context).GetInfo(SpTool.GuBbUserID).equals(SpTool.SpDefault)){
            uid = "";
        }else {
            uid = SpTool.Instance(context).GetInfo(SpTool.GuBbUserID);
        }
        ReqJustUserid req = new ReqJustUserid(uid);
        Observable<ResTeacherAll> observable = mReqBase.requestTeacherAllList(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, tag);
        return  observable;
    }

    /*
     * 请求搜索历史记录
     * */
    public Observable<ResSearchHis> RequestSearchHistory(Context context, String tag) {
        if (SpTool.Instance(context).IsGuBbLogin()) {
            ReqJustPhone req = new ReqJustPhone(SpTool.Instance(context).GetInfo(SpTool.UserPhone));
            Observable<ResSearchHis> observable
                    = mReqBase.requestSearchHistory(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
            observable = AddRetryReq(observable, tag);
            return observable;
        }
        return null;
    }

    /*
     * 请求模糊搜索
     * */
    public Observable<ResSearchNor> RequestSearchNormal(Context context, String keyword, String tag) {
        String phone = null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqJustKeywordPhone req = new ReqJustKeywordPhone(keyword, phone);
        Observable<ResSearchNor> observable
                = mReqBase.requestSearchNormal(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * 请求股票搜索
     * */
    public Observable<ResSearchStockMore> RequestSearchStock(Context context, String keyword, String tag) {
        String phone = null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqJustKeywordPhone req = new ReqJustKeywordPhone(keyword, phone);
        Observable<ResSearchStockMore> observable
                = mReqBase.requestSearchStockMore(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * 请求基金搜索
     * */
    public Observable<ResSearchFundMore> RequestSearchFund(Context context, String keyword, String tag) {
        String phone = null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqJustKeywordPhone req = new ReqJustKeywordPhone(keyword, phone);
        Observable<ResSearchFundMore> observable
                = mReqBase.requestSearchFundMore(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * 请求相关内容搜索
     * */
    public Observable<ResSearchArticleMore> RequestSearchArticle(Context context, String keyword, String tag) {
        String phone = null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqJustKeywordPhone req = new ReqJustKeywordPhone(keyword, phone);
        Observable<ResSearchArticleMore> observable
                = mReqBase.requestSearchArticleMore(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * 按日期请求盘前特供
     * */
    public Observable<ResSpecialSupport> RequestCheckSupport(Context context, String date, String tag) {
        String phone = null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqJustPhoneDate req = new ReqJustPhoneDate(date, phone);
        Observable<ResSpecialSupport> observable = mReqBase.requestSpecialSupByDate(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * 请求盘前特供详情
     * */
    public Observable<ResSpecialSupDetail> RequestResSpecialSupDetail(Context context, String sid, String tag) {
        String phone = null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqJustPhoneSId req = new ReqJustPhoneSId(sid, phone);
        Observable<ResSpecialSupDetail> observable = mReqBase.requestSpecialSupDetail(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * 请求盘前特供点赞
     * */
    public Observable<ResJustIntObj> RequestResSpecialSupPraise(Context context, String sid, String tag) {
        String phone = null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqJustPhoneSId req = new ReqJustPhoneSId(sid, phone);
        Observable<ResJustIntObj> observable = mReqBase.requestSpecialSupPraise(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * 请求添加我的自选股
     * */
    public Observable<ResJustStrObj> RequestAddOption(Context context, String[] stocks, String tag) {
        String phone = null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqPhoneWithSCodes req = new ReqPhoneWithSCodes(phone, stocks);
        Observable<ResJustStrObj> observable = mReqBase.requestAddOption(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * 请求删除我的自选股
     * */
    public Observable<ResJustStrObj> RequestDeleteOption(Context context, String[] stocks, String tag) {
        String phone = null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqPhoneWithSCodes req = new ReqPhoneWithSCodes(phone, stocks);
        Observable<ResJustStrObj> observable = mReqBase.requestDeleteOption(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * 请求默认自选股
     * */
    public Observable<ResDefaultOption> RequestDefOption(Context context, String tag) {
        Observable<ResDefaultOption> observable
                = mReqBase.requestDefaultOption(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken));
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * 请求我的自选股
     * */
    public Observable<ResMyOption> RequestMyOption(Context context, String tag) {
        if (SpTool.Instance(context).IsGuBbLogin()) {
            ReqJustPhone req = new ReqJustPhone(SpTool.Instance(context).GetInfo(SpTool.UserPhone));
            Observable<ResMyOption> observable
                    = mReqBase.requestMyOption(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
            observable = AddRetryReq(observable, tag);
            return observable;
        }else {
            return null;
        }
    }

    /*
    * 请求置顶自选股
    * */
    public Observable<ResJustStrObj> RequestSetTopOption(Context contex, String stockcode, String tag) {
        if (SpTool.Instance(contex).IsGuBbLogin()){
            ReqJustPhoneStockCode req = new ReqJustPhoneStockCode(SpTool.Instance(contex).GetInfo(SpTool.UserPhone), stockcode);
            Observable<ResJustStrObj> observable = mReqBase.requestSetTopOption(ConnPath.HeaderValue + SpTool.Instance(contex).GetInfo(SpTool.GuBbToken), req);
            observable = AddRetryReq(observable, tag);
            return observable;
        }else {
            return null;
        }
    }

    /*
     * 请求拖动我的自选股
     * */
    public Observable<ResJustStrObj> RequestDragOption(Context context, List<String> stockCode, List<Integer> pos, String tag) {
        if (SpTool.Instance(context).IsGuBbLogin()) {
            String phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
            List<ReqDragOption> req = new ArrayList<ReqDragOption>();
            for (int i=0; i<stockCode.size(); i++){
                req.add(new ReqDragOption(pos.get(i)+1, phone, stockCode.get(i)));
            }
            Log.d("MyOptionFrag", String.format("拖动后请求体:%s", new Gson().toJson(req)));
            Observable<ResJustStrObj> observable
                    = mReqBase.requestDragOption(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
            observable = AddRetryReq(observable, tag);
            return observable;
        }else {
            return null;
        }
    }

    /*
    * 获取用户信息
    * */
    public Observable<ResMineInfo> RequestMineInfo(Context context, String tag){
        if (SpTool.Instance(context).IsGuBbLogin()){
            String uid = SpTool.Instance(context).GetInfo(SpTool.GuBbUserID);
            ReqJustUserid req = new ReqJustUserid();
            req.setUserid(uid);
            Observable<ResMineInfo> observable =
                    mReqBase.requestMineInfo(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
            observable = AddRetryReq(observable, tag);
            return observable;
        }
        return null;
    }

    /*
    * 更换头像
    * */
    public Observable<ResJustStrObj> RequestChangeHeadImg(Context context, File file,  String tag){
        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("media", file.getName(), body);
        Observable<ResJustStrObj> observable = mReqBase.requestChangeHeadImg(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken),
                SpTool.Instance(context).GetInfo(SpTool.GuBbUserID), filePart);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * 获取用户信息
     * */
    public Observable<ResChangeNickName> RequestChangeNickName(Context context, String nickName, String tag){
        String uid = SpTool.Instance(context).GetInfo(SpTool.GuBbUserID);
        ReqChangeNickName req = new ReqChangeNickName(uid, nickName);
        Observable<ResChangeNickName> observable =
                mReqBase.requestChangeNickName(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * 产品建议
     * */
    public Observable<ResJustStrObj> RequestSuggest(Context context, String content, String tag){
        String phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        ReqSuggest req = new ReqSuggest(phone, content);
        Observable<ResJustStrObj> observable =
                mReqBase.requestSuggestCommit(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * 获取消息盒子类型
     * */
    public Observable<ResMsgBox> RequestMsgBoxStyle(Context context, String tag){
        String phone = null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqJustPhone req = new ReqJustPhone(phone);
        Observable<ResMsgBox> observable =
                mReqBase.requestMsgBoxStyle(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * 获取消息列表
     * */
    public Observable<ResMsgInfoList> RequestMsgInfoList(Context context, int MsgType,  int CurrentPage, String tag){
        String phone = null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqMsgInfoList req = new ReqMsgInfoList(MsgType, 5, CurrentPage, phone);
        Observable<ResMsgInfoList> observable =
                mReqBase.requestMsgInfoList(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * 更新消息列表已读
     * */
    public Observable<ResJustStrObj> RequestUpdateMsgList(Context context, int id, String tag){
        String phone = null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqJustIdPhone req = new ReqJustIdPhone(id, phone);
        Observable<ResJustStrObj> observable =
                mReqBase.requestMsgInfoUpdate(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * 更新消息列表已读
     * */
    public Observable<ResJustStrObj> RequestUpdatePush(Context context, String title, String url, String tag){
        String phone = null;
        if (SpTool.Instance(context).GetInfo(SpTool.UserPhone).equals(SpTool.SpDefault)){
            phone = "";
        }else {
            phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        }
        ReqPushUpdate req = new ReqPushUpdate(phone, title, url);
        Observable<ResJustStrObj> observable =
                mReqBase.requestPushInfoUpdate(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * 更新应用版本
     * */
    public Observable<ResNewVersion> RequestUpdateNewVersion(Context context, String tag){
        ReqJustPlateform req = new ReqJustPlateform("android");
        Observable<ResNewVersion> observable =
                mReqBase.requestUpdateNewVersion(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
    * 请求关注的越声栏目资讯列表
    * */
    public Observable<ResMyFocusInfoList> RequestMyFocusInfoList(Context context, int currentPage, String TAG){
        String uid=null;
        if (SpTool.Instance(context).GetInfo(SpTool.GuBbUserID).equals(SpTool.SpDefault)){
            uid = "";
        }else {
            uid = SpTool.Instance(context).GetInfo(SpTool.GuBbUserID);
        }
        ReqJustUseridCurrentPageSize req = new ReqJustUseridCurrentPageSize(uid, currentPage, 6);
        Observable<ResMyFocusInfoList> observable = mReqBase.requestMyFocusInfo(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken), req);
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
    * App特殊设置
    * */
    public Observable<ResAppSet> RequestAppSet(Context context, String TAG){
        Observable<ResAppSet> observable = mReqBase.requestAppSet(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.GuBbToken));
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * 注销账户
     * */
    public Observable<ResJustStrObj> RequestLogoutUser(Context context, String code, String tag) {
        ReqCancelUser req = new ReqCancelUser(SpTool.Instance(context).GetInfo(SpTool.UserPhone), code);
        Observable<ResJustStrObj> observable = mReqBase.requestCancelUser(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.FundTongToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

}



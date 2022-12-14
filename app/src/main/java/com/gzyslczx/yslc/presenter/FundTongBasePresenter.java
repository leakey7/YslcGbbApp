package com.gzyslczx.yslc.presenter;

import static com.gzyslczx.yslc.tools.ConnTool.AddRetryReq;

import android.content.Context;

import com.gzyslczx.yslc.modes.ConnMode;
import com.gzyslczx.yslc.modes.request.ReqCancelUser;
import com.gzyslczx.yslc.modes.request.ReqFundDetail;
import com.gzyslczx.yslc.modes.request.ReqFundTongRank;
import com.gzyslczx.yslc.modes.request.ReqFundTongTradeLevel;
import com.gzyslczx.yslc.modes.request.ReqFundTongVerifyCodeLogin;
import com.gzyslczx.yslc.modes.request.ReqJustCode;
import com.gzyslczx.yslc.modes.request.ReqJustCodeCurrentPageSize;
import com.gzyslczx.yslc.modes.request.ReqJustId;
import com.gzyslczx.yslc.modes.request.ReqJustUserid;
import com.gzyslczx.yslc.modes.response.ReqJustUseridAndFCode;
import com.gzyslczx.yslc.modes.response.ResAdv;
import com.gzyslczx.yslc.modes.response.ResConceptSelect;
import com.gzyslczx.yslc.modes.response.ResFundDetail;
import com.gzyslczx.yslc.modes.response.ResFundFindIndex;
import com.gzyslczx.yslc.modes.response.ResFundFindRank;
import com.gzyslczx.yslc.modes.response.ResFundTongDefault;
import com.gzyslczx.yslc.modes.response.ResFundTongHuShen;
import com.gzyslczx.yslc.modes.response.ResFundTongIcon;
import com.gzyslczx.yslc.modes.response.ResFundTongLogin;
import com.gzyslczx.yslc.modes.response.ResFundTongRank;
import com.gzyslczx.yslc.modes.response.ResFundTongTradeLevel;
import com.gzyslczx.yslc.modes.response.ResJustStrObj;
import com.gzyslczx.yslc.modes.response.ResLogin;
import com.gzyslczx.yslc.modes.response.ResMyFundOption;
import com.gzyslczx.yslc.modes.response.ResToken;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.SpTool;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class FundTongBasePresenter {
    private static volatile FundTongBasePresenter mBase;
    private ConnMode mReqBase;

    private FundTongBasePresenter() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(ConnPath.FundTongMain)
                .build();
        mReqBase = retrofit.create(ConnMode.class);
    }

    public static FundTongBasePresenter Create() {
        if (mBase == null) {
            synchronized (FundTongBasePresenter.class) {
                if (mBase == null) {
                    mBase = new FundTongBasePresenter();
                }
            }
        }
        return mBase;
    }

    /*
     * ??????Token
     * */
    public Observable<ResToken> RequestFundTongToken(String TAG){
        Observable<ResToken> observable = mReqBase.requestFundTongToken();
        observable = AddRetryReq(observable, TAG);
        return observable;
    }

    /*
     * ????????????????????????????????????????????????
     * id???1=?????????  2=?????????????????????
     * */
    public Observable<ResAdv> RequestFundTongAdv(Context context, int id, String tag){
        ReqJustId req = new ReqJustId(id);
        Observable<ResAdv> observable = mReqBase.requestFundTongAdv(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.FundTongToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * ???????????????Icon???Tab
     * */
    public Observable<ResFundTongIcon> RequestFundTongIcon(Context context, String tag){
        Observable<ResFundTongIcon> observable = mReqBase.requestFundTongIcon(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.FundTongToken));
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * ????????????????????????????????????
     * */
    public Observable<ResFundTongRank> RequestFundTongSortRank(Context context, int iconId, int typeId, int currentPage, int sort, String tag){
        ReqFundTongRank req = new ReqFundTongRank(iconId, typeId, currentPage, 6);
        req.setSort(sort);
        Observable<ResFundTongRank> observable = mReqBase.requestFundTongSortRank(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.FundTongToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * ???????????????????????????
     * */
    public Observable<ResFundTongLogin> RequestFundTongVerifyCode(Context context, String tag){
        String phone = SpTool.Instance(context).GetInfo(SpTool.UserPhone);
        ReqFundTongVerifyCodeLogin req = new ReqFundTongVerifyCodeLogin(phone);
        Observable<ResFundTongLogin> observable = mReqBase.requestFundTongLogin(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.FundTongToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * ???????????????????????????
     * */
    public Observable<ResFundTongDefault> RequestDefault(Context context, String tag){
        Observable<ResFundTongDefault> observable = mReqBase.requestFundTongDefault(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.FundTongToken));
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * ?????????????????????????????????
     * */
    public Observable<ResFundTongTradeLevel> RequestTradeLevelList(Context context, int level, String parentCode, String tag){
        ReqFundTongTradeLevel req = new ReqFundTongTradeLevel(parentCode, level);
        Observable<ResFundTongTradeLevel> observable = mReqBase.requestFundTongTradeLevel(
                ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.FundTongToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * ???????????????????????????
     * */
    public Observable<ResFundFindIndex> RequestTradeIndex(Context context, String code, String tag){
        ReqJustCode req = new ReqJustCode(code);
        Observable<ResFundFindIndex> observable = mReqBase.requestFundTongTradeIndex(
                ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.FundTongToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * ???????????????????????????
     * */
    public Observable<ResFundFindRank> RequestFundTradeList(Context context, String code, int currentPage, String tag){
        ReqJustCodeCurrentPageSize req = new ReqJustCodeCurrentPageSize(code, currentPage, 6);
        Observable<ResFundFindRank> observable = mReqBase.requestTradeList(
                ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.FundTongToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * ?????????????????????????????????
     * */
    public Observable<ResConceptSelect> RequestConceptSelectList(Context context, String tag){
        Observable<ResConceptSelect> observable = mReqBase.requestConceptSelectUrl(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.FundTongToken));
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * ???????????????????????????
     * */
    public Observable<ResFundFindIndex> RequestConceptIndex(Context context, String code, String tag){
        ReqJustCode req = new ReqJustCode(code);
        Observable<ResFundFindIndex> observable = mReqBase.requestFundTongConceptIndex(
                ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.FundTongToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * ???????????????????????????
     * */
    public Observable<ResFundFindRank> RequestFundConceptList(Context context, String code, int currentPage, String tag){
        ReqJustCodeCurrentPageSize req = new ReqJustCodeCurrentPageSize(code, currentPage, 6);
        Observable<ResFundFindRank> observable = mReqBase.requestConceptList(
                ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.FundTongToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
     * ??????????????????
     * */
    public Observable<ResFundDetail> RequestFundDetail(Context context, String code, String tag){
        ReqFundDetail req = new ReqFundDetail(code);
        if (SpTool.Instance(context).IsFundTongLogin()){
            req.setUserid(SpTool.Instance(context).GetInfo(SpTool.FundTongUID));
        }else {
            req.setUserid("");
        }
        Observable<ResFundDetail> observable = mReqBase.requestFundTongDetail(
                ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.FundTongToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
    * ??????????????????
    * */
    public Observable<ResFundTongHuShen> RequestHuShen(Context context, String tag){
        Observable<ResFundTongHuShen> observable = mReqBase.requestFundTongHuShen(
                ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.FundTongToken));
        observable = AddRetryReq(observable, tag);
        return observable;
    }

    /*
    * ???????????????????????????
    * */
    public Observable<ResJustStrObj> RequestAddFundOption(Context context, String fundCode, String tag){
        if (SpTool.Instance(context).IsFundTongLogin()) {
            String uid = SpTool.Instance(context).GetInfo(SpTool.FundTongUID);
            ReqJustUseridAndFCode req = new ReqJustUseridAndFCode(uid, fundCode);
            Observable<ResJustStrObj> observable = mReqBase.requestAddFundOption(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.FundTongToken), req);
            observable = AddRetryReq(observable, tag);
            return observable;
        }
        return null;
    }

    /*
     * ???????????????????????????
     * */
    public Observable<ResJustStrObj> RequestDeleteFundOption(Context context, String fundCode, String tag){
        if (SpTool.Instance(context).IsFundTongLogin()) {
            String uid = SpTool.Instance(context).GetInfo(SpTool.FundTongUID);
            ReqJustUseridAndFCode req = new ReqJustUseridAndFCode(uid, fundCode);
            Observable<ResJustStrObj> observable = mReqBase.requestDeleteFundOption(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.FundTongToken), req);
            observable = AddRetryReq(observable, tag);
            return observable;
        }
        return null;
    }

    /*
    * ??????????????????
    * */
    public Observable<ResMyFundOption> RequestMineOptionFund(Context context, String tag) {
        ReqJustUserid req = new ReqJustUserid(SpTool.Instance(context).GetInfo(SpTool.FundTongUID));
        Observable<ResMyFundOption> observable = mReqBase.requestMineFundOption(ConnPath.HeaderValue + SpTool.Instance(context).GetInfo(SpTool.FundTongToken), req);
        observable = AddRetryReq(observable, tag);
        return observable;
    }

}

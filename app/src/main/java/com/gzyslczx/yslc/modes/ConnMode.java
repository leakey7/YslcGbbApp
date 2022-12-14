package com.gzyslczx.yslc.modes;

import com.gzyslczx.yslc.modes.request.ReqCancelUser;
import com.gzyslczx.yslc.modes.request.ReqChangeNickName;
import com.gzyslczx.yslc.modes.request.ReqDragOption;
import com.gzyslczx.yslc.modes.request.ReqFlash;
import com.gzyslczx.yslc.modes.request.ReqFundDetail;
import com.gzyslczx.yslc.modes.request.ReqFundTongRank;
import com.gzyslczx.yslc.modes.request.ReqFundTongTradeLevel;
import com.gzyslczx.yslc.modes.request.ReqFundTongVerifyCodeLogin;
import com.gzyslczx.yslc.modes.request.ReqJustCode;
import com.gzyslczx.yslc.modes.request.ReqJustCodeCurrentPageSize;
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
import com.gzyslczx.yslc.modes.response.ReqJustUseridAndFCode;
import com.gzyslczx.yslc.modes.response.ResAdv;
import com.gzyslczx.yslc.modes.response.ResAppSet;
import com.gzyslczx.yslc.modes.response.ResChangeNickName;
import com.gzyslczx.yslc.modes.response.ResConceptSelect;
import com.gzyslczx.yslc.modes.response.ResDefaultOption;
import com.gzyslczx.yslc.modes.response.ResFlash;
import com.gzyslczx.yslc.modes.response.ResFundDetail;
import com.gzyslczx.yslc.modes.response.ResFundFindIndex;
import com.gzyslczx.yslc.modes.response.ResFundFindRank;
import com.gzyslczx.yslc.modes.response.ResFundTongDefault;
import com.gzyslczx.yslc.modes.response.ResFundTongHuShen;
import com.gzyslczx.yslc.modes.response.ResFundTongIcon;
import com.gzyslczx.yslc.modes.response.ResFundTongLogin;
import com.gzyslczx.yslc.modes.response.ResFundTongRank;
import com.gzyslczx.yslc.modes.response.ResFundTongTradeLevel;
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
import com.gzyslczx.yslc.modes.response.ResMyFundOption;
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

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ConnMode {

    //?????????Token
    @GET(ConnPath.GuBbTokenUrl)
    Observable<ResToken> requestToken();

    //?????????Token
    @GET(ConnPath.FundTongTokenUrl)
    Observable<ResToken> requestFundTongToken();

    //??????????????????????????????
    @POST(ConnPath.GuBbAdvUrl)
    Observable<ResAdv> requestGuBbAdv(@Header(ConnPath.HEAD) String header, @Body ReqJustId req);

    //??????????????????
    @POST(ConnPath.MainSpecialSupport)
    Observable<ResMainSup> requestMainSup(@Header(ConnPath.HEAD) String header);

    //??????????????????
    @POST(ConnPath.MainTeacherDynUrl)
    Observable<ResTeacherDyn> requestMainTDyn(@Header(ConnPath.HEAD) String header, @Body ReqJustUserid req);

    //??????????????????
    @POST(ConnPath.MainTeacherLiving)
    Observable<ResMainTeacherLiving> requestMainTLiving(@Header(ConnPath.HEAD) String header);

    //????????????
    @POST(ConnPath.MyFocusUrl)
    Observable<ResMyFocus> requestMainMyFocus(@Header(ConnPath.HEAD) String header, @Body ReqJustUserid req);

    //????????????
    @POST(ConnPath.MoreFocusUrl)
    Observable<ResMyFocus> requestMainMoreFocus(@Header(ConnPath.HEAD) String header, @Body ReqJustUserid req);

    //????????????
    @POST(ConnPath.FocusActionUrl)
    Observable<ResJustIntObj> requestFocusAction(@Header(ConnPath.HEAD) String header, @Body ReqJustNidUserid req);

    //????????????
    @POST(ConnPath.OnKeyLoginUrl)
    Observable<ResLogin> requestOneKeyLogin(@Header(ConnPath.HEAD) String header, @Body ReqPhonePlatform req);

    //?????????????????????
    @POST(ConnPath.LoginCodeUrl)
    Observable<ResJustStrObj> requestLoginCode(@Header(ConnPath.HEAD) String header, @Body ReqJustPhone req);

    //?????????????????????
    @POST(ConnPath.LoginByCode)
    Observable<ResLogin> requestLoginByCode(@Header(ConnPath.HEAD) String header, @Body ReqLoginByCode req);

    //????????????????????????????????????????????????
    @POST(ConnPath.MainRecoAndVideoUrl)
    Observable<ResMainReco> requestMainRecoAndVideo(@Header(ConnPath.HEAD) String header, @Body ReqMainReco req);

    //????????????????????????
    @POST(ConnPath.LabelSelfUrl)
    Observable<ResLabelSelf> requestLabelSelf(@Header(ConnPath.HEAD) String header, @Body ReqLabelSelf req);

    //???????????????????????????
    @POST(ConnPath.PraiseUrl)
    Observable<ResJustIntObj> requestPraise(@Header(ConnPath.HEAD) String header, @Body ReqJustNidUserid req);

    //????????????????????????
    @POST(ConnPath.MainNorthBuy)
    Observable<ResMainNorthBuy> requestMainNorthBuy(@Header(ConnPath.HEAD) String header);

    //??????????????????
    @POST(ConnPath.MainInstitutionsBuy)
    Observable<ResMainInstitutionBuy> requestInstitutionsBuy(@Header(ConnPath.HEAD) String header);

    //??????????????????
    @POST(ConnPath.MainFinancingBuy)
    Observable<ResMainFinancingBuy> requestFinancingBuy(@Header(ConnPath.HEAD) String header);

    //??????????????????
    @POST(ConnPath.MainRiskRadar)
    Observable<ResMainRiskRadar> requestRiskRadar(@Header(ConnPath.HEAD) String header);

    //?????????????????????
    @POST(ConnPath.TeacherSelfUrl)
    Observable<ResTSelf> requestTeacherSelf(@Header(ConnPath.HEAD) String header, @Body ReqTSelf req);

    //?????????????????????
    @POST(ConnPath.TeacherSelfLivingListUrl)
    Observable<ResTSelfLivingList> requestTeacherSelfLivingList(@Header(ConnPath.HEAD) String header, @Body ReqTSelfLivingList req);

    //??????????????????
    @POST(ConnPath.LabelArticleUrl)
    Observable<ResLabelArt> requestLabelArtDetail(@Header(ConnPath.HEAD) String header, @Body ReqJustNidUserid req);

    //??????????????????
    @POST(ConnPath.TeacherArticleUrl)
    Observable<ResTeacherArt> requestTeacherArtDetail(@Header(ConnPath.HEAD) String header, @Body ReqJustNidUserid req);

    //K???????????????
    @POST(ConnPath.KLineTypeUrl)
    Observable<ResKLineType> requestKLineType(@Header(ConnPath.HEAD) String header);

    //K?????????????????????
    @POST(ConnPath.KLineArtListUrl)
    Observable<ResKLineList> requestKLineArtList(@Header(ConnPath.HEAD) String header, @Body ReqKLineList req);

    //K?????????????????????
    @POST(ConnPath.KLineVideoListUrl)
    Observable<ResKLineList> requestKLineVideoList(@Header(ConnPath.HEAD) String header, @Body ReqJustPhone req);

    //K????????????????????????
    @POST(ConnPath.KLineLearnPraiseUrl)
    Observable<ResJustIntObj> requestKLineLearnPraise(@Header(ConnPath.HEAD) String header, @Body ReqKLineLearnPraise req);

    //K?????????????????????
    @POST(ConnPath.KLineArticleDetailUrl)
    Observable<ResKLineDetails> requestKLineArtDetails(@Header(ConnPath.HEAD) String header, @Body ReqJustIdPhone req);

    //K?????????????????????
    @POST(ConnPath.KLineVideoDetailUrl)
    Observable<ResKLineDetails> requestKLineVideoDetails(@Header(ConnPath.HEAD) String header, @Body ReqJustIdPhone req);

    //??????????????????
    @POST(ConnPath.FlashListUrl)
    Observable<ResFlash> requestFlashList(@Header(ConnPath.HEAD) String header, @Body ReqFlash req);

    //????????????????????????
    @POST(ConnPath.SmallScrollVideoUrl)
    Observable<ResScrollSmallVideo> requestSmallScrollVideoList(@Header(ConnPath.HEAD) String header, @Body ReqScrollSmallVideo req);

    //????????????
    @POST(ConnPath.TeacherAllUrl)
    Observable<ResTeacherAll> requestTeacherAllList(@Header(ConnPath.HEAD) String header, @Body ReqJustUserid req);

    //????????????
    @POST(ConnPath.SearchHistoryUrl)
    Observable<ResSearchHis> requestSearchHistory(@Header(ConnPath.HEAD) String header, @Body ReqJustPhone req);

    //????????????
    @POST(ConnPath.SearchNormalUrl)
    Observable<ResSearchNor> requestSearchNormal(@Header(ConnPath.HEAD) String header, @Body ReqJustKeywordPhone req);

    //??????????????????
    @POST(ConnPath.SearchStockMoreUrl)
    Observable<ResSearchStockMore> requestSearchStockMore(@Header(ConnPath.HEAD) String header, @Body ReqJustKeywordPhone req);

    //??????????????????
    @POST(ConnPath.SearchFundMoreUrl)
    Observable<ResSearchFundMore> requestSearchFundMore(@Header(ConnPath.HEAD) String header, @Body ReqJustKeywordPhone req);

    //????????????????????????
    @POST(ConnPath.SearchArticleMoreUrl)
    Observable<ResSearchArticleMore> requestSearchArticleMore(@Header(ConnPath.HEAD) String header, @Body ReqJustKeywordPhone req);

    //???????????????????????????
    @POST(ConnPath.CheckSpecialSupByDateUrl)
    Observable<ResSpecialSupport> requestSpecialSupByDate(@Header(ConnPath.HEAD) String header, @Body ReqJustPhoneDate req);

    //??????????????????
    @POST(ConnPath.SpecialSupDetailUrl)
    Observable<ResSpecialSupDetail> requestSpecialSupDetail(@Header(ConnPath.HEAD) String header, @Body ReqJustPhoneSId req);

    //??????????????????
    @POST(ConnPath.SpecialSupPraise)
    Observable<ResJustIntObj> requestSpecialSupPraise(@Header(ConnPath.HEAD) String header, @Body ReqJustPhoneSId req);

    //????????????
    @POST(ConnPath.AddMyOptionUrl)
    Observable<ResJustStrObj> requestAddOption(@Header(ConnPath.HEAD) String header, @Body ReqPhoneWithSCodes req);

    //????????????
    @POST(ConnPath.DeleteOptionUrl)
    Observable<ResJustStrObj> requestDeleteOption(@Header(ConnPath.HEAD) String header, @Body ReqPhoneWithSCodes req);

    //?????????????????????
    @POST(ConnPath.MyOptionUrl)
    Observable<ResMyOption> requestMyOption(@Header(ConnPath.HEAD) String header, @Body ReqJustPhone req);

    //????????????
    @POST(ConnPath.DefaultOptionUrl)
    Observable<ResDefaultOption> requestDefaultOption(@Header(ConnPath.HEAD) String header);

    //???????????????
    @POST(ConnPath.SetTopOptionUrl)
    Observable<ResJustStrObj> requestSetTopOption(@Header(ConnPath.HEAD) String header, @Body ReqJustPhoneStockCode req);

    //???????????????
    @POST(ConnPath.DragOptionUrl)
    Observable<ResJustStrObj> requestDragOption(@Header(ConnPath.HEAD) String header, @Body List<ReqDragOption> req);

    //??????????????????
    @POST(ConnPath.FundTongAdvUrl)
    Observable<ResAdv> requestFundTongAdv(@Header(ConnPath.HEAD) String header, @Body ReqJustId req);

    //????????????????????????
    @POST(ConnPath.FundTongSortRankUrl)
    Observable<ResFundTongRank> requestFundTongSortRank(@Header(ConnPath.HEAD) String header, @Body ReqFundTongRank req);

    //????????????????????????
    @POST(ConnPath.FundTongIconUrl)
    Observable<ResFundTongIcon> requestFundTongIcon(@Header(ConnPath.HEAD) String header);

    //???????????????????????????
    @POST(ConnPath.FundTongDefaultUrl)
    Observable<ResFundTongDefault> requestFundTongDefault(@Header(ConnPath.HEAD) String header);

    //???????????????????????????
    @POST(ConnPath.FundTongTradeLevelUrl)
    Observable<ResFundTongTradeLevel> requestFundTongTradeLevel(@Header(ConnPath.HEAD) String header, @Body ReqFundTongTradeLevel req);

    //???????????????????????????
    @POST(ConnPath.FundTongTradeIndexUrl)
    Observable<ResFundFindIndex> requestFundTongTradeIndex(@Header(ConnPath.HEAD) String header, @Body ReqJustCode req);

    //???????????????????????????
    @POST(ConnPath.FundTongTradeListUrl)
    Observable<ResFundFindRank> requestTradeList(@Header(ConnPath.HEAD) String header, @Body ReqJustCodeCurrentPageSize req);

    //?????????????????????
    @POST(ConnPath.FundTongConceptSelectUrl)

    Observable<ResConceptSelect> requestConceptSelectUrl(@Header(ConnPath.HEAD) String header);

    //?????????????????????
    @POST(ConnPath.FundTongConceptIndexUrl)
    Observable<ResFundFindIndex> requestFundTongConceptIndex(@Header(ConnPath.HEAD) String header, @Body ReqJustCode req);

    //?????????????????????
    @POST(ConnPath.FundTongConceptListUrl)
    Observable<ResFundFindRank> requestConceptList(@Header(ConnPath.HEAD) String header, @Body ReqJustCodeCurrentPageSize req);

    //?????????????????????
    @POST(ConnPath.FundTongDetailUrl)
    Observable<ResFundDetail> requestFundTongDetail(@Header(ConnPath.HEAD) String header, @Body ReqFundDetail req);

    //?????????????????????
    @POST(ConnPath.FundTongVerifyLogin)
    Observable<ResFundTongLogin> requestFundTongLogin(@Header(ConnPath.HEAD) String header, @Body ReqFundTongVerifyCodeLogin req);

    //??????????????????
    @POST(ConnPath.MineInfoUrl)
    Observable<ResMineInfo> requestMineInfo(@Header(ConnPath.HEAD) String header, @Body ReqJustUserid req);

    //????????????
    @Multipart
    @POST(ConnPath.ChangeHeadImgUrl)
    Observable<ResJustStrObj> requestChangeHeadImg(@Header(ConnPath.HEAD) String header, @Query("userid") String uid, @Part MultipartBody.Part media);

    //????????????
    @POST(ConnPath.ChangeNickNameUrl)
    Observable<ResChangeNickName> requestChangeNickName(@Header(ConnPath.HEAD) String header, @Body ReqChangeNickName req);

    //????????????
    @POST(ConnPath.SuggestUrl)
    Observable<ResJustStrObj> requestSuggestCommit(@Header(ConnPath.HEAD) String header, @Body ReqSuggest req);

    //????????????
    @POST(ConnPath.MsgBoxStyleUrl)
    Observable<ResMsgBox> requestMsgBoxStyle(@Header(ConnPath.HEAD) String header, @Body ReqJustPhone req);

    //????????????
    @POST(ConnPath.MsgInfoListUrl)
    Observable<ResMsgInfoList> requestMsgInfoList(@Header(ConnPath.HEAD) String header, @Body ReqMsgInfoList req);

    //????????????????????????
    @POST(ConnPath.MsgInfoUpdate)
    Observable<ResJustStrObj> requestMsgInfoUpdate(@Header(ConnPath.HEAD) String header, @Body ReqJustIdPhone req);

    //??????????????????
    @POST(ConnPath.PushInfoUpdate)
    Observable<ResJustStrObj> requestPushInfoUpdate(@Header(ConnPath.HEAD) String header, @Body ReqPushUpdate req);

    //???????????????
    @POST(ConnPath.UpdateNewVersion)
    Observable<ResNewVersion> requestUpdateNewVersion(@Header(ConnPath.HEAD) String header, @Body ReqJustPlateform req);

    //?????????????????????
    @POST(ConnPath.FundTongHuShen)
    Observable<ResFundTongHuShen> requestFundTongHuShen(@Header(ConnPath.HEAD) String header);

    //????????????????????????
    @POST(ConnPath.MyFocusInfoListUrl)
    Observable<ResMyFocusInfoList> requestMyFocusInfo(@Header(ConnPath.HEAD) String header, @Body ReqJustUseridCurrentPageSize req);

    //App????????????
    @POST(ConnPath.AppSetUrl)
    Observable<ResAppSet> requestAppSet(@Header(ConnPath.HEAD) String header);

    //??????????????????
    @POST(ConnPath.AddFundOptionUrl)
    Observable<ResJustStrObj> requestAddFundOption(@Header(ConnPath.HEAD) String header, @Body ReqJustUseridAndFCode req);

    //??????????????????
    @POST(ConnPath.DeleteFundOptionUrl)
    Observable<ResJustStrObj> requestDeleteFundOption(@Header(ConnPath.HEAD) String header, @Body ReqJustUseridAndFCode req);

    //??????????????????
    @POST(ConnPath.MineFundOptionUrl)
    Observable<ResMyFundOption> requestMineFundOption(@Header(ConnPath.HEAD) String header, @Body ReqJustUserid req);

    //????????????
    @POST(ConnPath.CancelUserUrl)
    Observable<ResJustStrObj> requestCancelUser(@Header(ConnPath.HEAD) String header, @Body ReqCancelUser req);

}

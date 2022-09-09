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

    //股扒扒Token
    @GET(ConnPath.GuBbTokenUrl)
    Observable<ResToken> requestToken();

    //基金通Token
    @GET(ConnPath.FundTongTokenUrl)
    Observable<ResToken> requestFundTongToken();

    //股扒扒广告图或小图标
    @POST(ConnPath.GuBbAdvUrl)
    Observable<ResAdv> requestGuBbAdv(@Header(ConnPath.HEAD) String header, @Body ReqJustId req);

    //首页盘前特供
    @POST(ConnPath.MainSpecialSupport)
    Observable<ResMainSup> requestMainSup(@Header(ConnPath.HEAD) String header);

    //首页名师动态
    @POST(ConnPath.MainTeacherDynUrl)
    Observable<ResTeacherDyn> requestMainTDyn(@Header(ConnPath.HEAD) String header, @Body ReqJustUserid req);

    //首页名师直播
    @POST(ConnPath.MainTeacherLiving)
    Observable<ResMainTeacherLiving> requestMainTLiving(@Header(ConnPath.HEAD) String header);

    //我的关注
    @POST(ConnPath.MyFocusUrl)
    Observable<ResMyFocus> requestMainMyFocus(@Header(ConnPath.HEAD) String header, @Body ReqJustUserid req);

    //更多关注
    @POST(ConnPath.MoreFocusUrl)
    Observable<ResMyFocus> requestMainMoreFocus(@Header(ConnPath.HEAD) String header, @Body ReqJustUserid req);

    //关注操作
    @POST(ConnPath.FocusActionUrl)
    Observable<ResJustIntObj> requestFocusAction(@Header(ConnPath.HEAD) String header, @Body ReqJustNidUserid req);

    //一键登录
    @POST(ConnPath.OnKeyLoginUrl)
    Observable<ResLogin> requestOneKeyLogin(@Header(ConnPath.HEAD) String header, @Body ReqPhonePlatform req);

    //获取登录验证码
    @POST(ConnPath.LoginCodeUrl)
    Observable<ResJustStrObj> requestLoginCode(@Header(ConnPath.HEAD) String header, @Body ReqJustPhone req);

    //手机验证码登录
    @POST(ConnPath.LoginByCode)
    Observable<ResLogin> requestLoginByCode(@Header(ConnPath.HEAD) String header, @Body ReqLoginByCode req);

    //分页查询手首页推荐、视频、小视频
    @POST(ConnPath.MainRecoAndVideoUrl)
    Observable<ResMainReco> requestMainRecoAndVideo(@Header(ConnPath.HEAD) String header, @Body ReqMainReco req);

    //分页查询栏目专业
    @POST(ConnPath.LabelSelfUrl)
    Observable<ResLabelSelf> requestLabelSelf(@Header(ConnPath.HEAD) String header, @Body ReqLabelSelf req);

    //普通文章和视频点赞
    @POST(ConnPath.PraiseUrl)
    Observable<ResJustIntObj> requestPraise(@Header(ConnPath.HEAD) String header, @Body ReqJustNidUserid req);

    //首页北向资金买入
    @POST(ConnPath.MainNorthBuy)
    Observable<ResMainNorthBuy> requestMainNorthBuy(@Header(ConnPath.HEAD) String header);

    //首页机构买入
    @POST(ConnPath.MainInstitutionsBuy)
    Observable<ResMainInstitutionBuy> requestInstitutionsBuy(@Header(ConnPath.HEAD) String header);

    //首页融资买入
    @POST(ConnPath.MainFinancingBuy)
    Observable<ResMainFinancingBuy> requestFinancingBuy(@Header(ConnPath.HEAD) String header);

    //首页风险雷达
    @POST(ConnPath.MainRiskRadar)
    Observable<ResMainRiskRadar> requestRiskRadar(@Header(ConnPath.HEAD) String header);

    //名师个人资讯列
    @POST(ConnPath.TeacherSelfUrl)
    Observable<ResTSelf> requestTeacherSelf(@Header(ConnPath.HEAD) String header, @Body ReqTSelf req);

    //名师个人直播列
    @POST(ConnPath.TeacherSelfLivingListUrl)
    Observable<ResTSelfLivingList> requestTeacherSelfLivingList(@Header(ConnPath.HEAD) String header, @Body ReqTSelfLivingList req);

    //栏目资讯详情
    @POST(ConnPath.LabelArticleUrl)
    Observable<ResLabelArt> requestLabelArtDetail(@Header(ConnPath.HEAD) String header, @Body ReqJustNidUserid req);

    //名师资讯详情
    @POST(ConnPath.TeacherArticleUrl)
    Observable<ResTeacherArt> requestTeacherArtDetail(@Header(ConnPath.HEAD) String header, @Body ReqJustNidUserid req);

    //K线秘籍类型
    @POST(ConnPath.KLineTypeUrl)
    Observable<ResKLineType> requestKLineType(@Header(ConnPath.HEAD) String header);

    //K线秘籍文章资讯
    @POST(ConnPath.KLineArtListUrl)
    Observable<ResKLineList> requestKLineArtList(@Header(ConnPath.HEAD) String header, @Body ReqKLineList req);

    //K线秘籍文章资讯
    @POST(ConnPath.KLineVideoListUrl)
    Observable<ResKLineList> requestKLineVideoList(@Header(ConnPath.HEAD) String header, @Body ReqJustPhone req);

    //K线秘籍学习和点赞
    @POST(ConnPath.KLineLearnPraiseUrl)
    Observable<ResJustIntObj> requestKLineLearnPraise(@Header(ConnPath.HEAD) String header, @Body ReqKLineLearnPraise req);

    //K线秘籍文章详情
    @POST(ConnPath.KLineArticleDetailUrl)
    Observable<ResKLineDetails> requestKLineArtDetails(@Header(ConnPath.HEAD) String header, @Body ReqJustIdPhone req);

    //K线秘籍视频详情
    @POST(ConnPath.KLineVideoDetailUrl)
    Observable<ResKLineDetails> requestKLineVideoDetails(@Header(ConnPath.HEAD) String header, @Body ReqJustIdPhone req);

    //首页越声快讯
    @POST(ConnPath.FlashListUrl)
    Observable<ResFlash> requestFlashList(@Header(ConnPath.HEAD) String header, @Body ReqFlash req);

    //竖屏滑动视频列表
    @POST(ConnPath.SmallScrollVideoUrl)
    Observable<ResScrollSmallVideo> requestSmallScrollVideoList(@Header(ConnPath.HEAD) String header, @Body ReqScrollSmallVideo req);

    //名师专栏
    @POST(ConnPath.TeacherAllUrl)
    Observable<ResTeacherAll> requestTeacherAllList(@Header(ConnPath.HEAD) String header, @Body ReqJustUserid req);

    //历史搜索
    @POST(ConnPath.SearchHistoryUrl)
    Observable<ResSearchHis> requestSearchHistory(@Header(ConnPath.HEAD) String header, @Body ReqJustPhone req);

    //综合搜索
    @POST(ConnPath.SearchNormalUrl)
    Observable<ResSearchNor> requestSearchNormal(@Header(ConnPath.HEAD) String header, @Body ReqJustKeywordPhone req);

    //股票更多搜索
    @POST(ConnPath.SearchStockMoreUrl)
    Observable<ResSearchStockMore> requestSearchStockMore(@Header(ConnPath.HEAD) String header, @Body ReqJustKeywordPhone req);

    //基金更多搜索
    @POST(ConnPath.SearchFundMoreUrl)
    Observable<ResSearchFundMore> requestSearchFundMore(@Header(ConnPath.HEAD) String header, @Body ReqJustKeywordPhone req);

    //相关内容更多搜索
    @POST(ConnPath.SearchArticleMoreUrl)
    Observable<ResSearchArticleMore> requestSearchArticleMore(@Header(ConnPath.HEAD) String header, @Body ReqJustKeywordPhone req);

    //按日期查询盘前特供
    @POST(ConnPath.CheckSpecialSupByDateUrl)
    Observable<ResSpecialSupport> requestSpecialSupByDate(@Header(ConnPath.HEAD) String header, @Body ReqJustPhoneDate req);

    //盘前特供详情
    @POST(ConnPath.SpecialSupDetailUrl)
    Observable<ResSpecialSupDetail> requestSpecialSupDetail(@Header(ConnPath.HEAD) String header, @Body ReqJustPhoneSId req);

    //盘前特供点赞
    @POST(ConnPath.SpecialSupPraise)
    Observable<ResJustIntObj> requestSpecialSupPraise(@Header(ConnPath.HEAD) String header, @Body ReqJustPhoneSId req);

    //添加自选
    @POST(ConnPath.AddMyOptionUrl)
    Observable<ResJustStrObj> requestAddOption(@Header(ConnPath.HEAD) String header, @Body ReqPhoneWithSCodes req);

    //删除自选
    @POST(ConnPath.DeleteOptionUrl)
    Observable<ResJustStrObj> requestDeleteOption(@Header(ConnPath.HEAD) String header, @Body ReqPhoneWithSCodes req);

    //我的自选股列表
    @POST(ConnPath.MyOptionUrl)
    Observable<ResMyOption> requestMyOption(@Header(ConnPath.HEAD) String header, @Body ReqJustPhone req);

    //默认选股
    @POST(ConnPath.DefaultOptionUrl)
    Observable<ResDefaultOption> requestDefaultOption(@Header(ConnPath.HEAD) String header);

    //置顶自选股
    @POST(ConnPath.SetTopOptionUrl)
    Observable<ResJustStrObj> requestSetTopOption(@Header(ConnPath.HEAD) String header, @Body ReqJustPhoneStockCode req);

    //拖动自选股
    @POST(ConnPath.DragOptionUrl)
    Observable<ResJustStrObj> requestDragOption(@Header(ConnPath.HEAD) String header, @Body List<ReqDragOption> req);

    //基金通广告图
    @POST(ConnPath.FundTongAdvUrl)
    Observable<ResAdv> requestFundTongAdv(@Header(ConnPath.HEAD) String header, @Body ReqJustId req);

    //基金通排行榜排序
    @POST(ConnPath.FundTongSortRankUrl)
    Observable<ResFundTongRank> requestFundTongSortRank(@Header(ConnPath.HEAD) String header, @Body ReqFundTongRank req);

    //基金通首页小图标
    @POST(ConnPath.FundTongIconUrl)
    Observable<ResFundTongIcon> requestFundTongIcon(@Header(ConnPath.HEAD) String header);

    //基金通默认行业数据
    @POST(ConnPath.FundTongDefaultUrl)
    Observable<ResFundTongDefault> requestFundTongDefault(@Header(ConnPath.HEAD) String header);

    //基金通默认行业等级
    @POST(ConnPath.FundTongTradeLevelUrl)
    Observable<ResFundTongTradeLevel> requestFundTongTradeLevel(@Header(ConnPath.HEAD) String header, @Body ReqFundTongTradeLevel req);

    //基金通默认行业指数
    @POST(ConnPath.FundTongTradeIndexUrl)
    Observable<ResFundFindIndex> requestFundTongTradeIndex(@Header(ConnPath.HEAD) String header, @Body ReqJustCode req);

    //基金通默认行业列表
    @POST(ConnPath.FundTongTradeListUrl)
    Observable<ResFundFindRank> requestTradeList(@Header(ConnPath.HEAD) String header, @Body ReqJustCodeCurrentPageSize req);

    //基金通概念选项
    @POST(ConnPath.FundTongConceptSelectUrl)

    Observable<ResConceptSelect> requestConceptSelectUrl(@Header(ConnPath.HEAD) String header);

    //基金通概念指数
    @POST(ConnPath.FundTongConceptIndexUrl)
    Observable<ResFundFindIndex> requestFundTongConceptIndex(@Header(ConnPath.HEAD) String header, @Body ReqJustCode req);

    //基金通概念列表
    @POST(ConnPath.FundTongConceptListUrl)
    Observable<ResFundFindRank> requestConceptList(@Header(ConnPath.HEAD) String header, @Body ReqJustCodeCurrentPageSize req);

    //基金通基金详情
    @POST(ConnPath.FundTongDetailUrl)
    Observable<ResFundDetail> requestFundTongDetail(@Header(ConnPath.HEAD) String header, @Body ReqFundDetail req);

    //基金通隐形登录
    @POST(ConnPath.FundTongVerifyLogin)
    Observable<ResFundTongLogin> requestFundTongLogin(@Header(ConnPath.HEAD) String header, @Body ReqFundTongVerifyCodeLogin req);

    //我的用户信息
    @POST(ConnPath.MineInfoUrl)
    Observable<ResMineInfo> requestMineInfo(@Header(ConnPath.HEAD) String header, @Body ReqJustUserid req);

    //更换头像
    @Multipart
    @POST(ConnPath.ChangeHeadImgUrl)
    Observable<ResJustStrObj> requestChangeHeadImg(@Header(ConnPath.HEAD) String header, @Query("userid") String uid, @Part MultipartBody.Part media);

    //更换昵称
    @POST(ConnPath.ChangeNickNameUrl)
    Observable<ResChangeNickName> requestChangeNickName(@Header(ConnPath.HEAD) String header, @Body ReqChangeNickName req);

    //产品建议
    @POST(ConnPath.SuggestUrl)
    Observable<ResJustStrObj> requestSuggestCommit(@Header(ConnPath.HEAD) String header, @Body ReqSuggest req);

    //消息类型
    @POST(ConnPath.MsgBoxStyleUrl)
    Observable<ResMsgBox> requestMsgBoxStyle(@Header(ConnPath.HEAD) String header, @Body ReqJustPhone req);

    //消息类型
    @POST(ConnPath.MsgInfoListUrl)
    Observable<ResMsgInfoList> requestMsgInfoList(@Header(ConnPath.HEAD) String header, @Body ReqMsgInfoList req);

    //消息列表更新已读
    @POST(ConnPath.MsgInfoUpdate)
    Observable<ResJustStrObj> requestMsgInfoUpdate(@Header(ConnPath.HEAD) String header, @Body ReqJustIdPhone req);

    //推送更新已读
    @POST(ConnPath.PushInfoUpdate)
    Observable<ResJustStrObj> requestPushInfoUpdate(@Header(ConnPath.HEAD) String header, @Body ReqPushUpdate req);

    //更新新版本
    @POST(ConnPath.UpdateNewVersion)
    Observable<ResNewVersion> requestUpdateNewVersion(@Header(ConnPath.HEAD) String header, @Body ReqJustPlateform req);

    //基金通沪深三百
    @POST(ConnPath.FundTongHuShen)
    Observable<ResFundTongHuShen> requestFundTongHuShen(@Header(ConnPath.HEAD) String header);

    //已关注的资讯列表
    @POST(ConnPath.MyFocusInfoListUrl)
    Observable<ResMyFocusInfoList> requestMyFocusInfo(@Header(ConnPath.HEAD) String header, @Body ReqJustUseridCurrentPageSize req);

    //App特殊设置
    @POST(ConnPath.AppSetUrl)
    Observable<ResAppSet> requestAppSet(@Header(ConnPath.HEAD) String header);

    //添加自选基金
    @POST(ConnPath.AddFundOptionUrl)
    Observable<ResJustStrObj> requestAddFundOption(@Header(ConnPath.HEAD) String header, @Body ReqJustUseridAndFCode req);

    //删除自选基金
    @POST(ConnPath.DeleteFundOptionUrl)
    Observable<ResJustStrObj> requestDeleteFundOption(@Header(ConnPath.HEAD) String header, @Body ReqJustUseridAndFCode req);

    //我的自选基金
    @POST(ConnPath.MineFundOptionUrl)
    Observable<ResMyFundOption> requestMineFundOption(@Header(ConnPath.HEAD) String header, @Body ReqJustUserid req);

    //注销用户
    @POST(ConnPath.CancelUserUrl)
    Observable<ResJustStrObj> requestCancelUser(@Header(ConnPath.HEAD) String header, @Body ReqCancelUser req);

}

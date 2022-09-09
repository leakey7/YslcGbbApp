package com.gzyslczx.yslc.tools;

public class ConnPath {

    //股扒扒域名
    public static final String GuBbMain = "http://app.yslc8.com/headline_api/";
    //基金通域名
    public static final String FundTongMain = "https://gbb.cffs927.com/FundSystemAPI/";
    //头部验证
    public static final String HEAD = "Authorization";
    public static final String HeaderValue = "Bearer ";
    //个股详情地址
    public static final String StockDetailUrl = "http://app.yslc8.com/StockDetail/Announcement/List.aspx?stockcode=";
    //股扒扒Token接口
    public static final String GuBbTokenUrl = "api/UserInfo/LoginToken?username=C2L0E1N6T&password=a8s9aucobtb8f088";
    //基金通Token接口
    public static final String FundTongTokenUrl = "api/UserInfo/LoginToken?username=C8L0E1N6T&password=a8s9aucobtb8f028";
    //股扒扒广告图标信息
    public static final String GuBbAdvUrl = "api/UserInfo/GetAdvertInfoList";
    //首页盘前特供列表信息
    public static final String MainSpecialSupport = "api/UserInfo/GetTgStockInfo";
    //首页名师动态
    public static final String MainTeacherDynUrl = "api/UserInfo/GetHeadLines";
    //首页名师直播
    public static final String MainTeacherLiving = "api/UserInfo/GetVideoService";
    //我的关注
    public static final String MyFocusUrl = "api/UserInfo/FocusInfo";
    //更多关注
    public static final String MoreFocusUrl = "api/UserInfo/MoreFocusInfo";
    //关注操作
    public static final String FocusActionUrl = "api/UserInfo/FocusTeacherInfo";
    //风险提示
    public static final String RiskXY = "http://app.yslc8.com/pfxh5/xy/page02.html";
    //用户协议
    public static final String UserXY = "http://app.yslc8.com/pfxh5/xy/page04.html";
    //隐私政策
    public static final String PrivateXY= "https://gbb.cffs927.com/headlines_admin/xy/page03.html";
    //关于我们
    public final static String AboutUs = "https://gbb.cffs927.com/headline_api/aspx/contact.html";
    //第三方须知
    public final static String ThirdUserUrl = "http://app.yslc8.com/pfxh5/xy/page01.html";
    //一键登录
    public static final String OnKeyLoginUrl = "api/UserInfo/OneClickLogin";
    //手机验证码登录
    public static final String LoginByCode = "api/UserInfo/UserCodeLogin";
    //获取登录验证码
    public static final String LoginCodeUrl = "api/UserInfo/SendMsgCode";
    //首页推荐、视频、小视频
    public static final String MainRecoAndVideoUrl = "api/UserInfo/GetNewsByTypePages";
    //栏目专页信息
    public static final String LabelSelfUrl = "api/UserInfo/GetNewsByLabelPage";
    //普通文章和视频点赞
    public static final String PraiseUrl = "api/UserInfo/LikeNews";
    //首页北向资金买入信息
    public static final String MainNorthBuy = "api/UserInfo/GetNorthBuyModel";
    //首页机构买入信息
    public static final String MainInstitutionsBuy = "api/UserInfo/GetInstutionBuyModel";
    //首页融资买入信息
    public static final String MainFinancingBuy = "api/UserInfo/GetFinanceBuyModel";
    //首页风险雷达
    public static final String MainRiskRadar = "api/UserInfo/GetRiskRadar";
    //名师个人信息
    public static final String TeacherSelfUrl = "api/UserInfo/GetTeacherNewsPages";
    //名师个人直播列
    public static final String TeacherSelfLivingListUrl = "api/UserInfo/GetLiveByTidPage";
    //栏目资讯详情
    public static final String LabelArticleUrl = "api/UserInfo/GetColumnNewsDetail";
    //资讯分享地址
    public static final String ShareUrl = "http://yslcwx.yslcw.com/ProductUi2019/online/content_2.aspx?newsid=";
    //名师资讯详情
    public static final String TeacherArticleUrl = "api/UserInfo/GetTNewsDetail";
    //K线秘籍类型
    public static final String KLineTypeUrl = "api/UserInfo/GetKxmjCategory";
    //K线秘籍列表
    public static final String KLineArtListUrl = "api/UserInfo/GetKxmjArticleInfo";
    //K线秘籍视频列表
    public static final String KLineVideoListUrl = "api/UserInfo/GetKxmjVideoInfo";
    //K线秘籍学习和点赞
    public static final String KLineLearnPraiseUrl = "api/UserInfo/KxmjUserAction";
    //K线秘籍视频详情
    public static final String KLineVideoDetailUrl = "api/UserInfo/GetKxmjVideoDetail";
    //K线秘籍文章详情
    public static final String KLineArticleDetailUrl = "api/UserInfo/GetKxmjArticleDetail";
    //首页越声快讯
    public static final String FlashListUrl = "api/UserInfo/GetZxtInfo";
    //竖屏滑动视频列表
    public static final String SmallScrollVideoUrl = "api/UserInfo/GetNewsVedioByPage";
    //名师专栏
    public static final String TeacherAllUrl = "api/UserInfo/GetTeacherColumnList";
    //搜索历史
    public static final String SearchHistoryUrl = "api/UserInfo/GetSearchedInfo";
    //综合搜索
    public static final String SearchNormalUrl = "api/UserInfo/GetSearchInfo";
    //股票更多搜索
    public static final String SearchStockMoreUrl = "api/UserInfo/SearchStockInfo";
    //基金更多搜索
    public static final String SearchFundMoreUrl = "api/UserInfo/GetFundList";
    //相关内容更多搜索
    public static final String SearchArticleMoreUrl = "api/UserInfo/SearchArtInfo";
    //按时期查询盘前特供
    public static final String CheckSpecialSupByDateUrl = "api/UserInfo/GetTgStockByDateInfo";
    //查询盘前特供详情
    public static final String SpecialSupDetailUrl = "api/UserInfo/GetTgStockInfoById";
    //盘前特供点赞
    public static final String SpecialSupPraise = "api/UserInfo/LikeTg";
    //默认自选股
    public static final String DefaultOptionUrl = "api/UserInfo/GetTjMyStockList";
    //我的自选股列表
    public static final String MyOptionUrl = "api/UserInfo/GetMyStockListModel";
    //批量添加自选股
    public static final String AddMyOptionUrl = "api/UserInfo/AddMyStocksInfo";
    //批量删除自选股
    public static final String DeleteOptionUrl = "api/UserInfo/DelMyStock";
    //置顶我的自选股
    public static final String SetTopOptionUrl = "api/UserInfo/SetMyStockTop";
    //拖动我的自选股
    public static final String DragOptionUrl = "api/UserInfo/SetSortMyStock";
    //基金通广告图
    public static final String FundTongAdvUrl = "api/UserInfo/GetAdvertInfoList";
    //基金通小图标
    public static final String FundTongIconUrl = "api/UserInfo/GetAdvInfoList";
    //基金通正倒序排行
    public static final String FundTongSortRankUrl = "api/UserInfo/GetFundPhbPages";
    //基金通默认行业和概念
    public static final String FundTongDefaultUrl = "api/UserInfo/GetDefaultCode";
    //基金通行业分级列表
    public static final String FundTongTradeLevelUrl = "api/UserInfo/GetHyTypeInfo";
    //基金通行业指数
    public static final String FundTongTradeIndexUrl = "api/UserInfo/GetHyIndexInfo";
    //基金通行业基金列表
    public static final String FundTongTradeListUrl = "api/UserInfo/GetHyFundInfoPage";
    //基金通概念基金列表
    public static final String FundTongConceptListUrl = "api/UserInfo/GetGnFundInfoPage";
    //基金通概念筛选基金
    public static final String FundTongConceptSelectUrl = "api/UserInfo/GetGnInfo";
    //基金通概念指数
    public static final String FundTongConceptIndexUrl = "api/UserInfo/GetGnIndexInfo";
    //基金通基金详情
    public static final String FundTongDetailUrl = "api/UserInfo/GetFundDetailInfo";
    //基金通验证码隐形登录接口
    public static final String FundTongVerifyLogin = "api/UserInfo/UserCodeLogin";
    //我的页面用户信息
    public static final String MineInfoUrl = "api/UserInfo/GetUserModel";
    //用户更换头像
    public static final String ChangeHeadImgUrl = "api/UserInfo/UpHeadImg";
    //用户更换昵称
    public static final String ChangeNickNameUrl = "api/UserInfo/UpdateNickName";
    //产品建议
    public static final String SuggestUrl = "api/UserInfo/AddFeedbackInfo";
    //消息类型
    public static final String MsgBoxStyleUrl = "api/UserInfo/GetPushMsgTypeList";
    //消息列表
    public static final String MsgInfoListUrl = "api/UserInfo/GetMsgPageByType";
    //消息列表更新已读
    public static final String MsgInfoUpdate = "api/UserInfo/UpdateMsgRead";
    //推送更新已读
    public static final String PushInfoUpdate = "/api/UserInfo/UpdateMsgReaded";
    //获取新版本
    public static final String UpdateNewVersion = "api/UserInfo/GetNewVersion";
    //基金通沪深三百
    public static final String FundTongHuShen = "api/UserInfo/GetFundImgInfo";
    //盘前特供分享地址
    public static final String SpecialSupShare = "http://app.yslc8.com/pfxh5/home/tgshare";
    //主题资讯，投顾论市分享地址
    public static final String NormalLabelShareUrl = "http://app.yslc8.com/pfxh5/home/artshare?newsid=";
    //越声快讯分享地址
    public static final String YsFlashShareUrl = "http://app.yslc8.com/pfxh5/home/yskxshare?newsid=";
    //大视频分享地址
    public static final String BigVideoShareUrl = "http://app.yslc8.com/pfxh5/home/videoshare?newsid=";
    //小视频分享地址
    public static final String SmallVideoShareUrl = "http://app.yslc8.com/pfxh5/home/svideoshare?newsid=";
    //名师文章分享地址
    public static final String TeacherArticleShareUrl = "http://app.yslc8.com/pfxh5/home/teachershare?newsid=";
    //首页关注的越声栏目资讯
    public static final String MyFocusInfoListUrl = "api/UserInfo/GetNewsByFocusPages";
    //应用特殊设置
    public static final String AppSetUrl = "api/UserInfo/GetAPPSet";
    //风险测评
    public static final String RiskTestUrl = "http://yszxv.etz927.com/Identity_App/App/index.aspx?pagetype=1";
    //添加自选基金地址
    public static final String AddFundOptionUrl = "api/UserInfo/AddMyFundInfo";
    //删除自选资金
    public static final String DeleteFundOptionUrl = "api/UserInfo/CancelMyFundInfo";
    //我的自选资金
    public static final String MineFundOptionUrl = "api/UserInfo/GetMyFundList";
    //注销用户
    public static final String CancelUserUrl = "api/UserInfo/CancelRegist";
}

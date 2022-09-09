package com.gzyslczx.yslc.modes.response;

public class ResMainRecoInfo {

    /*
     * @param ContentType 1文章；2观点；3音频；4视频
     * @param VideoType 0:横屏视频 1：竖屏视频
     * @param ViewUser 查看权限(1.全网，2.客户(登录)，3.用户（购买用户）
     * @param IsBuy ViewUser=3时才奏效
     * @param Status 1为直接可以点击进去看  2为需要登录  3需要购买 4为需要测评
     * @param CheckUrl Status=4时才奏效
     * @param ArrImg 多缩略图
     * @param Img 单缩略图
     * */

    private String NewsId, Title, Describe, Content, Img, FileUrl, AddDateTime, DateInfo, Source, UserId, Url,
            Author, AuthorImg, NewLabel, BuyWords, CheckUrl, RiskTips, LabelPrice, LabelName, LabelHeadImg, LabelId;
    private int ContentType, Reading, Praise, ViewUser, NewPrice, Unlock, Status, Tj, Jhtj, VideoType;
    private boolean Examine, Active, Important, IsLike, IsCollection, IsFocus, IsBuy, HasLabel;
    private String[] ArrImg;

    public void setFocus(boolean focus) {
        IsFocus = focus;
    }

    public void setPraise(int praise) {
        Praise = praise;
    }

    public void setLike(boolean like) {
        IsLike = like;
    }

    public String getNewsId() {
        return NewsId;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescribe() {
        return Describe;
    }

    public String getContent() {
        return Content;
    }

    public String getImg() {
        return Img;
    }

    public String getFileUrl() {
        return FileUrl;
    }

    public String getAddDateTime() {
        return AddDateTime;
    }

    public String getDateInfo() {
        return DateInfo;
    }

    public String getSource() {
        return Source;
    }

    public String getUserId() {
        return UserId;
    }

    public String getUrl() {
        return Url;
    }

    public String getAuthor() {
        return Author;
    }

    public String getAuthorImg() {
        return AuthorImg;
    }

    public String getNewLabel() {
        return NewLabel;
    }

    public String getBuyWords() {
        return BuyWords;
    }

    public String getCheckUrl() {
        return CheckUrl;
    }

    public String getRiskTips() {
        return RiskTips;
    }

    public String getLabelPrice() {
        return LabelPrice;
    }

    public String getLabelName() {
        return LabelName;
    }

    public String getLabelHeadImg() {
        return LabelHeadImg;
    }

    public int getContentType() {
        return ContentType;
    }

    public int getReading() {
        return Reading;
    }

    public int getPraise() {
        return Praise;
    }

    public int getViewUser() {
        return ViewUser;
    }

    public int getNewPrice() {
        return NewPrice;
    }

    public int getUnlock() {
        return Unlock;
    }

    public int getStatus() {
        return Status;
    }

    public int getTj() {
        return Tj;
    }

    public int getJhtj() {
        return Jhtj;
    }

    public int getVideoType() {
        return VideoType;
    }

    public boolean isExamine() {
        return Examine;
    }

    public boolean isActive() {
        return Active;
    }

    public boolean isImportant() {
        return Important;
    }

    public boolean isLike() {
        return IsLike;
    }

    public boolean isCollection() {
        return IsCollection;
    }

    public boolean isFocus() {
        return IsFocus;
    }

    public boolean isBuy() {
        return IsBuy;
    }

    public boolean isHasLabel() {
        return HasLabel;
    }

    public String[] getArrImg() {
        return ArrImg;
    }

    public String getLabelId() {
        return LabelId;
    }
}

package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResLabelArtDetail {

    private String NewsId, Title, Describe, Content, Img, FileUrl, AddDateTime, DateInfo, Source, UserId, Author, AuthorImg, NewLabel, BuyWords,
            CheckUrl, RiskTips, LabelPrice, LabelName, LabelHeadImg, Url, LabelId;
    private int ContentType, Reading, Praise, ViewUser, NewPrice, Unlock, Status, Tj, Jhtj, VideoType;
    private String[] ArrImg;
    private boolean Examine, Active, Important, IsLike, IsCollection, IsFocus, IsBuy, HasLabel;
    private List<ResArtDetailsStock> RecList;

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

    public String getUrl() {
        return Url;
    }

    public String getLabelId() {
        return LabelId;
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

    public String[] getArrImg() {
        return ArrImg;
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

    public List<ResArtDetailsStock> getRecList() {
        return RecList;
    }
}

package com.gzyslczx.yslc.modes.response;

public class ResMainTeacherLivingObj {

    /*
    * 直播状态：Status=1未开始;  2为直播中;  3为已结束;
    * */

    private int VId, vtype, currentstate;
    private String Cha_id, title, description, starttime, endtime, date, dateadd, img, state, videourl;


    public int getVId() {
        return VId;
    }

    public int getVtype() {
        return vtype;
    }

    public int getCurrentstate() {
        return currentstate;
    }

    public String getCha_id() {
        return Cha_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getDate() {
        return date;
    }

    public String getDateadd() {
        return dateadd;
    }

    public String getImg() {
        return img;
    }

    public String getState() {
        return state;
    }

    public String getVideourl() {
        return videourl;
    }
}

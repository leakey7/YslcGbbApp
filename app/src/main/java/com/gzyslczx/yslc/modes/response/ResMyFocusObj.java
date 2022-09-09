package com.gzyslczx.yslc.modes.response;

public class ResMyFocusObj {

    private String Tid, Name, Img, Desc, Remark;
    private boolean IsFocus;

    public ResMyFocusObj(String tid, String name, String img, String desc, String remark, boolean isFocus) {
        Tid = tid;
        Name = name;
        Img = img;
        Desc = desc;
        Remark = remark;
        IsFocus = isFocus;
    }

    public String getTid() {
        return Tid;
    }

    public String getName() {
        return Name;
    }

    public String getImg() {
        return Img;
    }

    public String getDesc() {
        return Desc;
    }

    public String getRemark() {
        return Remark;
    }

    public boolean isFocus() {
        return IsFocus;
    }

    public void setFocus(boolean focus) {
        IsFocus = focus;
    }
}

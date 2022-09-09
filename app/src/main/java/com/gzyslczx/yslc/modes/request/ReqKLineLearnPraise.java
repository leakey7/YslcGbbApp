package com.gzyslczx.yslc.modes.request;

public class ReqKLineLearnPraise {


    /*
    * typeinfo 1为视频类型 2为文章类型 3为视频 4为文章
    * id 视频类型或文章类型或视频或文章id
    * */
    private String phone;
    private int id, typeinfo, state;

    public ReqKLineLearnPraise(String phone, int id, int state, int typeinfo) {
        this.phone = phone;
        this.id = id;
        this.state = state;
        this.typeinfo = typeinfo;
    }
}

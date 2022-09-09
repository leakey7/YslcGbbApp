package com.gzyslczx.yslc.modes.response;

import java.io.Serializable;

public class ResKLineTypeObj {

    /*
     * TypeInfo：1为视频 2为文章
     * */
    private int CateId, TypeInfo, SortInfo;
    private String CateName;

    public int getCateId() {
        return CateId;
    }

    public int getTypeInfo() {
        return TypeInfo;
    }

    public int getSortInfo() {
        return SortInfo;
    }

    public String getCateName() {
        return CateName;
    }
}

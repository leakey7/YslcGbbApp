package com.gzyslczx.yslc.tools.yourui;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yourui.sdk.message.entity.RealTimeComplement;
import com.yourui.sdk.message.json.JsonFormat;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 项目名称：HsMarketSdkDemo
 * 类描述：
 * 创建人：zhouY
 * 创建时间：2018/12/21
 * 修改人：zhouY
 * 修改时间：2018/12/21
 * 修改备注：
 */
public class DefaultJsonFormat implements JsonFormat {

    private Gson gsonFormat;

    public DefaultJsonFormat() {
        gsonFormat = new Gson();
        gsonFormat.fromJson("",new TypeToken<List<RealTimeComplement>>(){}.getType());
    }

    @Override
    public String toJsonString(Object object) {
        String mFormatStr = null;
        if (null != gsonFormat) {
            mFormatStr = gsonFormat.toJson(object);
        }
        return mFormatStr;
    }

    @Override
    public <T> T fromJson(String json, Class<T> classOfT) {
        if (null != gsonFormat) {
            return gsonFormat.fromJson(json, classOfT);
        }
        return null;
    }

    @Override
    public <T> T fromJson(String json, Type typeOfT) {
        if (null != gsonFormat) {
            return gsonFormat.fromJson(json, typeOfT);
        }
        return null;
    }
}

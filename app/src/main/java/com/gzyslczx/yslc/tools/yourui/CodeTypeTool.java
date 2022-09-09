package com.gzyslczx.yslc.tools.yourui;

public class CodeTypeTool {

    public static int MatchingCodeType(String stockCode) {
        String str2 = stockCode.substring(0, 2);
        int typeCode = -1;
        if (str2.equals("00")){
            String str3 = stockCode.substring(0, 3);
            if (str3.equals("001") || str3.equals("002") || str3.equals("003")){
                typeCode = 4614;
            }else {
                typeCode = 4609;
            }
        }else {
            if (str2.equals("60")){
                typeCode = 4353;
            }else if (str2.equals("30")){
                typeCode = 4621;
            }else if (str2.equals("68")){
                typeCode = 4367;
            }
        }
        //返回-1时匹配不到相应股票代码类型
        return typeCode;
    }

    public static String MatchingTypeName(int code){
        String name = null;
        if (code==4353){
            name = "上海A股";
        } else if (code==4609){
            name = "深圳A股";
        } else if (code==4865){
            name = "上海、深圳A股";
        } else if (code==4614){
            name = "深圳中小板股";
        } else if (code==4621){
            name = "创业板";
        } else if (code==4358 || code==4367){
            name = "科创板";
        }
        //返回null时匹配不到相应类型名称
        return name;
    }

}

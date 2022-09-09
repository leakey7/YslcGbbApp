package com.gzyslczx.yslc.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTool {

    private static SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat weekFormat = new SimpleDateFormat("E");

    /*
    * 获取当天日期
    * */
    public static String GetTodayDate(){
        Date date = new Date();
        return ymdFormat.format(date);
    }

    /*
    * 获取星期几
    * */
    public static String GetWeek(String info){
        try {
            Date date = ymdFormat.parse(info);
            return weekFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    * 日期换算成毫秒
    * */
    public static long GetDateLong(String info){
        try {
            Date date = ymdFormat.parse(info);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /*
    * 是否超过23小时
    * */
    public static boolean MoreThan23Hour(String oldTime){
        long old =  DateTool.GetDateLong(oldTime);
        long today = DateTool.GetDateLong(DateTool.GetTodayDate());
        long cha = today-old;
        double result = cha * 1.0 / (1000 * 60 * 60);
        if (result>23)
            return true;
        return false;
    }


    /*
     * 是否超过hour小时
     * */
    public static boolean MoreThanHour(String oldTime, int hour){
        long old =  DateTool.GetDateLong(oldTime);
        long today = DateTool.GetDateLong(DateTool.GetTodayDate());
        long cha = today-old;
        double result = cha * 1.0 / (1000 * 60 * 60);
        if (result>hour) {
            return true;
        }
        return false;
    }

    public static int GetYear(String date){
        int year=0;
        try {
            Date yDate = ymdFormat.parse(date);
            Calendar yCalendar = Calendar.getInstance();
            yCalendar.setTime(yDate);
            year = yCalendar.get(Calendar.YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return year;
    }

    public static int GetMonth(String date){
        int month=0;
        try {
            Date mDate = ymdFormat.parse(date);
            Calendar yCalendar = Calendar.getInstance();
            yCalendar.setTime(mDate);
            month = yCalendar.get(Calendar.MONTH)+1;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return month;
    }

    public static int GetDay(String date){
        int day=0;
        try {
            Date mDate = ymdFormat.parse(date);
            Calendar yCalendar = Calendar.getInstance();
            yCalendar.setTime(mDate);
            day = yCalendar.get(Calendar.DATE);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return day;
    }

}

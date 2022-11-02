package com.baohulu.framework.common;

import com.baohulu.framework.basic.consts.Time;
import com.baohulu.framework.common.utils.DateUtils;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * @author heqing
 * @date 2022/10/12 16:17
 */
public class TestDate {

    String date = "2022-05-01 07:15:00";

    @Test
    public void checkDate() {
        boolean isDate = DateUtils.checkDate(date, Time.YYYY_MM_DD);
        System.out.println("-->: " + isDate);
    }

    @Test
    public void to() {
        Date oldDate = DateUtils.toDate(date);
        System.out.println("toDate -->: " + oldDate);

        LocalDateTime localDateTime = DateUtils.toLocalDateTime(oldDate);
        System.out.println("localDateTime -->: " + localDateTime);

        Date newDate = DateUtils.toDate(localDateTime);
        System.out.println("toDate -->: " + newDate);

        String dateString = DateUtils.toString(newDate);
        System.out.println("dateString -->: " + dateString);
    }

    @Test
    public void betweenDate() {
        Date oldDate = DateUtils.toDate(date);
        Date newDate = new Date();

        long millisecond = DateUtils.getMillisecondBetweenDate(oldDate, newDate);
        System.out.println("毫秒数 -->: " + millisecond);

        int day = DateUtils.getDaysBetweenDate(oldDate, newDate);
        System.out.println("天数 -->: " + day);

        int month = DateUtils.getMonthBetweenDate(oldDate, newDate);
        System.out.println("月数 -->: " + month);

        int year = DateUtils.getYearBetweenDate(oldDate, newDate);
        System.out.println("年数 -->: " + year);
    }

    @Test
    public void pastOrFuture() {
        Date oldDate = new Date();
        System.out.println("当前时间 -->: " + DateUtils.toString(oldDate));

        Date newDate = DateUtils.getPastOrFutureYear(oldDate, -1);
        System.out.println("一年前 -->: " + DateUtils.toString(newDate));

        newDate = DateUtils.getPastOrFutureMonth(oldDate, -2);
        System.out.println("两月前 -->: " + DateUtils.toString(newDate));

        newDate = DateUtils.getPastOrFutureDay(oldDate, -3);
        System.out.println("三日前 -->: " + DateUtils.toString(newDate));

        newDate = DateUtils.getPastOrFutureHour(oldDate, 1);
        System.out.println("一小时后 -->: " + DateUtils.toString(newDate));

        newDate = DateUtils.getPastOrFutureMinute(oldDate, 2);
        System.out.println("两分钟后 -->: " + DateUtils.toString(newDate));
    }

    @Test
    public void getTime() {
        Date oldDate = new Date();
        System.out.println("当前时间 -->: " + DateUtils.toString(oldDate));

        int year = DateUtils.getYearTime(oldDate);
        System.out.println("年 -->: " + year);

        int month = DateUtils.getMonthTime(oldDate);
        System.out.println("月 -->: " + month);

        int day = DateUtils.getDayTime(oldDate);
        System.out.println("日 -->: " + day);

        int hour = DateUtils.geHourTime(oldDate);
        System.out.println("时 -->: " + hour);

        int minute = DateUtils.geMinuteTime(oldDate);
        System.out.println("分 -->: " + minute);

        int second = DateUtils.getTimeField(Calendar.SECOND, oldDate);
        System.out.println("秒 -->: " + second);
    }

    @Test
    public void chineseName() {
        Date oldDate = new Date();
        System.out.println("当前时间 -->: " + DateUtils.toString(oldDate));

        String name = DateUtils.getChineseMonthName(oldDate);
        System.out.println("月 --> " + name);

        name = DateUtils.getChineseWeekName(oldDate);
        System.out.println("星期 --> " + name);
    }

    @Test
    public void minOrMax() {
        Date date1 = new Date();
        Date date2 =  DateUtils.getPastOrFutureHour(date1, -1);
        Date date3 =  DateUtils.getPastOrFutureHour(date1, 1);

        System.out.println("当前时间 -->: " + DateUtils.toString(date1));

        Date minDate = DateUtils.min(date1, date2, date3);
        System.out.println("最小时间 -->: " + DateUtils.toString(minDate));

        Date maxDate = DateUtils.max(date1, date2, date3);
        System.out.println("最大时间 -->: " + DateUtils.toString(maxDate));
    }

    @Test
    public void getBirth() {
        int year = 32, month = 6;

        Date date = DateUtils.getBirthByYear(year);
        System.out.println("出生时间 -->: " + DateUtils.toString(date));

        date = DateUtils.getBirthByMonth(month);
        System.out.println("出生时间 -->: " + DateUtils.toString(date));

        String idCard = "340826202205010000";
        date = DateUtils.getBirthdayByIdCardToStr(idCard);
        System.out.println("出生时间 -->: " + DateUtils.toString(date));
    }
}

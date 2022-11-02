package com.baohulu.framework.common.utils;

import com.baohulu.framework.basic.consts.Time;
import com.baohulu.framework.basic.enums.DateEnum;
import com.baohulu.framework.basic.enums.MonthEnum;
import com.baohulu.framework.basic.enums.WeekEnum;
import com.baohulu.framework.common.utils.validate.ValidateUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类
 *
 * @author heqing
 * @date 2022/10/12 09:32
 */
public class DateUtils {

    /**
     * 判断日期是否符合指定格式。
     *
     * @param date    日期
     * @param pattern 时间格式，如：yyyyMMdd, yyyy-MM-dd
     * @return 是否是时间
     */
    public static boolean checkDate(String date, String pattern) {
        if (StringUtils.isBlank(date)) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 格式化日期，date 转 string
     * @param date 时间
     * @return yyyy-mm-dd
     */
    public static String toString(Date date) {
        return toString(date, "");
    }

    /**
     * 格式化日期，date 转 string
     * @param date 时间
     * @param format 格式
     * @return yyyy-mm-dd
     */
    public static String toString(Date date, String format) {
        if(date == null) {
            return null;
        }
        try {
            if(StringUtils.isBlank(format)) {
                format = Time.YYYY_MM_DD_HH_MM_SS;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化日期，string 转d ate
     * @param dateStr 时间
     * @return 时间
     */
    public static Date toDate(String dateStr) {
        return toDate(dateStr, "");
    }

    /**
     * 格式化日期，string 转d ate
     * @param dateStr 时间
     * @param format 格式
     * @return 时间
     */
    public static Date toDate(String dateStr, String format) {
        if(dateStr == null) {
            return null;
        }
        try {
            if(StringUtils.isBlank(format)) {
                format = Time.YYYY_MM_DD_HH_MM_SS;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化日期，localDate 转 date
     * @param localDate 时间
     * @return 时间
     */
    public static Date toDate(LocalDate localDate) {
        if(localDate==null){
            return null;
        }
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 格式化日期，date 转 localDate
     * @param date 时间
     * @return 时间
     */
    public static LocalDate toLocalDate(Date date) {
        if(date==null){
            return null;
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 格式化日期，date 转 localDateTime
     * @param date 时间
     * @return 时间
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if(date==null){
            return null;
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 格式化日期，localDateTime 转 date
     * @param localDateTime 时间
     * @return 时间
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if(localDateTime==null){
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取时间之间的毫秒数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 毫秒数
     */
    public static long getMillisecondBetweenDate(Date startTime, Date endTime){
        long beforeTime = startTime.getTime();
        long afterTime = endTime.getTime();
        return afterTime - beforeTime;
    }

    /** 获取时间之间的天数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 天数
     */
    public static int getDaysBetweenDate(Date startTime, Date endTime) {
        int betweenDays = (int) (getMillisecondBetweenDate(startTime, endTime) / Time.DAYS);
        return betweenDays + 1;
    }

    /** 获取时间之间的月数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 月数
     */
    public static int getMonthBetweenDate(Date startTime, Date endTime) {
        int day = getDaysBetweenDate(startTime, endTime);
        return day / 30;
    }

    /** 获取时间之间的年数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 年数
     */
    public static int getYearBetweenDate(Date startTime, Date endTime) {
        int day = getDaysBetweenDate(startTime, endTime);
        return day / 365;
    }

    /**
     * 获取某个时间过去或将来的某个日期
     * @param date 时间
     * @param num 之前或之后 多少年
     * @return 日期
     */
    public static Date getPastOrFutureYear(Date date, int num) {
        return getPastOrFuture(Calendar.YEAR, date, num);
    }

    /**
     * 获取某个时间过去或将来的某个日期
     * @param date 时间
     * @param num 之前或之后 多少月
     * @return 日期
     */
    public static Date getPastOrFutureMonth(Date date, int num) {
        return getPastOrFuture(Calendar.MONTH, date, num);
    }

    /**
     * 获取某个时间过去或将来的某个日期
     * @param date 时间
     * @param num 之前或之后 多少天
     * @return 日期
     */
    public static Date getPastOrFutureDay(Date date, int num) {
        return getPastOrFuture(Calendar.DAY_OF_MONTH, date, num);
    }

    /**
     * 获取某个时间过去或将来的某个日期
     * @param date 时间
     * @param num 之前或之后 多少小时
     * @return 日期
     */
    public static Date getPastOrFutureHour(Date date, int num) {
        return getPastOrFuture(Calendar.HOUR_OF_DAY, date, num);
    }

    /**
     * 获取某个时间过去或将来的某个日期
     * @param date 时间
     * @param num 之前或之后 多少分钟
     * @return 日期
     */
    public static Date getPastOrFutureMinute(Date date, int num) {
        return getPastOrFuture(Calendar.MINUTE, date, num);
    }

    /**
     * 获取某个时间过去或将来的某个日期
     * @param field 时间日期
     * @param date 时间
     * @param num 之前或之后
     * @return 日期
     */
    public static Date getPastOrFuture(int field, Date date, int num) {
        if(date == null) {
            date = new Date();
        }
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(field, num);
            return calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 哪一年
     * @param date 时间
     * @return 具体年
     */
    public static int getYearTime(Date date) {
        return getTimeField(Calendar.YEAR, date);
    }

    /**
     * 哪一月
     * @param date 时间
     * @return 具体月
     */
    public static int getMonthTime(Date date) {
        return getTimeField(Calendar.MONTH, date) + 1;
    }

    /**
     * 哪一天
     * @param date 时间
     * @return 具体天
     */
    public static int getDayTime(Date date) {
        return getTimeField(Calendar.DAY_OF_MONTH, date);
    }

    /**
     * 哪一小时
     * @param date 时间
     * @return 具体小时
     */
    public static int geHourTime(Date date) {
        return getTimeField(Calendar.HOUR_OF_DAY, date);
    }

    /**
     * 哪一分
     * @param date 时间
     * @return 具体分
     */
    public static int geMinuteTime(Date date) {
        return getTimeField(Calendar.MINUTE, date);
    }

    /**
     * 获取某个时间具体数字
     * @param field 时间类型
     * @param date 时间
     * @return 具体数字
     */
    public static int getTimeField(int field, Date date) {
        if(date == null) {
            date = new Date();
        }
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return gc.get(field);
    }

    /**
     * 获取某个时间是几月
     * @param date 时间
     * @return 几月
     */
    public static String getChineseMonthName(Date date) {
        int w = getTimeField(Calendar.MONTH, date);
        return MonthEnum.getName(w);
    }

    /**
     * 获取某个时间是星期几
     * @param date 时间
     * @return 星期几
     */
    public static String getChineseWeekName(Date date) {
        int w = getTimeField(Calendar.DAY_OF_WEEK, date);
        return WeekEnum.getName(w);
    }

    /**
     * 获取时间中最小的
     * @param dates 时间
     * @return 最小的时间
     */
    public static Date min(Date... dates) {
        return minOrMax(true, dates);
    }

    /**
     * 获取时间中最大的
     * @param dates 时间
     * @return 最大的时间
     */
    public static Date max(Date... dates) {
        return minOrMax(false, dates);
    }

    private static Date minOrMax(boolean isMin, Date... dates) {
        Date newDate = null;
        for(Date date : dates) {
            if(newDate == null) {
                newDate = date;
            } else {
                if(isMin) {
                    newDate = date.getTime() > newDate.getTime() ? newDate : date;
                } else {
                    newDate = date.getTime() > newDate.getTime() ? date : newDate;
                }
            }
        }
        return newDate;
    }

    /**
     * 根据年龄获取出生年份
     * @param age 年龄
     * @return 出生年份
     */
    public static Date getBirthByYear(Integer age) {
        return getBirth(age, 1);
    }

    /**
     * 根据月份获取出生年份
     * @param age 月龄
     * @return 出生年份
     */
    public static Date getBirthByMonth(Integer age) {
        return getBirth(age, 2);
    }

    private static Date getBirth(Integer age, int ageType) {
        if(age == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        String currentYearStr = toString(new Date(), Time.YYYY_MM);
        Date currentYearDate = toDate(currentYearStr, Time.YYYY_MM);
        calendar.setTime(currentYearDate);
        if(ageType == DateEnum.MONTH.getCode()) {
            int month = 12;
            calendar.add(Calendar.MONTH, -(age%month));
            if(age % month == 0) {
                age = age / month;
            } else {
                age = age / month + 1;
            }
        }
        calendar.add(Calendar.YEAR, -age);
        return calendar.getTime();
    }

    /**
     * 根据身份证获取出生日期
     * @param idCard 身份证
     * @return 出生日期
     */
    public static Date getBirthdayByIdCardToStr(String idCard) {
        if(StringUtils.isBlank(idCard) || !ValidateUtils.isIdentityCard(idCard)) {
            return null;
        }
        String birthday = idCard.substring(6, 10) + "-" + idCard.substring(10, 12) + "-" + idCard.substring(12, 14);
        return toDate(birthday, Time.YYYY_MM_DD);
    }
}

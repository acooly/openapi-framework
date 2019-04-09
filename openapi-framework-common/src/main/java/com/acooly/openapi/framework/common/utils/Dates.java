package com.acooly.openapi.framework.common.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 简单 Date 相关静态工具
 *
 * @author zhangpu
 */
public class Dates {

  public static final String CHINESE_DATE_FORMAT_LINE = "yyyy-MM-dd";
  public static final String CHINESE_DATETIME_FORMAT_LINE = "yyyy-MM-dd HH:mm:ss";
  public static final String CHINESE_DATE_FORMAT_SLASH = "yyyy/MM/dd";
  public static final String CHINESE_DATETIME_FORMAT_SLASH = "yyyy/MM/dd HH:mm:ss";
  public static final String DATETIME_NOT_SEPARATOR = "yyyyMMddHHmmssSSS";
  // 中国周一是一周的第一天
  public static final int FIRST_DAY_OF_WEEK = Calendar.MONDAY;
  private static final String DEFAULT_DATE_FORMAT = CHINESE_DATETIME_FORMAT_LINE;
  /** 根据模式长度倒叙排列 */
  private static final List<String> patterns =
      Lists.newArrayList(
          CHINESE_DATETIME_FORMAT_LINE,
          CHINESE_DATETIME_FORMAT_SLASH,
          DATETIME_NOT_SEPARATOR,
          CHINESE_DATE_FORMAT_LINE,
          CHINESE_DATE_FORMAT_SLASH);

  public static void registerPattern(String pattern) {
    patterns.add(pattern);
    sort();
  }

  public static void unRegisterPattern(String pattern) {
    patterns.remove(pattern);
    sort();
  }

  private static void sort() {
    Collections.sort(
        patterns,
        new Comparator<String>() {
          @Override
          public int compare(String o1, String o2) {
            if (o1 == null || o2 == null) {
              return 0;
            }
            if (o1.length() > o2.length()) {
              return -1;
            } else if (o1.length() < o2.length()) {
              return 1;
            } else {
              return 0;
            }
          }
        });
    System.out.println(patterns);
  }

  public static String format(Date date, String pattern) {
    SimpleDateFormat sdf = getSimpleDateFormat(pattern);
    return sdf.format(date);
  }

  public static String format(Date date) {
    return format(date, DEFAULT_DATE_FORMAT);
  }

  public static String format(String pattern) {
    return format(new Date(), pattern);
  }

  public static Date parse(String dateString, String pattern) {
    SimpleDateFormat sdf = getSimpleDateFormat(pattern);
    try {
      return sdf.parse(dateString);
    } catch (Exception e) {
      throw new RuntimeException(
          "parse String[" + dateString + "] to Date faulure with pattern[" + pattern + "].");
    }
  }

  public static Date parse(String dateString, String[] patterns) {
    for (String pattern : patterns) {
      if (StringUtils.isBlank(pattern)) {
        continue;
      }
      SimpleDateFormat sdf = getSimpleDateFormat(pattern);
      try {
        return sdf.parse(dateString);
      } catch (Exception e) {
        // ignore exception
      }
    }
    throw new UnsupportedOperationException(
        "Parse String["
            + dateString
            + "] to Date faulure with patterns["
            + Arrays.toString(patterns)
            + "]");
  }

  public static Date parse(String dateString) {
    return parse(dateString, patterns.toArray(new String[0]));
  }

  public static Date addYear(Date date, int years) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.YEAR, years);
    return c.getTime();
  }

  public static Date addMonth(Date date, int months) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.MONTH, months);
    return c.getTime();
  }

  public static Date addWeek(Date date, int weeks) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.WEEK_OF_YEAR, weeks);
    return c.getTime();
  }

  public static Date addDay(Date date) {
    return addDay(date, 1);
  }

  public static Date addDay(Date date, int days) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.DAY_OF_YEAR, days);
    return c.getTime();
  }

  public static Date addWorkDay(Date date, int days) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int totalDays = 0;
    while (true) {
      c.add(Calendar.DAY_OF_YEAR, 1);
      int d = c.get(Calendar.DAY_OF_WEEK) - 1;
      if (d != 0 && d != 6) { // 0代表是周末 6代表是周六
        totalDays++;
      }
      if (totalDays >= days) {
        break;
      }
    }
    return c.getTime();
  }

  public static Date minusDay(Date date) {
    long oneDayMillisecond = 24 * 60 * 60 * 1000;
    return addDate(date, -oneDayMillisecond);
  }

  public static Date addDate(Date date, long millisecond) {
    return new Date(date.getTime() + millisecond);
  }

  public static long sub(Date left, Date right, int type) {
    long subms = left.getTime() - right.getTime();
    if (subms < 0) {
      throw new RuntimeException("left日期小于right日期");
    }

    switch (type) {
      case Calendar.SECOND:
        return subms % 1000 == 0 ? subms / 1000 : subms / 1000 + 1;
      case Calendar.MINUTE:
        return subms % 1000 / 60 == 0 ? subms / 1000 / 60 : subms / 1000 / 60 + 1;
      case Calendar.HOUR:
        return subms % 1000 / 60 / 60 == 0 ? subms / 1000 / 60 / 60 : subms / 1000 / 60 / 60 + 1;
      case Calendar.DAY_OF_MONTH:
        return subms % 1000 / 60 / 60 / 24 == 0
            ? subms / 1000 / 60 / 60 / 24
            : subms / 1000 / 60 / 60 / 24 + 1;
      default:
        return subms;
    }
  }

  /**
   * 取得日期：年
   *
   * @param date
   * @return
   */
  public static int getYear(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int year = c.get(Calendar.YEAR);
    return year;
  }

  /**
   * 取得日期：年
   *
   * @param date
   * @return
   */
  public static int getMonth(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int month = c.get(Calendar.MONTH);
    return month + 1;
  }

  /**
   * 取得日期：年
   *
   * @param date
   * @return
   */
  public static int getDay(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int da = c.get(Calendar.DAY_OF_MONTH);
    return da;
  }

  /**
   * 取得当天日期是周几
   *
   * @param date
   * @return
   */
  public static int getWeekDay(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int week_of_year = c.get(Calendar.DAY_OF_WEEK);
    return week_of_year - 1;
  }

  /**
   * 获取一年的第几周
   *
   * @param date
   * @return
   */
  public static int getWeekOfYear(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int week_of_year = c.get(Calendar.WEEK_OF_YEAR);
    return week_of_year;
  }

  /**
   * 根据日期取得对应周周一日期
   *
   * @param date
   * @return
   */
  public static Date getMondayOfWeek(Date date) {
    Calendar monday = Calendar.getInstance();
    monday.setTime(date);
    monday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
    monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    return monday.getTime();
  }

  /**
   * 根据日期取得对应周周日日期
   *
   * @param date
   * @return
   */
  public static Date getSundayOfWeek(Date date) {
    Calendar sunday = Calendar.getInstance();
    sunday.setTime(date);
    sunday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
    sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    return sunday.getTime();
  }

  /**
   * 取得月的剩余天数
   *
   * @param date
   * @return
   */
  public static int getRemainDayOfMonth(Date date) {
    int dayOfMonth = getDayOfMonth(date);
    int day = getPassDayOfMonth(date);
    return dayOfMonth - day;
  }

  /**
   * 取得月已经过的天数
   *
   * @param date
   * @return
   */
  public static int getPassDayOfMonth(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    return c.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * 取得月天数
   *
   * @param date
   * @return
   */
  public static int getDayOfMonth(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    return c.getActualMaximum(Calendar.DAY_OF_MONTH);
  }

  /**
   * 取得月第一天
   *
   * @param date
   * @return
   */
  public static Date getFirstDateOfMonth(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
    return c.getTime();
  }

  /**
   * 取得月最后一天
   *
   * @param date
   * @return
   */
  public static Date getLastDateOfMonth(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
    return c.getTime();
  }

  /**
   * 取得季度第一天
   *
   * @param date
   * @return
   */
  public static Date getFirstDateOfSeason(Date date) {
    return getFirstDateOfMonth(getQuarterDate(date)[0]);
  }

  /**
   * 取得季度最后一天
   *
   * @param date
   * @return
   */
  public static Date getLastDateOfSeason(Date date) {
    return getLastDateOfMonth(getQuarterDate(date)[2]);
  }

  /**
   * 取得季度天数
   *
   * @param date
   * @return
   */
  public static int getDayOfSeason(Date date) {
    int day = 0;
    Date[] seasonDates = getQuarterDate(date);
    for (Date date2 : seasonDates) {
      day += getDayOfMonth(date2);
    }
    return day;
  }

  /**
   * 取得季度剩余天数
   *
   * @param date
   * @return
   */
  public static int getRemainDayOfSeason(Date date) {
    return getDayOfSeason(date) - getPassDayOfSeason(date);
  }

  /**
   * 取得季度已过天数
   *
   * @param date
   * @return
   */
  public static int getPassDayOfSeason(Date date) {
    int day = 0;

    Date[] seasonDates = getQuarterDate(date);

    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int month = c.get(Calendar.MONTH);

    if (month == Calendar.JANUARY
        || month == Calendar.APRIL
        || month == Calendar.JULY
        || month == Calendar.OCTOBER) { // 季度第一个月
      day = getPassDayOfMonth(seasonDates[0]);
    } else if (month == Calendar.FEBRUARY
        || month == Calendar.MAY
        || month == Calendar.AUGUST
        || month == Calendar.NOVEMBER) { // 季度第二个月
      day = getDayOfMonth(seasonDates[0]) + getPassDayOfMonth(seasonDates[1]);
    } else if (month == Calendar.MARCH
        || month == Calendar.JUNE
        || month == Calendar.SEPTEMBER
        || month == Calendar.DECEMBER) { // 季度第三个月
      day =
          getDayOfMonth(seasonDates[0])
              + getDayOfMonth(seasonDates[1])
              + getPassDayOfMonth(seasonDates[2]);
    }
    return day;
  }

  /**
   * 取得季度月
   *
   * @param date
   * @return
   */
  public static Date[] getQuarterDate(Date date) {
    Date[] quarters = new Date[3];

    Calendar c = Calendar.getInstance();
    c.setTime(date);

    int nq = getQuarter(date);
    if (nq == 1) { // 第一季度
      c.set(Calendar.MONTH, Calendar.JANUARY);
      quarters[0] = c.getTime();
      c.set(Calendar.MONTH, Calendar.FEBRUARY);
      quarters[1] = c.getTime();
      c.set(Calendar.MONTH, Calendar.MARCH);
      quarters[2] = c.getTime();
    } else if (nq == 2) { // 第二季度
      c.set(Calendar.MONTH, Calendar.APRIL);
      quarters[0] = c.getTime();
      c.set(Calendar.MONTH, Calendar.MAY);
      quarters[1] = c.getTime();
      c.set(Calendar.MONTH, Calendar.JUNE);
      quarters[2] = c.getTime();
    } else if (nq == 3) { // 第三季度
      c.set(Calendar.MONTH, Calendar.JULY);
      quarters[0] = c.getTime();
      c.set(Calendar.MONTH, Calendar.AUGUST);
      quarters[1] = c.getTime();
      c.set(Calendar.MONTH, Calendar.SEPTEMBER);
      quarters[2] = c.getTime();
    } else if (nq == 4) { // 第四季度
      c.set(Calendar.MONTH, Calendar.OCTOBER);
      quarters[0] = c.getTime();
      c.set(Calendar.MONTH, Calendar.NOVEMBER);
      quarters[1] = c.getTime();
      c.set(Calendar.MONTH, Calendar.DECEMBER);
      quarters[2] = c.getTime();
    }
    return quarters;
  }

  /**
   * 获取指定日期在一年的第几季度
   *
   * @param date
   * @return
   */
  public static int getQuarter(Date date) {
    int quarter = 0;
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int month = c.get(Calendar.MONTH);
    switch (month) {
      case Calendar.JANUARY:
      case Calendar.FEBRUARY:
      case Calendar.MARCH:
        quarter = 1;
        break;
      case Calendar.APRIL:
      case Calendar.MAY:
      case Calendar.JUNE:
        quarter = 2;
        break;
      case Calendar.JULY:
      case Calendar.AUGUST:
      case Calendar.SEPTEMBER:
        quarter = 3;
        break;
      case Calendar.OCTOBER:
      case Calendar.NOVEMBER:
      case Calendar.DECEMBER:
        quarter = 4;
        break;
      default:
        break;
    }
    return quarter;
  }

  private static SimpleDateFormat getSimpleDateFormat(String defaultFormat) {
    if (StringUtils.isBlank(defaultFormat)) {
      defaultFormat = DEFAULT_DATE_FORMAT;
    }
    return new SimpleDateFormat(defaultFormat);
  }

  public static void main(String[] args) throws Exception {
    String d = "2016-09-26 23:23:23";
    System.out.println(parse(d));
  }
}

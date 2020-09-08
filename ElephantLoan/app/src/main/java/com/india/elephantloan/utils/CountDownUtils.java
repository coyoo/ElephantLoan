package com.india.elephantloan.utils;

public class CountDownUtils {
    public static long day = 0;
    public static long hour = 0;
    public static long minute = 0;
    public static long second = 0;

    private static boolean dayNotAlready = false;
    private static boolean hourNotAlready = false;
    private static boolean minuteNotAlready = false;
    public static boolean secondNotAlready = false;

    /**
     * 初始化赋值
     *
     * @param totalSeconds
     */
    public static void initData(long totalSeconds) {
        resetData();

        if (totalSeconds > 0) {
            secondNotAlready = true;
            second = totalSeconds;
            if (second >= 60) {
                minuteNotAlready = true;
                minute = second / 60;
                second = second % 60;
                if (minute >= 60) {
                    hourNotAlready = true;
                    hour = minute / 60;
                    minute = minute % 60;
                    if (hour > 24) {
                        dayNotAlready = true;
                        day = hour / 24;
                        hour = hour % 24;
                    }
                }
            }
        }

        System.out.println("初始格式化后——>" + day + "天" + hour + "小时" + minute
                + "分钟" + second + "秒");
    }

    private static void resetData() {
        day = 0;
        hour = 0;
        minute = 0;
        second = 0;
        dayNotAlready = false;
        hourNotAlready = false;
        minuteNotAlready = false;
        secondNotAlready = false;
    }

    /**
     * 计算各个值的变动
     *
     */
    public static void startCount() {
        if (secondNotAlready) {
            if (second > 0) {
                second--;
                if (second == 0 && !minuteNotAlready) {
                    secondNotAlready = false;
                }
            } else {
                if (minuteNotAlready) {
                    if (minute > 0) {
                        minute--;
                        second = 59;
                        if (minute == 0 && !hourNotAlready) {
                            minuteNotAlready = false;
                        }

                    } else {
                        if (hourNotAlready) {
                            if (hour > 0) {
                                hour--;
                                minute = 59;
                                second = 59;
                                if (hour == 0 && !dayNotAlready) {
                                    hourNotAlready = false;
                                }

                            } else {
                                if (dayNotAlready) {
                                    day--;
                                    hour = 23;
                                    minute = 59;
                                    second = 59;
                                    if (day == 0) {
                                        dayNotAlready = false;
                                    }

                                }
                            }
                        }
                    }

                }
            }

        }

        System.out.println("距离截止日期还有——>" + day + "天" + hour + "小时" + minute
                + "分钟" + second + "秒");
    }

}

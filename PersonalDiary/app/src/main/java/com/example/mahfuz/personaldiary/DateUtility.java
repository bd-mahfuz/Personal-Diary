package com.example.mahfuz.personaldiary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DateUtility {

    public static String getDateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date);
    }

    public static Date getStringToDate(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.parse(date);
    }

    public static String getDayString (int day) {
        String monthArray[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep",
        "Oct", "Nov", "Dec"};
        return monthArray[day];
    }

    public static int getCurrentYear() {
        Date date = new Date();
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getCurrentDay() {
        Date date = new Date();
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentMonth() {
        Date date = new Date();
//        Calendar calendar= Calendar.getInstance();
//        calendar.setTime(date);
        return date.getMonth();
    }

    public static int getRandomNumber(int from, int to) {
        Random rand = new Random();
        int  n = rand.nextInt(to) + from;
        return n;
    }
}

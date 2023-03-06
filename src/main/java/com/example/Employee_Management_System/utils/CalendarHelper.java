package com.example.Employee_Management_System.utils;

import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.example.Employee_Management_System.dto.response.WorkingScheduleResponse.*;

public class CalendarHelper {

    public static MonthInfo getMonthInfo(int yearNumber, int monthNumber) {
        Calendar calendar = Calendar.getInstance();
        Month month = Month.of(monthNumber + 1); // in Month object, month starts from 1, but in Calendar object, month starts from 0

        calendar.set(Calendar.YEAR, yearNumber);
        calendar.set(Calendar.MONTH, monthNumber);

        String monthName = month.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int numberOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<Integer> weekends = getWeekends(yearNumber, monthNumber, numberOfDays); // get the day number of the month that is either Saturday or Sunday

        return MonthInfo.builder()
                .name(monthName)
                .numberOfDays(numberOfDays)
                .weekends(weekends)
                .build();

    }

    private static List<Integer> getWeekends(int yearNumber, int monthNumber, int numberOfDays) {

        List<Integer> weekends = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, yearNumber);
        calendar.set(Calendar.MONTH, monthNumber);
        for (int i = 1; i <= numberOfDays; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i);

            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                weekends.add(i);
            }
        }

        return weekends;
    }
}

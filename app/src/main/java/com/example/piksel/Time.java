package com.example.piksel;

public class Time {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public Time(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    // parameter is the the deadline
    // if return true,then time expired
    // if return false,then time not expired yet


    public boolean comparison(int deadYear,int deadMonth,int deadDay,int deadHour,int deadMinute)
    {
        if(year>=deadYear)
        {
            if(month>=deadMonth)
            {
                if(day>=deadDay)
                {
                    if(hour>=deadHour)
                    {
                        if(minute>=deadMinute)
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}

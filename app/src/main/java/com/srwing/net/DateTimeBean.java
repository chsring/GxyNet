package com.srwing.net;

import com.google.gson.annotations.Expose;

import java.util.Calendar;

public class DateTimeBean {

    private date date;

    @Expose(serialize = false, deserialize = false)
    private Calendar calendar;
    private time time;

    public date getDate() {
        return date;
    }

    public void setDate(date date) {
        this.date = date;
    }

    public time getTime() {
        return time;
    }

    public void setTime(time time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "" + date.year + '-' + date.month + '-' + date.day;
    }

    public String toStringAll() {
        return "" + date.year + '-' + date.month + '-' + date.day + ' ' + time.hour + ':' + time.minute + ':' + time.second;
    }

    public String toString(String separator) {
        return "" + date.year + separator + date.month + separator + date.day;
    }

    void setCalendar() {
        calendar = Calendar.getInstance();
        calendar.set(date.year, date.month - 1, date.day, time.hour, time.minute, time.second);
    }

    public Calendar getCalendar() {
        if (calendar == null)
            setCalendar();
        return calendar;
    }

    public static class date {
        private int year;
        private int month;
        private int day;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }
    }

    public static class time {
        private int hour;
        private int minute;
        private int second;

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }
    }

}

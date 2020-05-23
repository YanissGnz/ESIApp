package com.example.esiapp.adapters;


public class Notetodo {

    private String text;
    private String date;
    private String time;
    private String uname;
    private String Key;
    private long time_in_milli;
    public Notetodo(String text, String date, String time, String uname,  long time_in_milli) {
        this.text = text;
        this.date = date;
        this.time = time;
        this.uname = uname;
        this.time_in_milli = time_in_milli;
    }


    public long getTime_in_milli() {
        return time_in_milli;
    }

    public void setTime_in_milli(long time_in_milli) {
        this.time_in_milli = time_in_milli;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public Notetodo() {
    }

    public Notetodo(String text, String date, String time, String uname) {
        this.text = text;
        this.date = date;
        this.time = time;
        this.uname = uname;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    @Override
    public String toString() {
        return "Note{" +
                "text='" + text + '\'' +
                ", completed=" + uname +
                ", created=" + date +
                ", userId='" + time + '\'' +
                '}';
    }
}
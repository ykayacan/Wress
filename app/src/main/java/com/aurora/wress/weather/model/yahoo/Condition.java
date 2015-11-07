package com.aurora.wress.weather.model.yahoo;

public class Condition {

    private String condition;
    private String date;
    private String temp;
    private String text;

    public String getCondition() {
        return condition;
    }

    public String getDate() {
        return date;
    }

    public String getTemp() {
        return temp;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Condition{" +
                "condition='" + condition + '\'' +
                ", date='" + date + '\'' +
                ", temp='" + temp + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

package com.aurora.wress.weather.model.yahoo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Item {

    private String title;
    private String lat;

    @SerializedName("long")
    private String _long;
    private String pubDate;
    private Condition condition;
    private String description;
    private List<Forecast> forecast;
    private Guid guid;

    public String getTitle() {
        return title;
    }

    public String getLat() {
        return lat;
    }

    public String get_long() {
        return _long;
    }

    public String getPubDate() {
        return pubDate;
    }

    public Condition getCondition() {
        return condition;
    }

    public String getDescription() {
        return description;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }

    public Guid getGuid() {
        return guid;
    }

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", lat='" + lat + '\'' +
                ", _long='" + _long + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", condition=" + condition +
                ", description='" + description + '\'' +
                ", forecast=" + forecast +
                ", guid=" + guid +
                '}';
    }
}

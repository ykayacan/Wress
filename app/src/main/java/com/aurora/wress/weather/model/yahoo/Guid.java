package com.aurora.wress.weather.model.yahoo;

public class Guid {

    private String isPermalink;
    private String content;

    public String getIsPermalink() {
        return isPermalink;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Guid{" +
                "isPermalink='" + isPermalink + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

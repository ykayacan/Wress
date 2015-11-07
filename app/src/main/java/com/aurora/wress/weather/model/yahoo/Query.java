package com.aurora.wress.weather.model.yahoo;

public class Query {

    private int count;
    private String created;
    private String lang;
    private Results results;

    public int getCount() {
        return count;
    }

    public String getCreated() {
        return created;
    }

    public String getLang() {
        return lang;
    }

    public Results getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "Query{" +
                "count=" + count +
                ", created='" + created + '\'' +
                ", lang='" + lang + '\'' +
                ", results=" + results +
                '}';
    }
}

package com.aurora.wress.weather.model.yahoo;

public class Channel {

    private Item item;

    public Item getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "item=" + item +
                '}';
    }
}

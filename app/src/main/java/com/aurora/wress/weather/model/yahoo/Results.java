package com.aurora.wress.weather.model.yahoo;

public class Results {

    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "Results{" +
                "channel=" + channel +
                '}';
    }
}

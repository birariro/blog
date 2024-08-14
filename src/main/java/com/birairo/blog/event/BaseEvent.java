package com.birairo.blog.event;

public class BaseEvent {
    private long timestamp;

    public BaseEvent() {
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }
}
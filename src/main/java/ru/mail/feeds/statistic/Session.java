package ru.mail.feeds.statistic;

import java.util.List;

public class Session {

    private int countViewedFeeds;
    private long sessionDuration;

    public Session(int countViewedFeeds, long sessionDuration) {
        this.countViewedFeeds = countViewedFeeds;
        this.sessionDuration = sessionDuration;
    }

    public int getCountViewedFeeds() {
        return countViewedFeeds;
    }

    public long getSessionDuration() {
        return sessionDuration;
    }
}

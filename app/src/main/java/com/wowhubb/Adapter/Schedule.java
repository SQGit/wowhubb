package com.wowhubb.Adapter;

/**
 * Created by Ramya on 09-09-2017.
 */

public class Schedule {
    private String  endtime, event,who;
    private String starttime;

    public Schedule() {
    }

    public Schedule(String starttime, String endtime, String event,String who) {
        this.starttime = starttime;
        this.endtime = endtime;
        this.event = event;
        this.who = who;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }
}

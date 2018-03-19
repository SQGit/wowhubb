package com.wowhubb.Nearbyeventsmodule;

/**
 * Created by Guna on 08-02-2018.
 */

public class ScheduleEventModelPojo {

    public String eventInnerTime = "";
    public String eventInnerEvent = "";
    public String eventInnerWho = "";
    public String eventInnerWhere ="";


    public ScheduleEventModelPojo(String eventInnerTime, String eventInnerEvent, String eventInnerWho, String eventInnerWhere) {
    this.eventInnerTime=eventInnerTime;
    this.eventInnerEvent=eventInnerEvent;
    this.eventInnerWho=eventInnerWho;
    this.eventInnerWhere=eventInnerWhere;


    }


    public String geteventInnerTime() {
        return eventInnerTime;
    }

    public void seteventInnerTime(String eventInnerTime) {
        this.eventInnerTime = eventInnerTime;
    }

    public String geteventInnerEvent() {
        return eventInnerEvent;
    }

    public void seteventInnerEvent(String eventInnerEvent) {
        this.eventInnerEvent = eventInnerEvent;
    }

    public String geteventInnerWho() {
        return eventInnerWho;
    }

    public void seteventInnerWho(String eventInnerWho) {
        this.eventInnerWho = eventInnerWho;
    }


    public String geteventInnerWhere() {
        return eventInnerWhere;
    }

    public void seteventInnerWhere(String eventInnerWhere) {
        this.eventInnerWhere = eventInnerWhere;
    }



}

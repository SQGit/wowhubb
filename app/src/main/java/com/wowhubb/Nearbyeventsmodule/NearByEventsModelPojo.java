package com.wowhubb.Nearbyeventsmodule;

/**
 * Created by Guna on 08-02-2018.
 */

public class NearByEventsModelPojo {

    public String eventName = "";
    public String eventCategory = "";
    public String eventAddress = "";
    public String eventTime ="";
    public String eventContact = "";
    public String eventAttending ="";

    public NearByEventsModelPojo(String eventName, String eventCategory, String eventAddress, String eventTime, String eventContact, String eventAttending) {
    this.eventName=eventName;
    this.eventCategory=eventCategory;
    this.eventAddress=eventAddress;
    this.eventTime=eventTime;
    this.eventContact=eventContact;
    this.eventAttending=eventAttending;

    }


    public String geteventName() {
        return eventName;
    }

    public void seteventName(String eventName) {
        this.eventName = eventName;
    }

    public String geteventCategory() {
        return eventCategory;
    }

    public void seteventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String geteventAddress() {
        return eventAddress;
    }

    public void seteventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }


    public String geteventTime() {
        return eventTime;
    }

    public void seteventTime(String eventTime) {
        this.eventTime = eventTime;
    }


    public String geteventContact() {
        return eventContact;
    }

    public void seteventContact(String eventContact) {
        this.eventContact = eventContact;
    }

    public String geteventAttending() {
        return eventAttending;
    }

    public void seteventAttending(String eventAttending) {
        this.eventAttending = eventAttending;
    }
}

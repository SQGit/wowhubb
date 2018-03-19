package com.wowhubb.Nearbyeventsmodule;

/**
 * Created by Guna on 08-02-2018.
 */

public class InfoEventModelPojo {

    public String eventLogo = "";
    public String eventDescription = "";
    public String eventAddress = "";
    public String eventDate ="";
    public String eventLink = "";
    public String eventAboutUs ="";

    public InfoEventModelPojo(String eventLogo, String eventDescription, String eventAddress, String eventDate, String eventLink, String eventAboutUs) {
    this.eventLogo=eventLogo;
    this.eventDescription=eventDescription;
    this.eventAddress=eventAddress;
    this.eventDate=eventDate;
    this.eventLink=eventLink;
    this.eventAboutUs=eventAboutUs;

    }


    public String geteventLogo() {
        return eventLogo;
    }

    public void seteventLogo(String eventLogo) {
        this.eventLogo = eventLogo;
    }

    public String geteventDescription() {
        return eventDescription;
    }

    public void seteventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String geteventAddress() {
        return eventAddress;
    }

    public void seteventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }


    public String geteventDate() {
        return eventDate;
    }

    public void seteventDate(String eventDate) {
        this.eventDate = eventDate;
    }


    public String geteventLink() {
        return eventLink;
    }

    public void seteventLink(String eventLink) {
        this.eventLink = eventLink;
    }

    public String geteventAboutUs() {
        return eventAboutUs;
    }

    public void seteventAboutUs(String eventAboutUs) {
        this.eventAboutUs = eventAboutUs;
    }
}

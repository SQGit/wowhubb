package com.wowhubb.data;

/**
 * Created by Guna on 19-12-2017.
 */

public class DayContent {
    private String dayNumber, eventNumber,eventStartTime, eventEndTime, itinerary_start_time, itinerary_end_time, event_agenta, facilitator, location_venue;


    public DayContent(String dayNumber, String eventNumber,String eventStartTime, String eventEndTime, String itinerary_start_time, String itinerary_end_time, String event_agenta, String facilitator, String location_venue) {

        this.dayNumber = dayNumber;
        this.eventNumber = eventNumber;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.itinerary_start_time = itinerary_start_time;
        this.itinerary_end_time = itinerary_end_time;
        this.event_agenta = event_agenta;
        this.facilitator = facilitator;
        this.location_venue = location_venue;
    }

    public String getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(String dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getEventNumber() {
        return eventNumber;
    }

    public void setEventNumber(String eventNumber) {
        this.eventNumber = eventNumber;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public String getItinerary_start_time() {
        return itinerary_start_time;
    }

    public void setItinerary_start_time(String itinerary_start_time) {
        this.itinerary_start_time = itinerary_start_time;
    }

    public String getItinerary_end_time() {
        return itinerary_end_time;
    }

    public void setItinerary_end_time(String itinerary_end_time) {
        this.itinerary_end_time = itinerary_end_time;
    }

    public String getEvent_agenta() {
        return event_agenta;
    }

    public void setEvent_agenta(String event_agenta) {
        this.event_agenta = event_agenta;
    }

    public String getFacilitator() {
        return facilitator;
    }

    public void setFacilitator(String facilitator) {
        this.facilitator = facilitator;
    }

    public String getLocation_venue() {
        return location_venue;
    }

    public void setLocation_venue(String location_venue) {
        this.location_venue = location_venue;
    }
}
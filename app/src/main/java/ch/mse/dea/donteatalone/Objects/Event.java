package ch.mse.dea.donteatalone.Objects;

import org.joda.time.DateTime;

public class Event {
    private String eventId;
    private String eventName;
    private DateTime date;
    private int duration;
    private String addresse;
    private String postcode;
    private String city;
    private String country;
    private int maxGuest;
    private double latitude;
    private double longitude;

    public Event(String eventId, String eventName, DateTime date, int duration, String addresse, String postcode, String city, String country, int maxGuest, double latitude, double longitude) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.date = date;
        this.duration = duration;
        this.addresse = addresse;
        this.postcode = postcode;
        this.city = city;
        this.country = country;
        this.maxGuest = maxGuest;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAddresse() {
        return addresse;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getMaxGuest() {
        return maxGuest;
    }

    public void setMaxGuest(int maxGuest) {
        this.maxGuest = maxGuest;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getGoingGuests() {
        //TODO to implement, get Data from DB
        return 0;
    }

    public String getEventId() {
        return eventId;
    }

}

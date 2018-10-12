package ch.mse.dea.donteatalone.Objects;

import android.location.Location;

import org.joda.time.DateTime;

public class Event {
    private int id;
    private String eventName;
    private DateTime date;
    private int duration;
    private String addrasse;
    private String postcode;
    private String city;
    private String country;
    private int maxGuest;
    private double latitude;
    private double longitude;

    public Event(int id, String eventName, DateTime date, int duration, String addrasse, String postcode, String city, String country, int maxGuest, double latitude, double longitude) {
        this.id = id;
        this.eventName = eventName;
        this.date = date;
        this.duration = duration;
        this.addrasse = addrasse;
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

    public String getAddrasse() {
        return addrasse;
    }

    public void setAddrasse(String addrasse) {
        this.addrasse = addrasse;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

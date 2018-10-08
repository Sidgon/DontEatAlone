package ch.mse.dea.donteatalone;

import android.location.Location;

import java.time.LocalDateTime;

public class Event {
    private String restaurantName;
    private LocalDateTime date;
    private int duration;
    private String addrasse;
    private String postcode;
    private String city;
    private String country;
    private int maxGuest;
    private Location location;

    public Event(String restaurantName, LocalDateTime date, int duration, String addrasse, String postcode, String city, String country, int maxGuest, Location location) {
        this.restaurantName = restaurantName;
        this.date = date;
        this.duration = duration;
        this.addrasse = addrasse;
        this.postcode = postcode;
        this.city = city;
        this.country = country;
        this.maxGuest = maxGuest;
        this.location = location;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

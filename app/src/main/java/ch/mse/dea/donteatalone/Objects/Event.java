package ch.mse.dea.donteatalone.Objects;

import com.google.firebase.database.Exclude;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Event {
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm " + "dd.MM.yy").withLocale(Locale.getDefault());
    private String eventId;
    private String userIdOfCreator;
    private String eventName;
    private String dateTimeString;
    private int duration;
    private String address;
    private String postcode;
    private String city;
    private String country;
    private int maxGuest;
    private double latitude;
    private double longitude;

    public Event() {

    }

    public Event(String eventId) {
        this.eventId = eventId;
    }

    public Event(String eventId, String userIdOfCreator, String eventName, String dateTimeString, int duration, String address, String postcode, String city, String country, int maxGuest, double latitude, double longitude) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.dateTimeString = dateTimeString;
        this.duration = duration;
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.country = country;
        this.maxGuest = maxGuest;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userIdOfCreator = userIdOfCreator;
    }

    public Event(String eventId, String userIdOfCreator, String eventName, DateTime dateTimeString, int duration, String address, String postcode, String city, String country, int maxGuest, double latitude, double longitude) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.dateTimeString = dateTimeFormatter.print(dateTimeString);
        this.duration = duration;
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.country = country;
        this.maxGuest = maxGuest;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userIdOfCreator = userIdOfCreator;
    }

    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("address",getAddress());
        map.put("country",getCountry());
        map.put("dateTimeString",getDateTimeString());
        map.put("duration",getDuration());
        map.put("eventId",getEventId());
        map.put("eventName",getEventName());
        map.put("latitude",getLatitude());
        map.put("longitude",getLongitude());
        map.put("maxGuest",getMaxGuest());
        map.put("postcode",getPostcode());
        map.put("userIdOfCreator",getUserIdOfCreator());

        return map;
    }

        public boolean haveSameContent(Event event) {
        return getDuration() == event.getDuration() &&
                getMaxGuest() == event.getMaxGuest() &&
                Double.compare(event.getLatitude(), getLatitude()) == 0 &&
                Double.compare(event.getLongitude(), getLongitude()) == 0 &&
                Objects.equals(getEventId(), event.getEventId()) &&
                Objects.equals(getUserIdOfCreator(), event.getUserIdOfCreator()) &&
                Objects.equals(getEventName(), event.getEventName()) &&
                Objects.equals(getDateTimeString(), event.getDateTimeString()) &&
                Objects.equals(getAddress(), event.getAddress()) &&
                Objects.equals(getPostcode(), event.getPostcode()) &&
                Objects.equals(getCity(), event.getCity()) &&
                Objects.equals(getCountry(), event.getCountry());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(getEventId(), event.getEventId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getEventId());
    }

//Getter und Setter

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDateTimeString() {
        return dateTimeString;
    }

    public void setDateTimeString(String dateTimeString) {
        this.dateTimeString = dateTimeString;
    }

    @Exclude
    public DateTime getDateTime() {
        return dateTimeFormatter.parseDateTime(dateTimeString);
    }

    @Exclude
    public void setDateTime(DateTime dateTime) {
        this.dateTimeString = dateTimeFormatter.print(dateTime);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    @Exclude
    public int getGoingGuests() {
        //TODO to implement, get Data from DB
        return 0;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserIdOfCreator() {
        return userIdOfCreator;
    }

    public void setUserIdOfCreator(String userIdOfCreator) {
        this.userIdOfCreator = userIdOfCreator;
    }


}

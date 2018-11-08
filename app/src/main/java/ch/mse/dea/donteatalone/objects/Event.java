package ch.mse.dea.donteatalone.objects;

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
    private int maxGuest;
    private Address address;

    public Event() {
    }

    public Event(String eventId) {
        this.eventId = eventId;
    }


    public Event(String eventId, String userIdOfCreator, String eventName, DateTime dateTimeString, int duration, int maxGuest, Address address) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.dateTimeString = dateTimeFormatter.print(dateTimeString);
        this.duration = duration;
        this.address = address;
        this.maxGuest = maxGuest;
        this.userIdOfCreator = userIdOfCreator;
    }

    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("address",getAddress());
        map.put("dateTimeString",getDateTimeString());
        map.put("duration",getDuration());
        map.put("eventId",getEventId());
        map.put("eventName",getEventName());
        map.put("maxGuest",getMaxGuest());
        map.put("userIdOfCreator",getUserIdOfCreator());

        return map;
    }

        public boolean haveSameContent(Event event) {
        return getDuration() == event.getDuration() &&
                getMaxGuest() == event.getMaxGuest() &&
                Objects.equals(getEventId(), event.getEventId()) &&
                Objects.equals(getUserIdOfCreator(), event.getUserIdOfCreator()) &&
                Objects.equals(getEventName(), event.getEventName()) &&
                Objects.equals(getDateTimeString(), event.getDateTimeString()) &&
                Objects.equals(getAddress(), event.getAddress());
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    public int getMaxGuest() {
        return maxGuest;
    }

    public void setMaxGuest(int maxGuest) {
        this.maxGuest = maxGuest;
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

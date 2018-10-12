package ch.mse.dea.donteatalone.Objects;

import org.joda.time.DateTime;

import java.util.ArrayList;

import ch.mse.dea.donteatalone.Objects.Event;
import ch.mse.dea.donteatalone.Objects.User;

public class UserProvider {

    public static ArrayList<Event> getAllUsers(){
        return new ArrayList<>();
    }

    public static void removeUser(Event event){

    }

    public static ArrayList<Event> getGoingEvents(User user){
        if (user== null){
            //TODO set user to App user
        }

        ArrayList<Event> events =new ArrayList<>();
        final int numRows = 64;
        for (int i = 0; i < numRows; i++) {
            events.add(new Event(i,"Restaurant " + i, DateTime.now(), 60 + i * 2, "Musterstrasse " + i, i * 100 + "", "Bern", "Switzerland", i, 0,0));
        }

        return events;
    }

    public static ArrayList<Event> getOwnEvents(User user){
        if (user== null){
            //TODO set user to App user
        }

        ArrayList<Event> events =new ArrayList<>();
        final int numRows = 13;
        for (int i = 0; i < numRows; i++) {
            events.add(new Event(i,"Restaurant " + i, DateTime.now(), 60 + i * 2, "Musterstrasse " + i, i * 100 + "", "Bern", "Switzerland", i, 0,0));
        }

        return events;
    }

    public static void setUser(Event event){

    }

    public static Event getUser(){
        return null;
    }
}

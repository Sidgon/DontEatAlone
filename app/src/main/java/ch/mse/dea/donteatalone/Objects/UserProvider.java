package ch.mse.dea.donteatalone.Objects;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;

import ch.mse.dea.donteatalone.Objects.User;

public class UserProvider {

    private DatabaseReference mDatabase;

    public UserProvider(){

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public  ArrayList<Event> getAllUsers(){
        return new ArrayList<>();
    }

    public boolean removeUser(Event event){
        return false;
    }

    public static ArrayList<Event> getGoingEvents(){
       /* if (user== null){
            //TODO set user to App user
        }*/

        ArrayList<Event> events =new ArrayList<>();
        final int numRows = 64;
        for (int i = 0; i < numRows; i++) {
            events.add(new Event("Sadly all same ID -> String ID","Restaurant " + i, DateTime.now(), 60 + i * 2, "Musterstrasse " + i, i * 100 + "", "Bern", "Switzerland", i, 0,0));
        }

        return events;
    }

    public static ArrayList<Event> getOwnEvents(){
       /* if (event== null){
            //TODO set user to App user
        } */

        ArrayList<Event> events =new ArrayList<>();
        final int numRows = 13;
        for (int i = 0; i < numRows; i++) {
            events.add(new Event("Sadly all same ID -> String ID","Restaurant " + i, DateTime.now(), 60 + i * 2, "Musterstrasse " + i, i * 100 + "", "Bern", "Switzerland", i, 0,0));
        }

        return events;
    }

    public void setUser(User user){

        mDatabase.child("users").child(user.getuserId()).setValue(user);

    }

    public User getUser(String userId){

        return null;
    }
}

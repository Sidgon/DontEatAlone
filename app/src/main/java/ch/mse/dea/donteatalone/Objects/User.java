package ch.mse.dea.donteatalone.Objects;

import android.util.Base64;

import com.google.firebase.database.Exclude;

import java.util.concurrent.ExecutionException;

import ch.mse.dea.donteatalone.DataHandling.GravatarTask;

public class User {
    private static final String TAG = User.class.getName();

    private static String loggedUserId;

    private String userId;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String imageString;

    //default constructor needed for firebase snapshots
    public User() {

    }

    public User(String userId, String username, String firstname, String lastname, String email) {
        this.userId = userId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.imageString=null;
    }

    public User(String userId, String username, String firstname, String lastname, String email, byte[] image) {
        this.userId = userId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.imageString = Base64.encodeToString(image, Base64.DEFAULT);
    }

    //---- Getter und Setter

    public static String getLoggedUserId() {
        return loggedUserId;
    }

    public static void setLoggedUserId(String loggedUserId) {
        User.loggedUserId = loggedUserId;
    }

    public static byte[] getGravatar(String email) {


        try {
            return new GravatarTask().execute(email).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;

    }

    public String getuserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public byte[] getImage() {
        return Base64.decode(imageString,Base64.DEFAULT);
    }

    @Exclude
    public void setImage(byte[] image) {
        this.imageString = Base64.encodeToString(image, Base64.DEFAULT); ;
    }


    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }
}




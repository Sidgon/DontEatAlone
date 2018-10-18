package ch.mse.dea.donteatalone.Objects;

import android.util.Base64;

import com.timgroup.jgravatar.Gravatar;
import com.timgroup.jgravatar.GravatarDefaultImage;
import com.timgroup.jgravatar.GravatarRating;

import java.security.Key;
import java.util.concurrent.ExecutionException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import ch.mse.dea.donteatalone.DataHandling.GravatarTask;

public class User {
    private static final String TAG = User.class.getName();

    private static String loggedUserId;

    private String userId;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private byte[] image;

    //default constructor needed for firebase snapshots
    public User(){

    }

    public User(String userId, String username, String firstname, String lastname, String email) {
        this.userId = userId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public User(String userId, String username, String firstname, String lastname, String email, byte[] image) {
        this.userId = userId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.image = image;
    }

    //---- Getter und Setter

    public String getuserId() {
        return userId;
    }

    public static String getLoggedUserId() {
        return loggedUserId;
    }

    public static void setLoggedUserId(String loggedUserId) {
        User.loggedUserId = loggedUserId;
    }

    public static byte[] getGravatar(String email) {


        try {
            return new GravatarTask().execute(email).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


}




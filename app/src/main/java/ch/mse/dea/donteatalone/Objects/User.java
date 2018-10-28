package ch.mse.dea.donteatalone.Objects;

import android.util.Base64;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import ch.mse.dea.donteatalone.DataHandling.GravatarTask;

public class User {
    private static final String TAG = User.class.getName();

    private String userId;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String imageString;


    public User() {

    }

    public User(String userId) {
        this.userId=userId;
    }

    public User(String userId, String username, String firstname, String lastname, String email) {
        this.userId = userId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.imageString = null;
    }

    public User(String userId, String username, String firstname, String lastname, String email, byte[] image) {
        this.userId = userId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.imageString = Base64.encodeToString(image, Base64.DEFAULT);
    }

    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("userId",getUserId());
        map.put("username",getUsername());
        map.put("firstname",getFirstname());
        map.put("lastname",getLastname());
        map.put("email",getEmail());
        map.put("imageString",getImageString());

        return map;
    }

    //---- Getter und Setter

    public static byte[] getGravatar(String email) {


        try {
            return new GravatarTask().execute(email).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;

    }

    public String getUserId() {
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
        return Base64.decode(imageString, Base64.DEFAULT);
    }

    @Exclude
    public void setImage(byte[] image) {
        this.imageString = Base64.encodeToString(image, Base64.DEFAULT);
        ;
    }


    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    @Exclude
    public boolean haveSameContent(User user) {
        return Objects.equals(userId, user.userId) &&
                Objects.equals(getUsername(), user.getUsername()) &&
                Objects.equals(getFirstname(), user.getFirstname()) &&
                Objects.equals(getLastname(), user.getLastname()) &&
                Objects.equals(getEmail(), user.getEmail());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getUserId(), user.getUserId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getUserId());
    }
}



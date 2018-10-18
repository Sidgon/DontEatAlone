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
    private static final String ALGORITHM = "DES/CBC/PKCS5Padding";
    private static final String KEY = "jasndfjnJNASJNFSjnd__==221423412341325134";


    private String userId;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String passswordHash;
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

    public User(String userId, String username, String firstname, String lastname, String email, String passswordHash, byte[] image) {
        this.userId = userId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.passswordHash = passswordHash;
        this.image = image;
    }

    public static String encrypt(String value) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        return Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);

    }

    private static String decrypt(String value) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM, "");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte[] decryptedByteValue = cipher.doFinal(decryptedValue64);

        return new String(decryptedByteValue, "utf-8");


    }

    private static Key generateKey() throws Exception {
        return new SecretKeySpec(KEY.getBytes(), ALGORITHM);
    }


    //---- Getter und Setter

    public String getuserId() {
        return userId;
    }

    public static User getGlobalUser() {
        return new User("ZUEAI77AEU", "Command1991", "Daniel", "Steinegger", "steinegger.daniel@gmail.com", "öajndsöfnDSF", User.getGravatar("steinegger.daniel@gmail.com"));
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

    public String getPassswordHash() {
        return passswordHash;
    }

    public void setPassswordHash(String passswordHash) {
        this.passswordHash = passswordHash;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


}




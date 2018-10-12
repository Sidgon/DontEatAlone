package ch.mse.dea.donteatalone.Objects;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class User {
    private static final String ALGORITHM = "DES/CBC/PKCS5Padding";
    private static final String KEY = "jasndfjnJNASJNFSjnd__==221423412341325134";


    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String passswordHash;
    private byte[] image;


    public User(int id, String username, String firstname, String lastname, String email, String passswordHash, byte[] image) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.passswordHash = passswordHash;
        this.image = image;
    }

    public static String encrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        return Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);

    }

    private static String decrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM,"");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);

        return new String(decryptedByteValue,"utf-8");

    }

    private static Key generateKey() throws Exception
    {
        return new SecretKeySpec(KEY.getBytes(),ALGORITHM);
    }


    //---- Getter und Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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




package ch.mse.dea.donteatalone.datahandling;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    private static final String TAG = MD5Util.class.getName();

    private MD5Util() {
    }

    private static String hex(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte anArray : array) {
            sb.append(Integer.toHexString((anArray
                    & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    public static String md5Hex(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            return hex(md.digest(message.getBytes("CP1252")));


        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ignored) {
            Log.v(TAG, ignored.toString());
        }
        return null;
    }
}

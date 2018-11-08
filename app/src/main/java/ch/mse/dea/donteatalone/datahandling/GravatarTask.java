package ch.mse.dea.donteatalone.datahandling;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class GravatarTask extends AsyncTask<String, Void, byte[]> {
    private static final String TAG = GravatarTask.class.getName();

    private byte[] image;

    public byte[] doInBackground(String... emails) {
        try {
            image=null;

            String hash = MD5Util.md5Hex(emails[0].toLowerCase().trim());

            String stringURL= "http://www.gravatar.com/avatar/"+hash+"?s=200&d=identicon";
            Log.v(TAG,stringURL);

            return loadImage(new URL(stringURL));


        } catch (MalformedURLException e) {
            Log.v(TAG,e.toString());
        }
        return new byte[0];
    }

    protected void onPostExecute(byte[] image) {
        this.image=image;
    }

    public byte[] getImage() {
        return image;
    }

    private byte[] loadImage(URL url){
        try (InputStream inputStream = url.openStream()) {

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[1024];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                 buffer.write(data, 0, nRead);
            }

            return buffer.toByteArray();
        } catch (IOException e) {
            Log.v(TAG,e.toString());
        }
        return new byte[0];
    }
}

package ch.mse.dea.donteatalone.objects;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.util.Iterator;
import java.util.Map;

import ch.mse.dea.donteatalone.R;

public class App extends Application {

    private static Context context;

    public static void setApplicationContext(Context context){
        App.context = context;
    }

    public static boolean isNetworkAvailable(boolean showToast) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        boolean isNetworkAvailable = (activeNetworkInfo != null && activeNetworkInfo.isConnected());

        if (showToast && !isNetworkAvailable)
            Toast.makeText(App.context, R.string.no_internet_connection_try_later, Toast.LENGTH_LONG).show();

        return isNetworkAvailable;
    }

    public static String getFromResource(int i) {
        return context.getResources().getString(i);
    }

    public static String getFromResource(int i, Object... object) {
        return context.getResources().getString(i, object);
    }

    public static boolean getDebug() {
        return true;
    }

    public static void log(String TAG, String str) {
        Log.v(TAG, "-");
        Log.v(TAG, "------------------------");
        Log.v(TAG, str);
        Log.v(TAG, "------------------------");
    }

    public static String mapToString(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, Object>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Object> entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=').append('"');
            sb.append(entry.getValue());
            sb.append('"');
            if (iter.hasNext()) {
                sb.append(',').append('\n');
            }
        }
        return sb.toString();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}

package ch.mse.dea.donteatalone.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import ch.mse.dea.donteatalone.objects.App;
import ch.mse.dea.donteatalone.R;

//https://stackoverflow.com/questions/25678216/android-internet-connectivity-change-listener
public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String  TAG= NetworkChangeReceiver.class.getName();

    @Override
    public void onReceive(final Context context, final Intent intent) {

        App.log(TAG,"No Internet");

        int status = NetworkUtil.getConnectivityStatusString(context);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {

            showToast(!(status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED),context);

        }
    }


    private void showToast(boolean status,Context context){
        if (!status) {
            Toast.makeText(context,R.string.no_internet_connection,Toast.LENGTH_LONG).show();
        } else {
            App.log(TAG,"Internet");
            Toast.makeText(context,R.string.no_internet_reconnection,Toast.LENGTH_LONG).show();
        }
    }



}

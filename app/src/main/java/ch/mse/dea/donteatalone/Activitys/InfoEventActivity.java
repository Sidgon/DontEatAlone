package ch.mse.dea.donteatalone.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import ch.mse.dea.donteatalone.Adapter.GsonAdapter;
import ch.mse.dea.donteatalone.DataHandling.DataFormatter;
import ch.mse.dea.donteatalone.Objects.Event;
import ch.mse.dea.donteatalone.R;

public class InfoEventActivity extends AppCompatActivity {

    private static final String TAG = InfoEventActivity.class.getName();

    TextView txtEventName;
    TextView txtDate;
    TextView txtTime;
    TextView txtDuration;
    TextView txtAddress;
    TextView txtPostcodeCity;

    TextView txtCountryName;
    TextView txtMaxGuest;
    Button btnUngoing;

    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_event);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            if (getIntent().getExtras() != null) {
                getViews();
                Gson gson = GsonAdapter.getGson();
                String json = getIntent().getExtras().getString(R.string.intent_info_event + "");
                event = gson.fromJson(json, Event.class);

                Log.v(TAG, "----------------------------------");
                Log.v(TAG, event.getEventName());
                setViewValues(event);
            }
        } else {
            Toast.makeText(this, R.string.user_not_logedin, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    private void setViewValues(Event event) {
        txtEventName.setText(event.getEventName());
        txtDate.setText(DataFormatter.getDateAsString(event.getDateTime(), "long"));
        txtTime.setText(DataFormatter.getTimeAsString(event.getDateTime()));
        txtDuration.setText(String.valueOf(event.getDuration()));
        txtAddress.setText(event.getAddress());
        txtPostcodeCity.setText(String.valueOf(event.getPostcode()) + " " + event.getCity());
        txtCountryName.setText(event.getCountry());
        txtMaxGuest.setText(String.valueOf(event.getMaxGuest()));
    }

    private void getViews() {
        txtEventName = findViewById(R.id.activity_info_event_eventName);
        txtDate = findViewById(R.id.activity_info_event_date);
        txtTime = findViewById(R.id.activity_info_event_time);
        txtDuration = findViewById(R.id.activity_info_event_duration);
        txtAddress = findViewById(R.id.activity_info_event_addresse);
        txtPostcodeCity = findViewById(R.id.activity_info_event_postcode_city);
        txtCountryName = findViewById(R.id.activity_info_event_countryName);
        txtMaxGuest = findViewById(R.id.activity_info_event_maxGuest);


        btnUngoing = findViewById(R.id.activity_info_event_ungoing);
    }


    public void onClick_ungoingEvent(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(R.string.activity_info_event_alert_title_ungoing);

        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.activity_info_event_alert_massage_going_user)
                .setCancelable(false)
                .setPositiveButton(R.string.edit_create_event_dialog_cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.activity_info_event_alert_button_going_user, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //TODO delete user from Event.
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void onClick_goingUserActivity(View view) {
        //TODO Add an activity with a list which contains all coming user.
        finish();
    }

}

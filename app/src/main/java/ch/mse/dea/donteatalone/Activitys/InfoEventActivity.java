package ch.mse.dea.donteatalone.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import ch.mse.dea.donteatalone.Adapter.GsonAdapter;
import ch.mse.dea.donteatalone.DataHandling.DataFormatter;
import ch.mse.dea.donteatalone.Objects.Event;
import ch.mse.dea.donteatalone.R;

public class InfoEventActivity extends AppCompatActivity {

    public static final int IS_GOING = 1;
    public static final int IS_NOT_GOING = 0;
    public static final int GOING_UNDIFINED = -1;
    private static final String TAG = InfoEventActivity.class.getName();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private TextView txtEventName;
    private TextView txtDate;
    private TextView txtTime;
    private TextView txtDuration;
    private TextView txtAddress;
    private TextView txtPostcodeCity;
    private TextView txtCountryName;
    private TextView txtMaxGuest;
    private Button btnUngoing;
    private Event event;
    private int isGoing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_event);

        isGoing = GOING_UNDIFINED;

        if (getIntent() != null && getIntent().getExtras() != null) {
            getViews();
            Gson gson = GsonAdapter.getGson();
            String json = getIntent().getExtras().getString(R.string.intent_info_event + "");
            event = gson.fromJson(json, Event.class);
            isGoing = getIntent().getExtras().getInt(R.string.intent_info_event_going_user + "", GOING_UNDIFINED);

            setViewValues(event);

            if (event.getUserIdOfCreator().equals(firebaseUser.getUid())) {
                btnUngoing.setVisibility(View.GONE);
            } else if (isGoing == IS_GOING) {
                btnUngoing.setText(R.string.activity_info_event_ungoing_event);
                btnUngoing.setBackgroundResource(R.color.colorDelete);
                btnUngoing.setTextAppearance(this, R.style.DeleteButton);
            } else {
                btnUngoing.setText(R.string.activity_info_event_going_event);
            }
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


    public void onClick_goingOrUngoingEvent(View view) {
        if (isGoing == IS_GOING) {
            // Der User nimmt bereits am event teil und will jetzt das event verlassen.
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
                        public void onClick(final DialogInterface dialog, int id) {

                            mDatabase.getReference("event_users").child(event.getEventId()).child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    mDatabase.getReference("users_going_events").child(firebaseUser.getUid()).child(event.getEventId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialog.cancel();
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(InfoEventActivity.this, R.string.activity_info_event_falure_ungoing_event, Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(InfoEventActivity.this, R.string.activity_info_event_falure_ungoing_event, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        if (isGoing == IS_NOT_GOING) {
            //Wenn der User bis jetzt noch nicht am Event teil nimmt, will er das jetzt tun.
            mDatabase.getReference("users_going_events").child(firebaseUser.getUid()).child(event.getEventId()).setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mDatabase.getReference("event_users").child(event.getEventId()).child(firebaseUser.getUid()).child("isComming").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(InfoEventActivity.this, R.string.activity_info_event_going_event_database, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(InfoEventActivity.this, R.string.activity_info_event_going_event_database, Toast.LENGTH_LONG).show();
                }
            });
        }


    }

    public void onClick_goingUserActivity(View view) {
        Gson gson = GsonAdapter.getGson();
        Intent intent = new Intent(this, GoingUserToEventListActivity.class);
        intent.putExtra(R.string.intent_going_user_to_event + "", gson.toJson(event));
        startActivity(intent);
    }

}

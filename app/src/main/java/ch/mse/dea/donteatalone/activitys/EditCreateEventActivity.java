package ch.mse.dea.donteatalone.activitys;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.joda.time.DateTime;

import ch.mse.dea.donteatalone.adapter.GsonAdapter;
import ch.mse.dea.donteatalone.datahandling.DataFormatter;
import ch.mse.dea.donteatalone.objects.Location;
import ch.mse.dea.donteatalone.objects.App;
import ch.mse.dea.donteatalone.objects.Event;
import ch.mse.dea.donteatalone.objects.EventValidation;
import ch.mse.dea.donteatalone.R;

public class EditCreateEventActivity extends AppCompatActivity {

    private static final String TAG = EditCreateEventActivity.class.getName();
    private static final int SEEKBAR_INCREMENT = 10;
    private static final int SEEKBAR_DURATION_MIN_VALUE = 10;
    private static final int SEEKBAR_MAX_GUEST_MIN_VALUE = 1;
    private TextView etxtEventName;
    private TextView txtDate;
    private TextView txtTime;
    private TextView txtDuration;
    private TextView txtAddress;
    private TextView txtPostcode;
    private TextView txtCity;
    private TextView txtCountryName;
    private TextView txtMaxGuest;
    private Button btnDeleteEvent;
    private Button btnSaveEvent;
    private double latitude;
    private double longitude;
    private boolean isEdit;
    private SeekBar seekBarDuration;
    private SeekBar seekBarMaxGuest;
    private Event event;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference refEvents = mDatabase.child("events");

    private int placePickerRequest = 1;

    @SuppressLint("DefaultLocale")
    private static String getStringDuration(int duration) {
        return String.format("%02dh %02dmin", (int) Math.floor(duration / 60.0), duration % 60);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_create_event);
        getViews();

        if (getIntent().getExtras() != null) {
            Log.i(TAG, "Edit event");
            Gson gson = GsonAdapter.getGson();
            String json = getIntent().getExtras().getString(R.string.intent_edit_create_event_event + "");
            event = gson.fromJson(json, Event.class);
            setViewValues(event);
            setTitle(R.string.edit_event_title);
            btnDeleteEvent.setVisibility(View.VISIBLE);
            btnSaveEvent.setText(R.string.edit_create_event_save);
            isEdit = true;
        } else {
            Log.i(TAG, "Create event");
            setTitle(R.string.create_event_title);
            event = null;
            btnDeleteEvent.setVisibility(View.GONE);
            btnSaveEvent.setText(R.string.edit_create_event_create);
            latitude = 0;
            longitude = 0;
            isEdit = false;
            if (App.getDebug())
                setViewValues(new Event("", firebaseUser.getUid(), "Migros", DateTime.now().plusDays(2), 60 , 8, new Location("Dragonerstrasse 55", "5600", "Lenzburg", "Switzerland",0,0)));
        }

        Log.i(TAG, "Finish on Create");
    }


    private Event getViewValues() {
        return new Event(
                event == null ? "" : event.getEventId(),
                event == null ? firebaseUser.getUid() : event.getUserIdOfCreator(),
                etxtEventName.getText().toString(),
                DataFormatter.getDateTimeFromString(txtDate.getText().toString(), txtTime.getText().toString(), "long"),
                seekBarDuration.getProgress() * SEEKBAR_INCREMENT+SEEKBAR_DURATION_MIN_VALUE,
                seekBarMaxGuest.getProgress()+SEEKBAR_MAX_GUEST_MIN_VALUE,
                new Location(txtAddress.getText().toString(),
                        txtPostcode.getText().toString(),
                        txtCity.getText().toString(),
                        txtCountryName.getText().toString(),latitude, longitude)
        );
    }

    private void setViewValues(Event event) {
        etxtEventName.setText(event.getEventName());
        txtDate.setText(DataFormatter.getDateAsString(event.getDateTime(), "long"));
        txtTime.setText(DataFormatter.getTimeAsString(event.getDateTime()));
        seekBarDuration.setProgress((event.getDuration()-SEEKBAR_DURATION_MIN_VALUE) / SEEKBAR_INCREMENT);
        txtAddress.setText(event.getLocation().getAddress());
        txtPostcode.setText(String.valueOf(event.getLocation().getPostcode()));
        txtCity.setText(event.getLocation().getCity());
        txtCountryName.setText(event.getLocation().getCountry());
        seekBarMaxGuest.setProgress(event.getMaxGuest()-SEEKBAR_MAX_GUEST_MIN_VALUE);

        latitude = event.getLocation().getLatitude();
        longitude = event.getLocation().getLongitude();
    }

    private boolean validateForm(Event event) {
        String str;
        boolean valid = true;

        str = EventValidation.standart(event.getEventName(), 2);
        if (str != null) valid = false;
        etxtEventName.setError(str);

        return valid;

    }

    private void getViews() {
        etxtEventName = findViewById(R.id.eventName);
        txtDate = findViewById(R.id.txt_date);
        txtTime = findViewById(R.id.txt_time);
        txtDuration = findViewById(R.id.duration);
        txtAddress = findViewById(R.id.address);
        txtPostcode = findViewById(R.id.postcode);
        txtCity = findViewById(R.id.city);
        txtCountryName = findViewById(R.id.countryName);
        txtMaxGuest = findViewById(R.id.maxGuest);
        btnDeleteEvent = findViewById(R.id.btn_delete_event);
        btnSaveEvent = findViewById(R.id.btn_save_event);

        setSeekBarDuration();
        setSeekBarMaxGuest();

    }

    private void setSeekBarDuration() {
        seekBarDuration = findViewById(R.id.edit_create_event_seekBar);
        seekBarDuration.setProgress(60 / SEEKBAR_INCREMENT);
        seekBarDuration.setMax((360 - SEEKBAR_DURATION_MIN_VALUE) / SEEKBAR_INCREMENT);
        seekBarDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                txtDuration.setText((getString(R.string.edit_create_event_duration_field)+" "+getStringDuration(progresValue * SEEKBAR_INCREMENT + SEEKBAR_DURATION_MIN_VALUE)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Not needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Not needed
            }
        });
    }

    private void setSeekBarMaxGuest() {
        seekBarMaxGuest = findViewById(R.id.edit_create_event_seekBar_maxGuest);
        seekBarMaxGuest.setProgress(8);
        seekBarMaxGuest.setMax(30 - SEEKBAR_MAX_GUEST_MIN_VALUE);
        seekBarMaxGuest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                txtMaxGuest.setText((getString(R.string.edit_create_event_max_guest)+" "+(progresValue + SEEKBAR_MAX_GUEST_MIN_VALUE)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Not needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Not needed
            }
        });
    }

    public void onClickSearchForAddress() {
        placePickerRequest += 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            if(App.isNetworkAvailable(true)) {
                startActivityForResult(builder.build(this), placePickerRequest);
            }
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Log.v(TAG,e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == placePickerRequest && resultCode == RESULT_OK) {
                Place pickedPlace = PlacePicker.getPlace(data, this);
                String fulladdress = pickedPlace.getAddress().toString();
                Log.d("", fulladdress);
                //manipulate address String "Kirchbreitestrasse 14, 6033 Buchrain, Switzerland" to get information
                String[] addressPLZPlaceCountry = fulladdress.split(",");

                if (addressPLZPlaceCountry.length==3) {
                    //plz + ort are not divided by "," therefore split again
                    String[] plzPlace = addressPLZPlaceCountry[1].split("\\s");
                    //remove leading whitespace from country
                    String country = addressPLZPlaceCountry[2].trim();
                    //put into textboxes
                    txtAddress.setText(addressPLZPlaceCountry[0]);
                    txtPostcode.setText(plzPlace[1]);
                    txtCity.setText(plzPlace[2]);
                    txtCountryName.setText(country);
                }else if(addressPLZPlaceCountry.length==2){
                    //plz + ort are not divided by "," therefore split again
                    //remove leading whitespace from country
                    String country = addressPLZPlaceCountry[1].trim();
                    //put into textboxes
                    txtAddress.setText(addressPLZPlaceCountry[0]);
                    txtPostcode.setText(" ");
                    txtCity.setText(" ");
                    txtCountryName.setText(country);
                }
                //save picked latlng position off place
                LatLng latlng = pickedPlace.getLatLng();
                latitude = latlng.latitude;
                longitude = latlng.longitude;

            }
    }

    public void onClickOpenDatePicker() {

        DateTime dateTime = null;
        if (!txtDate.getText().toString().isEmpty())
            dateTime = DataFormatter.getDateFromString(txtDate.getText().toString(), "long");
        if (dateTime == null) dateTime = DateTime.now();

        int mYear = dateTime.getYear();
        int mMonth = dateTime.getMonthOfYear();
        int mDay = dateTime.getDayOfMonth();


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txtDate.setText(DataFormatter.getDateAsString(year, monthOfYear, dayOfMonth, "long"));

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void onClickOpenTimePicker() {

        DateTime dateTime = null;
        if (!txtTime.getText().toString().isEmpty())
            dateTime = DataFormatter.getTimeFromString(txtTime.getText().toString());
        if (dateTime == null) dateTime = DateTime.now();

        int mHour = dateTime.getHourOfDay();
        int mMinute = dateTime.getMinuteOfHour();

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        txtTime.setText(DataFormatter.getTimeAsString(hourOfDay, minute));
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();


    }


    public void onClickDeleteEvent() {

        if (App.isNetworkAvailable(true)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle(R.string.edit_create_event_dialog_onDelete_title);

            // set dialog message
            alertDialogBuilder
                    .setMessage(R.string.edit_create_event_dialog_onDelete_massage)
                    .setCancelable(false)
                    .setPositiveButton(R.string.edit_create_event_dialog_cancel_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(R.string.edit_create_event_dialog_delete_button, new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            if (event != null) {


                                refEvents.child(event.getEventId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("DeleteEvent:failure", e);
                                                                Toast.makeText(EditCreateEventActivity.this,
                                                                        getString(R.string.edit_create_event_error_deleting_event),
                                                                        Toast.LENGTH_SHORT).show();

                                                                Log.i(TAG, "Event gelöscht: \n   -ID: " + event.getEventId() + " \n   -Name: " + event.getEventName());

                                                            }
                                                        }
                                );


                            }
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }


    }

    public void onClickSaveEvent() {
        if (App.isNetworkAvailable(true)) {
            Event viewValues = getViewValues();

            if (validateForm(viewValues)) {
                if (!isEdit) {
                    // Es wird ein neues Event erstellt und dazu einen Key von der Database angefordert
                    final String eventKey = refEvents.push().getKey();
                    if (eventKey != null) {
                        //Falls der key nicht null ist wird das Event der Database übergeben.
                        viewValues.setEventId(eventKey);
                        refEvents.child(eventKey).setValue(viewValues).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "CreateEvent:failure", e);
                                Toast.makeText(EditCreateEventActivity.this,
                                        getString(R.string.edit_create_event_error_crating_event),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Log.w(TAG, "CreateEvent:failure:NoKey");
                        Toast.makeText(EditCreateEventActivity.this,
                                getString(R.string.edit_create_event_error_crating_event),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Falls das event nur editiert wurde wird es upgedated
                    refEvents.child(viewValues.getEventId()).updateChildren(viewValues.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("UpdateEvent:failure", e);
                            Toast.makeText(EditCreateEventActivity.this,
                                    getString(R.string.edit_create_event_error_updating_event),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (event != null && !event.haveSameContent(getViewValues())) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle(R.string.edit_create_event_dialog_title);

            // set dialog message
            alertDialogBuilder
                    .setMessage(R.string.edit_create_event_dialog_massage)
                    .setCancelable(false)
                    .setPositiveButton(R.string.edit_create_event_dialog_cancel_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(R.string.edit_create_event_dialog_delete_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i(TAG, "Event not saved");
                            finish();
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();


            alertDialog.show();
        } else {
            finish();
        }
    }


}

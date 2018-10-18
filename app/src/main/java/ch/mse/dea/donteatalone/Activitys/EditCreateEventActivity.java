package ch.mse.dea.donteatalone.Activitys;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import org.joda.time.DateTime;
import org.w3c.dom.Text;

import ch.mse.dea.donteatalone.Adapter.GsonAdapter;
import ch.mse.dea.donteatalone.DataHandling.DataFormatter;
import ch.mse.dea.donteatalone.DataHandling.InputFilterMinMax;
import ch.mse.dea.donteatalone.Objects.App;
import ch.mse.dea.donteatalone.Objects.Event;
import ch.mse.dea.donteatalone.R;

public class EditCreateEventActivity extends AppCompatActivity {

    private static final String TAG = EditCreateEventActivity.class.getName();
    private final int SEEKBAR_INCREMENT = 10;
    private final int SEEKBAR_DURATION_MIN_VALUE=10;
    private final int SEEKBAR_MAX_GUEST_MIN_VALUE=1;
    private TextView txtEventName;
    private TextView txtDate;
    private TextView txtTime;
    private TextView txtDuration;
    private EditText etxtAddress;
    private EditText etxtPostcode;
    private EditText etxtCity;
    private TextView txtCountryName;
    private TextView txtMaxGuest;
    private Button btnDeleteEvent;
    private double latitude;
    private double longitude;
    private boolean isEdit;
    private SeekBar seekBarDuration;
    private SeekBar seekBarMaxGuest;
    private CountryPicker picker;
    private Event event;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("events");

    private static String getStringDuration(int duration) {
        return String.format("%02dh %02dmin", (int) Math.floor(duration / 60), (int) duration % 60);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_create_event);
        getViews();

        setupCountryPicker();

        if (getIntent().getExtras() != null) {
            Log.v(TAG, "Edit event");
            Gson gson = GsonAdapter.getGson();
            String json = getIntent().getExtras().getString(R.string.intent_edit_create_event_event + "");
            event = gson.fromJson(json, Event.class);
            setViewValues(event);
            setTitle(R.string.edit_event_title);
            btnDeleteEvent.setVisibility(View.VISIBLE);
            isEdit = true;
        } else {
            Log.v(TAG, "Create event");
            setTitle(R.string.create_event_title);
            event = null;
            btnDeleteEvent.setVisibility(View.GONE);
            latitude = 0;
            longitude = 0;
            isEdit = false;
            if (App.getDebug())
                setViewValues(new Event("", "Migros", DateTime.now().plusDays(2), 60, "Dragonerstrasse 55", "5600", "Lenzburg", "Switzerland", 8, 0, 0));
        }

        Log.i(TAG, "Finish on Create");
    }



    private Event getViewValues() {
        return new Event(
                event == null ? "" : event.getEventId(),
                txtEventName.getText().toString(),
                DataFormatter.getDateTimeFromString(txtDate.getText().toString(), txtTime.getText().toString(), "long"),
                seekBarDuration.getProgress() * SEEKBAR_INCREMENT+SEEKBAR_DURATION_MIN_VALUE,
                etxtAddress.getText().toString(),
                etxtPostcode.getText().toString(),
                etxtCity.getText().toString(),
                txtCountryName.getText().toString(),
                seekBarMaxGuest.getProgress()+SEEKBAR_MAX_GUEST_MIN_VALUE,
                latitude, longitude
        );
    }

    private void setViewValues(Event event) {
        txtEventName.setText(event.getEventName());
        txtDate.setText(DataFormatter.getDateAsString(event.getDateTime(), "long"));
        txtTime.setText(DataFormatter.getTimeAsString(event.getDateTime()));
        txtDuration.setText(getStringDuration(event.getDuration()));
        seekBarDuration.setProgress(event.getDuration());
        etxtAddress.setText(event.getAddresse());
        etxtPostcode.setText(String.valueOf(event.getPostcode()));
        etxtCity.setText(event.getCity());
        txtCountryName.setText(event.getCountry());
        txtMaxGuest.setText(String.valueOf(event.getMaxGuest()));
        seekBarMaxGuest.setProgress(event.getMaxGuest());

        latitude = event.getLatitude();
        longitude = event.getLongitude();


    }

    private void getViews() {
        txtEventName = findViewById(R.id.eventName);
        txtDate = findViewById(R.id.txt_date);
        txtTime = findViewById(R.id.txt_time);
        txtDuration = findViewById(R.id.duration);
        //etxtDuration.setFilters(new InputFilter[]{new InputFilterMinMax(0, 60 * 4)});
        etxtAddress = findViewById(R.id.addresse);
        etxtPostcode = findViewById(R.id.postcode);
        etxtCity = findViewById(R.id.city);
        txtCountryName = findViewById(R.id.countryName);
        txtMaxGuest = findViewById(R.id.maxGuest);
        btnDeleteEvent = findViewById(R.id.btn_delete_event);

        setSeekBarDuration();
        setSeekBarMaxGuest();

    }

    private void setSeekBarDuration(){
        seekBarDuration = findViewById(R.id.edit_create_event_seekBar);
        seekBarDuration.setProgress(60 / SEEKBAR_INCREMENT);
        seekBarDuration.setMax((360-SEEKBAR_DURATION_MIN_VALUE) / SEEKBAR_INCREMENT);
        seekBarDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                txtDuration.setText(getStringDuration((int) progresValue * SEEKBAR_INCREMENT+SEEKBAR_DURATION_MIN_VALUE));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void setSeekBarMaxGuest(){
        seekBarMaxGuest = findViewById(R.id.edit_create_event_seekBar_maxGuest);
        seekBarMaxGuest.setProgress(8);
        seekBarMaxGuest.setMax(30-SEEKBAR_DURATION_MIN_VALUE);
        seekBarMaxGuest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                txtDuration.setText(progresValue-SEEKBAR_MAX_GUEST_MIN_VALUE);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void setupCountryPicker() {
        picker = CountryPicker.newInstance("Select Country");  // dialog title
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                TextView countryName = findViewById(R.id.countryName);
                countryName.setText(name);
                picker.dismiss();

            }
        });

    }

    public void onClick_openCountryPicker(View view) {
        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
    }

    public void onClick_searchForAddress(View view) {
        //TODO implement Map to find Restaurant
        latitude = 0;
        longitude = 0;
    }

    public void onClick_openDatePicker(View view) {

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

    public void onClick_openTimePicker(View view) {

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

    public void onClick_deleteEvent(View view) {
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
                    public void onClick(DialogInterface dialog, int id) {
                        if (event != null) {
                            mDatabase.child(event.getEventId()).removeValue()
                                    .addOnFailureListener(
                                            new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("DeleteEvent:failure", e);
                                                    Toast.makeText(EditCreateEventActivity.this,
                                                            getString(R.string.edit_create_event_error_deleting_event),
                                                            Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                    );

                            Log.i(TAG, "Event gelöscht: \n   -ID: " + event.getEventId() + " \n   -Name: " + event.getEventName());
                            dialog.cancel();
                        }
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    public void onClick_saveEvent(View view) {

        if (!isEdit) {
            String key=mDatabase.push().getKey();
            if (key!=null) {
                Event event=getViewValues();
                event.setEventId(key);
                mDatabase.child(key).setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"CreateEvent:failure", e);
                        Toast.makeText(EditCreateEventActivity.this,
                                getString(R.string.edit_create_event_error_crating_event),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Log.w(TAG,"CreateEvent:failure:NoKey");
                Toast.makeText(EditCreateEventActivity.this,
                        getString(R.string.edit_create_event_error_crating_event),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            mDatabase.child(event.getEventId()).setValue(event, Event.class).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    @Override
    public void onBackPressed() {

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
    }


}

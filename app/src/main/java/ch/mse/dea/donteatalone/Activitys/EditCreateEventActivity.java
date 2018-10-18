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

import java.util.logging.Logger;

import ch.mse.dea.donteatalone.DataHandling.DataFormatter;
import ch.mse.dea.donteatalone.Objects.Event;
import ch.mse.dea.donteatalone.Objects.EventProvider;
import ch.mse.dea.donteatalone.Adapter.GsonAdapter;
import ch.mse.dea.donteatalone.DataHandling.InputFilterMinMax;
import ch.mse.dea.donteatalone.R;

public class EditCreateEventActivity extends AppCompatActivity {

    Logger LOGGER= Logger.getLogger(EditCreateEventActivity.class.getName());

    TextView txtEventName;
    TextView txtDate;
    TextView txtTime;
    EditText etxtDuration;
    EditText etxtAddress;
    EditText etxtPostcode;
    EditText etxtCity;
    TextView txtCountryName;
    EditText etxtMaxGuest;
    Button btnDeleteEvent;
    double latitude;
    double longitude;

    CountryPicker picker;
    Event event;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_create_event);
        getViews();
        //init db ref
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setupCountryPicker();

        if (getIntent().getExtras() != null) {
            Gson gson = GsonAdapter.getGson();
            String json = getIntent().getExtras().getString(R.string.intent_edit_create_event_event + "");
            event = gson.fromJson(json, Event.class);
            setViewValues(event);
            setTitle(R.string.edit_event_title);
            btnDeleteEvent.setVisibility(View.VISIBLE);
        } else {
            setTitle(R.string.create_event_title);
            event = null;
            btnDeleteEvent.setVisibility(View.GONE);
            latitude=0;
            longitude=0;
        }

        LOGGER.info("Finish on Create");
    }

    private Event getViewValues(){
        return new Event(
                event.getEventId(),
                txtEventName.getText().toString(),
                DataFormatter.getDateTimeFromString(txtDate.getText().toString(),txtTime.getText().toString(),"long"),
                Integer.parseInt(etxtDuration.getText().toString()),
                etxtAddress.getText().toString(),
                etxtPostcode.getText().toString(),
                etxtCity.getText().toString(),
                txtCountryName.getText().toString(),
                Integer.parseInt(etxtMaxGuest.getText().toString()),
                latitude,longitude
        );
    }

    private void setViewValues(Event event){
        txtEventName.setText(event.getEventName());
        txtDate.setText(DataFormatter.getDateAsString(event.getDate(),"long"));
        txtTime.setText(DataFormatter.getTimeAsString(event.getDate()));
        etxtDuration.setText(String.valueOf(event.getDuration()));
        etxtDuration.setFilters(new InputFilter[]{ new InputFilterMinMax(0, 60*4)});
        etxtAddress.setText(event.getAddresse());
        etxtPostcode.setText(String.valueOf(event.getPostcode()));
        etxtCity.setText(event.getCity());
        txtCountryName.setText(event.getCountry());
        etxtMaxGuest.setText(String.valueOf(event.getMaxGuest()));

        latitude=event.getLatitude();
        longitude=event.getLongitude();
    }

    private void getViews() {
        txtEventName=findViewById(R.id.eventName);
        txtDate = findViewById(R.id.txt_date);
        txtTime = findViewById(R.id.txt_time);
        etxtDuration = findViewById(R.id.duration);
        etxtDuration.setFilters(new InputFilter[]{ new InputFilterMinMax(0, 60*4)});
        etxtAddress = findViewById(R.id.addresse);
        etxtPostcode = findViewById(R.id.postcode);
        etxtCity = findViewById(R.id.city);
        txtCountryName = findViewById(R.id.countryName);
        etxtMaxGuest = findViewById(R.id.maxGuest);
        etxtMaxGuest.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 30)});

        btnDeleteEvent = findViewById(R.id.btn_delete_event);
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
        latitude=0;
        longitude=0;
    }

    public void onClick_openDatePicker(View view) {


        DateTime dateTime=DataFormatter.getDateFromString(txtDate.getText().toString(),"long");
        if (dateTime==null) dateTime=DateTime.now();

        int mYear = dateTime.getYear();
        int mMonth = dateTime.getMonthOfYear();
        int mDay = dateTime.getDayOfMonth();


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txtDate.setText(DataFormatter.getDateAsString(year,monthOfYear,dayOfMonth, "long"));

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void onClick_openTimePicker(View view) {

        DateTime dateTime=DataFormatter.getTimeFromString(txtTime.getText().toString());
        if (dateTime==null) dateTime=DateTime.now();

        int mHour = dateTime.getHourOfDay();
        int mMinute = dateTime.getMinuteOfHour();

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        txtTime.setText(DataFormatter.getTimeAsString(hourOfDay,minute));
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
                .setPositiveButton(R.string.edit_create_event_dialog_cancel_button,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.edit_create_event_dialog_delete_button,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        LOGGER.info("Event gelöscht. \nID: "+event+" \nName: "+event.getEventName());
                        EventProvider.removeEvent(event);
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void onClick_saveEvent(View view) {
        String keygen = mDatabase.child("events").push().getKey();
        mDatabase.child("events").child(keygen).setValue(getViewValues()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        //change intent here
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("CreateEvent:failure", e);
                        Toast.makeText(EditCreateEventActivity.this,
                                "Creating an event has failed, please try again later" +
                                        e.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
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
                .setPositiveButton(R.string.edit_create_event_dialog_cancel_button,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.edit_create_event_dialog_delete_button,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        LOGGER.info("Event not saved");
                        finish();
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}

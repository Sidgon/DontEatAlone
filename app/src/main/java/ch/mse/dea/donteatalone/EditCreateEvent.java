package ch.mse.dea.donteatalone;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import java.util.Calendar;

public class EditCreateEvent extends AppCompatActivity {


    CountryPicker picker;
    TextView txtDate;
    TextView txtTime;
    Intent intent;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_create_event);
        getViews();
        setupCountryPicker();

        if( getIntent().getExtras() != null)
        {
            Gson gson = new Gson();
            String json=getIntent().getExtras().getString(R.string.intent_edit_create_event_event+"");
            event=gson.fromJson(json,Event.class);
            setViewValues();
            setTitle(R.string.edit_event_title);
        }else {
            setTitle(R.string.create_event_title);
            event=null;
        }
    }

    private void setViewValues(){

    }


    private void getViews(){
        txtDate= findViewById(R.id.txt_date);
        txtTime= findViewById(R.id.txt_time);
    }


    private void setupCountryPicker(){
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

    public void onClick_openPicker(View view){
        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
    }

    public void onClick_searchForAddress(View view) {
    }

    public void onClick_openDatePicker(View view) {


            // Get Current Date
            final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }



    public void onClick_openTimePicker(View view) {

        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        txtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}

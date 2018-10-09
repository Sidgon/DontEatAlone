package ch.mse.dea.donteatalone;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.joda.time.DateTime;


public class GoingEventsListActivity extends ListActivity {

    Event[] events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int numRows = 64;
        super.onCreate(savedInstanceState);
        events = new Event[numRows];


        for (int i = 0; i < numRows; i++) {
            events[i] = new Event("Restaurant " + i, DateTime.now(), 60 + i * 2, "Musterstrasse " + i, i * 100 + "", "Bern", "Switzerland", i, null);
        }

        setContentView(R.layout.activity_going_events_list);
        // construct and register the adapter
        GoingEventsListArrayAdapter adapter = new GoingEventsListArrayAdapter(
                this, events);


        setListAdapter(adapter);


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(
                getApplicationContext(),
                "Position :" + position+" "+events[position].getRestaurantName(), Toast.LENGTH_LONG).show();
    }
}

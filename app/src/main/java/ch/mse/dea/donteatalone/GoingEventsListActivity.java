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
        events = new Event[numRows];
        for (int i = 0; i < numRows; i++) {
            events[i] = new Event("Restaurant " + i, DateTime.now(), 60 + i * 2, "Musterstrasse " + i, i * 100 + "", "Bern", "Switzerland", i, null);
        }

        setContentView(R.layout.activity_events_list);
        // construct and register the adapter
        EventsListArrayAdapter adapter = new EventsListArrayAdapter(
                this, events);


        setListAdapter(adapter);


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Event event = events[position];

        Toast.makeText(
                getApplicationContext(),
                "Position :" + position+" "+event.getEventName(), Toast.LENGTH_LONG).show();
    }
}

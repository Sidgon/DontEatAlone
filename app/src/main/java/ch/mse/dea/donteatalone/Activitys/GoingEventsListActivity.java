package ch.mse.dea.donteatalone.Activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ch.mse.dea.donteatalone.Objects.Event;
import ch.mse.dea.donteatalone.Adapter.EventsListArrayAdapter;
import ch.mse.dea.donteatalone.R;
import ch.mse.dea.donteatalone.Objects.UserProvider;


public class GoingEventsListActivity extends AppCompatActivity {

    ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        setTitle(R.string.going_events_list_activity_tile);

        events = UserProvider.getGoingEvents(null);

        setupListView();
    }

    private void setupListView() {
        ListView listView = findViewById(R.id.event_listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {

                Event event = events.get(position);

                Toast.makeText(
                        getApplicationContext(),
                        "Position :" + position + " " + event.getEventName(), Toast.LENGTH_LONG).show();
            }

        });

        listView.setEmptyView(findViewById(R.id.event_empty));

        EventsListArrayAdapter adapter = new EventsListArrayAdapter(this, events);
        listView.setAdapter(adapter);
    }


}

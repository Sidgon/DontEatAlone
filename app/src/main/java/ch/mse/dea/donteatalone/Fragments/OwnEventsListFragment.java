package ch.mse.dea.donteatalone.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.gson.Gson;

import ch.mse.dea.donteatalone.Activitys.EditCreateEventActivity;
import ch.mse.dea.donteatalone.Adapter.EventsListFirebaseAdapter;
import ch.mse.dea.donteatalone.Adapter.GsonAdapter;
import ch.mse.dea.donteatalone.Objects.Event;
import ch.mse.dea.donteatalone.R;


public class OwnEventsListFragment extends Fragment {
    private static final String TAG = OwnEventsListFragment.class.getName();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference refUsersEvents = mDatabase.child("users_events");
    private EventsListFirebaseAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {// Please note the third parameter should be false, otherwise a java.lang.IllegalStateException maybe thrown.
        View retView = inflater.inflate(R.layout.activity_events_list, container, false);
        onClick_newEvent(retView);

        Query query = refUsersEvents.child(firebaseUser.getUid());
        FirebaseListOptions<Event> options = new FirebaseListOptions.Builder<Event>()
                .setLayout(R.layout.activity_events_list_item)
                .setQuery(query, Event.class)
                .build();

        adapter = new EventsListFirebaseAdapter(options);

        ListView listView = retView.findViewById(R.id.event_listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {

                Event event = adapter.getItem(position);
                intentToEditCreateEventActivity(event);
            }

        });
        listView.setEmptyView(retView.findViewById(R.id.event_empty));
        listView.setAdapter(adapter);

        return retView;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    private void intentToEditCreateEventActivity(Event event) {
        Gson gson = GsonAdapter.getGson();
        Intent intent = new Intent(getActivity(), EditCreateEventActivity.class);
        intent.putExtra(R.string.intent_edit_create_event_event + "", gson.toJson(event));
        startActivity(intent);
    }

    public void onClick_newEvent(View view) {
        FloatingActionButton fabs = view.findViewById(R.id.user_profile_fab);
        fabs.show();
        fabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditCreateEventActivity.class);
                startActivity(intent);
            }
        });
    }
}
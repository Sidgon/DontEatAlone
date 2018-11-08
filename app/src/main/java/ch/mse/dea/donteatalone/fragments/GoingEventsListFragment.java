package ch.mse.dea.donteatalone.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import ch.mse.dea.donteatalone.activitys.InfoEventActivity;
import ch.mse.dea.donteatalone.adapter.EventsListFirebaseAdapter;
import ch.mse.dea.donteatalone.adapter.GsonAdapter;
import ch.mse.dea.donteatalone.objects.Event;
import ch.mse.dea.donteatalone.R;


public class GoingEventsListFragment extends Fragment {
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference refUsersGoingEvents = mDatabase.child("users_going_events");
    private EventsListFirebaseAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View retView = inflater.inflate(R.layout.activity_events_list, container, false);
        FloatingActionButton fabs = retView.findViewById(R.id.user_profile_fab);
        fabs.hide();

        Query query = refUsersGoingEvents.child(firebaseUser.getUid());
        FirebaseListOptions<Event> options = new FirebaseListOptions.Builder<Event>()
                .setLayout(R.layout.activity_events_list_item)
                .setQuery(query, Event.class)
                .build();

        adapter = new EventsListFirebaseAdapter(options);

        ListView listView = retView.findViewById(R.id.event_listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                Event event = adapter.getItem(position);
                intentToInfoEvent(event);
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

    private void intentToInfoEvent(Event event) {
        Gson gson = GsonAdapter.getGson();

        Intent intent = new Intent(getActivity(), InfoEventActivity.class);
        intent.putExtra(R.string.intent_info_event + "", gson.toJson(event));
        intent.putExtra(R.string.intent_info_event_going_user + "", InfoEventActivity.IS_GOING);

        startActivity(intent);
    }

}



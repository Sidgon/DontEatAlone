package ch.mse.dea.donteatalone.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import ch.mse.dea.donteatalone.Activitys.InfoEventActivity;
import ch.mse.dea.donteatalone.Adapter.EventsListArrayAdapter;
import ch.mse.dea.donteatalone.Adapter.GsonAdapter;
import ch.mse.dea.donteatalone.Objects.Event;
import ch.mse.dea.donteatalone.Objects.User;
import ch.mse.dea.donteatalone.R;


public class GoingEventsListFragment extends Fragment {
    private static final String TAG = GoingEventsListFragment.class.getName();
    private FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference refUsersGoingEvents = mDatabase.child("users_going_events");
    private DatabaseReference refEvents = mDatabase.child("events");
    private EventsListArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View retView = inflater.inflate(R.layout.activity_events_list, container, false);
        FloatingActionButton fabs= retView.findViewById(R.id.user_profile_fab);
        fabs.hide();

        adapter = new EventsListArrayAdapter(getContext(), new ArrayList<Event>());
        getEvents();

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

    private void intentToInfoEvent(Event event) {
        Gson gson = GsonAdapter.getGson();

        Intent intent = new Intent(getActivity(), InfoEventActivity.class);
        intent.putExtra(R.string.intent_info_event + "", gson.toJson(event));

        startActivity(intent);
    }

    private void getEvents() {
        refUsersGoingEvents.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey() != null) {
                        refEvents.child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Event event = dataSnapshot.getValue(Event.class);

                                if (event != null && event.getEventId() != null) {
                                    adapter.remove(event);
                                    adapter.add(event);
                                    Log.i(TAG, "GoingEvent Data Chanched");
                                } else {
                                    adapter.remove(new Event(dataSnapshot.getKey()));
                                    Log.i(TAG, "GoingEvent Data deleted");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }


                }
            }


        });
    }
}



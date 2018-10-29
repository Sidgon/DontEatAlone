package ch.mse.dea.donteatalone.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import ch.mse.dea.donteatalone.Activitys.EditCreateEventActivity;
import ch.mse.dea.donteatalone.Activitys.LoginActivity;
import ch.mse.dea.donteatalone.Activitys.MainActivity;
import ch.mse.dea.donteatalone.Adapter.EventsListArrayAdapter;
import ch.mse.dea.donteatalone.Adapter.GsonAdapter;
import ch.mse.dea.donteatalone.Objects.App;
import ch.mse.dea.donteatalone.Objects.Event;
import ch.mse.dea.donteatalone.Objects.User;
import ch.mse.dea.donteatalone.R;


public class OwnEventsListFragment extends Fragment {
    private static final String TAG= OwnEventsListFragment.class.getName();
    private FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference refUsersEvents = mDatabase.child("users_events");
    private DatabaseReference refEvents = mDatabase.child("events");
    private EventsListArrayAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {// Please note the third parameter should be false, otherwise a java.lang.IllegalStateException maybe thrown.
        View retView = inflater.inflate(R.layout.activity_events_list, container, false);
        onClick_newEvent(retView);
        adapter = new EventsListArrayAdapter(getContext(), new ArrayList<Event>());
        getEvents();
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



    private void intentToEditCreateEventActivity(Event event) {
        Gson gson = GsonAdapter.getGson();
        Intent intent = new Intent(getActivity(), EditCreateEventActivity.class);
        intent.putExtra(R.string.intent_edit_create_event_event + "", gson.toJson(event));
        startActivity(intent);
    }


    private void getEvents() {
        refUsersEvents.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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

                                if (event!=null && event.getEventId()!=null) {
                                    adapter.remove(event);
                                    adapter.add(event);
                                    Log.i(TAG,"OwnEvent Data Chanched");
                                }else {
                                    adapter.remove(new Event(dataSnapshot.getKey()));
                                    Log.i(TAG,"OwnEvent Data deleted");
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

    public void onClick_newEvent(View view) {
        FloatingActionButton fabs= view.findViewById(R.id.user_profile_fab);
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
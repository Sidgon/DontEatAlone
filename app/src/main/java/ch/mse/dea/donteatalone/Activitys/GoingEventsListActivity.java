package ch.mse.dea.donteatalone.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

import ch.mse.dea.donteatalone.Adapter.EventsListArrayAdapter;
import ch.mse.dea.donteatalone.Adapter.GsonAdapter;
import ch.mse.dea.donteatalone.Objects.App;
import ch.mse.dea.donteatalone.Objects.Event;
import ch.mse.dea.donteatalone.Objects.User;
import ch.mse.dea.donteatalone.R;


public class GoingEventsListActivity extends AppCompatActivity {
    private static final String TAG=GoingEventsListActivity.class.getName();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference refUsersGoingEvents = mDatabase.child("users_going_events");
    private DatabaseReference refEvents = mDatabase.child("events");
    private EventsListArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser!=null) {
            setTitle(R.string.going_events_list_activity_tile);
            adapter = new EventsListArrayAdapter(this, new ArrayList<Event>());
            getEvents();
            setupListView();
        }else {
            Toast.makeText(this,R.string.user_not_logedin,Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void setupListView() {
        ListView listView = findViewById(R.id.event_listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                Event event = adapter.getItem(position);
                intentToInfoEvent(event);
            }

        });

        listView.setEmptyView(findViewById(R.id.event_empty));

        listView.setAdapter(adapter);
    }

    private void intentToInfoEvent(Event event) {
        Gson gson = GsonAdapter.getGson();

        Intent intent = new Intent(this, InfoEventActivity.class);
        intent.putExtra(R.string.intent_info_event + "", gson.toJson(event));

        startActivity(intent);
    }

    private void getEvents() {
        refUsersGoingEvents.child(User.getLoggedUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                    Log.i(TAG,"GoingEvent Data Chanched");
                                }else {
                                    adapter.remove(new Event(dataSnapshot.getKey()));
                                    Log.i(TAG,"GoingEvent Data deleted");
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



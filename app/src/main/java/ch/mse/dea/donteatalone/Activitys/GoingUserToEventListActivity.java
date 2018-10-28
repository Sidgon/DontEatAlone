package ch.mse.dea.donteatalone.Activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import ch.mse.dea.donteatalone.Adapter.GsonAdapter;
import ch.mse.dea.donteatalone.Adapter.UsersListArrayAdapter;
import ch.mse.dea.donteatalone.Objects.App;
import ch.mse.dea.donteatalone.Objects.Event;
import ch.mse.dea.donteatalone.Objects.User;
import ch.mse.dea.donteatalone.R;

public class GoingUserToEventListActivity extends AppCompatActivity {

    private static final String TAG = GoingUserToEventListActivity.class.getName();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference refEventUsers = mDatabase.child("event_users");
    private DatabaseReference refUsers = mDatabase.child("users");
    private UsersListArrayAdapter adapter;

    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_going_user_to_event_list);

        setTitle(R.string.going_user_to_event_list_title);


        if (getIntent().getExtras() != null) {
            Gson gson = GsonAdapter.getGson();
            String json = getIntent().getExtras().getString(R.string.intent_going_user_to_event + "");
            event = gson.fromJson(json, Event.class);
            App.log(TAG, "Event of Intent:\n"+event.getEventId());
        } else {
            App.log(TAG, "Couldn't extract User from Intent");
            finish();
        }

        adapter = new UsersListArrayAdapter(this, new ArrayList<User>());
        getUsers();
        setupListView();


    }


    private void setupListView() {
        ListView listView = findViewById(R.id.eventUsers_listView);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
//                User item = adapter.getItem(position);
//                // TODO Falls mal mehr Userinformationen vorhanden zum Profile des Users
//            }
//
//        });

        listView.setEmptyView(findViewById(R.id.eventUsers_empty));

        listView.setAdapter(adapter);
    }


    private void getUsers() {
        refEventUsers.child(event.getEventId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey() != null) {
                        refUsers.child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);

                                if (user != null && user.getUserId() != null) {
                                    adapter.remove(user);
                                    adapter.add(user);
                                    Log.i(TAG, "User Data Chanched");
                                } else {
                                    adapter.remove(new User(dataSnapshot.getKey()));
                                    Log.i(TAG, "User Data deleted");
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

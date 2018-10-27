package ch.mse.dea.donteatalone.Activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import ch.mse.dea.donteatalone.Adapter.GsonAdapter;
import ch.mse.dea.donteatalone.Objects.User;
import ch.mse.dea.donteatalone.R;

public class UserProfileActivity extends AppCompatActivity {

    private static String TAG = UserProfileActivity.class.getName();
    ImageView image;
    TextView txtUsername;
    TextView txtFullName;
    User user;

    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser!=null) {
            getViews();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            setUserListener();
        }else {
            Toast.makeText(this,R.string.user_not_logedin,Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void setUserListener() {
        mDatabase.child("users").child(User.getLoggedUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                if (user != null) {
                    if (user.getImage() != null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
                        image.setImageBitmap(bmp);
                    }

                    String fullname = user.getFirstname() + " " + user.getLastname();
                    txtFullName.setText(fullname);
                    txtUsername.setText(user.getUsername());
                    Log.w(TAG, "UserDataChange:" + user.getUserId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    private void getViews() {
        image = findViewById(R.id.user_profile_image);
        txtFullName = findViewById(R.id.user_profile_txtFullName);
        txtUsername = findViewById(R.id.user_profile_txtUsername);
    }

    public void onClick_btnLogout(View view) {
        FirebaseAuth.getInstance().signOut();
        moveTaskToBack(true);
        System.exit(0);
    }

    public void onClick_btnEditProfile(View view) {

        Gson gson = GsonAdapter.getGson();
        Intent intent = new Intent(this, EditUserProfileActivity.class);
        intent.putExtra(R.string.intent_edit_user_profile_user + "", gson.toJson(user));
        startActivity(intent);
    }

    public void onClick_btnGoingEvents(View view) {
        Intent intent = new Intent(this, GoingEventsListActivity.class);
        startActivity(intent);

    }

    public void onClick_btnCreatedEvents(View view) {
        Intent intent = new Intent(this, OwnEventsListActivity.class);
        startActivity(intent);
    }

    public void onClick_newEvent(View view) {
        Intent intent = new Intent(this, EditCreateEventActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }
}

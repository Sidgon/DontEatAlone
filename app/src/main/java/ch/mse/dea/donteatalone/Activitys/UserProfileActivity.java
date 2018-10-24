package ch.mse.dea.donteatalone.Activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;

import ch.mse.dea.donteatalone.Adapter.GsonAdapter;
import ch.mse.dea.donteatalone.Objects.User;
import ch.mse.dea.donteatalone.R;

public class UserProfileActivity extends AppCompatActivity {

    private static String TAG=UserProfileActivity.class.getName();
    ImageView image;
    TextView txtUsername;
    TextView txtFullName;
    User user;

    DatabaseReference mDatabase;
    ValueEventListener userListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getViews();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setUserListener();
    }

    public void setUserListener(){
        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                if (user.getImage()!=null){
                    Bitmap bmp = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
                    image.setImageBitmap(bmp);
                }
                txtFullName.setText(user.getFirstname()+" "+user.getLastname());
                txtUsername.setText(user.getUsername());
                Log.w(TAG, "UserDataChange:" + user.getuserId());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child("users").child(User.getLoggedUserId()).addValueEventListener(userListener);
    }

    private void getViews(){

        image=findViewById(R.id.user_profile_image);
        txtFullName=findViewById(R.id.user_profile_txtFullName);
        txtUsername=findViewById(R.id.user_profile_txtUsername);

    }

    public void onClick_btnEditProfile(View view) {

        Gson gson=GsonAdapter.getGson();
        Intent intent=new Intent(this,EditUserProfileActivity.class);
        intent.putExtra(R.string.intent_edit_user_profile_user+"",gson.toJson(user));
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
        Intent intent=new Intent(this,EditCreateEventActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDatabase.removeEventListener(userListener);
    }
}
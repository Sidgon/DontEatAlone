package ch.mse.dea.donteatalone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

    public void onClick_btnEditProfile(View view) {
        Intent intent = new Intent(this, EditUserProfileActivity.class);
        startActivity(intent);
    }

    public void onClick_btnGoingEvents(View view) {
        Intent intent = new Intent(this, GoingEventsListActivity.class);
        startActivity(intent);

    }

    public void onClick_btnCreatedEvents(View view) {
    }
}

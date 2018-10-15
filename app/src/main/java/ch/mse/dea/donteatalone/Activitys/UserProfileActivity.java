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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getViews();

        user=User.getGlobalUser();

        if (user.getImage()!=null){
            Bitmap bmp = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
            image.setImageBitmap(bmp);
        }

        txtFullName.setText(user.getFirstname()+" "+user.getLastname());
        txtUsername.setText(user.getUsername());

    }

    private void getViews(){

        image=findViewById(R.id.user_profile_image);
        txtFullName=findViewById(R.id.user_profile_txtFullName);
        txtUsername=findViewById(R.id.user_profile_txtUsername);

    }

    public void onClick_btnEditProfile(View view) {

        Gson gson=GsonAdapter.getGson();
        Intent intent=new Intent(this,EditUserProfileActivity.class);
        intent.putExtra(R.string.intent_edit_user_profile_user+"",gson.toJson(User.getGlobalUser()));
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
}

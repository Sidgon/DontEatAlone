package ch.mse.dea.donteatalone.Activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ch.mse.dea.donteatalone.Fragments.BlankFragment;
import ch.mse.dea.donteatalone.Fragments.GoingEventsListFragment;
import ch.mse.dea.donteatalone.Fragments.OwnEventsListFragment;
import ch.mse.dea.donteatalone.Objects.App;
import ch.mse.dea.donteatalone.R;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager = this.getSupportFragmentManager();
    FragmentTransaction fragmentTransaction;
    BlankFragment blankFragment;
    OwnEventsListFragment ownEventsListFragment;
    GoingEventsListFragment goingEventsListFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginCheck();

        blankFragment=new BlankFragment();
        ownEventsListFragment=new OwnEventsListFragment();
        goingEventsListFragment=new GoingEventsListFragment();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_activity_fragment, goingEventsListFragment);
        fragmentTransaction.add(R.id.main_activity_fragment, ownEventsListFragment);
        fragmentTransaction.add(R.id.main_activity_fragment,blankFragment);
        fragmentTransaction.show(blankFragment);
        fragmentTransaction.hide(goingEventsListFragment);
        fragmentTransaction.hide(ownEventsListFragment);
        fragmentTransaction.commit();

        final BottomNavigationView bottomNavigationView = findViewById(R.id.main_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.main_navigation_item_map:
                                setTitle(R.string.app_name);
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.show(blankFragment);
                                fragmentTransaction.hide(goingEventsListFragment);
                                fragmentTransaction.hide(ownEventsListFragment);

                                fragmentTransaction.commit();
                                break;
                            case R.id.main_navigation_item_goning_events:
                                setTitle(R.string.going_events_list_activity_tile);
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.hide(blankFragment);
                                fragmentTransaction.show(goingEventsListFragment);
                                fragmentTransaction.hide(ownEventsListFragment);
                                fragmentTransaction.commit();
                                break;
                            case R.id.main_navigation_item_own_events:
                                setTitle(R.string.own_events_list_activity_tile);
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.hide(blankFragment);
                                fragmentTransaction.hide(goingEventsListFragment);
                                fragmentTransaction.show(ownEventsListFragment);
                                fragmentTransaction.commit();
                                break;
                        }

                        bottomNavigationView.getMenu().findItem(item.getItemId()).setChecked(true);
                        return false;
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        loginCheck();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_icons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_bar_icon_profile:
                Intent intent=new Intent(this, UserProfileActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
    }


    private void loginCheck(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null){
            App.print("User=null");

            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}

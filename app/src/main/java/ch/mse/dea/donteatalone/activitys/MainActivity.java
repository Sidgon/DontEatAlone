package ch.mse.dea.donteatalone.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import ch.mse.dea.donteatalone.R;
import ch.mse.dea.donteatalone.fragments.GoingEventsListFragment;
import ch.mse.dea.donteatalone.fragments.MapFragment;
import ch.mse.dea.donteatalone.fragments.OwnEventsListFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private FragmentManager fragmentManager = this.getSupportFragmentManager();
    private FragmentTransaction fragmentTransaction;
    private MapFragment mapFragment;
    private OwnEventsListFragment ownEventsListFragment;
    private GoingEventsListFragment goingEventsListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mapFragment = new MapFragment();
        ownEventsListFragment = new OwnEventsListFragment();
        goingEventsListFragment = new GoingEventsListFragment();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_activity_fragment, goingEventsListFragment);
        fragmentTransaction.add(R.id.main_activity_fragment, ownEventsListFragment);
        fragmentTransaction.add(R.id.main_activity_fragment, mapFragment);
        fragmentTransaction.show(mapFragment);
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
                                fragmentTransaction.show(mapFragment);
                                fragmentTransaction.hide(goingEventsListFragment);
                                fragmentTransaction.hide(ownEventsListFragment);
                                fragmentTransaction.commit();
                                break;
                            case R.id.main_navigation_item_goning_events:
                                setTitle(R.string.going_events_list_activity_tile);
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.hide(mapFragment);
                                fragmentTransaction.show(goingEventsListFragment);
                                fragmentTransaction.hide(ownEventsListFragment);
                                fragmentTransaction.commit();
                                break;
                            case R.id.main_navigation_item_own_events:
                                setTitle(R.string.own_events_list_activity_tile);
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.hide(mapFragment);
                                fragmentTransaction.hide(goingEventsListFragment);
                                fragmentTransaction.show(ownEventsListFragment);
                                fragmentTransaction.commit();
                                break;
                            default:
                                break;
                        }

                        bottomNavigationView.getMenu().findItem(item.getItemId()).setChecked(true);
                        return false;
                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_icons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_bar_icon_profile) {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        //Not needed
    }


}

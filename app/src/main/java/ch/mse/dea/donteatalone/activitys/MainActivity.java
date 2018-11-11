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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import ch.mse.dea.donteatalone.R;
import ch.mse.dea.donteatalone.fragments.BlankFragment;
import ch.mse.dea.donteatalone.fragments.GoingEventsListFragment;
import ch.mse.dea.donteatalone.fragments.OwnEventsListFragment;
import ch.mse.dea.donteatalone.objects.App;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private FragmentManager fragmentManager = this.getSupportFragmentManager();
    private FragmentTransaction fragmentTransaction;
    private BlankFragment blankFragment;
    private OwnEventsListFragment ownEventsListFragment;
    private GoingEventsListFragment goingEventsListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        loginCheck();

        blankFragment = new BlankFragment();
        ownEventsListFragment = new OwnEventsListFragment();
        goingEventsListFragment = new GoingEventsListFragment();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_activity_fragment, goingEventsListFragment);
        fragmentTransaction.add(R.id.main_activity_fragment, ownEventsListFragment);
        fragmentTransaction.add(R.id.main_activity_fragment, blankFragment);
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
                            default:
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


    private void loginCheck() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null && currentUser.getEmail()!=null) {
            mAuth.fetchSignInMethodsForEmail(currentUser.getEmail()).addOnSuccessListener(new OnSuccessListener<SignInMethodQueryResult>() {
                @Override
                public void onSuccess(SignInMethodQueryResult signInMethodQueryResult) {
                    if (signInMethodQueryResult==null || signInMethodQueryResult.getSignInMethods()==null || signInMethodQueryResult.getSignInMethods().isEmpty()) {
                        intentToLogin();
                    }
                }
            });

        } else {
            intentToLogin();
        }
    }

    private void intentToLogin() {
        FirebaseAuth.getInstance().signOut();

        App.log(TAG, "User=null");

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}

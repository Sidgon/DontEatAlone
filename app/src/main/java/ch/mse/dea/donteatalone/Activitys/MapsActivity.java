package ch.mse.dea.donteatalone.Activitys;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ch.mse.dea.donteatalone.Adapter.EventsListArrayAdapter;
import ch.mse.dea.donteatalone.Objects.Event;
import ch.mse.dea.donteatalone.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static final int REQUEST_LOCATION_PERM = 59;
    private static final String TAG = "Maps";

    //define db refs
    private DatabaseReference mDatabase;
    private DatabaseReference refEvents;
    private EventsListArrayAdapter adapter;
    private ArrayList<Event> events;

    //map
    private GoogleMap mMap;

    //locations
    private LocationManager locationManager;
    private String provider;
    private Location mLastLocation;
    private Location closestLocation;

    //authenticated user
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //requests location permission
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION_PERM);

    }

    @Override
    public void onStart() {
        super.onStart();
        //get phone location
        try {
            getPhoneLocation();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //get current user from authentication service
        mAuth=FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        //add db refs
        mDatabase = FirebaseDatabase.getInstance().getReference();
        refEvents = mDatabase.child("events");
        //attach firebase listener
        setEventListener();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }

    /*
        @Override
        protected void onRestart() {
            super.onRestart();
            mMap.clear();
            try {
                loadMap();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    */

    //lets you allow location access in app instead of going to phone settings
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERM: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Location permission was granted");
                    return;

                } else {

                    AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                    myAlert.setMessage("Could not access your location!")
                            .setPositiveButton("Continue..", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setTitle("Location error!")
                            .create();
                    myAlert.show();
                }
                return;
            }
        }
    }

    public void getPhoneLocation()throws ExecutionException, InterruptedException {
        //get location of phone
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use
        // default
        Criteria criteria = new Criteria();
        criteria.setAccuracy( Criteria.ACCURACY_FINE );
        provider = locationManager.getBestProvider(criteria, false);
        if ( provider == null ) {
            Log.e( TAG, "No location provider found!" );
            return;
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
            myAlert.setMessage("Could not access your location!")
                    .setPositiveButton("Continue..", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setTitle("Location error!")
                    .create();
            myAlert.show();
        }
        mLastLocation = locationManager.getLastKnownLocation(provider);
        Log.d(TAG, mLastLocation.toString());
    }


    // load db with events
    public void setEventListener() {
        refEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                events = new ArrayList<> ();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey() != null && snapshot.getValue()!=null) {
                        events.add(snapshot.getValue(Event.class));
                    }
                    else{
                        Log.d(TAG, "Exepction reading from firebase");
                    }
                }




            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //create all markers that need to be put onto map
        if (events != null) {
            Marker[] markers = new Marker[events.size()];
            //loop all event entries
            for (int i = 0; i < events.size(); i++) {
                //check if logged user is NOT the owner of the event
                //if (!events.get(i).getUserIdOfCreator().equals(currentUser.getUid())) {
                //creates a marker
                markers[i] = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(events.get(i).getLatitude(), events.get(i).getLongitude()))
                        .title(events.get(i).getAddress()));
                markers[i].setTag(0);
                //}
            }
        }

        if (mLastLocation != null) {
            Marker currentposition = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                    .title("You")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            currentposition.setTag(0);

            calculateClosest();
            if (closestLocation != null) {
                Marker closest = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(closestLocation.getLatitude(), closestLocation.getLongitude()))
                        .title("you shouldnt see this marker :)"));
                closest.setTag(0);
                closest.setVisible(false);

                builder.include(closest.getPosition());
            }

            //the include method will calculate the min and max bound.
            builder.include(currentposition.getPosition());

            LatLngBounds bounds = builder.build();

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

            //mMap.setOnMarkerClickListener(this);
            mMap.animateCamera(cu);
        }
        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);

    }

    //calculate closest restaurants relative to phone location
    public Location calculateClosest(){

        closestLocation=new Location("");

        float smallestDistance = -1;
        ArrayList<Location> locations = new ArrayList<>();
        //create a list of all possible locations available
        for (int i=0; i<events.size(); i++){
            Location temp = new Location("");
            temp.setLatitude(events.get(i).getLatitude());
            temp.setLongitude(events.get(i).getLongitude());
            locations.add(temp);
        }
        //algo which calculates closes from list
        for(Location location : locations){
            float distance = mLastLocation.distanceTo(location);
            if(smallestDistance == -1 || distance < smallestDistance){
                closestLocation = location;
                smallestDistance = distance;
            }
        }
        return closestLocation;
    }

    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        String addressClicked =  marker.getTitle();

        //open eventview here

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }


}
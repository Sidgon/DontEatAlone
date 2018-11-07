package ch.mse.dea.donteatalone.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ch.mse.dea.donteatalone.Activitys.EditCreateEventActivity;
import ch.mse.dea.donteatalone.Activitys.InfoEventActivity;
import ch.mse.dea.donteatalone.Adapter.GsonAdapter;
import ch.mse.dea.donteatalone.Objects.Event;
import ch.mse.dea.donteatalone.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static final int REQUEST_LOCATION_PERM = 59;
    private static final String TAG = "Maps";

    //define db refs
    private DatabaseReference mDatabase;
    private DatabaseReference refEvents;
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
    private View refView;


    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        refView = inflater.inflate(R.layout.activity_maps, container, false);
        //register button event
        onClick_MyLocationButton(refView);
        // Inflate the layout for this fragment
        requestPermissions(
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION_PERM);


        return refView;
    }

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

                    //get phone location
                    try {
                        getPhoneLocation();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //get current user from authentication service
                    mAuth = FirebaseAuth.getInstance();
                    currentUser = mAuth.getCurrentUser();
                    //add db refs
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    refEvents = mDatabase.child("events");
                    //attach firebase listener
                    setEventListener();
                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                    return;

                } else {

                    AlertDialog.Builder myAlert = new AlertDialog.Builder(getActivity());
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

    public void getPhoneLocation() throws ExecutionException, InterruptedException {
        //get location of phone
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use
        // default
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = locationManager.getBestProvider(criteria, false);
        if (provider == null) {
            Log.e(TAG, "No location provider found!");
            return;
        }
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder myAlert = new AlertDialog.Builder(getActivity());
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

        if (mLastLocation != null) Log.d(TAG, mLastLocation.toString());
    }

    // load db with events
    public void setEventListener() {
        refEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                events = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey() != null && snapshot.getValue() != null) {
                        events.add(snapshot.getValue(Event.class));
                    } else {
                        Log.d(TAG, "Exepction reading from firebase");
                    }
                }

                loadMap();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void loadMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

                Event event = events.get(i);


                //check if logged user is owner off event and change its color
                String loggedUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (event.getUserIdOfCreator().equals(loggedUserId)) {
                    markers[i] = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(event.getLatitude(), event.getLongitude()))
                            .icon(BitmapDescriptorFactory.defaultMarker(45)));
                    markers[i].setTag(event.getEventId());
                } else {
                    markers[i] = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(event.getLatitude(), event.getLongitude()))
                            .icon(BitmapDescriptorFactory.defaultMarker(0)));
                    markers[i].setTag(event.getEventId());
                }
            }
        }

        if (mLastLocation != null) {
            Marker currentposition = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                    .title("You")
                    .icon(BitmapDescriptorFactory.defaultMarker(200)));
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

            mMap.animateCamera(cu, new GoogleMap.CancelableCallback() {
                //needed because zoom is too much in bounds, workaround for google map
                //https://stackoverflow.com/questions/32044086/fix-zoom-level-between-two-marker-in-google-map
                public void onCancel() {
                }

                public void onFinish() {
                    CameraUpdate zout = CameraUpdateFactory.zoomBy(-0.5f);
                    mMap.animateCamera(zout);
                }
            });
        }
        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);

    }

    //calculate closest restaurants relative to phone location
    public Location calculateClosest() {

        closestLocation = new Location("");

        float smallestDistance = -1;
        ArrayList<Location> locations = new ArrayList<>();
        //create a list of all possible locations available
        for (int i = 0; i < events.size(); i++) {
            Location temp = new Location("");
            temp.setLatitude(events.get(i).getLatitude());
            temp.setLongitude(events.get(i).getLongitude());
            locations.add(temp);
        }
        //algo which calculates closes from list
        for (Location location : locations) {
            float distance = mLastLocation.distanceTo(location);
            if (smallestDistance == -1 || distance < smallestDistance) {
                closestLocation = location;
                smallestDistance = distance;
            }
        }
        return closestLocation;
    }

    public void onClick_MyLocationButton(View view) {
        FloatingActionButton fabs = view.findViewById(R.id.my_location_fab);
        fabs.show();
        fabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getPhoneLocation();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(),
                //         mLastLocation.getLongitude()), -0.5f));

                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude()
                        , mLastLocation.getLongitude()));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);

            }
        });
    }

    public boolean onMarkerClick(final Marker marker) {
        Event temp = null;
        String userID = FirebaseAuth.getInstance().getUid();
        for (Event e : events) {
            if (e.getEventId().equals(marker.getTag())) {
                temp = e;
                break;
            }
        }

        final Event event = temp;

        if (event != null) {

            if (event.getUserIdOfCreator().equals(userID)) {
                Gson gson = GsonAdapter.getGson();
                Intent intent = new Intent(getActivity(), EditCreateEventActivity.class);
                intent.putExtra(R.string.intent_edit_create_event_event + "", gson.toJson(event));
                startActivity(intent);

            } else {

                FirebaseDatabase.getInstance().getReference("users_going_events").child(userID).child(event.getEventId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Gson gson = GsonAdapter.getGson();

                        Intent intent = new Intent(getActivity(), InfoEventActivity.class);
                        intent.putExtra(R.string.intent_info_event + "", gson.toJson(event));
                        intent.putExtra(R.string.intent_info_event_going_user + "", dataSnapshot.exists() ? InfoEventActivity.IS_GOING : InfoEventActivity.IS_NOT_GOING);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        } else {
            Toast.makeText(getActivity(), R.string.activity_maps_marker_no_event_found, Toast.LENGTH_LONG);
        }


        return false;
    }

    private String markerTitle(Event event) {
        String str = "";

        if (event != null) {
            str += event.getEventName() + "\n";
            str += event.getDateTimeString();
            str += event.getAddress() + "\n";
            str += event.getPostcode() + " " + event.getAddress() + "\n";
        }

        return str;
    }

}

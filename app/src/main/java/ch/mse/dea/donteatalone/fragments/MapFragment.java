package ch.mse.dea.donteatalone.fragments;


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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ch.mse.dea.donteatalone.R;
import ch.mse.dea.donteatalone.activitys.EditCreateEventActivity;
import ch.mse.dea.donteatalone.activitys.InfoEventActivity;
import ch.mse.dea.donteatalone.adapter.GsonAdapter;
import ch.mse.dea.donteatalone.objects.App;
import ch.mse.dea.donteatalone.objects.Event;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static final int REQUEST_LOCATION_PERM = 59;
    private static final String TAG = "Maps";
    private static final String CANCELED = "canceled";

    private DatabaseReference refEvents;
    private ArrayList<Event> events;

    //map
    private GoogleMap mMap;

    private Location mLastLocation;
    private Location closestLocation;


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View refView = inflater.inflate(R.layout.activity_maps, container, false);
        //register button event
        onClickMyLocationButton(refView);
        // Inflate the layout for this fragment
        requestPermissions(
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION_PERM);


        return refView;
    }

    //lets you allow location access in app instead of going to phone settings
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERM) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Location permission was granted");

                //get phone location
                try {
                    getPhoneLocation();
                } catch (ExecutionException | InterruptedException e) {
                    Log.v(TAG, e.toString());
                }
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                refEvents = mDatabase.child("events");
                setEventListener();

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
        }
    }

    public void getPhoneLocation() throws ExecutionException, InterruptedException {
        //get location of phone
        //locations
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use
        // default


        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        String provider = locationManager.getBestProvider(criteria, true);
//        if (provider == null) {
//            Log.e(TAG, "No location provider found!");
//            return;
//        }
//        mLastLocation = locationManager.getLastKnownLocation(provider);
//
//        App.log(TAG, mLastLocation + " Location");
//
//        if (mLastLocation != null) Log.d(TAG, mLastLocation.toString());


        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mLastLocation = location;
                        }
                    }
                });

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
                Log.v(TAG, CANCELED);
            }
        });
    }


    private void loadMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) mapFragment.getMapAsync(this);
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
                            .position(new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()))
                            .icon(BitmapDescriptorFactory.defaultMarker(45)));
                    markers[i].setTag(event.getEventId());
                } else {
                    markers[i] = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()))
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
                    Log.v(TAG, CANCELED);
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
            temp.setLatitude(events.get(i).getLocation().getLatitude());
            temp.setLongitude(events.get(i).getLocation().getLongitude());
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

    public void onClickMyLocationButton(View view) {
        if (App.isNetworkAvailable(true)) {
            FloatingActionButton fabs = view.findViewById(R.id.my_location_fab);
            fabs.show();
            fabs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        getPhoneLocation();

                        CameraUpdate center = CameraUpdateFactory.newLatLng(getLastPosition());
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                        mMap.moveCamera(center);
                        mMap.animateCamera(zoom);

                    } catch (ExecutionException | InterruptedException e) {
                        Log.v(TAG, e.toString());
                    }
                }
            });
        }
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
                        Log.v(TAG, CANCELED);
                    }
                });

            }
        } else {
            Toast.makeText(getActivity(), R.string.activity_maps_marker_no_event_found, Toast.LENGTH_LONG);
        }


        return false;
    }

    private LatLng getLastPosition() {

        if (mLastLocation != null) {
            return new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        } else {
            return new LatLng(0, 0);
        }
    }

}

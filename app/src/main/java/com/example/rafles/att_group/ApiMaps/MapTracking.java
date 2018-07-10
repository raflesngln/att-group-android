package com.example.rafles.att_group.ApiMaps;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafles.att_group.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
public class MapTracking extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private String email;
    DatabaseReference dbuserlist;
    Double lat,lng;
    Marker myMarker;
    TextView txt_search;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_tracking);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Ref to firebase
        dbuserlist= FirebaseDatabase.getInstance().getReference("Locations");
//        //get intent
        if(getIntent() != null)
        {
            email=getIntent().getStringExtra("email");
            lat=getIntent().getDoubleExtra("lat",0);
            lng=getIntent().getDoubleExtra("lng",0);

            Toast.makeText(this, "hai"+email+lat+"==="+lng, Toast.LENGTH_SHORT).show();
        }

        //for dialog searching
        txt_search=(TextView) findViewById(R.id.txt_search);
        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog customDialog = new Dialog(MapTracking.this);
                // the setContentView() method is used to set the custom layout for the dialog
                customDialog.setContentView(R.layout.dialog_search_maps);

                // using window set the hight and width of custom dialog
                Window window = customDialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                customDialog.show();// this show() method is used to show custom dialog


                Button bt_submit = (Button) customDialog.findViewById(R.id.bt_submit);
                bt_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MapTracking.this, "Searching data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        ImageView gotoCurrent=(ImageView) findViewById(R.id.gotoCurrent);
        gotoCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLocationForThisUser(email);
            }
        });
        
    }

    private void loadLocationForThisUser(String email) {
        Query user_location=dbuserlist.orderByKey();
        user_location.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMap.clear();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()) {
                    Tracking tracking = postSnapshot.getValue(Tracking.class);

                    //ad marker for other
                    LatLng friendLocation=new LatLng(Double.parseDouble(tracking.getLat()),Double.parseDouble(tracking.getLng()));

                    //create location from user koordinat
                    Location currentUser=new Location("");
                    currentUser.setLatitude(lat);
                    currentUser.setLongitude(lng);

                    //create location from others koordinat
                    Location friendsLatLng=new Location("");
                    friendsLatLng.setLatitude(Double.parseDouble(tracking.getLat()));
                    friendsLatLng.setLongitude(Double.parseDouble(tracking.getLng()));

                    //function calculate distancebettwen location
                    distance(currentUser,friendsLatLng);


                    //add other marker on the map,if not same on current user then created marker
                    LatLng lokasi_user=new LatLng(lat,lng);
                    if (!lokasi_user.equals(friendLocation))
                    {
                        mMap.addMarker(new MarkerOptions()
                                .position(friendLocation)
                                .title("- " + tracking.getEmail())
                                .snippet("This is Driver")
//                                    .snippet("distance"+ new DecimalFormat("#.#").format((currentUser.distanceTo(friendsLatLng)) / 1000+"KM"))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),12.0f));
//                        mMap.moveCamera(CameraUpdateFactory.newLatLng(friendLocation));
//                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((friendLocation), 12.0f));
                    }

                }

//                //create marker for current user
                LatLng lokasi=new LatLng(lat,lng);
//                mMap.addMarker(new MarkerOptions().position(current).title(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                mMap.addMarker(new MarkerOptions()
                        .position(lokasi)
                        .title( "My Current Location ")
                        .snippet(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasi));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((lokasi), 12.0f));
                Toast.makeText(MapTracking.this, "current location is" +lat+" and "+lng, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private double distance(Location currentUser, Location friends) {
        double theta=currentUser.getLongitude()- friends.getLongitude();
        double dist=Math.sin(deg2rad(currentUser.getLatitude()))
                * Math.sin(deg2rad(friends.getLatitude()))
                * Math.cos(deg2rad(currentUser.getLatitude()))
                * Math.cos(deg2rad(friends.getLatitude()))
                * Math.cos(deg2rad(theta));
        dist=Math.acos(dist);
        dist=rad2reg(dist);
        dist=dist* 60 * 1.1515;
        return (dist);
    }

    private double rad2reg(double rad) {
        return (rad * 180 / Math.PI);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        mMap.setTrafficEnabled(true);
        loadLocationForThisUser(email);

//        LatLng lokasi = new LatLng(lat, lng);
//        mMap.addMarker(new MarkerOptions().position(lokasi).title( "posisi"+lat+"dan"+lng));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasi));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((lokasi), 15.0f));
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(MapTracking.this, ListOnline.class);
        startActivity(myIntent);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String title = marker.getTitle();
        if ("User".equals(title)) {
            Toast.makeText(this, "hai"+title, Toast.LENGTH_SHORT).show();
        } else if ("Event".equals(title)) {
            // do thing for events
            Toast.makeText(this, "nooo", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}

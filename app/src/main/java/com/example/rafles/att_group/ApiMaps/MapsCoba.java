package com.example.rafles.att_group.ApiMaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafles.att_group.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsCoba extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //firebase
    DatabaseReference onlineRef,currentUserRef,counterRef,locations,dbuserlist;
    FirebaseRecyclerAdapter<User,ListOnlineViewHolder> adapter;

    private GoogleMap mMap;
    private String email;
    Double lat,lng;
    Marker myMarker;
    TextView txt_search;
    private Context context;

    //location
    private static final int MY_PERMISSION_REQUEST_CODE=7171;
    private static final int PLAY_SERVICES_RES_REQUEST=7172;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    //for refresh internal
    private static int UPDATE_INTERVAL=15000;
    private static int FASTEST_INTERVAL=10000;
    private static int DISTANCE=10;
    private static double zoomLevel=10;

    //for get address longitude
    Geocoder geocoder;
    List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_coba);
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
                final Dialog customDialog = new Dialog(MapsCoba.this);
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
                        Toast.makeText(MapsCoba.this, "Searching data", Toast.LENGTH_SHORT).show();
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
        /////////for auto refresh
        //firebase
        locations=FirebaseDatabase.getInstance().getReference("Locations");
        onlineRef= FirebaseDatabase.getInstance().getReference().child(".info/connected");
        counterRef=FirebaseDatabase.getInstance().getReference("lastOnline");
        currentUserRef=FirebaseDatabase.getInstance().getReference("lastOnline").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if(ActivityCompat.checkSelfPermission(MapsCoba.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MapsCoba.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            },MY_PERMISSION_REQUEST_CODE);
            Toast.makeText(MapsCoba.this,"oke requested",Toast.LENGTH_LONG).show();
        }
        else
        {
            if(checkPlayServices())
            {
                buildGoogleApiCLient();
                createLocationRequest();
                displayLocation();
            }
            Toast.makeText(MapsCoba.this,"Permission granted to user",Toast.LENGTH_LONG).show();
        }

//        setupSystem();
        //address
        getAddressDetail();
    }


    private void loadLocationForThisUser(final String email) {
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
                    if (!tracking.getEmail().equals(email))

                    {
                        mMap.addMarker(new MarkerOptions()
                                .position(friendLocation)
                                .title("- " + tracking.getEmail())
                                .snippet("Driver "+friendLocation)
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
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((lokasi), 18.0f));
//                Toast.makeText(MapsCoba.this, "current location is" +lat+" and "+lng, Toast.LENGTH_SHORT).show();
                Log.d("current location is => " ,lat+" and "+lng);
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
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        loadLocationForThisUser(email);

        // for circle area current location,use style from folder raw/map.json
        try{
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map));
            if(!success){
                Log.e("Maps", "Style Parsing failed");
            }
        }catch(Resources.NotFoundException ex){
            Log.e("status","Can't find style. Error"+ex);
        }

    }


    //=================================TAMBAHAN=============================================
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSION_REQUEST_CODE:
            {
                if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    if(checkPlayServices())
                    {
                        buildGoogleApiCLient();
                        createLocationRequest();
                        displayLocation();
                        startLocationUpdate();
                    }
                }
            }
            break;
        }
    }

    private void displayLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        mLastLocation= LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation !=null)
        {
            //update data firebase
            locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(new Tracking(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            String.valueOf(mLastLocation.getLatitude()),
                            String.valueOf(mLastLocation.getLongitude())));
            Log.d("from maps",mLastLocation.getLatitude()+" dan "+ mLastLocation.getLongitude());
//            Toast.makeText(this, "Save maps location "+mLastLocation.getLatitude()+" dan "+ mLastLocation.getLongitude() , Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,"No",Toast.LENGTH_SHORT).show();
        }

    }
    @SuppressLint("RestrictedApi")
    private void createLocationRequest() {
        mLocationRequest=new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(DISTANCE);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildGoogleApiCLient() {
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices() {
        int resultCode= GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS)
        {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICES_RES_REQUEST).show();
            } else
            {
                Toast.makeText(MapsCoba.this,"this device not support",Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void startLocationUpdate() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, (com.google.android.gms.location.LocationListener) this);
    }

    //=================================TAMBAHAN=============================================

    private void getAddressDetail() {
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            TextView txt_search=(TextView) findViewById(R.id.txt_search);
            txt_search.setText(address);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdate();
        getAddressDetail();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation=location;
        displayLocation();
        startLocationUpdate();
        getAddressDetail();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if(mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        if(adapter !=null)
            adapter.stopListening();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(MapsCoba.this, ListOnline.class);
        startActivity(myIntent);
    }
}

package com.example.rafles.att_group.ApiMaps;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.rafles.att_group.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
public class MapTracking extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String email;
    DatabaseReference dbuserlist;
    Double lat,lng;

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
//        if(!TextUtils.isEmpty(email))
//            loadLocationForThisUser(email);
//        Toast.makeText(this, "alamat "+lat+"dan"+lng, Toast.LENGTH_SHORT).show();
        
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

                    //add other marker on the map
                    mMap.addMarker(new MarkerOptions()
                                    .position(friendLocation)
                                    .title("- "+tracking.getEmail())
//                                    .snippet("distance"+new DecimalFormat("#.#").format((currentUser.distanceTo(friendsLatLng)) / 1000+"KM"))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),12.0f));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(friendLocation));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((friendLocation), 10.0f));


                }

//                //create marker for current user
                LatLng lokasi=new LatLng(lat,lng);
//                mMap.addMarker(new MarkerOptions().position(current).title(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
//

//                lat=getIntent().getDoubleExtra("lat",0);
//                lng=getIntent().getDoubleExtra("lng",0);
//                LatLng lokasi = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions()
                        .position(lokasi)
                        .title( "Me "+FirebaseAuth.getInstance().getCurrentUser().getEmail()+"dan"+lng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasi));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((lokasi), 15.0f));


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
        loadLocationForThisUser(email);

//         Add a marker in Sydney and move the camera
//        email=getIntent().getStringExtra("email");
//        lat=getIntent().getDoubleExtra("lat",0);
//        lng=getIntent().getDoubleExtra("lng",0);
//
//        LatLng lokasi = new LatLng(lat, lng);
//        mMap.addMarker(new MarkerOptions().position(lokasi).title( "posisi"+lat+"dan"+lng));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasi));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((lokasi), 15.0f));
    }
}

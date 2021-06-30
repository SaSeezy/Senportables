package com.is.sn.senportable;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final int PERMS_CALL_ID=1234;
    private LocationManager lm;
    private MapFragment mapFragment;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.app.FragmentManager fragmentManager=getFragmentManager();
        mapFragment=(MapFragment) fragmentManager.findFragmentById(R.id.map);
    }

    @Override
    //@SuppressWarnings("MissingPermission")
    protected void onResume() {
        super.onResume();
        checkPermissions();

    }
    private void checkPermissions(){
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String []{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, PERMS_CALL_ID);
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lm = (LocationManager) getSystemService(LOCATION_SERVICE);

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        }

        if (lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)){
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 10000, 0, this);
        }

        if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
        }

        loadMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lm !=null){
            lm.removeUpdates(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void loadMap(){
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MainActivity.this.googleMap=googleMap;
                googleMap.moveCamera(CameraUpdateFactory.zoomBy(15));
                googleMap.setMyLocationEnabled(true);
                googleMap.addMarker(new MarkerOptions().position(new LatLng(14.716677, -16.963093)).title("Ma position"));
            }
        });
    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude= location.getLatitude();
        double longitude=location.getLongitude();
        Toast.makeText(this, "Location="+latitude +"/"+longitude,Toast.LENGTH_LONG).show();
        if (googleMap!=null){
            LatLng googleLocation= new LatLng(latitude, longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(googleLocation));

        }
    }
}

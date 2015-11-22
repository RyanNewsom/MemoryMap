package kylefrisbie.com.memorymap.presentation;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import kylefrisbie.com.memorymap.Model.Memory;
import kylefrisbie.com.memorymap.R;
import kylefrisbie.com.memorymap.controller.MemoryController;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private MemoryController mController;
    private GoogleMap mMap;
    private Location mUserLocation;
    private List<Memory> mMemories;
    private boolean mUserLocationInitiallyFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mController = MemoryController.getInstance();
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
        setupUI();

        populateMemories(mController.getMemories());

//        mMap.getUiSettings().
//        myLocation = mMap.getMyLocation();
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())));
    }

    private void setupUI() {
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (location != null && !mUserLocationInitiallyFound) {
                    mUserLocation = location;
                    mUserLocationInitiallyFound = true;
                    goToLocation(location);
                }
            }
        });
    }

    private void populateMemories(List<Memory> theMemories) {
        mMemories = theMemories;
        if (theMemories != null) {
            for (int i = 0; i < theMemories.size(); i++) {
                Memory currentMemory = theMemories.get(i);
                addMemory(currentMemory);
            }
        }

    }

    /**
     * This will find a memory that a user searches for, and take them to it
     *
     * @param searchQuery
     */
    private void searchForMemory(String searchQuery) {
        //find the memory
        //pass the controller a string, get back the list
    }

    /**
     * Moves the camera to a specific location
     *
     * @param location - the location to go to
     */

    private void goToLocation(Location location){
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()), mMap.getMaxZoomLevel(), 0, 0)));
    }

    private void expandAMemory(Memory memoryClicked) {

    }

    private void getMoreInfoForMemory() {
        MemoryFragment memoryFragment = new MemoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.map, memoryFragment);
    }

    private void addMemory(Memory newMemory) {
        Location location = newMemory.getLocation();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.addMarker(new MarkerOptions().position(latLng)
                .title(newMemory.getTitle())
                .snippet("" + newMemory.getDate()));
    }


}

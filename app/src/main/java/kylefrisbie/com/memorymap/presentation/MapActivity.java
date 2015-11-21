package kylefrisbie.com.memorymap.presentation;

import android.location.Location;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import kylefrisbie.com.memorymap.Memory;
import kylefrisbie.com.memorymap.R;
import kylefrisbie.com.memorymap.controller.MemoryController;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private MemoryController mController;
    private GoogleMap mMap;
    private ArrayList<Memory> mMemories;

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
        Location myLocation;

        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);

        populateMemories(mController.getMemories());

//        mMap.getUiSettings().
//        myLocation = mMap.getMyLocation();
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())));
    }

    private void populateMemories(ArrayList<Memory> theMemories) {
        mMemories = theMemories;
        for(int i = 0; i < theMemories.size(); i++){
            Memory currentMemory = theMemories.get(i);
            addMemory(currentMemory);
        }

    }

    private void searchForMemory(Memory memory){
        //find the memory
    }

    private void goToLocation(Location location){

    }

    private void expandAMemory(Memory memoryClicked){

    }

    private void getMoreInfoForMemory(){
        MemoryActivity memoryActivity = new MemoryActivity();
        getSupportFragmentManager().beginTransaction().replace()
    }

    private void addMemory(Memory newMemory){
        //Draw the memory
    }


}

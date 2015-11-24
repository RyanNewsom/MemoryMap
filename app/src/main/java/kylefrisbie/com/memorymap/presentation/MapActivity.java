package kylefrisbie.com.memorymap.presentation;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import kylefrisbie.com.memorymap.R;
import kylefrisbie.com.memorymap.controller.MemoryController;
import kylefrisbie.com.memorymap.listener.OnMemoryChangedListener;
import kylefrisbie.com.memorymap.model.Memory;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, OnMemoryChangedListener {

    public static String MEMORY_ID = "MEMORY_ID";
    public static String LATLNGARRAY = "LATLNG";

    private MemoryController mController;
    private GoogleMap mMap;
    private Location mUserLocation;
    private List<Memory> mMemories;
    private List<Marker> mMarkers;
    private boolean mUserLocationInitiallyFound;

    //Views
    private ImageButton mAddMemoryButton;
    private ImageButton mMyLocationButton;

    @Override
    public void onMemoryAdded(Memory memory) {
        addMemory(memory);
        mMemories.add(memory);
    }

    @Override
    public void onMemoryUpdated(Memory memory) {
        //Find the memory in the array list, and update the information
    }

    @Override
    public void onMemoryRemoved(Memory memory) {
        //Find the memory in the array list, remove it, then remove it from the list of markers, and remove
        //that specific marker
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mController = MemoryController.getInstance(this);
        mMyLocationButton = (ImageButton) findViewById(R.id.my_location_button);
        mAddMemoryButton = (ImageButton) findViewById(R.id.add_memory_button);
    }

    private void addListeners() {
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mUserLocation = location;

                if (location != null && !mUserLocationInitiallyFound) {
                    mUserLocationInitiallyFound = true;
                    goToLocation(location);
                }
            }
        });

        mMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLocation(mUserLocation);
            }
        });

        mAddMemoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMemoryFragment(mUserLocation);
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });

        /**
         * Finds where the marker the user clicked can be found in the markers list, then compares it with the markers in the
         * marker array list,
         */
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int positionOfMemory = 0;
                for(int i = 0; i < mMarkers.size(); i++){
                    Marker currentMarker = mMarkers.get(i);
                    if(marker.getId() == currentMarker.getId()){
                        positionOfMemory = i;
                        break;
                    }
                }
                Memory theMemory = mMemories.get(positionOfMemory);
                openMemoryFragment(theMemory);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupUI();
        populateMemories(mController.getMemories());
    }

    private void setupUI() {
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);
        addListeners();
    }

    private void populateMemories(List<Memory> theMemories) {
        mMemories = theMemories;
        mMarkers = new ArrayList<>();
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
     * Viewing/editing a memory
     * @param memory
     */
    private void openMemoryFragment(Memory memory) {
        Bundle bundle = new Bundle();
        bundle.putLong(MEMORY_ID, memory.getId());
        MemoryFragment memoryFragment = new MemoryFragment();
        memoryFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.wholemap, memoryFragment).addToBackStack("null").commit();
    }

    /**
     * Adding new memory
     * @param location
     */
    private void openMemoryFragment(Location location){
        Bundle bundle = new Bundle();
        double[] array = new double[2];
        array[0] = location.getLatitude();
        array[1] = location.getLongitude();
        bundle.putLong(MEMORY_ID, -1);
        bundle.putDoubleArray(LATLNGARRAY, array);
        MemoryFragment memoryFragment = new MemoryFragment();
        memoryFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.wholemap, memoryFragment).addToBackStack("null").commit();
    }

    /**
     * Adds a memory to the map, also records the memories marker for future reference
     * @param newMemory
     */
    private void addMemory(Memory newMemory) {
        Location location = newMemory.getLocation();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Marker newMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                .title(newMemory.getTitle())
                .snippet("" + newMemory.getDate()));

        mMarkers.add(newMarker);
    }

    /**
     * Moves the camera to a specific location
     *
     * @param location - the location to go to
     */

    private void goToLocation(Location location){
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()), mMap.getMaxZoomLevel() - 5, 0, 0)));
    }
}

package kylefrisbie.com.memorymap.presentation;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kylefrisbie.com.memorymap.R;
import kylefrisbie.com.memorymap.controller.MemoryController;
import kylefrisbie.com.memorymap.listener.OnMemoryChangedListener;
import kylefrisbie.com.memorymap.model.Memory;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, OnMemoryChangedListener {

    public static String MEMORY_ID = "MEMORY_ID";
    public static String MARKER_ID = "MARKER_ID";
    public static String LATLNGARRAY = "LATLNG";

    private MemoryController mController;
    private GoogleMap mMap;
    private Location mUserLocation;
    private List<Memory> mMemories;
    private List<Marker> mMarkers;
    private String mMemoryMarkerID;
    private boolean mUserLocationInitiallyFound;

    //Views
    private SearchListAdapter mCustomAdapter;
    private AutoCompleteTextView mSearchView;
    private ImageButton mAddMemoryButton;
    private ImageButton mMyLocationButton;

    @Override
    public void onMemoryAdded(Memory memory) {
        addMemory(memory);
        int position = getLocationOfMemory(memory);
        mMemories.set(position, memory);
    }

    @Override
    public void onMemoryUpdated(Memory memory) {
        //Find the memory in the array list, and update the information
        int position = getLocationOfMemory(memory);
        if (position == -1) {
            //there was no match...
            mMemories.add(memory);
            Log.e(getLocalClassName(), "onMemoryUpdated could not find a match, so it added a new memory");
        } else {
            mMemories.set(position, memory);
            mMarkers.get(position).setTitle(memory.getTitle());
        }
    }

    @Override
    public void onMemoryRemoved(Memory memory, String markerID) {
        //Find the memory in the array list, remove it, then remove it from the list of markers, and remove
        //that specific marker
        int position = getMemoryMarkerPosition(markerID);
        mMemories.remove(position);
        Marker toRemove = mMarkers.remove(position);
        toRemove.remove();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mController = MemoryController.getInstance(this);
        mSearchView = (AutoCompleteTextView) findViewById(R.id.auto_complete_search);
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
                Memory theMemory = mMemories.get(
                        getMemoryMarkerPosition(marker.getId())
                );
                openMemoryFragment(theMemory);
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Memory newMemory = new Memory(null, null, null, null, null, latLng.latitude, latLng.longitude, null);
                mController.createMemory(newMemory);

            }
        });

        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomAdapter.notifyDataSetChanged();
            }
        });

        mSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Memory selectedMemory = (Memory) parent.getItemAtPosition(position);
                int loc = getLocationOfMemory(selectedMemory);
                Marker selectedMarker = mMarkers.get(loc);
                Location location = new Location("");
                location.setLatitude(selectedMemory.getLatitude());
                location.setLongitude(selectedMemory.getLongitude());
                goToLocation(location);
                selectedMarker.showInfoWindow();
            }
        });

    }

    private int getMemoryMarkerPosition(String markerID) {
        for(int i = 0; i < mMarkers.size(); i++){
            Marker currentMarker = mMarkers.get(i);
            if(markerID.equals(currentMarker.getId())){
                mMemoryMarkerID = markerID;
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupUI();
        populateMemories(mController.getMemories());
        mCustomAdapter = new SearchListAdapter(this, R.layout.itemlistrow, mMemories);
        mSearchView.setAdapter(mCustomAdapter);
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
     * Viewing/editing a memory
     * @param memory
     */
    private void openMemoryFragment(Memory memory) {
        Bundle bundle = new Bundle();
        bundle.putLong(MEMORY_ID, memory.getId());
        bundle.putString(MARKER_ID, mMemoryMarkerID);
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
        Marker newMarker;
        if(mCustomAdapter != null) {
            mCustomAdapter.add(newMemory);
        }
        Calendar calendar = newMemory.getDate();
        Date newDate = new Date(calendar.getTimeInMillis());
        DateFormat df = new SimpleDateFormat("MM/dd/yy");
        String theDate = df.format(newDate);
        LatLng latLng = new LatLng(newMemory.getLatitude(), newMemory.getLongitude());

        newMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                .title(newMemory.getTitle())
                .snippet("" + theDate));

        mMarkers.add(newMarker);
    }

    /**
     * Moves the camera to a specific location
     *
     * @param location - the location to go to
     */

    private void goToLocation(Location location) {
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()), mMap.getMaxZoomLevel() - 5, 0, 0)));
    }

    private int getLocationOfMemory(Memory theMemory){
        for(int i = 0; i < mMemories.size(); i++){
            if(theMemory.getId() == mMemories.get(i).getId()){
                return i;
            }
        }
        return -1;
    }
}

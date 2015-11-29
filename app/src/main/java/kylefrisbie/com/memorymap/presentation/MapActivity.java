package kylefrisbie.com.memorymap.presentation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
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

    public static final String MEMORY_ID = "MEMORY_ID";
    public static final String MARKER_ID = "MARKER_ID";
    public static final String LATLNGARRAY = "LATLNG";

    private MemoryController mController;
    private GoogleMap mMap;
    private Location mUserLocation;
    private List<Memory> mMemories;
    private List<Memory> mSearchMemories;
    private List<Marker> mMarkers;
    private String mMemoryMarkerID;
    private boolean mUserLocationInitiallyFound;
    private Marker mViewedMarker;


    //Views
    private SearchListAdapter mCustomAdapter;
    private AutoCompleteTextView mSearchView;
    private ImageButton mAddMemoryButton;
    private ImageButton mMyLocationButton;

    @Override
    public void onMemoryAdded(Memory memory) {
        addMemory(memory);
        mSearchMemories = new ArrayList<>(mMemories);
        mCustomAdapter = new SearchListAdapter(this, R.layout.itemlistrow, mSearchMemories);
        mSearchView.setAdapter(mCustomAdapter);
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
            mMarkers.get(position).setSnippet("" + getFormattedTime(memory.getDate()));
            if (mViewedMarker != null) {
                if (mViewedMarker.isInfoWindowShown()) {
                    mViewedMarker.hideInfoWindow();
                    mViewedMarker.showInfoWindow();
                }
            }
        }
        mSearchMemories = new ArrayList<>(mMemories);
        mCustomAdapter = new SearchListAdapter(this, R.layout.itemlistrow, mSearchMemories);
        mSearchView.setAdapter(mCustomAdapter);
    }

    @Override
    public void onMemoryRemoved(Memory memory, String markerID) {
        //Find the memory in the array list, remove it, then remove it from the list of markers, and remove
        //that specific marker
        int position = getMemoryMarkerPosition(markerID);
        mMemories.remove(position);
        Marker toRemove = mMarkers.remove(position);
        toRemove.remove();

        mSearchMemories = new ArrayList<>(mMemories);
        mCustomAdapter = new SearchListAdapter(this, R.layout.itemlistrow, mSearchMemories);
        mSearchView.setAdapter(mCustomAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMemories = new ArrayList<>();
        mSearchMemories = new ArrayList<>();
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
        addButtonListeners(false);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mUserLocation = location;

                if (location != null) {
                    addButtonListeners(true);
                    mMyLocationButton.setImageResource(R.drawable.my_location_icon);
                    mAddMemoryButton.setImageResource(R.drawable.add_memory_icon);
                } else {
                    addButtonListeners(false);
                    mMyLocationButton.setImageResource(R.drawable.my_location_icon_gray);
                    mAddMemoryButton.setImageResource(R.drawable.add_memory_icon_gray);
                }

                if (location != null && !mUserLocationInitiallyFound) {
                    mUserLocationInitiallyFound = true;
                    goToLocation(location);
                }

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                mViewedMarker = marker;
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

        mSearchView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Memory selectedMemory = (Memory) parent.getItemAtPosition(position);
                int loc = getLocationOfMemory(selectedMemory);
                Marker selectedMarker = mMarkers.get(loc);
                Location location = new Location("");
                location.setLatitude(selectedMemory.getLatitude());
                location.setLongitude(selectedMemory.getLongitude());
                goToLocation(location);
                selectedMarker.showInfoWindow();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        for (int i = 0; i < mMarkers.size(); i++) {
            Marker currentMarker = mMarkers.get(i);
            if (markerID.equals(currentMarker.getId())) {
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
        mSearchMemories = new ArrayList<>(mMemories);
        mCustomAdapter = new SearchListAdapter(this, R.layout.itemlistrow, mSearchMemories);
        mSearchView.setAdapter(mCustomAdapter);

        // check to see if location services are enabled
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            enableGPS();
        }
    }

    private void setupUI() {
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);
        addListeners();
    }

    private void populateMemories(List<Memory> theMemories) {
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
     *
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
     *
     * @param location
     */
    private void openMemoryFragment(Location location) {
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
     *
     * @param newMemory
     */
    private void addMemory(Memory newMemory) {
        Marker newMarker;

        String theDate = getFormattedTime(newMemory.getDate());

        LatLng latLng = new LatLng(newMemory.getLatitude(), newMemory.getLongitude());

        newMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                .title(newMemory.getTitle())
                .snippet("" + theDate));

        mMarkers.add(newMarker);
        mMemories.add(newMemory);
        if (mCustomAdapter != null) {
            mCustomAdapter.notifyDataSetChanged();
        }
    }

    private String getFormattedTime(Calendar date) {
        Date newDate = new Date(date.getTimeInMillis());
        DateFormat df = new SimpleDateFormat("MM/dd/yy");
        return df.format(newDate);
    }

    /**
     * Moves the camera to a specific location
     *
     * @param location - the location to go to
     */

    private void goToLocation(Location location) {
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()), mMap.getMaxZoomLevel() - 5, 0, 0)));
    }

    private int getLocationOfMemory(Memory theMemory) {
        for (int i = 0; i < mMemories.size(); i++) {
            if (theMemory.getId().equals(mMemories.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }

    // provide a dialog notifying user to enable gps, bring user to location services
    private void enableGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Location Services Not Active");
        builder.setMessage("Please enable Location Services and GPS");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                // Show location settings when the user acknowledges the alert dialog
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void addButtonListeners(Boolean isLocationAvail) {
        if (isLocationAvail) {
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
        } else {
            mMyLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enableGPS();
                }
            });

            mAddMemoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enableGPS();
                }
            });
        }
    }
}

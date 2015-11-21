//package kylefrisbie.com.memorymap.presentation;
//
//import android.location.Location;
//import android.os.PersistableBundle;
//import android.support.v4.app.FragmentActivity;
//import android.os.Bundle;
//
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//
//import java.util.ArrayList;
//
//import kylefrisbie.com.memorymap.Model.Memory;
//import kylefrisbie.com.memorymap.R;
//import kylefrisbie.com.memorymap.controller.MemoryController;
//
//public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
//
//    private MemoryController mController;
//    private GoogleMap mMap;
//    private ArrayList<Memory> mMemories;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_map);
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }
//
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        Location myLocation;
//
//        mMap = googleMap;
//        mMap.getUiSettings().setMyLocationButtonEnabled(false);
//        mMap.setMyLocationEnabled(true);
//
//        populateMemories();
//
////        mMap.getUiSettings().
////        myLocation = mMap.getMyLocation();
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())));
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//
//        mController = MemoryController.getInstance();
//
//
//
//    }
//
//    private void populateMemories(ArrayList<Memory> theMemories) {
//        mMemories = theMemories;
//
//    }
//
//    private void searchForMemory(){
//
//
//    }
//
//    private void goToMyLocation(){
//
//    }
//
//    private void expandAMemory(){
//
//    }
//
//    private void getMoreInfoForMemory(){
//
//    }
//
//    private void addMemory(Memory newMemory){
//
//    }
//
//
//}

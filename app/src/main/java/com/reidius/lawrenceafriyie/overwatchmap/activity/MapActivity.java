package com.reidius.lawrenceafriyie.overwatchmap.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.reidius.lawrenceafriyie.overwatchmap.R;
import com.reidius.lawrenceafriyie.overwatchmap.ViewIncidents;
import com.reidius.lawrenceafriyie.overwatchmap.activities.PlaceInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener{

    //Log
    private static final String TAG = "MapActivity";

    // Menu
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    //  Widget Section
    private AutoCompleteTextView mSearchText;
    private ImageView mGps, mInfo, mPlacePicker, mLocation;

    //Toggle switch
    private Switch safezoneSwitch;
    private  Switch hotspotSwitch;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Menu
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Navigation View
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Switch Functions
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.findItem(R.id.safezone);
        MenuItem item1 = menu.findItem(R.id.hotspot);
        MenuItem item2 = menu.findItem(R.id.location);
        View safeZoneToggle = MenuItemCompat.getActionView(item);
        View hotspotToggle = MenuItemCompat.getActionView(item1);
        safezoneSwitch = safeZoneToggle.findViewById(R.id.safezone_switch);
        hotspotSwitch = hotspotToggle.findViewById(R.id.hotspot_switch);



        safezoneSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getApplicationContext(), "Overwatch Safezone On", Toast.LENGTH_SHORT).show();
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Overwatch Safezone Off", Toast.LENGTH_SHORT).show();
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        hotspotSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getApplicationContext(), "Overwatch Hotspots On", Toast.LENGTH_SHORT).show();
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Overwatch Hotspots Off", Toast.LENGTH_SHORT).show();
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        //Map
        // Location Permission

        //Search Text
        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        //Navigation pin
        mGps = (ImageView) findViewById(R.id.ic_gps);
        //Location Information
        mInfo = (ImageView) findViewById(R.id.place_info);
        //Place Picker
        mPlacePicker = (ImageView) findViewById(R.id.place_picker);
        //Location on/off
        mLocation = (ImageView) findViewById(R.id.location);
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationPermission();
                mLocation.setClickable(false);
            }
        });

        //Safezone and hotspot switch
        //safezoneSwitch = (Switch) findViewById(R.id.safezone_switch);
        //hotspotSwitch = (Switch)findViewById(R.id.hotspot);
    }



    // Menu Open/Close
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // Open the selected menu option
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.emergency_service:
                Intent intent1 = new Intent(this, EmergencyService.class);
                startActivity(intent1);
                break;

            case R.id.report_incident:
                Intent intent = new Intent(this, ReportIncident.class);
                startActivity(intent);
                break;

            case R.id.help:
                Intent intent2 = new Intent(this, About.class);
                startActivity(intent2);
                break;
            case R.id.view_incident:
                Intent intent3 = new Intent(this, IncidentView.class);
                startActivity(intent3);
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        if(!mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            //Close the application
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    // Map Activities
    //---------------
    // Map Variables
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 17f;
    private static final int PLACE_PICKER_REQUEST = 1;
    // Latitude and Longitude bounds
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168),
            new LatLng(71, 130));

    // Variables
    private boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;
    private Marker mMarker;

    // GeoLocate
    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.d(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: Found location" + address.toString());
            // Move the camera to the searched for location
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }
    }
    
    // Init Method
    private void init(){

        Log.d(TAG, "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        mSearchText.setOnItemClickListener(mAutoCompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, LAT_LNG_BOUNDS
        ,null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            // Override the Enter Key
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId ==EditorInfo.IME_ACTION_DONE  ||
                        event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    
                    // Execute searching
                    // GeoLocating the search
                    geoLocate();
                }
                return false;
            }
        });
        hideSoftKeyboard();
        // Navigate back to the device location
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked on GPS icon");
                getDeviceLocation();
                mMap.clear();
            }
        });
        // Display information about a searched for location
        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked place information");
                try{
                    if(mMarker.isInfoWindowShown()){
                        mMarker.hideInfoWindow();
                    } else {
                        mMarker.showInfoWindow();
                        Log.d(TAG, "onClick: Place Information: " + mPlace.toString());
                    }
                } catch (NullPointerException e){
                    Log.d(TAG, "onClick: NullPointerException: " + e.getMessage());
                }
            }
        });
        // Open a place picker window
        mPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(MapActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.d(TAG, "onClick: GooglePlayServicesRepairableException: " + e.getMessage() );
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.d(TAG, "onClick: GooglePlayServicesRepairableException: " + e.getMessage() );
                }
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions marker = new MarkerOptions();
                marker.position(latLng);
                marker.title(latLng.latitude + " : " + latLng.longitude);
                mMap.clear();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(marker);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace( this, data);

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, place.getId());
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        }
    }

    // Getting the device's location
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: Getting the device's current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Current location");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM, "My Location");
                            // Move the camera to the device's current location
                        } else {
                            Log.d(TAG, "onComplete: Current location not found");
                            Toast.makeText(MapActivity.this,
                                    "Unable to get the device current location",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    // Move the camera to the current location of the device, this method will be used by other functions such as searching for a location
    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo) {
        Log.d(TAG, "moveCamera: Moving the camera to : lat: " + latLng.latitude + "lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        //Clear the map
        mMap.clear();

        // Custom Information window display
        mMap.setInfoWindowAdapter(new ReidiusInfoWindowAdapter(MapActivity.this));

        if(placeInfo != null){
            try{
                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
                        "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
                        "Website: " + placeInfo.getWebsiteUri() + "\n";

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(placeInfo.getName())
                        .snippet(snippet);

                mMarker = mMap.addMarker(options);

            }catch (NullPointerException e){
                Log.d(TAG, "moveCamera: NullPointerException " + e.getMessage());
            }
        } else {
            mMap.addMarker(new MarkerOptions().position(latLng));
        }
        hideSoftKeyboard();
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: Moving the camera to : lat: " + latLng.latitude + "lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")) {

            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }
        hideSoftKeyboard();
    }

    // Initialize Map Method
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Overwatch Map has initiated", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is Ready");
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();
            // Check if permission has been granted
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            // Disable the default location button
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            //Geolocate search
            init();
        }
    }
    private void initMap(){
        Log.d(TAG, "initMap: Initializing Map");
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    // Check for permission
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: Getting location permission");
        // Send permission request
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // Look for a permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionResult: This method was called");
        mLocationPermissionGranted = false;
        // Check request code
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionResult: Permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionResult: Permission Granted");
                    mLocationPermissionGranted = true;
                    // initialize the map
                    initMap();
                }
            }
        }
    }
    // Hide the Keyboard when searching
    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /*
        Google Places API autocomplete suggestions
     */

    // Move to the location suggested on the autocomplete suggestion list
    private AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };
    // When the callback is successful, the location will be retrieved
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            // A place was got successfully gotten
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully: " +
                        places.getStatus().toString());
                // To prevent memory leaks
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                mPlace.setAddress(place.getAddress().toString());
                //mPlace.setAttributions(place.getAttributions().toString());
                mPlace.setId(place.getId());
                mPlace.setLatlng(place.getLatLng());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG, "onResult: place: " + mPlace.toString());
            } catch(NullPointerException e) {
                Log.d(TAG, "onResult: NullPointerException: " + e.getMessage());
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace);
            places.release();
        }
    };
}

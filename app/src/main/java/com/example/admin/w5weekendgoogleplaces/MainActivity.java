package com.example.admin.w5weekendgoogleplaces;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.w5weekendgoogleplaces.model.googleplaces.GooglePlacesResponse;
import com.example.admin.w5weekendgoogleplaces.model.googleplaces.Result;
import com.example.admin.w5weekendgoogleplaces.model.queryautocomplete.GoogleAutoCompleteResponse;
import com.example.admin.w5weekendgoogleplaces.model.queryautocomplete.Prediction;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener,
        PlacesRecyclerViewAdapter.OnListInteractionListener, PredictionsFragment.OnListFragmentInteractionListener {

    private static final int MY_PERMISSIONS_REQUEST_GEOLOCATION = 101;
    private static final String TAG = "MainActivityTag";
    private MapFragment mMapFragment;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location currentLocation;
    private GoogleMap mMap;
    List<Result> results;
    private MainActivity activity = this;
    private SearchView svSearch;
    private Spinner spinnerCategories;
    private List<Prediction> predictions;

    private RecyclerView.ItemAnimator itemAnimator;
    private FragmentManager fm = getSupportFragmentManager();
    //Observers
    private Observer<Response<GooglePlacesResponse>> placesObserver = new Observer<Response<GooglePlacesResponse>>() {
        @Override
        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
            Log.d(TAG, "onSubscribe: ");
        }

        @Override
        public void onNext(@io.reactivex.annotations.NonNull Response<GooglePlacesResponse> googlePlacesResponseResponse) {
            Log.d(TAG, "onNext: ");
            if (googlePlacesResponseResponse != null){
                results = googlePlacesResponseResponse.body().getResults();
            }
        }

        @Override
        public void onError(@io.reactivex.annotations.NonNull Throwable e) {

        }

        @Override
        public void onComplete() {
            RecyclerView rvPLaces = findViewById(R.id.rvPlaces);
            rvPLaces.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            PlacesRecyclerViewAdapter placesRecyclerViewAdapter = new PlacesRecyclerViewAdapter(results, activity, activity);
            //Animation animation = AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.slide_in_left);
            //rvPLaces.setItemAnimator();
            rvPLaces.setAdapter(placesRecyclerViewAdapter);
            mMapFragment.getMapAsync(activity);
        }
    };

    private Observer<Response<GoogleAutoCompleteResponse>> autoFillObserver = new Observer<Response<GoogleAutoCompleteResponse>>() {
        @Override
        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

        }

        @Override
        public void onNext(@io.reactivex.annotations.NonNull Response<GoogleAutoCompleteResponse> googleAutoCompleteResponseResponse) {
            if(googleAutoCompleteResponseResponse != null){
                predictions = googleAutoCompleteResponseResponse.body().getPredictions();
                Log.d(TAG, "onNext: " + predictions.size());
            }
            Log.d(TAG, "onNext: " + googleAutoCompleteResponseResponse.message());
            Log.d(TAG, "onNext: " + googleAutoCompleteResponseResponse.body().getStatus());
            Log.d(TAG, "onNext: " + googleAutoCompleteResponseResponse.raw().request().url());
        }

        @Override
        public void onError(@io.reactivex.annotations.NonNull Throwable e) {

        }

        @Override
        public void onComplete() {
            pf = PredictionsFragment.newInstance(1,predictions);

            fm.beginTransaction().replace(R.id.flAutoFill, pf).addToBackStack(null).commit();
        }
    };
    private String search = "food";
    private PredictionsFragment pf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        svSearch = findViewById(R.id.svSearch);
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search = s;
                /*String latLng = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
                final Observable<Response<GooglePlacesResponse>> geocodeResponseCall = RetrofitHelper.createPlacesCall(latLng,500,s);
                geocodeResponseCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(placesObserver);
*/
                changeMap(mMap);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    Log.d(TAG, "onQueryTextChange: " + s);
                    final Observable<Response<GoogleAutoCompleteResponse>> autoCompleteCall = RetrofitHelper.createAutoCompleteCall(s);
                    autoCompleteCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(autoFillObserver);

                }catch (Exception e){
                    Log.d(TAG, "onQueryTextChange: " + e.getMessage());
                }
                return false;
            }
        });

        /*SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_GEOLOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            //mMap.setMyLocationEnabled(true);
            setUpMap();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GEOLOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUpMap();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "Need the Location", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void setUpMap() {
        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
        //getMapAsync();

        Log.d(TAG, "onMapReady: ");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d(TAG, "onSuccess: " + location.toString());
                        currentLocation = location;
                        String latLng = currentLocation.getLatitude() + "," + currentLocation.getLongitude();

                        Log.d(TAG, "onMapReady: ");
                        final Observable<Response<GooglePlacesResponse>> geocodeResponseCall = RetrofitHelper.createPlacesCall(latLng,500,search);
                        geocodeResponseCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(placesObserver);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ");
                    }
                });

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,100,this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        for (Result r:
                results) {
            LatLng latLng = new LatLng(r.getGeometry().getLocation().getLat(), r.getGeometry().getLocation().getLng());
            mMap = googleMap;
            if(mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.addMarker(new MarkerOptions().position(latLng).title(r.getName()));
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
        mMap.setMinZoomPreference(5);
    }


    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onListInteraction(Result result) {
        Intent intent = new Intent(this,DetailedPlaceActivity.class);
        Log.d(TAG, "onListInteraction: " + result.getId());
        intent.putExtra("result", result.getPlaceId());
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Prediction item) {
        Log.d(TAG, "onListFragmentInteraction: ");

        int totalNumFrags = getSupportFragmentManager().getBackStackEntryCount();
        for(int i = 0; i < totalNumFrags; i++) {
            fm.popBackStackImmediate();
        }
        search = item.getStructuredFormatting().getMainText();
        Log.d(TAG, "onListFragmentInteraction: " + search);
        changeMap(mMap);

    }

    private void changeMap(GoogleMap mMap) {
        mMap.clear();
        String latLng = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        final Observable<Response<GooglePlacesResponse>> geocodeResponseCall = RetrofitHelper.createPlacesCall(latLng,500,search);
        geocodeResponseCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(placesObserver);

    }
}

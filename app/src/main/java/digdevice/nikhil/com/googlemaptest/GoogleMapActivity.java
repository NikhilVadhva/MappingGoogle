package digdevice.nikhil.com.googlemaptest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "GoogleMapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private EditText mapSearch;
    private float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mfusedLocationProvider;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemap);
         mfusedLocationProvider= LocationServices.getFusedLocationProviderClient(this);
         mapSearch=(EditText)findViewById(R.id.input_search);
        Log.d("OnCreate Called", "Successfully Called");
        getLocationPermission();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        if (mLocationPermissionGranted)
        {
            getDeviceLcoation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            init();
        }
    }


     private void init()
     {

          mapSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
              @Override
              public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent)
              {
                  Log.d(TAG,"Init Initializing");
                  Log.d("TEST RESPONSE", "Action ID = " + actionId + " KeyEvent = " + keyEvent);

                  if(actionId == EditorInfo.IME_ACTION_SEARCH
                          || actionId == EditorInfo.IME_ACTION_DONE
                          || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                          || keyEvent.getAction() ==  KeyEvent.KEYCODE_ENTER)
                  {
                       // Execute our methods for searching
                      Log.d(TAG, "Inside the INIT Method");
                      geoLocate();
                  }
                  return false;
              }
          });
     }

    @SuppressLint("LongLogTag")
    private void geoLocate()
    {
        Log.d(TAG,"GeoLocate: GeoLocating");
        String searchString =mapSearch.getText().toString();
        Log.d("Searched City",searchString.toString());
        Geocoder geocoder= new Geocoder(GoogleMapActivity.this);
        List<Address> list = new ArrayList<>();
        Address address=null;
        try
        {
            list=geocoder.getFromLocationName(searchString,1);

        }
        catch(IOException e)
        {
             Log.d(TAG,"geoLocate IOException"+e.getMessage());
        }
        if(list.size()>0 && list!= null)
        {
            address=list.get(0);
            Log.d(TAG,"geoLocate found a location "+address.toString());
        }

    }

    // /*
    //     * Get the best and most recent location of the device, which may be null in rare
    //     * cases when a location is not available.
    //     */
     private  void getDeviceLcoation()
     {
          Log.d(TAG,"Getting the current device location");

          try
          {
              if(mLocationPermissionGranted)
              {
                  final Task locationResult=mfusedLocationProvider.getLastLocation();
                  locationResult.addOnCompleteListener(new OnCompleteListener()
                  {
                      @Override
                      public void onComplete(@NonNull Task task)
                      {
                          if(task.isSuccessful() && task.getResult()!=null)
                          {
                               // Set the map's camera location to the current device location
                              Log.d(TAG,"onComplete Found Location");
                              Location mLastknownLocation =(Location)task.getResult();

                                  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastknownLocation.getLatitude(),
                                          mLastknownLocation.getLongitude()),DEFAULT_ZOOM));


                          }
                          else
                          {
                               Log.d(TAG,"Current location is null. Using defaults");
                               Log.e(TAG, "Exception: %s", task.getException());
                               Toast.makeText(GoogleMapActivity.this,"unable to get Current Location",Toast.LENGTH_LONG).show();
                              mMap.getUiSettings().setMyLocationButtonEnabled(false);
                          }
                      }
                  });
              }
          }
          catch (SecurityException e)
          {
              Log.d(TAG,"GetDeviceLocation Security Exception "+e.getMessage());
          }
     }


     // Moving Camera
     private void moveCamera(LatLng latLng ,float zoom)
     {
        Log.d(TAG,"Moving the camera to "+latLng.latitude +" and "+latLng.longitude );

     }
    private void initMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(GoogleMapActivity.this);
    }


    // RuntTime Permission checking for location permission

    private void getLocationPermission()
    {
        Log.d("Location Inside","Inside the Location Permission");
         String[] permissions={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
         if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
         {
              if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COARSE)==PackageManager.PERMISSION_GRANTED)
              {
                   mLocationPermissionGranted=true;
                   Log.d("Location Permission :","Location Permission Granted");
                   initMap();
              }
              else
              {
                  ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
              }
         }
         else
         {
             ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
             Log.d("Location Permission :","Location Permission Failed");

         }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        Log.d("Request","OnRequestPermissionCalled");
        mLocationPermissionGranted=false;
        switch(requestCode)
        {
            case LOCATION_PERMISSION_REQUEST_CODE :
                if(grantResults.length>0)
                {
                     for(int i=0;i<grantResults.length;i++)
                     {
                          if(grantResults[i]!=PackageManager.PERMISSION_GRANTED)
                          {
                               mLocationPermissionGranted=false;
                               Log.d("Request Permission:","OnRequest permission Faliled");
                               return;
                          }
                     }
                    Log.d("Request Permission:","OnRequest permission Granted");
                     mLocationPermissionGranted=true;
                     //initate our map
                     initMap();
                }

        }
    }


}

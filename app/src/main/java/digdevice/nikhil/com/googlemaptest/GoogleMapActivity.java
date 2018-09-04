package digdevice.nikhil.com.googlemaptest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private static final String TAG="GoogleMapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE=Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE= 1234;
    private Boolean mLocationPermissionGranted=false;
    private GoogleMap mMap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemap);
        Log.d("OnCreate Called","Successfully Called");
        getLocationPermission();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
            mMap=googleMap;
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

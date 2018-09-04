package digdevice.nikhil.com.googlemaptest;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity
{

    private static final int ERROR_DIALOG_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isServicesOk())
        {
             inti();
        }
    }

    private void inti()
    {
        Button btnMap = (Button)findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent =new Intent(MainActivity.this,GoogleMapActivity.class);
                startActivity(intent);
            }
        });
    }


    // Method for checking the google play service is available or not at user's device
    public boolean isServicesOk()
    {
        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(available == ConnectionResult.SUCCESS)
        {
            // Everything is fine and user can make a map request
            Log.d("isServiceOk","Google Play Service is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            // an error occured but we can fixed it
            Log.d("isServiceOk","An error occured but we can't fix it");
            Dialog dialog=GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();

        }
        else
        {
            Toast.makeText(this, "You can't make Map request", Toast.LENGTH_LONG).show();
        }
        return false;
    }

}

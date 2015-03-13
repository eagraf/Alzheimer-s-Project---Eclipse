package graf.ethan.alzheimers_project;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements LocationListener {
    private TextView latituteField;
    private TextView longitudeField;
    private TextView addressField;
    private LocationManager locationManager;
    private Geocoder geoCoder;
    private String provider;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latituteField = (TextView) findViewById(R.id.TextViewLatitudeCoordinate);
        longitudeField = (TextView) findViewById(R.id.TextViewLongitudeCoordinate);
        addressField = (TextView) findViewById(R.id.TextViewAddressString);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        
        geoCoder = new Geocoder(this);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            latituteField.setText("Location not available");
            longitudeField.setText("Location not available");
            addressField.setText("Address not available");
        }
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Address address = null;
        try {
			address = ((ArrayList<Address>) geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 16)).get(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));
        if(address != null) {
        	addressField.setText(address.getAddressLine(0));
        }
        else {
        	addressField.setText("Address not available");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}
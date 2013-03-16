package ru.mahon.MahRoad;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.location.LocationListener;
import android.widget.TextView;

public class MainActivity extends Activity implements LocationListener{
    /**
     * Called when the activity is first created.
     */
    private LocationManager myManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //
        myManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //
        myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        TextView la = (TextView) findViewById(R.id.la);
        TextView lo = (TextView) findViewById(R.id.lo);
        TextView GPS = (TextView) findViewById(R.id.GPS);
        la.setText("Широта: "	+ location.getLatitude());
        lo.setText("Долгота: " + location.getLongitude());
        GPS.setText("GPS: " + location.getProvider());

    }

    public void onProviderDisabled(String provider) {}

    public void onProviderEnabled(String provider) {}

    public void onStatusChanged(String provider, int status, Bundle extras) {}
}

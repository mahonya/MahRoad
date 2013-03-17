package ru.mahon.MahRoad;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.location.LocationListener;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements LocationListener{
    /**
     * Called when the activity is first created.
     */
    private LocationManager myManager;
    private LongOperation myLongOper;
    private Location lastLocation=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //
        myManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //
        myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        lastLocation = myManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final Button button = (Button) findViewById(R.id.load);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) // клик на кнопку
            {
                new LongOperation()
                {
                    public void onPostExecute(String result)
                    {
                        TextView txt = (TextView) findViewById(R.id.Inet);
                        txt.setText(result);
                    }
                }.execute(myManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            }
        });

    }

    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub


        TextView la = (TextView) findViewById(R.id.la);
        TextView lo = (TextView) findViewById(R.id.lo);
        TextView GPS = (TextView) findViewById(R.id.GPS);
        if(location.distanceTo(lastLocation)>10){
            lastLocation=location;
            GPS.setText("GPS: " + "Last change");
        }
        la.setText("Широта: " + location.getLatitude());
        lo.setText("Долгота: " + location.getLongitude());

    }

    public void onProviderDisabled(String provider) {}

    public void onProviderEnabled(String provider) {}

    public void onStatusChanged(String provider, int status, Bundle extras) {}
}

class LongOperation extends AsyncTask<Location, Void, String> {

       @Override
    protected String doInBackground(Location... params) {
        String str = "Error";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://myroad.info/track.php");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("us", "mahon"));
            nameValuePairs.add(new BasicNameValuePair("pw", "Pol1901"));
            nameValuePairs.add(new BasicNameValuePair("la", String.valueOf(params[0].getLatitude())));
            nameValuePairs.add(new BasicNameValuePair("lo", String.valueOf(params[0].getLongitude())));
            //nameValuePairs.add(new BasicNameValuePair("nm", "Test"));
            nameValuePairs.add(new BasicNameValuePair("dt", String.valueOf(params[0].getTime())));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
        /*
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("http://myroad.info/track.php");
            HttpResponse response = client.execute(request);
            */
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                str=response.getStatusLine().toString();
            }
        } catch (ClientProtocolException e) {
        // TODO Auto-generated catch block
        } catch (IOException e) {
        // TODO Auto-generated catch block
        }

        return str;
    }

    protected void onPostExecute(HttpEntity result) {
        //might want to change "executed" for the returned string passed into onPostExecute() but that is upto you
    }

    protected void onPreExecute() {
    }

    protected void onProgressUpdate(Void... values) {
    }
}

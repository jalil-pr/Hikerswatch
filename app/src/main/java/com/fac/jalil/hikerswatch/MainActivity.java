package com.fac.jalil.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView tvLat,tvLong;

    public void updateLocation(Location location)
    {


        TextView lat=(TextView)findViewById(R.id.lat);
        TextView lang=(TextView)findViewById(R.id.lang);
        TextView acctv=(TextView)findViewById(R.id.acctv);
        TextView altv=(TextView)findViewById(R.id.alttv);
        TextView addrtv=(TextView)findViewById(R.id.addtv);
        lat.setText("latitide:"+location.getLatitude());
        lang.setText("langitude:"+location.getLongitude());
        acctv.setText("Accuracy:"+location.getAccuracy());
        altv.setText("Altitude:"+location.getAltitude());
        Geocoder geocoder =new Geocoder(this, Locale.getDefault());
        try
        {
            List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String addr="Could not find address";
            if (addresses!=null && addresses.size()>0)
            {
                addr="Address:\n";
               // Log.i("checkerr",addresses.get(0).toString());
               if(addresses.get(0).getSubThoroughfare()!=null)
               {
                   String subthorouphare=addresses.get(0).getSubThoroughfare();
                   addr+=subthorouphare+"\n";
               }
                if(addresses.get(0).getThoroughfare()!=null)
                {
                    String thorouphare=addresses.get(0).getThoroughfare();
                    addr+=thorouphare;
                }
                if(addresses.get(0).getLocality()!=null)
                {
                    String locality=addresses.get(0).getLocality();
                    addr+=locality+"\n";
                }
                if(addresses.get(0).getCountryName()!=null)
                {
                    String country=addresses.get(0).getCountryName();
                    addr+=country;
                }

            }
            addrtv.setText(addr);
        } catch (IOException e) {
            Toast.makeText(this,"check the error log",Toast.LENGTH_SHORT).show();
            Log.i("checkError",e.toString());
        }


    }

    public void startListening()
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLat=(TextView) findViewById(R.id.lat);
        tvLong=(TextView) findViewById(R.id.lang);
        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocation(location);

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
        };
        if(Build.VERSION.SDK_INT<23)
        {
           startListening();
        }
        else
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            else
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
        {
            if (grantResults!=null && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
               startListening();
            }
        }
    }
}

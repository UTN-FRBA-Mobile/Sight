package ar.com.sight.android.comun;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import ar.com.sight.android.Sight;

import static android.content.Context.LOCATION_SERVICE;

public class Gps implements LocationListener {

    Context thiscontext = null;

    Location location; // location

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            return location.getLatitude();
        }
        return 0;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            return location.getLongitude();
        }
        return 0;
    }

    public static Gps newInstance() {
        return new Gps();
    }

    @SuppressLint("MissingPermission")
    public void getLocacion(Context context) {

        thiscontext = context;

        LocationManager locationManager = (LocationManager) thiscontext.getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0, this);

            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        } else {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        0,
                        0, this);

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }
        }
    }

    static LocalDateTime ultimoCambioLocacion = LocalDateTime.now();

    @Override
    public void onLocationChanged(Location location) {
        if (LocalDateTime.now().until(ultimoCambioLocacion, ChronoUnit.MINUTES) > 1) {
            ultimoCambioLocacion = LocalDateTime.now();
            if (thiscontext != null && Sight.getEventoReciente(thiscontext)) {
                this.location = location;
                Sight.sendEvento(thiscontext);
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

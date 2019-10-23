package ar.com.sight.android.comun;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import ar.com.sight.android.Sight;

import static android.content.Context.LOCATION_SERVICE;

public class Gps {
    private static Location location;
    static LocalDateTime ultimoCambioLocacion = LocalDateTime.now();

    public static double getLatitude() {
        if (getLocation() != null) {
            return getLocation().getLatitude();
        }
        return 0;
    }

    public static double getLongitude() {
        if (getLocation() != null) {
            return getLocation().getLongitude();
        }
        return 0;
    }

    public static Location getLocation() {
        return location;
    }

    public static void setLocation(Location location) {
        Gps.location = location;
        ultimoCambioLocacion = LocalDateTime.now();
    }
}

package rsa.sight;

import android.app.Application;

public class MyApplication extends Application {
    private String userLogueado;
    private String emailLogueado;
    private double latitud;
    private double longitud;

    public final static String EXTRA_MESSAGE = "rsa.siglop";
    public final static String EXTRA_MESSAGE2 = "rsa.siglop";
    private static final String TAG = "MainActivity";

    public String getUserLogueado(){
        return userLogueado = userLogueado;
    }
    public void setUserLogueado(String user){

        this.userLogueado = user;
    }

    public String getEmailLogueado(){
        return emailLogueado = emailLogueado;
    }
    public void setEmailLogueado(String email){

        this.emailLogueado = email;
    }


    public double getLatitud(){
        return latitud = latitud;
    }
    public void setLatitud(double latitud2){

        this.latitud = latitud2;
    }

    public double getLongitud(){
        return longitud = longitud;
    }
    public void setLongitud(double longitud2){

        this.longitud = longitud2;
    }

    public String getToken()
    {
        return getApplicationContext().getSharedPreferences("Sight", MODE_PRIVATE).getString("Token", "");
    }
}

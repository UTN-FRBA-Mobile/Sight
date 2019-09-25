package rsa.sight.api;

import android.content.Context;

import com.google.android.gms.common.api.Response;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class APIAdapter {
    public static final String ROOT_URL = "http://alarmas.ctmelectronica.com.ar/utn/api/";

    public static APIService crearConexion(Gson gson)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(APIService.class);
    }

    public static APIService crearConexion()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .build();

        return retrofit.create(APIService.class);
    }
}

package ar.com.sight.android.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIAdapter {
    public static final String ROOT_URL = "http://utn.ctmelectronica.com.ar/utn/api/";

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

    public static APIService crearConexion(Class returnClasss, JsonDeserializer deserializador) {

        Gson gson = (new GsonBuilder()).registerTypeAdapter(returnClasss, deserializador).disableHtmlEscaping().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(APIService.class);
    }
}

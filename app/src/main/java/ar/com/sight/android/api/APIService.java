package ar.com.sight.android.api;

import java.util.ArrayList;

import ar.com.sight.android.api.modelos.Evento;
import ar.com.sight.android.api.modelos.Usuario;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIService {

    @POST("token")
    @FormUrlEncoded
    Call<String> getAccessToken(@Field("username") String username,
                                @Field("password") String password,
                                @Field("grant_type") String grantType);

    @GET("usuarios/get")
    Call<Usuario> getMisDatos(@Header("Authorization") String token);

    @POST("eventos/nuevo")
    @FormUrlEncoded
    Call<Void> setNuevoEvento(@Header("Authorization") String token,
                              @Field("evento_tipo_id") Integer tipo_evento,
                              @Field("latitud") Double latitud,
                              @Field("longitud") Double longitud);

    @POST("usuarios/nuevacontraseña")
    @FormUrlEncoded
    Call<String> setNuevaContraseña(@Header("Authorization") String token, @Field("actual") String actual, @Field("nueva") String nueva, @Field("confirmacion") String confirmacion);

    @POST("usuarios/ficha_medica")
    @FormUrlEncoded
    Call<Void> setFichaMedica(@Header("Authorization") String token, @Field("ficha_medica") String ficha_medica);
						  
    @GET("usuarios/ficha_medica")
    Call<String> getFichaMedica(@Header("Authorization") String token);

    @GET("usuarios/vecinos")
    Call<ArrayList<Usuario>> getVecinos(@Header("Authorization") String token);

    @POST("usuarios/vecino")
    @FormUrlEncoded
    Call<Void> setVecino(@Header("Authorization") String token, @Field("email") String email);

    @HTTP(method = "DELETE", path = "usuarios/vecino", hasBody = true)
    @FormUrlEncoded
    Call<Void> deleteVecino(@Header("Authorization") String token, @Field("id") Integer id);

    @GET("eventos/mis_denuncias")
    Call<ArrayList<Evento>> getMisDenuncias(@Header("Authorization") String token);

    @GET("eventos/mis_notificaciones")
    Call<ArrayList<Evento>> getMisNotificaciones(@Header("Authorization") String token);

    @GET("eventos/get")
    Call<Evento> getEvento(@Header("Authorization") String token, @Query("evento_id") Integer evento_id);

    @Multipart
    @POST("eventos/adicional_archivo")
    Call<String> setArchivoAdicional(@Header("Authorization") String token
            //, @Header("content-type") String hContentType1
            //, @Header("Content-Type") String hContentType2, @Header("Accept-Encoding") String hAcceptEncoding
            //, @Header("Accept") String hAccept, @Header("Cache-Control") String hCache
            //, @Header("Connection") String hConnection
            , @Part("description") RequestBody description
            , @Part MultipartBody.Part file);

    @POST("eventos/adicional_mensaje")
    @FormUrlEncoded
    Call<String> setMensajeAdicional(@Header("Authorization") String token, @Field("mensaje") String mensaje);

    @POST("eventos/calificar")
    @FormUrlEncoded
    Call<String> setCalificar(@Header("Authorization") String token, @Field("evento_id") Integer evento_id
            , @Field("puntuacion") Integer puntuacion, @Field("mensaje") String mensaje);
}

package ar.com.sight.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import ar.com.sight.android.api.APIAdapter;
import ar.com.sight.android.api.deserializadores.StringDeserializador;
import ar.com.sight.android.api.deserializadores.UsuarioDeserializador;
import ar.com.sight.android.api.modelos.Usuario;
import ar.com.sight.android.comun.Gps;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.Part;

import static android.content.Context.MODE_PRIVATE;

public class Sight {

    static Usuario usuario = null;
    static String token = null;
    static LocalDateTime timestamp_ultimo_evento;

    public static String getToken(Context context) {
        if (token == null) {
            token = context.getSharedPreferences("Sight", MODE_PRIVATE).getString("Token", "");
        }
        return token;
    }

    public static void setToken(Context context, String token) {
        Sight.token = String.format("Bearer %s", token);
        SharedPreferences.Editor editor = context.getSharedPreferences("Sight", MODE_PRIVATE).edit();
        editor.putString("Token", Sight.token);
        editor.apply();
        getMisDatos(context);
    }

    public static Usuario getMisDatos(final Context context) {
        APIAdapter.crearConexion(Usuario.class, new UsuarioDeserializador()).getMisDatos(token).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Usuario u = response.body();
                if (u == null) {
                    borrarDatos(context);
                    return;
                }
                setUsuario(context, u);
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                borrarDatos(context);
            }
        });

        return usuario;
    }

    public static Usuario getUsuario(Context context) {
        if (usuario == null) {
            if (context.getSharedPreferences("Sight", MODE_PRIVATE).getString("ID", "") != "") {
                usuario = new Usuario();
                usuario.setApellido(context.getSharedPreferences("Sight", MODE_PRIVATE).getString("Apellido", ""));
                usuario.setEmail(context.getSharedPreferences("Sight", MODE_PRIVATE).getString("Email", ""));
                usuario.setId(Integer.valueOf(context.getSharedPreferences("Sight", MODE_PRIVATE).getString("ID", "-1")));
                usuario.setNombre(context.getSharedPreferences("Sight", MODE_PRIVATE).getString("Nombre", ""));
            }
        }

        if (usuario != null) {
            startFirebase(context);
        }

        return usuario;
    }

    public static void setUsuario(Context context, Usuario u) {
        Sight.usuario = u;
        SharedPreferences.Editor editor = context.getSharedPreferences("Sight", MODE_PRIVATE).edit();
        editor.putString("Email", usuario.getEmail() == null? "" : usuario.getEmail());
        editor.putString("Nombre", usuario.getNombre() == null? "" : usuario.getNombre());
        editor.putString("Apellido", usuario.getApellido() == null? "" : usuario.getApellido());
        editor.putString("ID", String.valueOf(usuario.getId()));
        editor.apply();
        startFirebase(context);
    }

    public static void borrarDatos(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("Sight", MODE_PRIVATE).edit();
        editor.putString("Email", "");
        editor.putString("Nombre", "");
        editor.putString("Apellido", "");
        editor.putString("Token", "");
        editor.putString("ID", "");
        editor.apply();
        token = "";
        usuario = null;
    }

    public static void startFirebase(Context context) {
        //FirebaseApp.initializeApp(context);
        FirebaseMessaging.getInstance().subscribeToTopic(String.valueOf(usuario.getId()));
    }

    public static boolean getEventoReciente(Context context) {
        if (timestamp_ultimo_evento == null) {
            timestamp_ultimo_evento = LocalDateTime.ofEpochSecond(context.getSharedPreferences("Sight", MODE_PRIVATE).getLong("timestamp_ultimo_evento", 0), 0, OffsetDateTime.now(ZoneId.systemDefault()).getOffset());
        }
        return LocalDateTime.now().minusHours(1).isBefore(timestamp_ultimo_evento);
    }

    public static Integer getTipoEventoReciente(Context context) {
        return context.getSharedPreferences("Sight", MODE_PRIVATE).getInt("tipo_ultimo_evento", 0);
    }

    public static void setEventoReciente(Context context, Integer tipo_evento) {
        timestamp_ultimo_evento = LocalDateTime.now();
        SharedPreferences.Editor editor = context.getSharedPreferences("Sight", MODE_PRIVATE).edit();
        editor.putLong("timestamp_ultimo_evento", timestamp_ultimo_evento.toEpochSecond(OffsetDateTime.now(ZoneId.systemDefault()).getOffset()));
        editor.putInt("tipo_ultimo_evento", tipo_evento);
        editor.apply();
    }

    public static void sendEvento(Context context){
        sendEvento(context, getTipoEventoReciente(context));
    }

    public static void sendEvento(final Context context, Integer tipo_evento){
        try {

            Gps gps = Gps.newInstance();
            gps.getLocacion(context);

            APIAdapter.crearConexion().setNuevoEvento(getToken(context),
                    tipo_evento, gps.getLatitude(), gps.getLongitude()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast mensaje = Toast.makeText(context, "Evento Enviado: La ayuda va en camino", Toast.LENGTH_SHORT);
                    mensaje.show();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast mensaje = Toast.makeText(context, "ERROR: Intente nuevamente", Toast.LENGTH_SHORT);
                    mensaje.show();
                }
            });
        }
        catch (Exception ex) {
            Toast mensaje = Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT);
            mensaje.show();
        }

        setEventoReciente(context, tipo_evento);
    }

    public static void sendImagenAdicional(final Context context, String filePath) {
        try {
            //Create a file object using file path
            File file = new File(filePath);

            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse("image/*"),
                            file
                    );

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("pepe", file.getName(), requestFile);

            // add another part within the multipart request
            String descriptionString = "hello, this is description speaking";
            RequestBody description =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, descriptionString);

            Call call = APIAdapter.crearConexion(String.class, new StringDeserializador()).setArchivoAdicional(getToken(context),
                    //"multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW",
                    //"application/x-www-form-urlencoded","gzip, deflate",
                    //"*/*", "no-cache", "keep-alive",
                    description, body);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Toast mensaje = Toast.makeText(context, response.body(), Toast.LENGTH_SHORT);
                    mensaje.show();
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast mensaje = Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT);
                    mensaje.show();
                }

            });
        }
        catch (Exception ex) {
            Toast mensaje = Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT);
            mensaje.show();
        }
    }

    public static void sendMesnajeAdicional(Context context, String mensaje){
        final String[] mensajebd = {""};

        try {
            APIAdapter.crearConexion(String.class, new StringDeserializador()).setMensajeAdicional(getToken(context), mensaje).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    mensajebd[0] = response.body();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    mensajebd[0] = t.getMessage();
                }
            });
        }
        catch (Exception ex)
        {
            mensajebd[0] = ex.getMessage();
        }

        Toast msg = Toast.makeText(context, mensajebd[0], Toast.LENGTH_SHORT);
        msg.show();
    }
}

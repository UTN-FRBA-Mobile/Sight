package rsa.sight;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rsa.sight.api.APIAdapter;
import rsa.sight.api.deserializadores.TokenDeserializador;
import rsa.sight.api.deserializadores.UsuarioDeserializador;
import rsa.sight.api.modelos.Usuario;

public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText usuario;
    private EditText password;
    private View mProgressView;
    private View mLoginFormView;
    private static final String TAG = "MainActivity";

    public final static String EXTRA_MESSAGE = "rsa.siglop"; //PARA MOSTRAR EL USUARIO ARRIBA Y VER QUE FUNCIONA
    public final static String EXTRA_MESSAGE2 = "rsa.siglop"; //PARA MOSTRAR MAIL ARRIBA

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        usuario = (EditText) findViewById(R.id.usuario);

        password = (EditText) findViewById(R.id.password);

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button ingresar = (Button) findViewById(R.id.ingresar);
        ingresar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void attemptLogin() {
        // Reset errors.
        usuario.setError(null);
        password.setError(null);

        // Store values at the time of the login attempt.
        String usuarioIngresado = usuario.getText().toString();
        String passwordIngresado = password.getText().toString();
        String mensajebd = "";
        View focusView = null;

        if (TextUtils.isEmpty(usuarioIngresado)) {
            usuario.setError(getString(R.string.error_usuario_requerido));
            focusView = usuario;
            return;
        }

        if (TextUtils.isEmpty(passwordIngresado)) {
            password.setError(getString(R.string.error_password_requerido));
            focusView = password;
            return;
        }

        try
        {
            Gson gson = (new GsonBuilder()).registerTypeAdapter(String.class, new TokenDeserializador()).create();
            APIAdapter.crearConexion(gson).getAccessToken(usuarioIngresado, passwordIngresado, "password").enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String token = String.format("Bearer %s", response.body());
                    SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("Sight", MODE_PRIVATE).edit();
                    editor.putString("Token", token);
                    editor.commit();

                    getMisDatos(token);
                    // Close the activity so the user won't able to go back this
                    // activity pressing Back button
                    finish();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });
        }
        catch (Exception ex)
        {
            mensajebd = ex.getMessage();
        }
    }

    public void getMisDatos(final String token)
    {
        Gson gson = (new GsonBuilder()).registerTypeAdapter(Usuario.class, new UsuarioDeserializador()).create();
        APIAdapter.crearConexion(gson).getMisDatos(token).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Usuario u = response.body();
                if (u == null)
                {
                    borrarDatos(getApplicationContext());
                    return;
                }

                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("Sight", MODE_PRIVATE).edit();
                editor.putString("Email", u.getEmail());
                editor.putString("Nombre", u.getNombre());
                editor.putString("Apellido", u.getApellido());
                editor.putString("ID", String.valueOf(u.getId()));
                editor.commit();

                FirebaseMessaging.getInstance().subscribeToTopic(String.valueOf(u.getId()));

                if (u.getFirebase() != FirebaseInstanceId.getInstance().getToken())
                    RefrescarFirebase(token, FirebaseInstanceId.getInstance().getToken());

                Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
                intent.putExtra(EXTRA_MESSAGE, u.getNombre() + ' ' + u.getApellido());
                intent.putExtra(EXTRA_MESSAGE2, u.getEmail());
                ((MyApplication)getApplicationContext()).setUserLogueado(u.getNombre() + ' ' + u.getApellido());
                ((MyApplication)getApplicationContext()).setEmailLogueado(u.getEmail());
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                borrarDatos(getApplicationContext());
            }
        });
    }

    public static void borrarDatos(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("Sight", MODE_PRIVATE).edit();
        editor.putString("Email", "");
        editor.putString("Nombre", "");
        editor.putString("Apellido", "");
        editor.putString("Token", "");
        editor.putString("ID", "");
        editor.commit();
    }

    public void RefrescarFirebase(String token, String firebase) {


        //OBTENGO EL TOKEN, CONECTADO A FIREBASE
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        try
        {
            APIAdapter.crearConexion().setFirebase(token, firebase).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    borrarDatos(getApplicationContext());
                }
            });
        }
        catch (Exception ex)
        {
        }

        Log.d(TAG, "Refreshed token: " + refreshedToken);



    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}

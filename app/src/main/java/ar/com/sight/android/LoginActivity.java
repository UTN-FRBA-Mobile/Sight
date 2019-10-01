package ar.com.sight.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ar.com.sight.android.api.APIAdapter;
import ar.com.sight.android.api.deserializadores.TokenDeserializador;
import ar.com.sight.android.configuracion.BotonBluetoothActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText txtPassword;
    EditText txtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPassword);

        Button btnIngresar = (Button)findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentarLogin();
            }
        });
    }

    private void intentarLogin() {
        txtUsername.setError(null);
        txtPassword.setError(null);

        // Store values at the time of the login attempt.
        String usuarioIngresado = txtUsername.getText().toString();
        String passwordIngresado = txtPassword.getText().toString();

        if (TextUtils.isEmpty(usuarioIngresado)) {
            txtUsername.setError(getString(R.string.error_usuario_requerido));
            return;
        }

        if (TextUtils.isEmpty(passwordIngresado)) {
            txtPassword.setError(getString(R.string.error_password_requerido));
            return;
        }

        try
        {
            APIAdapter.crearConexion(String.class, new TokenDeserializador()).getAccessToken(usuarioIngresado, passwordIngresado, "password").enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Sight.setToken(getApplication(), response.body());
                    Intent mainIntent = new Intent().setClass(LoginActivity.this, MenuPrincipalActivity.class);
                    startActivity(mainIntent);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Sight.borrarDatos(getApplication());
                    Toast mensaje = Toast.makeText(getApplicationContext(), R.string.error_password_incorrecto, Toast.LENGTH_SHORT);
                    mensaje.show();
                }
            });
        }
        catch (Exception ex) {
            Toast mensaje = Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT);
            mensaje.show();
        }
    }
}
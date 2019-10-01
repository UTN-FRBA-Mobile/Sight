package ar.com.sight.android.configuracion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ar.com.sight.android.ConfiguracionActivity;
import ar.com.sight.android.MenuPrincipalActivity;
import ar.com.sight.android.R;
import ar.com.sight.android.Sight;
import ar.com.sight.android.api.APIAdapter;
import ar.com.sight.android.api.deserializadores.StringDeserializador;
import ar.com.sight.android.comun.EncabezadoFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambiarContraseniaActivity extends AppCompatActivity {

    EditText txtPasswordActual;
    EditText txtPasswordNueva;
    EditText txtPasswordConfirmacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasenia);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameHeader, EncabezadoFragment.newInstance(getResources().getString(R.string.tituloCambiarContrasenia)));
        ft.commit();

        txtPasswordActual = (EditText)findViewById(R.id.txtPasswordActual);
        txtPasswordNueva = (EditText)findViewById(R.id.txtPasswordNueva);
        txtPasswordConfirmacion = (EditText)findViewById(R.id.txtPasswordConfirmacion);

        final String token = Sight.getToken(this.getApplication());
        final Resources res = getResources();

        Button btnBlueTooth = (Button) findViewById(R.id.btnCambiarContrasenia);
        btnBlueTooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    APIAdapter.crearConexion(String.class, new StringDeserializador()).setNuevaContraseña(token,
                            txtPasswordActual.getText().toString(), txtPasswordNueva.getText().toString(),
                            txtPasswordConfirmacion.getText().toString()).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Toast mensaje = Toast.makeText(getApplicationContext(), response.body(), Toast.LENGTH_SHORT);
                            mensaje.show();

                            if (response.body().equals(res.getString(R.string.success_contrasenia_cambiada))) {
                                Intent mainIntent = new Intent().setClass(CambiarContraseniaActivity.this, MenuPrincipalActivity.class);
                                startActivity(mainIntent);
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast mensaje = Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                            mensaje.show();
                        }
                    });
                }
                catch (Exception ex) {
                    Toast mensaje = Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT);
                    mensaje.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent().setClass(CambiarContraseniaActivity.this, ConfiguracionActivity.class);
        startActivity(mainIntent);
    }
}

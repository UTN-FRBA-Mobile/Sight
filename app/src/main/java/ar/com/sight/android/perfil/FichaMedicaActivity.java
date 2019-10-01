package ar.com.sight.android.perfil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ar.com.sight.android.PerfilActivity;
import ar.com.sight.android.R;
import ar.com.sight.android.Sight;
import ar.com.sight.android.api.APIAdapter;
import ar.com.sight.android.api.deserializadores.StringDeserializador;
import ar.com.sight.android.comun.EncabezadoFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FichaMedicaActivity extends AppCompatActivity {

    EditText txtFichaMedica;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_medica);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameHeader, EncabezadoFragment.newInstance(getResources().getString(R.string.tituloMiFichaMedica)));
        ft.commit();

        txtFichaMedica = (EditText)findViewById(R.id.txtFichaMedica);

        final String token = Sight.getToken(this.getApplication());
        final Resources res = getResources();

        try
        {
            APIAdapter.crearConexion(String.class, new StringDeserializador()).getFichaMedica(token).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String ficha_medica = response.body();

                    if (ficha_medica.equals(res.getString(R.string.error_ficha_medica_no_existente))) {
                        Toast mensaje = Toast.makeText(getApplicationContext(), ficha_medica, Toast.LENGTH_SHORT);
                        mensaje.show();
                    } else {
                        txtFichaMedica.setText(response.body());
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

        Button btnGuardarFichaMedica = (Button)findViewById(R.id.btnGuardarFichaMedica);
        btnGuardarFichaMedica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtFichaMedica.setError(null);
                String ficha_medica = txtFichaMedica.getText().toString();

                if (TextUtils.isEmpty(ficha_medica)) {
                    txtFichaMedica.setError(getString(R.string.error_ficha_medica_requerida));
                    return;
                }

                try
                {
                    APIAdapter.crearConexion().setFichaMedica(token, ficha_medica).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Toast mensaje = Toast.makeText(getApplicationContext(), R.string.success_ficha_medica, Toast.LENGTH_SHORT);
                            mensaje.show();

                            Intent mainIntent = new Intent().setClass(FichaMedicaActivity.this, PerfilActivity.class);
                            startActivity(mainIntent);

                            finish();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast mensaje = Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT);
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
        Intent mainIntent = new Intent().setClass(FichaMedicaActivity.this, PerfilActivity.class);
        startActivity(mainIntent);
    }
}

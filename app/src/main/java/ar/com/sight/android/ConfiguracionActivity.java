package ar.com.sight.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ar.com.sight.android.comun.EncabezadoFragment;
import ar.com.sight.android.configuracion.BotonBluetoothActivity;
import ar.com.sight.android.configuracion.CambiarContraseniaActivity;

public class ConfiguracionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameHeader, EncabezadoFragment.newInstance(getResources().getString(R.string.tituloConfiguracion)));
        ft.commit();

        Button btnContrasenia = (Button) findViewById(R.id.btnContrasenia);
        btnContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(ConfiguracionActivity.this, CambiarContraseniaActivity.class);
                startActivity(mainIntent);
            }
        });

        Button btnBlueTooth = (Button) findViewById(R.id.btnBotonBlueTooth);
        btnBlueTooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(ConfiguracionActivity.this, BotonBluetoothActivity.class);
                startActivity(mainIntent);
            }
        });

        Button btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sight.borrarDatos(getApplication());
                Intent mainIntent = new Intent().setClass(ConfiguracionActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent().setClass(ConfiguracionActivity.this, MenuPrincipalActivity.class);
        startActivity(mainIntent);
    }
}

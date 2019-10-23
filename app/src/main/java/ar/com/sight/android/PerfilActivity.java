package ar.com.sight.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ar.com.sight.android.comun.EncabezadoFragment;
import ar.com.sight.android.perfil.FichaMedicaActivity;
import ar.com.sight.android.perfil.MisDenunciasActivity;
import ar.com.sight.android.perfil.MisNotificacionesActivity;
import ar.com.sight.android.perfil.MisVecinosActivity;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameHeader, EncabezadoFragment.newInstance(getResources().getString(R.string.tituloPerfil)));
        ft.commit();

        Button btnFichaMedica = (Button) findViewById(R.id.btnFichaMedica);
        btnFichaMedica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(PerfilActivity.this, FichaMedicaActivity.class);
                startActivity(mainIntent);
            }
        });

        Button btnVecinos = (Button) findViewById(R.id.btnVecinos);
        btnVecinos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(PerfilActivity.this, MisVecinosActivity.class);
                startActivity(mainIntent);
            }
        });

        Button btnDenuncias = (Button) findViewById(R.id.btnDenuncias);
        btnDenuncias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(PerfilActivity.this, MisDenunciasActivity.class);
                startActivity(mainIntent);
            }
        });

        Button btnMisNotificaciones = (Button) findViewById(R.id.btnMisNotificaciones);
        btnMisNotificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(PerfilActivity.this, MisNotificacionesActivity.class);
                startActivity(mainIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent().setClass(PerfilActivity.this, MenuPrincipalActivity.class);
        startActivity(mainIntent);
    }
}

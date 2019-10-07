package ar.com.sight.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import ar.com.sight.android.comun.AdicionalesActivity;
import ar.com.sight.android.comun.EncabezadoFragment;

public class MenuPrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameHeader, EncabezadoFragment.newInstance(""));
        ft.commit();

        ImageButton btnPerfil = (ImageButton)findViewById(R.id.btnPerfil);
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(MenuPrincipalActivity.this, PerfilActivity.class);
                startActivity(mainIntent);
            }
        });

        ImageButton btnConfiguracion = (ImageButton)findViewById(R.id.btnConfiguracion);
        btnConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(MenuPrincipalActivity.this, ConfiguracionActivity.class);
                startActivity(mainIntent);
            }
        });

        ImageButton btnRobo = (ImageButton)findViewById(R.id.btnRobo);
        btnRobo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sight.sendEvento(getApplication(), 1);
            }
        });

        ImageButton btnSos = (ImageButton)findViewById(R.id.btnSos);
        btnSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(MenuPrincipalActivity.this, SosActivity.class);
                startActivity(mainIntent);
            }
        });

        ImageButton btnAdicionales = (ImageButton)findViewById(R.id.btnAdicionales);
        btnAdicionales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(MenuPrincipalActivity.this, AdicionalesActivity.class);
                startActivity(mainIntent);
            }
        });

        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getResources().getString(R.string.error_permiso_ubicacion), Toast.LENGTH_LONG).show();
        }
    }
}

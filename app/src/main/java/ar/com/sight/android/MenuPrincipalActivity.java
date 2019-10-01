package ar.com.sight.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

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

        ImageButton btnSos = (ImageButton)findViewById(R.id.btnSos);
        btnSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(MenuPrincipalActivity.this, SosActivity.class);
                startActivity(mainIntent);
            }
        });
    }
}

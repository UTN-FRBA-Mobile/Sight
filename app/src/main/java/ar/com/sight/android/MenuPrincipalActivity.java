package ar.com.sight.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import ar.com.sight.android.comun.AdicionalesActivity;
import ar.com.sight.android.comun.EncabezadoFragment;
import ar.com.sight.android.comun.Gps;

public class MenuPrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        if (BluetoothAdapter.getDefaultAdapter() != null) {
            if (!Sight.isServiceRunning(this)) {
                Intent backgroundService = new Intent(getApplicationContext(), ServicioBackground.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    startForegroundService(backgroundService);
                else
                    startService(backgroundService);
            }
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameHeader, EncabezadoFragment.newInstance(""));
        ft.commit();

        ImageButton btnPerfil = (ImageButton) findViewById(R.id.btnPerfil);
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(MenuPrincipalActivity.this, PerfilActivity.class);
                startActivity(mainIntent);
            }
        });

        ImageButton btnConfiguracion = (ImageButton) findViewById(R.id.btnConfiguracion);
        btnConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(MenuPrincipalActivity.this, ConfiguracionActivity.class);
                startActivity(mainIntent);
            }
        });

        ImageButton btnAmbulancia = (ImageButton) findViewById(R.id.btnAmbulancia);
        btnAmbulancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sight.sendEvento(getApplication(), 5);
            }
        });

        ImageButton btnRobo = (ImageButton) findViewById(R.id.btnRobo);
        btnRobo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sight.sendEvento(getApplication(), 1);
            }
        });

        ImageButton btnSos = (ImageButton) findViewById(R.id.btnSos);
        btnSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(MenuPrincipalActivity.this, SosActivity.class);
                startActivity(mainIntent);
            }
        });

        ImageButton btnAdicionales = (ImageButton) findViewById(R.id.btnAdicionales);
        btnAdicionales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(MenuPrincipalActivity.this, AdicionalesActivity.class);
                startActivity(mainIntent);
            }
        });

        checkearPermisos();

        configurarGps();
    }

    private void configurarGps() {
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Gps.setLocation(location);
                }
            }
        });
    }

    private void checkearPermisos() {
        if ((checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) ||
                (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) ||
                (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getResources().getString(R.string.error_permiso), Toast.LENGTH_LONG).show();
            }
        }
    }
}

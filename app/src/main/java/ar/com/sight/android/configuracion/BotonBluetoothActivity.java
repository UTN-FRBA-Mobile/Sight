package ar.com.sight.android.configuracion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import ar.com.sight.android.MenuPrincipalActivity;
import ar.com.sight.android.R;
import ar.com.sight.android.Sight;
import ar.com.sight.android.api.APIAdapter;
import ar.com.sight.android.comun.EncabezadoFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BotonBluetoothActivity extends AppCompatActivity {

    private static final int REQUEST_BLURTOOTH_PERMISSION = 200;
    private static final int REQUEST_BLURTOOTH_ADMIN_PERMISSION = 201;

    TableLayout tabla;
    BluetoothAdapter mBluetoothAdapter;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_BLURTOOTH_PERMISSION) {
                actualizarBluetooth();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_permiso_camara), Toast.LENGTH_LONG).show();
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_BLURTOOTH_ADMIN_PERMISSION) {
                actualizarBluetooth();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_permiso_bluetooth), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boton_bluetooth);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameHeader, EncabezadoFragment.newInstance(getResources().getString(R.string.tituloBlueTooth)));
        ft.commit();

        tabla = findViewById(R.id.tablaBluetooth);

        if (checkSelfPermission(Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.BLUETOOTH}, REQUEST_BLURTOOTH_PERMISSION);
        }

        if (checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_BLURTOOTH_ADMIN_PERMISSION);
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled())
            mBluetoothAdapter.enable();

        actualizarBluetooth();

        final Context thisContext = this;

        findViewById(R.id.btnBorrarBluetooth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sight.setBluetooth(thisContext, "");
                Toast.makeText(thisContext, "El dispositivo Bluetooth ha sido desasociado al evento de SOS", Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent().setClass(BotonBluetoothActivity.this, MenuPrincipalActivity.class);
                startActivity(mainIntent);
            }
        });
    }

    private void actualizarBluetooth() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        final Context thisContext = this;

        for(final BluetoothDevice bt : pairedDevices) {
            TableRow tbrow = new TableRow(this);

            tbrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Sight.setBluetooth(thisContext, bt.getAddress());
                    Toast.makeText(thisContext, "El dispositivo " + bt.getName() + " ha sido asociado al evento de SOS", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent().setClass(BotonBluetoothActivity.this, MenuPrincipalActivity.class);
                    startActivity(mainIntent);
                }
            });

            TextView t1v = new TextView(this);
            t1v.setText(bt.getName());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                t1v.setTextAppearance(R.style.TextTableRowView);
            }
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            tabla.addView(tbrow);
        }
    }
}

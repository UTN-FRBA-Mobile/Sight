package rsa.sight;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rsa.sight.api.APIAdapter;

public class RoboActivity extends AppCompatActivity {
    private TextView bienvenida;
    private Spinner selectorTipoRobo, selectorViolencia;
    private String usuario, email;
    private Context mContext;


    GPSTracker gpsTracker;
    double latitudnum,longitudnum;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        //usuario = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        //email = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE2);
        usuario = ((MyApplication) this.getApplication()).getUserLogueado();
        email = ((MyApplication) this.getApplication()).getEmailLogueado();
        setContentView(R.layout.activity_robo);
        bienvenida = (TextView) findViewById(R.id.bienvenida);
        String mensaje_bienvenida = getString(R.string.mensaje_bienvenida) + " " + usuario + " " + email;
        bienvenida.setText(mensaje_bienvenida);
        String[] tiporobo={"Delito","Hurto","Amenaza"};
        String[] violenciarobo={"Si","No"};



        Spinner selectorTipoRobo = (Spinner) findViewById(R.id.listaRobos);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,tiporobo);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        selectorTipoRobo.setAdapter(aa);


        Spinner selectorViolencia = (Spinner) findViewById(R.id.violenciaRobo);
        ArrayAdapter ab = new ArrayAdapter(this,android.R.layout.simple_spinner_item,violenciarobo);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        selectorViolencia.setAdapter(ab);








        ImageButton ingresar = (ImageButton) findViewById(R.id.ingresar);
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptInsert();
            }
        });

        gpsTracker = new GPSTracker(RoboActivity.this);
        setLocationAddress();



        //CHEQUEPO PERMISOS (LOCAACION=

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }



    }

    //LOCACION POR DEFECTO!!
    private void setLocationAddress() {
        if (gpsTracker.getLocation() != null) {
            if (gpsTracker.getLatitude() != 0 && gpsTracker.getLongitude() != 0) {

                latitudnum = gpsTracker.getLatitude();
                longitudnum = gpsTracker.getLongitude();
            } else {

            }
        } else {

        }
    }







    public void attemptInsert() {
        final String[] mensajebd = {""};

        try {

            APIAdapter.crearConexion().setNuevoEvento(((MyApplication) getApplicationContext()).getToken(),
                    1, latitudnum, longitudnum).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    mensajebd[0] = "Evento Enviado: La ayuda va en camino.";
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    mensajebd[0] = "Error. Intente nuevamente.";
                }
            });

            finish();
            Intent intent = new Intent(this, MenuPrincipal.class);
            startActivity(intent);
        }
        catch (Exception ex)
        {
            mensajebd[0] = ex.getMessage();
        }

        Toast mensaje = Toast.makeText(getApplicationContext(), mensajebd[0], Toast.LENGTH_SHORT);
        mensaje.show();
    }



    public void volver(View v) {
        finish();
        Intent intent = new Intent(this, MenuPrincipal.class);
        startActivity(intent);
    }

    public void perfil(View v){
        Intent intent = new Intent(this, PerfilActivity.class);
        startActivity(intent);
        finish();
    }


    public void inicio(View v){
        Intent intent = new Intent(this, MenuPrincipal.class);
        startActivity(intent);
        finish();
    }





}
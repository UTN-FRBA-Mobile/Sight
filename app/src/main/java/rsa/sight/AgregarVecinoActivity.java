package rsa.sight;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rsa.sight.api.APIAdapter;
import rsa.sight.api.deserializadores.UsuariosDeserializador;
import rsa.sight.api.modelos.Usuario;

public class AgregarVecinoActivity extends AppCompatActivity {
    private TextView bienvenida, nombrevecid, apellidovecid, id;
    private EditText idVecino;
    private String usuario, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario = ((MyApplication) this.getApplication()).getUserLogueado();
        email = ((MyApplication) this.getApplication()).getEmailLogueado();
        setContentView(R.layout.activity_agregar_vecino);
        bienvenida = (TextView) findViewById(R.id.bienvenida);
        String mensaje_bienvenida = getString(R.string.mensaje_bienvenida) + " " + usuario + " " + email;
        bienvenida.setText(mensaje_bienvenida);


       idVecino = (EditText) findViewById(R.id.idVecino);

        insertarVec ();



    }
     private void insertarVec (){


         final String[] mensajebd2 = {""};
         ImageButton agregarvecino = (ImageButton) findViewById(R.id.agregarvecino);

         agregarvecino.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 final String vecinoIngresado = idVecino.getText().toString();
                 Log.i("TAG", "The email is" + vecinoIngresado);


                 APIAdapter.crearConexion().setVecino(((MyApplication) getApplicationContext()).getToken(), vecinoIngresado).enqueue(new Callback<Void>() {
                     @Override
                     public void onResponse(Call<Void> call, Response<Void> response) {
                         mensajebd2[0] = "Vecino agregado.";
                         Toast.makeText(getApplicationContext(),"Vecino agregado",Toast.LENGTH_SHORT).show();
                         Intent intent = getIntent();
                         finish();
                         startActivity(intent);

                     }

                     @Override
                     public void onFailure(Call<Void> call, Throwable t) {
                         mensajebd2[0] = "Error. Intente nuevamente.";
                     }
                 });
             }
         });
     }












    public void volver(View v) {
        finish();
        Intent intent = new Intent(this, VecinosConfianzaActivity.class);
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








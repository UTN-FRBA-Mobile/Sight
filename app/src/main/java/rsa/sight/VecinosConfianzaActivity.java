package rsa.sight;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rsa.sight.api.APIAdapter;
import rsa.sight.api.deserializadores.UsuariosDeserializador;
import rsa.sight.api.modelos.Usuario;

public class VecinosConfianzaActivity extends AppCompatActivity {
    private TextView bienvenida, nombreid, apellidoid, usuarioid;
    private String usuario, email;


    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    EditText edtTitle;
    EditText edtMessage;
    private static final String TAG = "MainActivity";

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAPTo9RD0:APA91bH4juaWut4RQn8Gvy_UvP7l17M7dnzSKf9mWkPH1E1mQCD1HVSH1fz-SkqaKIlQho4MmNjGYE-rxpGDUUAXmLbrVnmCN_U30pIMojFuJs49mydnQQzAFtiaLXl6CEvHpX_ykT2X";
    final private String contentType = "application/json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        usuario = ((MyApplication) this.getApplication()).getUserLogueado();
        email = ((MyApplication) this.getApplication()).getEmailLogueado();
        setContentView(R.layout.activity_vecinos_confianza);
        bienvenida = (TextView) findViewById(R.id.bienvenida);
        String mensaje_bienvenida = getString(R.string.mensaje_bienvenida) + " " + usuario + " " + email;
        bienvenida.setText(mensaje_bienvenida);

        nombreid = (TextView) findViewById(R.id.nombreid);
        apellidoid = (TextView) findViewById(R.id.apellidoid);
        usuarioid = (TextView) findViewById(R.id.usuarioid);




        attemptBusquedaVecinos();

    }

    public void actualizarVecinos(ArrayList<Usuario> vecinos) {
        try {
            //DECLARO VARIABLE TABLA
            TableLayout stk = (TableLayout) findViewById(R.id.table_main);
            TableRow tbrow0 = new TableRow(this);
            TextView tv0 = new TextView(this);
            tv0.setText(" Nombre ");
            tv0.setTextColor(Color.WHITE);
            tbrow0.addView(tv0);
            TextView tv1 = new TextView(this);
            tv1.setText(" Apellido");
            tv1.setTextColor(Color.WHITE);
            tbrow0.addView(tv1);
            TextView tv2 = new TextView(this);
            tv2.setText(" Id ");
            tv2.setTextColor(Color.WHITE);
            tbrow0.addView(tv2);
            stk.addView(tbrow0);

            for (Usuario vecino : vecinos) {
                TableRow tbrow = new TableRow(this);
                TextView t1v = new TextView(this);
                t1v.setText(vecino.getNombre());
                t1v.setTextColor(Color.WHITE);
                t1v.setGravity(Gravity.CENTER);
                tbrow.addView(t1v);
                TextView t2v = new TextView(this);
                t2v.setText(vecino.getApellido());
                t2v.setTextColor(Color.WHITE);
                t2v.setGravity(Gravity.CENTER);
                tbrow.addView(t2v);
                TextView t3v = new TextView(this);
                t3v.setText(String.valueOf(vecino.getId()));
                t3v.setTextColor(Color.WHITE);
                t3v.setGravity(Gravity.CENTER);
                tbrow.addView(t3v);
                Button t4v = new Button(this);
                t4v.setText("Eliminar Vecino");
                t4v.setGravity(Gravity.CENTER);
                final Integer indexmostrar = vecino.getId();
                final String[] mensajebd2 = {""};

                t4v.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {




                        APIAdapter.crearConexion().deleteVecino(((MyApplication) getApplicationContext()).getToken(), indexmostrar).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                mensajebd2[0] = "Referencia a vecino eliminada.";
                                Toast.makeText(getApplicationContext(),"Referencia eliminada",Toast.LENGTH_SHORT).show();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                mensajebd2[0] = "Error. Intente nuevamente.";
                                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                tbrow.addView(t4v);
                stk.addView(tbrow);
            }
        } catch (Exception ex) {

        }
    }

    private void attemptBusquedaVecinos() {
        String token = ((MyApplication) this.getApplication()).getToken();

        Gson gson = (new GsonBuilder()).registerTypeAdapter(ArrayList.class, new UsuariosDeserializador()).create();
        APIAdapter.crearConexion(gson).getVecinos(token).enqueue(new Callback<ArrayList<Usuario>>() {
            @Override
            public void onResponse(Call<ArrayList<Usuario>> call, Response<ArrayList<Usuario>> response) {

                actualizarVecinos(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Usuario>> call, Throwable t) {
                Toast mensaje = Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                mensaje.show();
            }
        });
    }



    public void volver(View v) {
        finish();
        Intent intent = new Intent(this, MenuPrincipal.class);
        startActivity(intent);
    }

    public void agregarvecino(View v) {
        finish();
        Intent intent = new Intent(this, AgregarVecinoActivity.class);
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


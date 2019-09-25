package rsa.sight;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rsa.sight.api.APIAdapter;
import rsa.sight.api.deserializadores.EventosDeserializador;
import rsa.sight.api.deserializadores.UsuariosDeserializador;
import rsa.sight.api.modelos.Evento;
import rsa.sight.api.modelos.Usuario;

public class DenunciasActivity extends AppCompatActivity {
    TextView bienvenida, descripcion, usuarioid, latitud, longitud, evento;
    private String usuario, email;
    Connection con;
    String db_username, db_pass, db_name, ip_server, port;
    private GoogleApiClient googleApiClient;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        usuario = ((MyApplication) this.getApplication()).getUserLogueado();
        email = ((MyApplication) this.getApplication()).getEmailLogueado();
        setContentView(R.layout.activity_misdenuncias);
        bienvenida = (TextView) findViewById(R.id.bienvenida);
        String mensaje_bienvenida = getString(R.string.mensaje_bienvenida) + " " + usuario + " " + email;
        bienvenida.setText(mensaje_bienvenida);
        descripcion = (TextView) findViewById(R.id.descripcion);
        usuarioid = (TextView) findViewById(R.id.usuarioid);
        latitud = (TextView) findViewById(R.id.latitud);
        longitud = (TextView) findViewById(R.id.longitud);
        evento = (TextView) findViewById(R.id.eventoid);


        //CHEQUEPO PERMISOS (LOCACION)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }




        attemptBusquedaDenuncias();












    }








    private void attemptLogin() {

        boolean exito = true;
        String mensajebd = "";
        String idevento = null;
        String usuariomovilid = null;
        String latitudst = null;
        String longitudst = null;
        String descripcionst = null;


        try
        {
            // Obtiene preferencias
            SharedPreferences prefs = getSharedPreferences(getString(R.string.preferences), MODE_PRIVATE);
            ip_server = prefs.getString("ip_server", getString(R.string.ip_server));
            port = prefs.getString("port_server", getString(R.string.port_server));
            db_name = prefs.getString("database", getString(R.string.database));
            db_username = prefs.getString("username", getString(R.string.username));
            db_pass = prefs.getString("password", getString(R.string.password));

            ConexionSQL conexionSQL = new ConexionSQL();
            con = conexionSQL.connectionHelper(db_username, db_pass, db_name, ip_server, port); // Connect to database
            if (con == null)
            {
                exito = false;

                mensajebd = getString(R.string.error_conexion);
            }
            else
            {
                String usuarioIngresado = ((MyApplication) getApplication()).getUserLogueado();
                int usuarioId= Integer.parseInt(usuarioIngresado);

                Statement stmt = con.createStatement();
                String query = "SELECT dbo.EVENTO_MOBILE.evento_id, dbo.EVENTO_ADICIONALES.descripcion, dbo.EVENTO_MOBILE.usuario_mobile_ID, dbo.EVENTO_MOBILE.latitud, dbo.EVENTO_MOBILE.longitud FROM dbo.EVENTO_MOBILE, dbo.EVENTO_ADICIONALES WHERE dbo.EVENTO_MOBILE.usuario_mobile_id = (SELECT id from dbo.USUARIO_MOBILE where usuario_id = '" + usuarioId+ "') AND dbo.EVENTO_MOBILE.evento_id = dbo.evento_adicionales.evento_id ";

                // String query = "SELECT dbo.evento_adicionales.descripcion, dbo.evento_mobile FROM dbo.evento_adicionales, dbo.evento_mobile WHERE  dbo.evento_adicionales.usuario_mobile_id = '" + usuarioId + "')";
                // String query = "SELECT id, evento_id, descripcion, usuario_mobile_id FROM dbo.evento_adicionales WHERE  usuario_mobile_id = '" + usuarioId + "')";

                ResultSet rs = stmt.executeQuery(query);

                //DECLARO VARIABLE TABLA
                TableLayout stk = (TableLayout) findViewById(R.id.table_main);
                TableRow tbrow0 = new TableRow(this);
                TextView tv0 = new TextView(this);
                tv0.setText(" ID evento ");
                tv0.setTextColor(Color.WHITE);
                tbrow0.addView(tv0);
                TextView tv1 = new TextView(this);
                tv1.setText(" MobileId");
                tv1.setTextColor(Color.WHITE);
                tbrow0.addView(tv1);
                TextView tv2 = new TextView(this);
                tv2.setText(" latitud ");
                tv2.setTextColor(Color.WHITE);
                tbrow0.addView(tv2);
                TextView tv3 = new TextView(this);
                tv3.setText(" longitud ");
                tv3.setTextColor(Color.WHITE);
                tbrow0.addView(tv3);
                TextView tv4 = new TextView(this);
                tv4.setText(" descripcion");
                tv4.setTextColor(Color.WHITE);
                tbrow0.addView(tv4);

                stk.addView(tbrow0);


                while(rs.next())
                {
                    idevento = rs.getString("evento_id");
                    usuariomovilid = rs.getString("usuario_mobile_id");
                    latitudst = rs.getString("latitud");
                    longitudst = rs.getString("longitud");
                    descripcionst = rs.getString("descripcion");

                    //  descripcionst = rs.getString("descripcion");

                    exito = true;
                    if (exito) {


                      //  usuarioid.setText(usuarioid.getText().toString() + usuariomovilid + "'\n '"  );
                      //  latitud.setText(latitud.getText().toString() + latitudst + "'\n '"  );
                      //  longitud.setText(longitud.getText().toString() + longitudst + "'\n '"  );
                     //   evento.setText(evento.getText().toString() + idevento + "'\n '"  );
                     //   descripcion.setText(descripcion.getText().toString() + descripcionst + "'\n '"  );


                        TableRow tbrow = new TableRow(this);
                        TextView t1v = new TextView(this);
                        t1v.setText(idevento);
                        //for (int i = 0; i < 3; i++) {     t1v.setTextColor(Color.WHITE);
                        t1v.setTextColor(Color.WHITE);
                        t1v.setGravity(Gravity.CENTER);
                        tbrow.addView(t1v);
                        TextView t2v = new TextView(this);
                        t2v.setText(usuariomovilid);
                        t2v.setTextColor(Color.WHITE);
                        t2v.setGravity(Gravity.CENTER);
                        tbrow.addView(t2v);
                        TextView t3v = new TextView(this);
                        t3v.setText(latitudst);
                        t3v.setTextColor(Color.WHITE);
                        t3v.setGravity(Gravity.CENTER);
                        tbrow.addView(t3v);
                        TextView t4v = new TextView(this);
                        t4v.setText(longitudst);
                        t4v.setTextColor(Color.WHITE);
                        t4v.setGravity(Gravity.CENTER);
                        tbrow.addView(t4v);
                        TextView t5v = new TextView(this);
                        t5v.setText(descripcionst);
                        t5v.setTextColor(Color.WHITE);
                        t5v.setGravity(Gravity.CENTER);
                        tbrow.addView(t5v);
                        Button t6v = new Button(this);
                        t6v.setText("Ver Info");
                        t6v.setGravity(Gravity.CENTER);
                        final String indexmostrar = longitudst;
                        final String[] mensajebd2 = {""};

                        t6v.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                //Log.i("TAG", "The index is" + indexmostrar);

                                /*
                                APIAdapter.crearConexion().deleteVecino(((MyApplication) getApplicationContext()).getToken(), indexmostrar).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        mensajebd2[0] = "Evento Borrado.";
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        mensajebd2[0] = "Error. Intente nuevamente.";
                                    }
                                });

                                */
                            }
                        });
                        tbrow.addView(t6v);

                        stk.addView(tbrow);


                    }

                    else {
                        // There was an error; don't attempt login and focus the first
                        // form field with an error.
                        Toast mensaje = Toast.makeText(getApplicationContext(), mensajebd, Toast.LENGTH_SHORT);
                        mensaje.show();
                        //focusView.requestFocus();
                    }


                }

                //else
                //{
                //  mensajebd = getString(R.string.error_usuario_requerido);
                // exito = false;
                // }
            }
        }
        catch (Exception ex)
        {
            exito = false;
            mensajebd = ex.getMessage();
        }

        //  if (exito) {
        // listadenuncias.setText(idevento);
        //    listadenuncias.setText(listadenuncias.getText().toString() + idevento);

    } //else {
    // There was an error; don't attempt login and focus the first
    // form field with an error.
    //  Toast mensaje = Toast.makeText(getApplicationContext(), mensajebd, Toast.LENGTH_SHORT);
    // mensaje.show();
    //focusView.requestFocus();
    //}





    public void actualizarDenuncias(ArrayList<Evento> eventos) {
        try {
            //DECLARO VARIABLE TABLA
            TableLayout stk = (TableLayout) findViewById(R.id.table_main);
            TableRow tbrow0 = new TableRow(this);
            TextView tv0 = new TextView(this);
            tv0.setText(" Id evento");
            tv0.setTextColor(Color.WHITE);
            tbrow0.addView(tv0);
            TextView tv1 = new TextView(this);
            tv1.setText(" Timestamp");
            tv1.setTextColor(Color.WHITE);
            tbrow0.addView(tv1);
            TextView tv2 = new TextView(this);
            tv2.setText(" Latitud ");
            tv2.setTextColor(Color.WHITE);
            tbrow0.addView(tv2);
            TextView tv3 = new TextView(this);
            tv3.setText(" Longitud");
            tv3.setTextColor(Color.WHITE);
            tbrow0.addView(tv3);
            stk.addView(tbrow0);

            for (Evento evento : eventos) {
                TableRow tbrow = new TableRow(this);
                TextView t1v = new TextView(this);
                t1v.setText(String.valueOf(evento.getId()));
                t1v.setTextColor(Color.WHITE);
                t1v.setGravity(Gravity.CENTER);
                tbrow.addView(t1v);
                TextView t2v = new TextView(this);
                t2v.setText(evento.getTimestamp());
                t2v.setTextColor(Color.WHITE);
                t2v.setGravity(Gravity.CENTER);
                tbrow.addView(t2v);
                TextView t3v = new TextView(this);
                t3v.setText(String.valueOf(evento.getLatitud()));
                t3v.setTextColor(Color.WHITE);
                t3v.setGravity(Gravity.CENTER);
                tbrow.addView(t3v);
                Button t4v = new Button(this);
                t4v.setText("Eliminar Vecino");
                t4v.setGravity(Gravity.CENTER);
                final int indexmostrar = evento.getId();

                t4v.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Log.i("TAG", "The index is" + indexmostrar);
                    }
                });
                tbrow.addView(t4v);
                stk.addView(tbrow);
            }
        } catch (Exception ex) {

        }
    }

    private void attemptBusquedaDenuncias() {
        String token = ((MyApplication) this.getApplication()).getToken();

        Gson gson = (new GsonBuilder()).registerTypeAdapter(ArrayList.class, new EventosDeserializador()).create();
        APIAdapter.crearConexion(gson).getMisDenuncias(token).enqueue(new Callback<ArrayList<Evento>>() {
            @Override
            public void onResponse(Call<ArrayList<Evento>> call, Response<ArrayList<Evento>> response) {

                actualizarDenuncias(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Evento>> call, Throwable t) {
                Toast mensaje = Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                mensaje.show();
            }
        });
    }




    public void volver(View v) {
        finish();
        Intent intent = new Intent(this, PerfilActivity.class);
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

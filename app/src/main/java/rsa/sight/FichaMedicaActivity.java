package rsa.sight;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class FichaMedicaActivity extends AppCompatActivity {
    private TextView bienvenida;
    private EditText listaPadecimientos;
    private String usuario, email;
    Connection con;
    String db_username, db_pass, db_name, ip_server, port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        usuario = ((MyApplication) this.getApplication()).getUserLogueado();
        email = ((MyApplication) this.getApplication()).getEmailLogueado();
        setContentView(R.layout.activity_ficha_medica);
        bienvenida = (TextView) findViewById(R.id.bienvenida);
        String mensaje_bienvenida = getString(R.string.mensaje_bienvenida) + " " + usuario + " " + email;
        bienvenida.setText(mensaje_bienvenida);


        listaPadecimientos = (EditText) findViewById(R.id.listaPadecimientos);


        attemptLogin();


    }


    private void attemptLogin() {


        boolean exito = true;
        String mensajebd = "";
        String padecimientos = null;
        String medicamentos = null;



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








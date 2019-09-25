package rsa.sight;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class PerfilActivity extends AppCompatActivity {
    private TextView bienvenida;
    private String usuario, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        //usuario = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        //email = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE2);
        //TESTEANDO
        usuario = ((MyApplication) this.getApplication()).getUserLogueado();
        email = ((MyApplication) this.getApplication()).getEmailLogueado();
        setContentView(R.layout.activity_perfil);
        bienvenida = (TextView) findViewById(R.id.bienvenida);
        String mensaje_bienvenida = getString(R.string.mensaje_bienvenida) + " " + usuario + " " + email;
        bienvenida.setText(mensaje_bienvenida);


    }

    public void misdenuncias(View v) {
        Intent intent = new Intent(this, DenunciasActivity.class);
        startActivity(intent);
        finish();
    }

    public void fichamedica(View v) {
        Intent intent = new Intent(this, FichaMedicaActivity.class);
        startActivity(intent);
        finish();
    }

    public void vecinosconfianza(View v) {
        Intent intent = new Intent(this, VecinosConfianzaActivity.class);
        startActivity(intent);
        finish();
    }

    public void volver(View v) {
        finish();
        Intent intent = new Intent(this, MenuPrincipal.class);
        startActivity(intent);
    }

    public void logout(View v) {
        finish();
        LoginActivity.borrarDatos(getApplicationContext());
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void inicio(View v) {
        Intent intent = new Intent(this, MenuPrincipal.class);
        startActivity(intent);
        finish();
    }
}








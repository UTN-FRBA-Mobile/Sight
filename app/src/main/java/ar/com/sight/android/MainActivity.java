package ar.com.sight.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String token = Sight.getToken(this.getApplication());

        if (token != null && Sight.getUsuario(this.getApplication()) == null) {
            Sight.getMisDatos(this.getApplication());
        }

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // Start the next activity
                Intent mainIntent = new Intent().setClass(MainActivity.this, (token != null && token != "") ? MenuPrincipalActivity.class : LoginActivity.class);
                startActivity(mainIntent);

                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
        //welcome message
        showStartDialog();
    }
    private void showStartDialog(){
        new AlertDialog.Builder(this)
                .setTitle("bienvenido a Sight")
                .setMessage("Usa el boton Robo para Aletar a la policia, Emergencia para ambulancia, SOS para simular el boton bluetooth, Adicionales para añadir informacion, agrega vecinos de confianza para compartir notificaciones")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }
}

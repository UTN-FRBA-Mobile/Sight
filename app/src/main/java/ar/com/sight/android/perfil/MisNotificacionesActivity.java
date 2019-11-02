package ar.com.sight.android.perfil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ar.com.sight.android.PerfilActivity;
import ar.com.sight.android.R;
import ar.com.sight.android.Sight;
import ar.com.sight.android.api.APIAdapter;
import ar.com.sight.android.api.deserializadores.EventosDeserializador;
import ar.com.sight.android.api.modelos.Evento;
import ar.com.sight.android.comun.EncabezadoFragment;
import ar.com.sight.android.comun.EventoActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisNotificacionesActivity extends AppCompatActivity {

    TableLayout tabla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_notificaciones);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameHeader, EncabezadoFragment.newInstance(getResources().getString(R.string.tituloMisNotificaciones)));
        ft.commit();

        tabla = findViewById(R.id.tablaNotificaciones);
        getNotificaciones();
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent().setClass(MisNotificacionesActivity.this, PerfilActivity.class);
        startActivity(mainIntent);
    }

    private void showEvento(final Evento e) {
        Intent intent = new Intent().setClass(MisNotificacionesActivity.this, EventoActivity.class);
        intent.putExtra("evento_id", e.getId());
        intent.putExtra("latitud", e.getLatitud());
        intent.putExtra("longitud", e.getLongitud());
        intent.putExtra("evento_estado", e.getEvento_estado_decripcion());
        intent.putExtra("calificacion", false);
        intent.putExtra("evento_descripcion", e.getDescripcion());
        intent.putExtra("evento_fecha", e.getTimestamp());
        intent.putExtra("vecino", e.getVecino());
        startActivity(intent);
    }

    private void getNotificaciones() {
        final String token = Sight.getToken(this.getApplication());

        final Context thisContext = this;

        try
        {
            APIAdapter.crearConexion(ArrayList.class, new EventosDeserializador()).getMisNotificaciones(token).enqueue(new Callback<ArrayList<Evento>>() {
                @Override
                public void onResponse(Call<ArrayList<Evento>> call, Response<ArrayList<Evento>> response) {
                    actualizarNotificaciones(response.body(), thisContext);
                }

                @Override
                public void onFailure(Call<ArrayList<Evento>> call, Throwable t) {
                    Toast mensaje = Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                    mensaje.show();
                }
            });
        }
        catch (Exception ex) {
            Toast mensaje = Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT);
            mensaje.show();
        }
    }

    private void actualizarNotificaciones(ArrayList<Evento> notificaciones, Context thisContext) {
        if (notificaciones.size() == 0) {
            Toast mensaje = Toast.makeText(getApplicationContext(), thisContext.getString(R.string.mis_notificaciones_vacio), Toast.LENGTH_SHORT);
            mensaje.show();
            return;
        }

        for (final Evento e: notificaciones) {
            TableRow tbrow = new TableRow(thisContext);

            tbrow.setClickable(true);

            tbrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showEvento(e);
                }
            });

            TextView t1v = new TextView(thisContext);
            t1v.setText(e.getTimestamp());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                t1v.setTextAppearance(R.style.TextTableRowView);
            }
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);

            TextView t2v = new TextView(thisContext);
            t2v.setText(e.getEvento_tipo_decripcion());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                t1v.setTextAppearance(R.style.TextTableRowView);
            }
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);

            TextView t3v = new TextView(thisContext);
            t3v.setText(e.getVecino());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                t1v.setTextAppearance(R.style.TextTableRowView);
            }
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);

            tabla.addView(tbrow);
        }
    }
}

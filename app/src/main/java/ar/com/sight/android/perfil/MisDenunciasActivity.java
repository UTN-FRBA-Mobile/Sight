package ar.com.sight.android.perfil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
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

public class MisDenunciasActivity extends AppCompatActivity {

    TableLayout tabla;
    Resources res;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_denuncias);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameHeader, EncabezadoFragment.newInstance(getResources().getString(R.string.tituloMisDenuncias)));
        ft.commit();

        tabla = findViewById(R.id.tablaDenuncias);
        res = getResources();

        constraintLayout = findViewById(R.id.constrainMisDenuncias);
        getDenuncias();
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent().setClass(MisDenunciasActivity.this, PerfilActivity.class);
        startActivity(mainIntent);
    }

    private void showEvento(final Evento e) {
        Intent intent = new Intent().setClass(MisDenunciasActivity.this, EventoActivity.class);
        intent.putExtra("evento_id", e.getId());
        intent.putExtra("latitud", e.getLatitud());
        intent.putExtra("longitud", e.getLongitud());
        intent.putExtra("evento_estado", e.getEvento_estado_decripcion());
        intent.putExtra("calificacion", true);
        intent.putExtra("evento_descripcion", e.getDescripcion());
        intent.putExtra("evento_fecha", e.getTimestamp());
        intent.putExtra("vecino", e.getVecino());
        startActivity(intent);
    }

    private void getDenuncias() {
        final String token = Sight.getToken(this.getApplication());

        final Context thisContext = this;

        try
        {
            APIAdapter.crearConexion(ArrayList.class, new EventosDeserializador()).getMisDenuncias(token).enqueue(new Callback<ArrayList<Evento>>() {
                @Override
                public void onResponse(Call<ArrayList<Evento>> call, Response<ArrayList<Evento>> response) {
                    actualizarDenuncias(response.body(), thisContext);
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

    private void actualizarDenuncias(ArrayList<Evento> denuncias, Context thisContext) {
        if (denuncias.size() == 0) {
            Toast mensaje = Toast.makeText(getApplicationContext(), thisContext.getString(R.string.mis_denuncias_vacio), Toast.LENGTH_SHORT);
            mensaje.show();
            return;
        }

        for (final Evento e: denuncias) {
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
            t3v.setText(e.getEvento_estado_decripcion());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                t1v.setTextAppearance(R.style.TextTableRowView);
            }
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);

            tabla.addView(tbrow);
        }
    }
}

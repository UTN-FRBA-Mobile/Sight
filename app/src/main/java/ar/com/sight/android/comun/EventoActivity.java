package ar.com.sight.android.comun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ar.com.sight.android.R;
import ar.com.sight.android.Sight;
import ar.com.sight.android.api.APIAdapter;
import ar.com.sight.android.api.deserializadores.StringDeserializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventoActivity extends AppCompatActivity implements OnMapReadyCallback {

    Integer evento_id = -1;
    RatingBar ratingBar;
    TextView txtCalificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        evento_id = getIntent().getIntExtra("evento_id", -1);
        boolean verCalificacion = getIntent().getBooleanExtra("calificacion", false);

        ((TextView)findViewById(R.id.lblEventoDescripcion)).setText(getIntent().getStringExtra("evento_descripcion"));
        ((TextView)findViewById(R.id.lblEventoEstado)).setText(getIntent().getStringExtra("evento_estado"));
        ((TextView)findViewById(R.id.lblEventoVecino)).setText(getIntent().getStringExtra("vecino"));
        ((TextView)findViewById(R.id.lblEventoFecha)).setText(getIntent().getStringExtra("evento_fecha"));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameHeader, EncabezadoFragment.newInstance(getResources().getString(R.string.tituloEvento)));
        ft.commit();

        findViewById(R.id.layoutCalificacion).setEnabled(verCalificacion);

        ratingBar = findViewById(R.id.ratingBar);
        txtCalificacion = findViewById(R.id.txtCalificacion);

        ImageButton btnEnviarCalificacion = findViewById(R.id.btnEnviarCalificacion);
        btnEnviarCalificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    APIAdapter.crearConexion(String.class, new StringDeserializador()).setCalificar(
                            Sight.getToken(getApplication()), evento_id, (int)ratingBar.getRating(),
                            txtCalificacion.getText().toString()).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Toast msg = Toast.makeText(getApplication(), response.body(), Toast.LENGTH_SHORT);
                            msg.show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast msg = Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT);
                            msg.show();
                        }
                    });
                } catch (Exception ex) {
                    Toast msg = Toast.makeText(getApplication(), ex.getMessage(), Toast.LENGTH_SHORT);
                    msg.show();
                }
            }
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng eventoPosicion = new LatLng(getIntent().getDoubleExtra("latitud", 0),
                getIntent().getDoubleExtra("longitud", 0));
        googleMap.addMarker(new MarkerOptions().position(eventoPosicion)
                .title("Ubicación del Evento"));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(eventoPosicion).zoom(16.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.moveCamera(cameraUpdate);
    }
}

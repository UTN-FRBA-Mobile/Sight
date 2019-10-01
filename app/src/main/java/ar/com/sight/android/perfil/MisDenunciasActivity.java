package ar.com.sight.android.perfil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisDenunciasActivity extends AppCompatActivity {

    public static final float INITIAL_ZOOM = 12f;
    TableLayout tabla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_denuncias);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameHeader, EncabezadoFragment.newInstance(getResources().getString(R.string.tituloMisDenuncias)));
        ft.commit();

        tabla = findViewById(R.id.tablaDenuncias);
        getDenuncias();
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent().setClass(MisDenunciasActivity.this, PerfilActivity.class);
        startActivity(mainIntent);
    }

    private void showEvento(final Evento e) {
        //instantiate the popup.xml layout file
/*
        ConstraintLayout linearLayout1 = findViewById(R.id.denunciasLayout);
        LayoutInflater layoutInflater = (LayoutInflater) DenunciasActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.activity_evento,null);

        FloatingActionButton closePopupBtn = customView.findViewById(R.id.closePopupBtn);

        ((TextView)customView.findViewById(R.id.lblEventoEstado)).setText(e.getEvento_estado_decripcion());
        ((TextView)customView.findViewById(R.id.lblEventoFecha)).setText(e.getTimestamp());
        ((TextView)customView.findViewById(R.id.lblEventoTipo)).setText(e.getEvento_tipo_decripcion());

        ((MapView)customView.findViewById(R.id.mapView)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // Pan the camera to your home address (in this case, Google HQ).
                LatLng home = new LatLng(e.getLatitud(), e.getLongitud());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, INITIAL_ZOOM));

                // Add a ground overlay 100 meters in width to the home location.
                GroundOverlayOptions homeOverlay = new GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation))
                        .position(home, 100);

                googleMap.addGroundOverlay(homeOverlay);

                //setMapLongClick(googleMap); // Set a long click listener for the map;
                //setPoiClick(googleMap); // Set a click listener for points of interest.
                //setMapStyle(googleMap); // Set the custom map style.
                //enableMyLocation(googleMap); // Enable location tracking.
                // Enable going into StreetView by clicking on an InfoWindow from a
                // point of interest.
                //setInfoWindowClickToPanorama(googleMap);

            }
        });

        //instantiate popup window
        final PopupWindow popupWindow = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        //display the popup window
        popupWindow.showAtLocation(linearLayout1, Gravity.CENTER, 0, 0);

        //close the popup window on button click
        closePopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

 */
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

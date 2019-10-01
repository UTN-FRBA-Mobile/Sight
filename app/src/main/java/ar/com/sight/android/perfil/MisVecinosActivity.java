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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ar.com.sight.android.PerfilActivity;
import ar.com.sight.android.R;
import ar.com.sight.android.Sight;
import ar.com.sight.android.api.APIAdapter;
import ar.com.sight.android.api.deserializadores.UsuariosDeserializador;
import ar.com.sight.android.api.modelos.Usuario;
import ar.com.sight.android.comun.EncabezadoFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisVecinosActivity extends AppCompatActivity {

    TableLayout tabla;
    Resources res;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_vecinos);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameHeader, EncabezadoFragment.newInstance(getResources().getString(R.string.tituloMisVecinos)));
        ft.commit();

        res = getResources();

        constraintLayout = findViewById(R.id.constrainMisVecinos);
        tabla = findViewById(R.id.tablaVecinos);
        getVecinos();

        final Context thisContext = this;

        final String token = Sight.getToken(this.getApplication());

        findViewById(R.id.btnBuscarVecino).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APIAdapter.crearConexion().setVecino(token, ((EditText)findViewById(R.id.txtVecinoEmail)).getText().toString()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(thisContext,"Vecino Agregado", Toast.LENGTH_SHORT).show();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Hubo un error al agregar al vecino o no existe un vecino con ese email.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent().setClass(MisVecinosActivity.this, PerfilActivity.class);
        startActivity(mainIntent);
    }


    private void getVecinos() {

        final String token = Sight.getToken(this.getApplication());

        final Context thisContext = this;

        try
        {
            APIAdapter.crearConexion(ArrayList.class, new UsuariosDeserializador()).getVecinos(token).enqueue(new Callback<ArrayList<Usuario>>() {
                @Override
                public void onResponse(Call<ArrayList<Usuario>> call, Response<ArrayList<Usuario>> response) {
                    actualizarVecinos(response.body(), thisContext, token);
                }

                @Override
                public void onFailure(Call<ArrayList<Usuario>> call, Throwable t) {
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

    private void actualizarVecinos(ArrayList<Usuario> vecinos, final Context thisContext, final String token) {

        LinearLayout.LayoutParams params_row = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params_row.gravity = Gravity.CENTER_VERTICAL;
/*
        ConstraintLayout.LayoutParams params_button = new ConstraintLayout.LayoutParams(thisContext, null);
        params_button.verticalBias= (float)0.5;
        params_button.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        params_button.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
*/

        for (final Usuario e: vecinos) {
            TableRow tbrow = new TableRow(thisContext);
            tbrow.setLayoutParams(params_row);

            TextView t1v = new TextView(thisContext);
            t1v.setText(e.getNombre());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                t1v.setTextAppearance(R.style.TextTableRowView);
            }
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);

            TextView t2v = new TextView(thisContext);
            t2v.setText(e.getApellido());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                t2v.setTextAppearance(R.style.TextTableRowView);
            }
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);

            ImageButton t3v = new ImageButton(thisContext);
            t3v.setImageDrawable(res.getDrawable(R.drawable.boton_borrar));
            t3v.setBackgroundColor(Color.TRANSPARENT);
            ConstraintSet set = new ConstraintSet();
            set.setVerticalBias(t3v.getId(), (float) 0.5);
            set.addToVerticalChain(t3v.getId(), tbrow.getId(), tbrow.getId());
            set.applyTo(constraintLayout);
            //t3v.setLayoutParams(params_button);
            t3v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    APIAdapter.crearConexion().deleteVecino(token, e.getId()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Toast.makeText(thisContext,"Referencia eliminada",Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            tbrow.addView(t3v);
            tabla.addView(tbrow);
        }
    }
}
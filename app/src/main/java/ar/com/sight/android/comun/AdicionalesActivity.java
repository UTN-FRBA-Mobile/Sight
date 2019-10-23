package ar.com.sight.android.comun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import ar.com.sight.android.ConfiguracionActivity;
import ar.com.sight.android.MenuPrincipalActivity;
import ar.com.sight.android.R;
import ar.com.sight.android.Sight;
import ar.com.sight.android.configuracion.BotonBluetoothActivity;
import ar.com.sight.android.configuracion.CambiarContraseniaActivity;

public class AdicionalesActivity extends AppCompatActivity {
    private static final int REQUEST_TAKE_PHOTO = 200;
    private static final int REQUEST_FOTO_PERMISSION = 200;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_FOTO_PERMISSION) {
                fotoIntent();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_permiso_camara), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent().setClass(AdicionalesActivity.this, MenuPrincipalActivity.class);
        startActivity(mainIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionales);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameHeader, EncabezadoFragment.newInstance(getResources().getString(R.string.tituloAdicionales)));
        ft.commit();

        findViewById(R.id.btnEnviarMensaje).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensaje = ((EditText)findViewById(R.id.txtMensajeAdicional)).getText().toString();

                if (mensaje != "") {
                    Sight.sendMesnajeAdicional(getApplicationContext(), mensaje);
                    return;
                }
                Toast.makeText(getApplicationContext(), R.string.error_mensaje_adicional_vacio, Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnTomarFoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) ||
                        (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    requestPermissions(new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FOTO_PERMISSION);
                }

                fotoIntent();
            }
        });
    }

    String currentPhotoPath;

    private void fotoIntent() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File newFile = File.createTempFile(timeStamp, ".jpg", storageDir);
            currentPhotoPath = newFile.getAbsolutePath();
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, "ar.com.sight.android.fileprovider", new File(currentPhotoPath)));
            // Start the camera activity with the request code and waiting for the app process result.
            startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);

        }catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Intent mIntent = new Intent(getApplicationContext(), FileUploadService.class);
            mIntent.putExtra("mFilePath", currentPhotoPath);
            FileUploadService.enqueueWork(getApplicationContext(), mIntent);
            //Sight.sendImagenAdicional(getApplicationContext(), currentPhotoPath);
        }
    }
}

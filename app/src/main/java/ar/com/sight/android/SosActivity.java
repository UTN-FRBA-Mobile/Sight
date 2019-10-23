package ar.com.sight.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.location.Location;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import ar.com.sight.android.comun.EncabezadoFragment;
import ar.com.sight.android.comun.FileUploadService;
import ar.com.sight.android.comun.Gps;

public class SosActivity extends AppCompatActivity  implements SurfaceHolder.Callback {
    private GoogleMap mMap;
    private Marker myMarker;
    private boolean cancelar = false;

    private MediaRecorder recorder;
    private SurfaceHolder holder;
    private CamcorderProfile camcorderProfile;
    private Camera camera;

    boolean recording = false;
    boolean previewRunning = false;

    private Context thisContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);

        setContentView(R.layout.activity_sos);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameHeader, EncabezadoFragment.newInstance(getResources().getString(R.string.tituloSos)));
        ft.commit();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        SurfaceView cameraView = findViewById(R.id.surfaceView);
        holder = cameraView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        thisContext = this;
        final Button btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelar = true;
               // Intent mainIntent = new Intent().setClass(SosActivity.this, SosActivity.class);
              //  startActivity(mainIntent);
            }
        });

        final Timer timer = new Timer();

        final TimerTask task = new TimerTask() {
            int contador = 5;

            @Override
            public void run() {
                if (cancelar) {
                    timer.cancel();
                } else {
                    if (contador <= 0) {
                        Sight.sendEvento(thisContext, 3);
                        btnCancelar.setText("Evento Enviado");
                        cancelar = true;
                    } else {
                        contador--;
                        btnCancelar.setText(String.format("Enviar evento en %s segundos", contador));
                    }
                }
            }
        };

        timer.scheduleAtFixedRate(task, 1000, 1000);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                try {
                    mMap = googleMap;
                    mMap.setMyLocationEnabled(true);

                    Location loc = Gps.getLocation();
                    CameraPosition camPos = new CameraPosition.Builder()
                            .target(new LatLng(loc.getLatitude(), loc.getLongitude()))
                            .zoom(18)
                            .bearing(loc.getBearing())
                            .build();
                    CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
                    googleMap.animateCamera(camUpd3);

                    LatLng position = myMarker.getPosition();
                    Toast.makeText(
                            SosActivity.this,
                            "Lat " + position.latitude + " " + "Long " + position.longitude,
                            Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                }
            }
        });
    }

    String filename = "";





    private void prepararGrabacion() {
        recorder = new MediaRecorder();
        recorder.setPreviewDisplay(holder.getSurface());

        try {
            camera.unlock();
        } catch (Exception ex) {
        }

        recorder.setCamera(camera);

        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        recorder.setProfile(camcorderProfile);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // This is all very sloppy
        if (camcorderProfile.fileFormat == MediaRecorder.OutputFormat.THREE_GPP) {
            try {
                File newFile = File.createTempFile(timeStamp, ".3gp", storageDir);
                filename = newFile.getAbsolutePath();
                recorder.setOutputFile(filename);
            } catch (IOException e) {
            }
        } else if (camcorderProfile.fileFormat == MediaRecorder.OutputFormat.MPEG_4) {
            try {
                File newFile = File.createTempFile(timeStamp, ".mp4", storageDir);
                filename = newFile.getAbsolutePath();
                recorder.setOutputFile(filename);
            } catch (IOException e) {
            }
        } else {
            try {
                File newFile = File.createTempFile(timeStamp, ".mp4", storageDir);
                filename = newFile.getAbsolutePath();
                recorder.setOutputFile(filename);
            } catch (IOException e) {
            }
        }

        final Timer timer = new Timer();

        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent mIntent = new Intent(thisContext, FileUploadService.class);
                mIntent.putExtra("mFilePath", filename);
                FileUploadService.enqueueWork(thisContext, mIntent);

                //Sight.sendVideoAdicional(thisContext, filename);

                timer.cancel();
            }
        };

        timer.schedule(task, 4000);

        recorder.setMaxDuration(3000); // 10 seconds

        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
        } catch (Exception e) {
        }
        recording = true;
        recorder.start();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
            camera.setDisplayOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            camera.setPreviewDisplay(holder);
            camera.startPreview();
            previewRunning = true;
        } catch (Exception e) {
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            if (!recording) {
                if (previewRunning) {
                    camera.stopPreview();
                }

                try {
                    Camera.Parameters p = camera.getParameters();

                    p.setPreviewSize(camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
                    p.setPreviewFrameRate(camcorderProfile.videoFrameRate);

                    camera.setParameters(p);

                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                    previewRunning = true;
                } catch (IOException e) {
                }

                prepararGrabacion();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            if (recording) {
                recorder.stop();
                recording = false;
            }
            recorder.release();
            previewRunning = false;
            camera.release();
        } catch (Exception e) {
        }
    }
}
package ar.com.sight.android.comun;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.io.File;
import java.time.Instant;

import ar.com.sight.android.Sight;

public class FileUploadService  extends JobIntentService {

    private static final int JOB_ID = 102;

    private static int milisegundos = 5000;

    public static void enqueueWork(Context context, Intent intent, int milisegundos) {
        FileUploadService.milisegundos = milisegundos;
        enqueueWork(context, FileUploadService.class, JOB_ID, intent);
    }

    public static void enqueueWork(Context context, Intent intent) {
        FileUploadService.milisegundos = 5000;
        enqueueWork(context, FileUploadService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String mFilePath = intent.getStringExtra("mFilePath");
        Log.d("File Upload Service", "onHandleWork: " + mFilePath);
        if (mFilePath == null) {
            Log.e("File Upload Service", "onHandleWork: Invalid file URI");
            return;
        }

        if (mFilePath.contains(".jpg")) {
            Sight.sendImagenAdicional(this, mFilePath);
        } else {
            File fs = new File(mFilePath);
            int counter = 0;

            while (Instant.now().toEpochMilli() - fs.lastModified() < milisegundos && counter < 10) {
                counter++;
                Log.d("File Upload Service", "Last Modified: " + fs.lastModified());
                Log.d("File Upload Service", "Counter: " + counter);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Log.d("File Upload Service", "Send Last Modified: " + (Instant.now().toEpochMilli() - fs.lastModified()));
            Sight.sendVideoAdicional(this, mFilePath);
        }
    }
}
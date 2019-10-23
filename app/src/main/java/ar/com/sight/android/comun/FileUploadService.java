package ar.com.sight.android.comun;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import ar.com.sight.android.Sight;

public class FileUploadService  extends JobIntentService {

    private static final int JOB_ID = 102;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, FileUploadService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String mFilePath = intent.getStringExtra("mFilePath");
        if (mFilePath == null) {
            Log.e("File Upload Service", "onHandleWork: Invalid file URI");
            return;
        }

        if (mFilePath.contains(".jpg")) {
            Sight.sendImagenAdicional(this, mFilePath);
        } else {
            Sight.sendVideoAdicional(this, mFilePath);
        }
    }
}
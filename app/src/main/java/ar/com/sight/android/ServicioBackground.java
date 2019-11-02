package ar.com.sight.android;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import ar.com.sight.android.recibidores.RecibeBluetooth;

public class ServicioBackground  extends Service {
    public static final String CHANNEL_ID = "CanalServicioSight";
    private RecibeBluetooth receiver = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Servicio Sight")
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(getColor(R.color.colorNotificacion))
                .setContentIntent(pendingIntent)
                .setSound(null)
                .build();

        startForeground(1, notification);

        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Servicio Sight",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Create an IntentFilter instance.
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);

        BluetoothAdapter.getDefaultAdapter().disable();
        BluetoothAdapter.getDefaultAdapter().enable();

        // Create a network change broadcast receiver.
        receiver = new RecibeBluetooth();

        // Register the broadcast receiver with the intent filter object.
        registerReceiver(receiver, intentFilter);

        Log.d("test", "Service onCreate: screenOnOffReceiver is registered.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unregister screenOnOffReceiver when destroy.
        if(receiver!=null)
        {
            unregisterReceiver(receiver);
            Log.d("Sight", "Service onDestroy: screenOnOffReceiver is unregistered.");
        }
    }
}

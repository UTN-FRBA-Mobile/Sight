package ar.com.sight.android.recibidores;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import ar.com.sight.android.ServicioBackground;
import ar.com.sight.android.Sight;

public class RecibeBootEvent extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (BluetoothAdapter.getDefaultAdapter() != null) {
            Intent backgroundService = new Intent(context, ServicioBackground.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(backgroundService);
            else
                context.startService(backgroundService);
        }
    }
}
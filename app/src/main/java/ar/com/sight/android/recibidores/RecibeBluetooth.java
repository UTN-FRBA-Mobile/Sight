package ar.com.sight.android.recibidores;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;

import ar.com.sight.android.Sight;
import ar.com.sight.android.SosActivity;

public class RecibeBluetooth extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            if (device.getAddress().equals(Sight.getBluetooth(context))) {

                PowerManager mPowerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "sigh:wakeup");
                if(mWakeLock.isHeld()) {
                    mWakeLock.release();
                }

                mWakeLock.acquire();

                Intent i = new Intent(context, SosActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                context.startActivity(i);

                BluetoothAdapter.getDefaultAdapter().disable();
                BluetoothAdapter.getDefaultAdapter().enable();
            }
        }
    }
}

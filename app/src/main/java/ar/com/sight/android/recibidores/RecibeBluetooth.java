package ar.com.sight.android.recibidores;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;

import ar.com.sight.android.Sight;
import ar.com.sight.android.SosActivity;

public class RecibeBluetooth extends BroadcastReceiver {

    static boolean ignorarConexion = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
/*
        if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action) && !ignorarConexion) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            if(state == BluetoothAdapter.STATE_ON) {
                // Create an IntentFilter instance.
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
                intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
                intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);

                // Create a network change broadcast receiver.
                RecibeBluetooth receiver = new RecibeBluetooth();

                // Register the broadcast receiver with the intent filter object.
                context.registerReceiver(receiver, intentFilter);
            }
        }
*/
        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
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
                i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                context.startActivity(i);

                ignorarConexion = true;
                BluetoothAdapter.getDefaultAdapter().disable();
                BluetoothAdapter.getDefaultAdapter().enable();
                ignorarConexion = false;
            }
        }
    }
}

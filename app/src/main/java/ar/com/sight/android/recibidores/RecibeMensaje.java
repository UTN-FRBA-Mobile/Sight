package ar.com.sight.android.recibidores;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ar.com.sight.android.R;
import ar.com.sight.android.api.modelos.Evento;
import ar.com.sight.android.comun.EventoActivity;
import ar.com.sight.android.perfil.MisNotificacionesActivity;

public class RecibeMensaje extends FirebaseMessagingService {

    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    private static final String TAG = "MyFirebaseMsgService";

    private void showEvento(final Evento e, String title, String text) {
        Intent intent = new Intent(getApplicationContext(), EventoActivity.class);
        intent.putExtra("evento_id", e.getId());
        intent.putExtra("latitud", e.getLatitud());
        intent.putExtra("longitud", e.getLongitud());
        intent.putExtra("evento_estado", e.getEvento_estado_decripcion());
        intent.putExtra("calificacion", false);
        intent.putExtra("evento_descripcion", e.getDescripcion());
        intent.putExtra("evento_fecha", e.getTimestamp());
        intent.putExtra("vecino", e.getVecino());
        intent.addCategory(Intent. CATEGORY_LAUNCHER ) ;
        intent.setAction(Intent. ACTION_MAIN ) ;
        intent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
        PendingIntent resultIntent = PendingIntent. getActivity (getApplicationContext() , 0 , intent , 0 ) ;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext() ,
                default_notification_channel_id )
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(getColor(R.color.colorNotificacion))
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setContentIntent(resultIntent) ;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context. NOTIFICATION_SERVICE ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new
                    NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis () ,
                mBuilder.build()) ;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Evento e = new Evento();
        e.setLatitud(Double.valueOf(remoteMessage.getData().get("latitud")));
        e.setLongitud(Double.valueOf(remoteMessage.getData().get("longitud")));
        e.setTimestamp(remoteMessage.getData().get("timestamp"));
        e.setEvento_tipo_id(Integer.valueOf(remoteMessage.getData().get("evento_tipo_id")));
        e.setId(Integer.valueOf(remoteMessage.getData().get("id")));
        e.setEvento_estado_id(Integer.valueOf(remoteMessage.getData().get("evento_estado_id")));
        e.setEvento_estado_decripcion(remoteMessage.getData().get("evento_estado"));
        e.setEvento_tipo_decripcion(remoteMessage.getData().get("evento_tipo"));
        e.setDireccion(remoteMessage.getData().get("direccion"));
        e.setVecino(remoteMessage.getData().get("vecino"));
        showEvento(e, remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }
}

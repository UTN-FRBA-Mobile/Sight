package ar.com.sight.android;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
/* import ar.com.sight.android.LoginActivity; If we have time, check the login first (or not we have to talk about it) */

/**
 * Implementation of App Widget functionality.
 */
public class Sight_Widget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, SosActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.sight__widget);
            views.setOnClickPendingIntent(R.id.widget_btnSos, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}

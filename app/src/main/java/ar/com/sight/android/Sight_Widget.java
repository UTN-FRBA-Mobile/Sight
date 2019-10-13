package ar.com.sight.android;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class Sight_Widget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Intent sos= new Intent(context, SosActivity.class);
            PendingIntent pendingSos= PendingIntent.getActivity(context, 0, sos, 0);
            RemoteViews view= new RemoteViews(context.getPackageName(), R.layout.sight__widget);
            view.setOnClickPendingIntent(R.id.widget_btnSos, pendingSos);
            appWidgetManager.updateAppWidget(appWidgetId, view);

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


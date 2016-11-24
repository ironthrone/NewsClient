package com.guo.news.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.guo.news.PreferenceConstant;
import com.guo.news.R;

/**
 * Created by Administrator on 2016/11/13.
 */
public class NewsListWidgetProvider extends AppWidgetProvider {
    private static final String TAG = NewsListWidgetProvider.class.getSimpleName();

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        Log.d(TAG, "onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PreferenceConstant.MAIN_PREFERENCE, Context.MODE_PRIVATE);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for(int i = 0; i < appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.news_app_widget);

            Intent intent = new Intent(context, SelectWidgetSectionActivity.class);
            PendingIntent setPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.setting,setPendingIntent);

            String sectionId = sharedPreferences.getString(PreferenceConstant.KEY_APP_WIDGET_SECTION, PreferenceConstant.APP_WIDGET_SECTION_DEFAULT);
            remoteViews.setTextViewText(R.id.section_id,sectionId);

            Intent adapterIntent = new Intent(context,NewsListRemoteViewService.class);
            remoteViews.setRemoteAdapter(R.id.news_list,adapterIntent);

            remoteViews.setEmptyView(R.id.news_list,R.id.empty);

            appWidgetManager.updateAppWidget(appWidgetId,remoteViews);
        }

    }
}

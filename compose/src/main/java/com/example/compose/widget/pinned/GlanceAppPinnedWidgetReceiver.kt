package com.example.compose.widget.pinned

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class GlanceAppPinnedWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = GlanceAppPinnedWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        println("AAA GlanceAppPinnedWidgetReceiver onReceive $intent, ${intent.extras}")
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        println(
            "AAA GlanceAppPinnedWidgetReceiver onAppWidgetOptionsChanged() called with: context = $context, " +
                    "appWidgetManager = $appWidgetManager, " +
                    "appWidgetId = $appWidgetId, " +
                    "newOptions = $newOptions"
        )
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        println(
            "AAA GlanceAppPinnedWidgetReceiver called onUpdate with: context = $context, " +
                    "appWidgetManager = $appWidgetManager, " +
                    "appWidgetIds = ${appWidgetIds.contentToString()}"
        )
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        println("AAA GlanceAppPinnedWidgetReceiver called onEnabled")
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        println("AAA GlanceAppPinnedWidgetReceiver called onDisabled")
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
        println("AAA GlanceAppPinnedWidgetReceiver called onRestored")
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        println("AAA GlanceAppPinnedWidgetReceiver called onDeleted")
    }
}

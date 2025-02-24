package com.example.compose.widget.configurable

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class GlanceAppConfigurableWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = GlanceConfigurableWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        println("AAA GlanceAppConfigurableWidgetReceiver onReceive $intent")
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        println(
            "AAA GlanceAppConfigurableWidgetReceiver onAppWidgetOptionsChanged() called with: context = $context, " +
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
            "AAA GlanceAppConfigurableWidgetReceiver called onUpdate with: context = $context, " +
                    "appWidgetManager = $appWidgetManager, " +
                    "appWidgetIds = ${appWidgetIds.contentToString()}"
        )
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        println("AAA GlanceAppConfigurableWidgetReceiver called onEnabled")
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        println("AAA GlanceAppConfigurableWidgetReceiver called onDisabled")
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
        println("AAA GlanceAppConfigurableWidgetReceiver called onRestored")
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        println("AAA GlanceAppConfigurableWidgetReceiver called onDeleted")
    }
}

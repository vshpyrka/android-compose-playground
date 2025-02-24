package com.example.compose.widget.resizable

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class GlanceAppResizableWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = GlanceAppResizableWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        println("AAA GlanceAppResizableWidgetReceiver onReceive $intent")
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        println(
            "AAA GlanceAppResizableWidgetReceiver onAppWidgetOptionsChanged() called with: context = $context, " +
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
            "AAA GlanceAppResizableWidgetReceiver called onUpdate with: context = $context, " +
                    "appWidgetManager = $appWidgetManager, " +
                    "appWidgetIds = ${appWidgetIds.contentToString()}"
        )
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        println("AAA GlanceAppResizableWidgetReceiver called onEnabled")
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        println("AAA GlanceAppResizableWidgetReceiver called onDisabled")
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
        println("AAA GlanceAppResizableWidgetReceiver called onRestored")
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        println("AAA GlanceAppResizableWidgetReceiver called onDeleted")
    }
}

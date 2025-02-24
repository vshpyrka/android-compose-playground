package com.example.compose.widget.simple

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.example.compose.widget.tiny.GlanceComposeAppWidget

class GlanceAppWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = GlanceComposeAppWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        println("AAA onReceive $intent")
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        println(
            "AAA onAppWidgetOptionsChanged() called with: context = $context, " +
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
            "AAA GlanceComposeAppWidgetReceiver called onUpdate with: context = $context, " +
                    "appWidgetManager = $appWidgetManager, " +
                    "appWidgetIds = ${appWidgetIds.contentToString()}"
        )
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        println("AAA GlanceComposeAppWidgetReceiver called onEnabled")
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        println("AAA GlanceComposeAppWidgetReceiver called onDisabled")
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
        println("AAA GlanceComposeAppWidgetReceiver called onRestored")
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        println("AAA GlanceComposeAppWidgetReceiver called onDeleted")
    }
}

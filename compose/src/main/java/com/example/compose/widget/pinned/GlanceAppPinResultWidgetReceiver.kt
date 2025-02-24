package com.example.compose.widget.pinned

import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.compose.R

const val KEY_BUNDLE_ARGUMENT = "BUNDLE_ARGUMENT"

class GlanceAppPinResultWidgetReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val widgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID, -1)
        val extraArgument = intent.getStringExtra(KEY_BUNDLE_ARGUMENT)
        println("AAA GlanceAppPinResultWidgetReceiver onReceive ${intent.action} ${intent.extras}")
        println("AAA GlanceAppPinResultWidgetReceiver widgetId=$widgetId, extraArgument=$extraArgument")

        val state = GlanceAppPinnedWidgetState(
            appWidgetId = widgetId,
            userName = "Jane Doe",
            userIcon = R.drawable.ic_parrot,
        )
        GlancePinnedWidgetRepository.addWidgetState(state)
    }
}

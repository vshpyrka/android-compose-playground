package com.example.compose.widget.error

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import com.example.compose.R

class GlanceComposeErrorAppWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceComposeErrorAppWidgetContent()
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        println("AAA GlanceComposeAppWidget is Deleted")
    }

    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable,
    ) {
//        val widgetId = GlanceAppWidgetManager(context).getAppWidgetId(glanceId)
        val rv = RemoteViews(context.packageName, R.layout.glance_app_error_layout)
        rv.setTextViewText(
            R.id.error_text_view,
            "Error was thrown. \nThis is a custom view \nError Message: `${throwable.message}`"
        )
        rv.setOnClickPendingIntent(R.id.error_icon, getErrorIntent(context))
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, rv)
    }

    private fun getErrorIntent(context: Context): PendingIntent {
        return PendingIntent.getActivity(
            context,
            1000,
            Intent(context, GlanceWidgetErrorActivity::class.java).apply {
                setAction("com.example.compose.widget.action.APPWIDGET_ERROR")
            },
            PendingIntent.FLAG_IMMUTABLE
        )
//        return PendingIntent.getBroadcast(
//            context,
//            1000,
//            Intent(context, GlanceComposeAppWidgetReceiver::class.java).apply {
//                setAction("com.example.compose.widget.action.APPWIDGET_ERROR")
//            },
//            PendingIntent.FLAG_IMMUTABLE
//        )
    }

}

@Composable
fun GlanceComposeErrorAppWidgetContent() {
     // Throw exception to call onCompositionError() method and display a different error content
     throw IllegalStateException()
}

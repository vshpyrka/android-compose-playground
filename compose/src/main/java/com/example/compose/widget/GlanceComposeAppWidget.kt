package com.example.compose.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.text.Text
import com.example.compose.ui.theme.AndroidPlaygroundTheme

class GlanceComposeAppWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            AndroidPlaygroundTheme {
                Text("App Widget")
//                actionStartActivity()
            }
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
    }
}


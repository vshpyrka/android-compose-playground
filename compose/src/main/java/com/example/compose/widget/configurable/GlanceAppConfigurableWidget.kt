package com.example.compose.widget.configurable

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.wrapContentHeight
import com.example.compose.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

class GlanceConfigurableWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        coroutineScope {
            val images = GlanceConfigurableWidgetRepository.state
                .shareIn(this@coroutineScope, SharingStarted.Eagerly, 1)
            provideContent {
                val image by images.collectAsState(R.drawable.ic_parrot)
                GlanceConfigurableWidgetContent(image)
            }
        }
    }

    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable
    ) {
        super.onCompositionError(context, glanceId, appWidgetId, throwable)
        println("AAA GlanceConfigurableWidget onCompositionError $throwable")
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        println("AAA GlanceConfigurableWidget is Deleted")
    }
}

@Composable
fun GlanceConfigurableWidgetContent(resource: Int) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground()
            .padding(bottom = 8.dp)
    ) {
        Image(
            modifier = GlanceModifier
                .fillMaxWidth()
                .wrapContentHeight()
                .defaultWeight()
                .cornerRadius(16.dp),
            provider = ImageProvider(resource),
            contentDescription = null,
        )
    }
}

package com.example.compose.widget.tiny

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.ContentScale
import com.example.compose.R

class GlanceAppTinyWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceAppTinyWidgetContent()
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        println("AAA GlanceAppTinyWidget is Deleted")
    }
}

@Composable
private fun GlanceAppTinyWidgetContent() {
    Image(
        modifier = GlanceModifier.cornerRadius(4.dp),
        provider = ImageProvider(R.drawable.ic_parrot),
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
}

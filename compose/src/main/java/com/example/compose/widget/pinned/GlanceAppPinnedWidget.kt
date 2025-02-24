package com.example.compose.widget.pinned

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalState
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.wrapContentSize
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.compose.R

class GlanceAppPinnedWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val glanceAppWidgetManager = GlanceAppWidgetManager(context)
            val appWidgetId = glanceAppWidgetManager.getAppWidgetId(id)
            println("AAA state appWidgetId=$appWidgetId")

            val widgetPreview = LocalState.current as? GlanceAppPinnedWidgetState
            if (widgetPreview != null) {
                GlanceAppPinnedWidgetContent(state = widgetPreview)
            } else {
                val widgetState by GlancePinnedWidgetRepository
                    .getAppWidgetState(appWidgetId)
                    .collectAsState(WidgetState.Loading)
                when (widgetState) {
                    is WidgetState.Complete -> {
                        GlanceAppPinnedWidgetContent(state = (widgetState as WidgetState.Complete).state)
                    }

                    is WidgetState.Loading -> {
                        Box(
                            modifier = GlanceModifier
                                .fillMaxSize()
                                .wrapContentSize()
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        println("AAA GlanceComposeAppWidget is Deleted")
    }
}

@Composable
fun GlanceAppPinnedWidgetContent(state: GlanceAppPinnedWidgetState) {
    GlanceTheme {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.widgetBackground)
                .cornerRadius(16.dp)
                .clickable(actionStartActivity<GlancePinnedWidgetActivity>())
        ) {
            Image(
                provider = ImageProvider(state.userIcon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(
                        imageProvider = ImageProvider(R.drawable.bg_widget_gradient)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = state.userName,
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = ColorProvider(Color.White)
                    )
                )
            }
            Column(
                modifier = GlanceModifier.fillMaxSize()
            ) {
                Spacer(
                    modifier = GlanceModifier.defaultWeight()
                )
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = GlanceModifier.padding(4.dp)
                    ) {
                        CircleIconButton(
                            imageProvider = ImageProvider(R.drawable.ic_call),
                            contentDescription = null,
                            backgroundColor = GlanceTheme.colors.secondaryContainer,
                            contentColor = GlanceTheme.colors.onSecondaryContainer,
                            onClick = {},
                        )
                    }
                    Box(
                        modifier = GlanceModifier.padding(4.dp)
                    ) {
                        CircleIconButton(
                            imageProvider = ImageProvider(R.drawable.ic_message),
                            contentDescription = null,
                            backgroundColor = GlanceTheme.colors.secondaryContainer,
                            contentColor = GlanceTheme.colors.onSecondaryContainer,
                            onClick = {},
                        )
                    }
                }
            }
        }
    }
}

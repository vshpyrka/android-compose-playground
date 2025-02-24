package com.example.compose.widget.simple

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.glance.appwidget.compose
import androidx.lifecycle.lifecycleScope
import com.example.compose.widget.error.GlanceAppComposeErrorWidgetReceiver
import com.example.compose.widget.error.GlanceComposeErrorAppWidget
import kotlinx.coroutines.launch

class GlanceWidgetActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    Column {
                        Button(
                            onClick = {
                                updateWidget()
                            }
                        ) {
                            Text("Update Widget")
                        }
                    }
                }
            }
        }
    }

    private fun updateWidget() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            lifecycleScope.launch {
                AppWidgetManager.getInstance(this@GlanceWidgetActivity)
                    .setWidgetPreview(
                        ComponentName(
                            this@GlanceWidgetActivity,
                            GlanceAppComposeErrorWidgetReceiver::class.java
                        ),
                        AppWidgetProviderInfo.WIDGET_CATEGORY_HOME_SCREEN,
                        GlanceComposeErrorAppWidget().compose(
                            context = this@GlanceWidgetActivity
                        ),
                    )
            }
        }
    }
}

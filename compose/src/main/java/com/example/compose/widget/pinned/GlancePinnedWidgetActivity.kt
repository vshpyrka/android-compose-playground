package com.example.compose.widget.pinned

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.lifecycleScope
import com.example.compose.R
import kotlinx.coroutines.launch

class GlancePinnedWidgetActivity : ComponentActivity() {

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
                    Button(
                        onClick = {
                            addWidget()
                        }
                    ) {
                        Text("Add New Widget To Home")
                    }
                }
            }
        }
    }

    private fun addWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        if (appWidgetManager.isRequestPinAppWidgetSupported) {
            lifecycleScope.launch {
                // Create the PendingIntent object only if your app needs to be notified
                // when the user chooses to pin the widget. Note that if the pinning
                // operation fails, your app isn't notified. This callback receives the ID
                // of the newly pinned widget (EXTRA_APPWIDGET_ID).
                val intent = Intent(
                    this@GlancePinnedWidgetActivity,
                    GlanceAppPinResultWidgetReceiver::class.java,
                ).apply {
                    putExtra(KEY_BUNDLE_ARGUMENT, "widget attribute")
                }
                val successCallback = PendingIntent.getBroadcast(
                    /* context = */ this@GlancePinnedWidgetActivity,
                    /* requestCode = */ 1000,
                    /* intent = */ intent,
                    /* flags = */ PendingIntent.FLAG_MUTABLE,
                )

                val widgetState = GlanceAppPinnedWidgetState(
                    appWidgetId = 0,
                    userName = "Jane Doe",
                    userIcon = R.drawable.ic_parrot,
                )

                val isRequestedSuccessfully =
                    GlanceAppWidgetManager(this@GlancePinnedWidgetActivity)
                        .requestPinGlanceAppWidget(
                            receiver = GlanceAppPinnedWidgetReceiver::class.java,
                            preview = GlanceAppPinnedWidget(),
                            previewState = widgetState,
                            successCallback = successCallback,
                        )
                println("AAA isRequestedSuccessfully=$isRequestedSuccessfully")
            }
        }
    }
}

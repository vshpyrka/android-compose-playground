package com.example.compose.widget.pinned

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.example.compose.R
import kotlinx.coroutines.launch

class GlancePinnedWidgetConfigActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID,
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_CANCELED, resultValue)

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }


        setContent {
            Scaffold { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {

                    Column {
                        Text(text = "Configure your widget here")
                        Image(
                            modifier = Modifier
                                .size(200.dp)
                                .clickable {
                                    GlancePinnedWidgetRepository.addWidgetState(
                                        GlanceAppPinnedWidgetState(
                                            appWidgetId = appWidgetId,
                                            userName = "Jane Doe",
                                            userIcon = R.drawable.ic_parrot
                                        )
                                    )
                                },
                            painter = painterResource(R.drawable.ic_parrot),
                            contentDescription = null,
                        )
                        Image(
                            modifier = Modifier
                                .size(200.dp)
                                .clickable {
                                    GlancePinnedWidgetRepository.addWidgetState(
                                        GlanceAppPinnedWidgetState(
                                            appWidgetId = appWidgetId,
                                            userName = "John Doe",
                                            userIcon = R.drawable.ic_sheep
                                        )
                                    )
                                },
                            painter = painterResource(R.drawable.ic_sheep),
                            contentDescription = null,
                        )
                        val scope = rememberCoroutineScope()
                        Button(
                            onClick = {
                                scope.launch {
                                    updateWidget(appWidgetId)
                                }
                            }
                        ) {
                            Text("Apply changes to widget")
                        }
                    }
                }
            }
        }
    }

    private suspend fun updateWidget(appWidgetId: Int) {
        val glanceId = GlanceAppWidgetManager(this@GlancePinnedWidgetConfigActivity)
            .getGlanceIdBy(appWidgetId)
        GlanceAppPinnedWidget().update(
            context = this@GlancePinnedWidgetConfigActivity,
            id = glanceId,
        )
        val value = Intent().putExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            appWidgetId,
        )
        setResult(RESULT_OK, value)
        finish()
    }
}
package com.example.compose.widget.tiny

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.compose.R
import com.example.compose.widget.simple.GlanceWidgetActivity

class GlanceComposeAppWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceComposeAppWidgetContent()
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        println("AAA GlanceComposeAppWidget is Deleted")
    }
}

@Composable
fun GlanceComposeAppWidgetContent() {
    val context = LocalContext.current
    println("AAA context $context")
    val drawable = ContextCompat.getDrawable(context, R.drawable.ic_heart_broken)
    println("AAA drawable $drawable")

    var error by remember { mutableStateOf(false) }
    if (!error) {
        Scaffold(
            titleBar = {
                TitleBar(
                    startIcon = ImageProvider(R.drawable.ic_arrow),
                    title = "Glance App Widget",
                )
            },
            backgroundColor = GlanceTheme.colors.widgetBackground
        ) {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Glance App Widget",
                    modifier = GlanceModifier.padding(12.dp),
                    style = TextStyle(color = GlanceTheme.colors.onSurface)
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(
                        text = "Open Home",
                        onClick = actionStartActivity<GlanceWidgetActivity>()
                    )
                    Button(
                        text = "Display error",
                        onClick = {
                            error = true
                        }
                    )
                    Button(
                        onClick = actionRunCallback<ButtonActionRunCallback>(
                            parameters = actionParametersOf(
                                actionWidgetKey to "log event"
                            )
                        ),
                        text = "ActionRunCallback"
                    )
                    var isChecked by remember { mutableStateOf(false) }
                    CheckBox(
                        text = "Checkbox",
                        checked = isChecked,
                        onCheckedChange = {
                            isChecked = !isChecked
                        }
                    )
                }
            }
        }
    } else {
        GlanceWidgetError(
            onClick = {
                error = false
            }
        )
    }
}

@Composable
private fun GlanceWidgetError(
    onClick: () -> Unit = {}
) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color.Red)
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Error", modifier = GlanceModifier.padding(12.dp))
    }
}

val actionWidgetKey = ActionParameters.Key<String>("action-widget-key")

/**
 * public because
 * Error in Glance App Widget
 * java.lang.IllegalAccessException: java.lang.Class<com.example.compose.widget.ButtonActionRunCallback> is not accessible from java.lang.Class<androidx.glance.appwidget.action.RunCallbackAction$Companion>
 * 	at java.lang.reflect.Constructor.newInstance0(Native Method)
 * 	at java.lang.reflect.Constructor.newInstance(Constructor.java:343)
 * 	at androidx.glance.appwidget.action.RunCallbackAction$Companion.run(RunCallbackAction.kt:46)
 * 	at androidx.glance.appwidget.action.ActionCallbackBroadcastReceiver$onReceive$1.invokeSuspend(ActionCallbackBroadcastReceiver.kt:64)
 * 	at androidx.glance.appwidget.action.ActionCallbackBroadcastReceiver$onReceive$1.invoke(Unknown Source:8)
 * 	at androidx.glance.appwidget.action.ActionCallbackBroadcastReceiver$onReceive$1.invoke(Unknown Source:4)
 * 	at androidx.glance.appwidget.CoroutineBroadcastReceiverKt$goAsync$1.invokeSuspend(CoroutineBroadcastReceiver.kt:45)
 * 	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
 *  at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:100)
 *  at kotlinx.coroutines.scheduling.CoroutineScheduler.runSafely(CoroutineScheduler.kt:586)
 *  at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.executeTask(CoroutineScheduler.kt:829)
 *  at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.runWorker(CoroutineScheduler.kt:717)
 *  at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.run(CoroutineScheduler.kt:704)
 */
class ButtonActionRunCallback : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        println("AAA Item with id $glanceId and params $parameters clicked.")
    }
}

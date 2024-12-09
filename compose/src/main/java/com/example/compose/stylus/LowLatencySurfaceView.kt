package com.example.compose.stylus

import android.content.Context
import android.os.Build
import android.view.SurfaceView
import androidx.annotation.RequiresApi

class LowLatencySurfaceView(context: Context, private val fastRenderer: FastRenderer) :
    SurfaceView(context) {

    init {
        setOnTouchListener(fastRenderer.onTouchListener)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        fastRenderer.attachSurfaceView(this)
    }

    override fun onDetachedFromWindow() {
        fastRenderer.release()
        super.onDetachedFromWindow()
    }
}
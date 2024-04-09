package com.example.compose

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.compose.databinding.ActivityComposeLauncherBinding

class ComposeLauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityComposeLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.container) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.simpleComposable.setOnClickListener {
            startActivity(Intent(this, SimpleComposableActivity::class.java))
        }

        binding.codelabComposable.setOnClickListener {
            startActivity(Intent(this, ComposableCodeLabActivity::class.java))
        }

        binding.surfaceComposable.setOnClickListener {
            startActivity(Intent(this, ComposeSurfaceActivity::class.java))
        }

        binding.codelabBasicLayoutComposable.setOnClickListener {
            startActivity(Intent(this, CodeLabComposeBasicLayoutActivity::class.java))
        }

        binding.codelabComposeState.setOnClickListener {
            startActivity(Intent(this, CodeLabComposeStateActivity::class.java))
        }

        binding.codelabLazyLayouts.setOnClickListener {
            startActivity(Intent(this, CodeLabComposeLazyLayoutsActivity::class.java))
        }

        binding.codelabStylingText.setOnClickListener {
            startActivity(Intent(this, CodeLabStylingTextActivity::class.java))
        }

        binding.codelabSimpleAnimations.setOnClickListener {
            startActivity(Intent(this, CodeLabSimpleAnimationsActivity::class.java))
        }

        binding.codelabSimpleDrawing.setOnClickListener {
            startActivity(Intent(this, CodeLabComposeDrawingActivity::class.java))
        }

        binding.codelabAdvancedLayouts.setOnClickListener {
            startActivity(Intent(this, CodeLabAdvancedLayoutsActivity::class.java))
        }
    }
}

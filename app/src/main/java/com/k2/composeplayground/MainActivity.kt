package com.k2.composeplayground

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.k2.composeplayground.ui.MakeNavUi
import com.k2.composeplayground.ui.theme.ComposePlaygroundTheme

class MainActivity : ComponentActivity() {

    private var isInImmersiveMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePlaygroundTheme {
                MakeNavUi(list) {
                    toggleImmersive()
                }
            }
        }
    }

    companion object {
        val list = listOf("Night Sky", "Waves")
    }

    fun toggleImmersive() {
        var flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (!isInImmersiveMode) {
            flags = View.SYSTEM_UI_FLAG_IMMERSIVE or flags or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        window.decorView.systemUiVisibility = flags
        isInImmersiveMode = !isInImmersiveMode
    }

}

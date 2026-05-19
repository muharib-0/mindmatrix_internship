package com.virasat.nammaguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.virasat.nammaguide.ui.navigation.VirasatApp
import com.virasat.nammaguide.ui.theme.VirasatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            VirasatTheme {
                VirasatApp()
            }
        }
    }
}

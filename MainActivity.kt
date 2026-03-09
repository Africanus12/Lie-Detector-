package com.liedetector.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.liedetector.app.ui.navigation.LieDetectorNavHost
import com.liedetector.app.ui.theme.LieDetectorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LieDetectorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = LieDetectorTheme.colors.background
                ) {
                    LieDetectorNavHost()
                }
            }
        }
    }
}

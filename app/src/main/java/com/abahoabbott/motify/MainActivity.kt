package com.abahoabbott.motify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.abahoabbott.motify.navigation.MotifyApp
import com.abahoabbott.motify.ui.theme.MotifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val quoteFromNotification = intent?.getStringExtra("QUOTE_FROM_NOTIFICATION")

        enableEdgeToEdge()
        setContent {
            MotifyTheme(
                dynamicColor = false
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surface
                ) {
                    MotifyApp(quoteFromNotification = quoteFromNotification)
                }

            }
        }
    }
}


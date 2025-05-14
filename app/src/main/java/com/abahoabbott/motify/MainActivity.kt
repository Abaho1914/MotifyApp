package com.abahoabbott.motify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.abahoabbott.motify.motivate.MotivationScreen
import com.abahoabbott.motify.ui.theme.MotifyTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val quote = intent.getStringExtra("QUOTE_FROM_NOTIFICATION")
        quote?.let {
            (application as MotifyHiltApp).latestLaunchedQuote = it
        }
        enableEdgeToEdge()
          setContent {
            MotifyTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MotivationScreen()
                }
            }
        }
    }
}


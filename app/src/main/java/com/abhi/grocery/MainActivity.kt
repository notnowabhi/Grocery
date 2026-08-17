package com.abhi.grocery

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.abhi.grocery.main.MainScreen
import com.abhi.grocery.main.cart.presentation.viewmodel.CartViewModel
import com.abhi.grocery.main.home.presentation.viewmodel.HomeViewModel
import com.abhi.grocery.ui.theme.GroceryTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val cartViewModel: CartViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale("hi", "IN")
                tts.setSpeechRate(0.75f) // a little slower than normal.
            }
        }

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            GroceryTheme {
                MainScreen(
                    cartViewModel = cartViewModel,
                    homeViewModel = homeViewModel,
                    onSpeak = ::speak,
                    context = this
                )
            }
        }
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }

}
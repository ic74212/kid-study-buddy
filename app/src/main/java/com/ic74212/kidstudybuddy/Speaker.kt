package com.ic74212.kidstudybuddy

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

/** 用手機內建的文字轉語音念英文（或中文）。 */
class Speaker(context: Context) : TextToSpeech.OnInitListener {
    private val tts = TextToSpeech(context.applicationContext, this)
    private var ready = false
    // 語音引擎還沒準備好時先記下來，準備好再念
    private var pending: Pair<String, Locale>? = null

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setSpeechRate(0.8f)
            ready = true
            pending?.let { (text, locale) -> speak(text, locale) }
            pending = null
        }
    }

    fun speak(text: String, locale: Locale = Locale.US) {
        if (!ready) {
            pending = text to locale
            return
        }
        tts.language = locale
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "word")
    }

    fun shutdown() {
        tts.stop()
        tts.shutdown()
    }
}

@Composable
fun rememberSpeaker(): Speaker {
    val context = LocalContext.current
    val speaker = remember { Speaker(context) }
    DisposableEffect(speaker) {
        onDispose { speaker.shutdown() }
    }
    return speaker
}

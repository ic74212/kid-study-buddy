package com.ic74212.kidstudybuddy

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

object EnglishNumbers {
    val en = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten")
    val zh = listOf("一", "二", "三", "四", "五", "六", "七", "八", "九", "十")

    // 語音辨識常聽成的同音字也算對
    private val aliases = mapOf(
        1 to setOf("won"),
        2 to setOf("to", "too"),
        3 to setOf("tree"),
        4 to setOf("for", "fore"),
        8 to setOf("ate"),
    )

    fun spokenMatches(n: Int, alternatives: List<String>): Boolean {
        val ok = setOf(en[n - 1], n.toString()) + aliases[n].orEmpty()
        return alternatives.any { alt -> alt.lowercase().split(Regex("[^a-z0-9]+")).any { it in ok } }
    }
}

private data class NumMode(val emoji: String, val title: String, val desc: String)

private val numModes = mapOf(
    1 to NumMode("👂", "聽中文，寫英文", "聽到「三」，打出 three"),
    2 to NumMode("🔢", "聽英文，寫數字", "聽到 three，按出 3"),
    3 to NumMode("🎤", "看數字，說英文", "看到 3 three，對著麥克風念出來"),
)

private const val MAX_TRIES = 3
private val Blue = Color(0xFF42A5F5)

@Composable
fun NumMenuScreen(onBack: () -> Unit, onStart: (mode: Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TopBar("英文數字 1～10", onBack)
        Text("每次 10 題，1～10 順序打亂。選一種題型：", fontSize = 18.sp)
        for ((m, mode) in numModes) {
            MenuCard(mode.emoji, "$m. ${mode.title}", mode.desc, Color(0xFFFFF59D)) { onStart(m) }
        }
    }
}

@Composable
fun NumQuizScreen(mode: Int, speaker: Speaker, onBack: () -> Unit, onFinish: (score: Int) -> Unit) {
    val order = remember { (1..10).shuffled() }
    var index by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var input by remember { mutableStateOf("") }
    // null = 作答中，true / false = 已作答（對 / 錯）
    var correct by remember { mutableStateOf<Boolean?>(null) }
    var revealed by remember { mutableStateOf(false) }
    var tries by remember { mutableIntStateOf(0) }
    var heard by remember { mutableStateOf<String?>(null) }
    var noRecognizer by remember { mutableStateOf(false) }

    val n = order[index]
    val en = EnglishNumbers.en[n - 1]
    val zh = EnglishNumbers.zh[n - 1]
    val isLast = index == order.lastIndex
    val answered = correct != null

    fun speakQuestion() {
        when (mode) {
            1 -> speaker.speak(zh, Locale.TAIWAN)
            else -> speaker.speak(en)
        }
    }

    // 換題時自動念題目（第 3 種題型是自己念，不自動播放）
    LaunchedEffect(index) {
        if (mode != 3) speakQuestion()
    }

    fun finish(ok: Boolean) {
        correct = ok
        if (ok) score++
        if (mode != 3) speaker.speak(en)
    }

    val recognizer = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (correct == null) {
            val alternatives = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).orEmpty()
            when {
                EnglishNumbers.spokenMatches(n, alternatives) -> finish(true)
                result.resultCode != Activity.RESULT_OK -> Unit // 小朋友自己關掉了，不算一次
                else -> {
                    tries++
                    heard = alternatives.firstOrNull() ?: "（沒聽清楚）"
                    if (tries >= MAX_TRIES) finish(false)
                }
            }
        }
    }

    fun listen() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "請說：$en")
        }
        try {
            recognizer.launch(intent)
        } catch (e: ActivityNotFoundException) {
            noRecognizer = true
        }
    }

    fun submit() {
        if (answered || input.isBlank()) return
        finish(
            when (mode) {
                1 -> input.trim().lowercase() == en
                else -> input.toIntOrNull() == n
            },
        )
    }

    fun next() {
        if (isLast) {
            onFinish(score)
        } else {
            index++
            input = ""
            correct = null
            revealed = false
            tries = 0
            heard = null
        }
    }

    val mood = when (correct) {
        true -> Color(0xFFC8E6C9)
        false -> Color(0xFFFFCDD2)
        null -> Color.White
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopBar("$mode. ${numModes.getValue(mode).title}", onBack)
        Text("第 ${index + 1} / ${order.size} 題　⭐ $score", fontSize = 18.sp)

        when (mode) {
            1 -> {
                Text("👂 聽中文，寫出英文", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Blue)
                BigButton(text = "🔊 再聽一次", onClick = { speakQuestion() }, color = Blue)
                Text(zh, fontSize = 56.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = input,
                    onValueChange = { if (!answered) input = it.take(20) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(mood, RoundedCornerShape(8.dp)),
                    enabled = !answered,
                    singleLine = true,
                    placeholder = { Text("在這裡打英文", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                    textStyle = TextStyle(fontSize = 36.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { submit() }),
                )
            }

            2 -> {
                Text("👂 聽英文，寫出數字", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Blue)
                BigButton(text = "🔊 再聽一次", onClick = { speakQuestion() }, color = Blue)
                if (revealed || answered) {
                    Text(en, fontSize = 44.sp, fontWeight = FontWeight.Bold, color = Blue)
                } else {
                    TextButton(onClick = { revealed = true }) { Text("👀 聽不到？看英文", fontSize = 20.sp) }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 72.dp)
                        .background(mood, RoundedCornerShape(20.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(input, fontSize = 40.sp, fontWeight = FontWeight.Bold)
                }
            }

            else -> {
                Text("🎤 看著念念看", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Blue)
                Text("$n", fontSize = 110.sp, fontWeight = FontWeight.Bold)
                Text(en, fontSize = 52.sp, fontWeight = FontWeight.Bold, color = Blue)
                Text(zh, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                BigButton(text = "🔊 聽示範", onClick = { speakQuestion() }, color = Blue)
            }
        }

        when (correct) {
            true -> Text("🎉 答對了！好棒！", fontSize = 26.sp, color = Green, fontWeight = FontWeight.Bold)
            false -> Text("💪 正確答案是 $n $en", fontSize = 26.sp, color = Red, fontWeight = FontWeight.Bold)
            null -> if (mode == 3) {
                val hint = when {
                    noRecognizer -> "😢 這支手機沒有語音辨識，請安裝 Google App"
                    heard != null -> "我聽到「$heard」，再試一次（還有 ${MAX_TRIES - tries} 次）"
                    else -> "按下麥克風，大聲念出英文"
                }
                Text(hint, fontSize = 18.sp, color = Color.Gray, textAlign = TextAlign.Center)
            }
        }

        if (answered) {
            BigButton(text = if (isLast) "看成績 🏆" else "下一題 →", color = Blue, onClick = { next() })
        } else {
            when (mode) {
                1 -> BigButton(text = "確定", enabled = input.isNotBlank(), onClick = { submit() })
                2 -> {
                    NumberPad(allowDecimal = false, onKey = { key -> input = nextInput(input, key) })
                    BigButton(text = "確定", enabled = input.isNotEmpty(), onClick = { submit() })
                }
                else -> {
                    BigButton(text = "🎤 按我說英文", onClick = { listen() })
                    TextButton(onClick = { finish(false) }) { Text("跳過這題", fontSize = 18.sp) }
                }
            }
        }
    }
}

package com.ic74212.kidstudybuddy

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blue = Color(0xFF42A5F5)

@Composable
fun EnglishCardsScreen(words: List<Word>, speaker: Speaker, onBack: () -> Unit) {
    var index by remember { mutableIntStateOf(0) }
    val word = words[index]

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopBar("英文單字卡", onBack)
        Text("${index + 1} / ${words.size}", fontSize = 18.sp)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable { speaker.speak(word.en) },
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(word.emoji, fontSize = 110.sp)
                Spacer(Modifier.height(16.dp))
                Text(word.en, fontSize = 52.sp, fontWeight = FontWeight.Bold, color = Blue)
                Text(word.zh, fontSize = 30.sp)
                Spacer(Modifier.height(16.dp))
                Text("👆 點一下聽發音", fontSize = 16.sp, color = Color.Gray)
            }
        }

        BigButton(text = "🔊 聽發音", onClick = { speaker.speak(word.en) }, color = Blue)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            BigButton(
                text = "◀ 上一個",
                onClick = { index = (index - 1 + words.size) % words.size },
                modifier = Modifier.weight(1f),
            )
            BigButton(
                text = "下一個 ▶",
                onClick = { index = (index + 1) % words.size },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

private data class WordQuestion(val target: Word, val options: List<Word>)

@Composable
fun EnglishQuizScreen(words: List<Word>, speaker: Speaker, onBack: () -> Unit, onFinish: (score: Int) -> Unit) {
    val questions = remember {
        (words.shuffled() + words.shuffled()).take(QUIZ_SIZE).map { target ->
            WordQuestion(target, ((words - target).shuffled().take(3) + target).shuffled())
        }
    }
    var index by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var chosen by remember { mutableStateOf<Word?>(null) }
    val question = questions[index]
    val isLast = index == questions.lastIndex

    // 答完後念出正確答案
    LaunchedEffect(chosen) {
        if (chosen != null) speaker.speak(question.target.en)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopBar("英文小測驗", onBack)
        Text("第 ${index + 1} / ${questions.size} 題　⭐ $score", fontSize = 18.sp)
        LinearProgressIndicator(
            progress = { index / questions.size.toFloat() },
            modifier = Modifier.fillMaxWidth(),
        )

        Text(question.target.emoji, fontSize = 80.sp)
        Text(question.target.zh, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Text("英文是哪一個？", fontSize = 18.sp, color = Color.Gray)

        for (option in question.options) {
            val answered = chosen != null
            val color = when {
                !answered -> Color.White
                option == question.target -> Color(0xFFC8E6C9)
                option == chosen -> Color(0xFFFFCDD2)
                else -> Color.White
            }
            Button(
                onClick = {
                    if (chosen == null) {
                        if (option == question.target) score++
                        chosen = option
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = Color(0xFF3E2723)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
            ) {
                Text(option.en, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.weight(1f))

        if (chosen != null) {
            val ok = chosen == question.target
            Text(
                if (ok) "🎉 答對了！" else "💪 答案是 ${question.target.en}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (ok) Green else Red,
            )
            BigButton(
                text = if (isLast) "看成績 🏆" else "下一題 →",
                color = Blue,
                onClick = {
                    if (isLast) {
                        onFinish(score)
                    } else {
                        index++
                        chosen = null
                    }
                },
            )
        }
    }
}

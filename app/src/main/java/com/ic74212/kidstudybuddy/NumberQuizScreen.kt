package com.ic74212.kidstudybuddy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

/** 用數字鍵盤作答的測驗（數學練習、九九乘法共用）。 */
@Composable
fun NumberQuizScreen(
    title: String,
    questions: List<Question>,
    allowDecimal: Boolean,
    onBack: () -> Unit,
    onFinish: (score: Int) -> Unit,
) {
    var index by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var input by remember { mutableStateOf("") }
    // null = 作答中，true / false = 已作答（對 / 錯）
    var correct by remember { mutableStateOf<Boolean?>(null) }
    val question = questions[index]
    val isLast = index == questions.lastIndex

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopBar(title, onBack)
        Text("第 ${index + 1} / ${questions.size} 題　⭐ $score", fontSize = 18.sp)
        LinearProgressIndicator(
            progress = { index / questions.size.toFloat() },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(8.dp))
        Text("${question.text} = ?", fontSize = 36.sp, fontWeight = FontWeight.Bold)

        val boxColor = when (correct) {
            true -> Color(0xFFC8E6C9)
            false -> Color(0xFFFFCDD2)
            null -> Color.White
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(boxColor, RoundedCornerShape(20.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(input.ifEmpty { " " }, fontSize = 40.sp, fontWeight = FontWeight.Bold)
        }

        when (correct) {
            true -> Text("🎉 答對了！好棒！", fontSize = 26.sp, color = Green, fontWeight = FontWeight.Bold)
            false -> Text("💪 正確答案是 ${question.answerText}", fontSize = 26.sp, color = Red, fontWeight = FontWeight.Bold)
            null -> Spacer(Modifier.height(34.dp))
        }

        Spacer(Modifier.weight(1f))

        if (correct == null) {
            NumberPad(
                allowDecimal = allowDecimal,
                onKey = { key -> input = nextInput(input, key) },
            )
            BigButton(
                text = "確定",
                enabled = input.isNotEmpty(),
                onClick = {
                    val ok = question.check(input)
                    if (ok) score++
                    correct = ok
                },
            )
        } else {
            BigButton(
                text = if (isLast) "看成績 🏆" else "下一題 →",
                color = MaterialThemeSecondary,
                onClick = {
                    if (isLast) {
                        onFinish(score)
                    } else {
                        index++
                        input = ""
                        correct = null
                    }
                },
            )
        }
    }
}

private val MaterialThemeSecondary = Color(0xFF42A5F5)

private fun nextInput(input: String, key: String): String = when (key) {
    "⌫" -> input.dropLast(1)
    "." -> if ('.' in input) input else input.ifEmpty { "0" } + "."
    else -> when {
        input.length >= 9 -> input
        input == "0" -> key
        else -> input + key
    }
}

@Composable
private fun NumberPad(allowDecimal: Boolean, onKey: (String) -> Unit) {
    val rows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf(if (allowDecimal) "." else "", "0", "⌫"),
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (row in rows) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (key in row) {
                    if (key.isEmpty()) {
                        Spacer(Modifier.weight(1f))
                    } else {
                        Button(
                            onClick = { onKey(key) },
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFF3E2723),
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                        ) {
                            Text(key, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

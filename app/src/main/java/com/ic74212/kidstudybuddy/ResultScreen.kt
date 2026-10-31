package com.ic74212.kidstudybuddy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResultScreen(score: Int, total: Int, onRetry: () -> Unit, onHome: () -> Unit) {
    val ratio = score.toFloat() / total
    val stars = when {
        ratio >= 0.9f -> 3
        ratio >= 0.7f -> 2
        ratio >= 0.5f -> 1
        else -> 0
    }
    val message = when (stars) {
        3 -> "太厲害了！你是學習小天才！"
        2 -> "很棒喔！再練習一下就滿分了！"
        1 -> "不錯喔！多練習會更厲害！"
        else -> "沒關係，我們再試一次！"
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(if (stars == 3) "🏆" else "🎈", fontSize = 100.sp)
        Text("⭐".repeat(stars) + "☆".repeat(3 - stars), fontSize = 48.sp, color = Color(0xFFFFB300))
        Text("$score / $total", fontSize = 56.sp, fontWeight = FontWeight.Bold)
        Text(message, fontSize = 22.sp)
        Spacer(Modifier.height(24.dp))
        BigButton(text = "再玩一次 🔁", onClick = onRetry, color = Green)
        BigButton(text = "回首頁 🏠", onClick = onHome)
    }
}

package com.ic74212.kidstudybuddy

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(grade: Int, onGradeChange: (Int) -> Unit, onOpen: (Screen) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("📚 學習小夥伴", fontSize = 34.sp, fontWeight = FontWeight.Bold)
        Text("今天也一起加油吧！", fontSize = 18.sp)

        Spacer(Modifier.height(4.dp))
        Text("我是幾年級？", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            for (g in 1..6) {
                val selected = g == grade
                Button(
                    onClick = { onGradeChange(g) },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected) Orange else Color.White,
                        contentColor = if (selected) Color.White else Orange,
                    ),
                    contentPadding = ButtonDefaults.TextButtonContentPadding,
                    modifier = Modifier.weight(1f).padding(horizontal = 2.dp),
                ) {
                    Text("$g", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Text("目前：${grade} 年級", fontSize = 16.sp, color = Color.Gray)

        Spacer(Modifier.height(4.dp))
        MenuCard("🔢", "數學練習", MathQuestions.gradeTopics[grade] ?: "", Color(0xFFFFE0B2)) {
            onOpen(Screen.MathQuiz)
        }
        MenuCard("✖️", "九九乘法", "背乘法表、挑戰小測驗", Color(0xFFC8E6C9)) {
            onOpen(Screen.TimesTable)
        }
        MenuCard("🔤", "英文單字卡", "看圖、聽發音、記單字", Color(0xFFBBDEFB)) {
            onOpen(Screen.EnglishCards)
        }
        MenuCard("📝", "英文小測驗", "看圖選出正確的單字", Color(0xFFE1BEE7)) {
            onOpen(Screen.EnglishQuiz)
        }
    }
}

@Composable
private fun MenuCard(emoji: String, title: String, subtitle: String, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = color),
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(emoji, fontSize = 40.sp)
            Column {
                Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, fontSize = 15.sp)
            }
        }
    }
}

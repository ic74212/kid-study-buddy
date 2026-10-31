package com.ic74212.kidstudybuddy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimesTableScreen(onBack: () -> Unit, onStart: (n: Int) -> Unit) {
    var selected by remember { mutableIntStateOf(2) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TopBar("九九乘法", onBack)
        Text("選一個數字：", fontSize = 18.sp)
        for (row in listOf(2..5, 6..9)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (n in row) {
                    val isSelected = n == selected
                    Button(
                        onClick = { selected = n },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Green else Color.White,
                            contentColor = if (isSelected) Color.White else Green,
                        ),
                    ) {
                        Text("$n", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            for (b in 1..9) {
                Text("$selected × $b = ${selected * b}", fontSize = 30.sp, fontWeight = FontWeight.Medium)
            }
        }

        BigButton(text = "挑戰 $selected 的乘法", onClick = { onStart(selected) }, color = Green)
        BigButton(text = "2～9 混合挑戰", onClick = { onStart(0) })
    }
}

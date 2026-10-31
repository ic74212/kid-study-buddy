package com.ic74212.kidstudybuddy

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

sealed interface Screen {
    data object Home : Screen
    data object MathQuiz : Screen
    data object TimesTable : Screen
    data class TimesQuiz(val n: Int) : Screen
    data object EnglishCards : Screen
    data object EnglishQuiz : Screen
    data class Result(val score: Int, val total: Int, val retry: Screen) : Screen
}

const val QUIZ_SIZE = 10

@Composable
fun App() {
    val context = LocalContext.current
    val prefs = remember { Prefs(context) }
    var grade by remember { mutableIntStateOf(prefs.grade) }
    var screen by remember { mutableStateOf<Screen>(Screen.Home) }
    val speaker = rememberSpeaker()

    val goHome = { screen = Screen.Home }
    BackHandler(enabled = screen != Screen.Home) {
        screen = if (screen is Screen.TimesQuiz) Screen.TimesTable else Screen.Home
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(16.dp),
        ) {
            when (val s = screen) {
                Screen.Home -> HomeScreen(
                    grade = grade,
                    onGradeChange = { grade = it; prefs.grade = it },
                    onOpen = { screen = it },
                )

                Screen.MathQuiz -> {
                    val questions = remember { MathQuestions.generate(grade, QUIZ_SIZE) }
                    NumberQuizScreen(
                        title = "數學練習 · ${grade} 年級",
                        questions = questions,
                        allowDecimal = grade >= 5,
                        onBack = goHome,
                        onFinish = { score -> screen = Screen.Result(score, questions.size, Screen.MathQuiz) },
                    )
                }

                Screen.TimesTable -> TimesTableScreen(
                    onBack = goHome,
                    onStart = { n -> screen = Screen.TimesQuiz(n) },
                )

                is Screen.TimesQuiz -> {
                    val questions = remember { MathQuestions.timesTable(s.n, QUIZ_SIZE) }
                    NumberQuizScreen(
                        title = if (s.n == 0) "九九乘法 · 混合" else "九九乘法 · ${s.n} 的乘法",
                        questions = questions,
                        allowDecimal = false,
                        onBack = { screen = Screen.TimesTable },
                        onFinish = { score -> screen = Screen.Result(score, questions.size, s) },
                    )
                }

                Screen.EnglishCards -> EnglishCardsScreen(
                    words = EnglishWords.forGrade(grade),
                    speaker = speaker,
                    onBack = goHome,
                )

                Screen.EnglishQuiz -> EnglishQuizScreen(
                    words = EnglishWords.forGrade(grade),
                    speaker = speaker,
                    onBack = goHome,
                    onFinish = { score -> screen = Screen.Result(score, QUIZ_SIZE, Screen.EnglishQuiz) },
                )

                is Screen.Result -> ResultScreen(
                    score = s.score,
                    total = s.total,
                    onRetry = { screen = s.retry },
                    onHome = goHome,
                )
            }
        }
    }
}

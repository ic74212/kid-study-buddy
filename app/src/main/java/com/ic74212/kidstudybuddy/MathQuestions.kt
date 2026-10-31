package com.ic74212.kidstudybuddy

import java.math.BigDecimal
import kotlin.random.Random

data class Question(val text: String, val answer: BigDecimal) {
    val answerText: String get() = answer.fmt()

    fun check(input: String): Boolean = input.toBigDecimalOrNull()?.compareTo(answer) == 0
}

private fun BigDecimal.fmt(): String = stripTrailingZeros().toPlainString()

/** 依照年級出數學題目（大致對應台灣國小課程）。 */
object MathQuestions {

    val gradeTopics = mapOf(
        1 to "20 以內加減、二位數加一位數",
        2 to "三位數加減、九九乘法",
        3 to "四位數加減、乘法、除法",
        4 to "多位數乘法、除法",
        5 to "小數加減、四則運算",
        6 to "小數乘除、括號運算",
    )

    fun generate(grade: Int, count: Int): List<Question> = List(count) { one(grade) }

    /** 某一列九九乘法的題目；n = 0 代表 2~9 混合。 */
    fun timesTable(n: Int, count: Int): List<Question> = List(count) {
        val a = if (n == 0) r(2, 9) else n
        val b = r(1, 9)
        q("$a × $b", a * b)
    }

    private fun r(from: Int, to: Int) = Random.nextInt(from, to + 1)

    private fun q(text: String, answer: Int) = Question(text, answer.toBigDecimal())

    private fun q(text: String, answer: BigDecimal) = Question(text, answer)

    /** units / 10^scale，例如 dec(35, 1) = 3.5 */
    private fun dec(units: Int, scale: Int): BigDecimal = BigDecimal.valueOf(units.toLong(), scale)

    private fun one(grade: Int): Question = when (grade) {
        1 -> when (r(0, 2)) {
            0 -> { val a = r(1, 10); val b = r(1, 10); q("$a + $b", a + b) }
            1 -> { val a = r(5, 20); val b = r(1, a); q("$a − $b", a - b) }
            else -> { val a = r(10, 89); val b = r(1, 9); q("$a + $b", a + b) }
        }

        2 -> when (r(0, 3)) {
            0 -> { val a = r(10, 499); val b = r(10, 499); q("$a + $b", a + b) }
            1 -> { val a = r(100, 999); val b = r(10, a); q("$a − $b", a - b) }
            else -> { val a = r(2, 9); val b = r(1, 9); q("$a × $b", a * b) }
        }

        3 -> when (r(0, 3)) {
            0 -> { val a = r(100, 4999); val b = r(100, 4999); q("$a + $b", a + b) }
            1 -> { val a = r(1000, 9999); val b = r(100, a); q("$a − $b", a - b) }
            2 -> { val a = r(11, 999); val b = r(2, 9); q("$a × $b", a * b) }
            else -> { val d = r(2, 9); val ans = r(2, 99); q("${d * ans} ÷ $d", ans) }
        }

        4 -> when (r(0, 2)) {
            0 -> { val a = r(101, 999); val b = r(11, 99); q("$a × $b", a * b) }
            1 -> { val d = r(2, 9); val ans = r(100, 999); q("${d * ans} ÷ $d", ans) }
            else -> { val d = r(11, 40); val ans = r(2, 50); q("${d * ans} ÷ $d", ans) }
        }

        5 -> when (r(0, 3)) {
            0 -> { val a = dec(r(11, 999), 1); val b = dec(r(11, 999), 1); q("${a.fmt()} + ${b.fmt()}", a + b) }
            1 -> {
                val au = r(100, 9999)
                val a = dec(au, 2); val b = dec(r(1, au), 2)
                q("${a.fmt()} − ${b.fmt()}", a - b)
            }
            2 -> { val a = r(1, 50); val b = r(2, 9); val c = r(2, 9); q("$a + $b × $c", a + b * c) }
            else -> { val a = r(2, 12); val b = r(2, 9); val c = r(1, a * b); q("$a × $b − $c", a * b - c) }
        }

        else -> when (r(0, 3)) {
            0 -> { val a = dec(r(11, 99), 1); val b = r(2, 9); q("${a.fmt()} × $b", a * b.toBigDecimal()) }
            1 -> { val a = dec(r(11, 99), 1); val b = dec(r(1, 9), 1); q("${a.fmt()} × ${b.fmt()}", a * b) }
            2 -> { val a = r(2, 30); val b = r(2, 30); val c = r(2, 9); q("($a + $b) × $c", (a + b) * c) }
            else -> {
                val ans = dec(r(11, 99), 1); val d = r(2, 9)
                q("${(ans * d.toBigDecimal()).fmt()} ÷ $d", ans)
            }
        }
    }
}

package com.ic74212.kidstudybuddy

data class Word(val en: String, val zh: String, val emoji: String)

/** 各年級的英文單字（由簡單到難）。 */
object EnglishWords {
    private val byGrade = mapOf(
        1 to listOf(
            Word("cat", "貓", "🐱"), Word("dog", "狗", "🐶"), Word("apple", "蘋果", "🍎"),
            Word("egg", "蛋", "🥚"), Word("fish", "魚", "🐟"), Word("ball", "球", "⚽"),
            Word("sun", "太陽", "☀️"), Word("car", "汽車", "🚗"), Word("book", "書", "📖"),
            Word("milk", "牛奶", "🥛"), Word("bird", "鳥", "🐦"), Word("hat", "帽子", "👒"),
        ),
        2 to listOf(
            Word("red", "紅色", "🔴"), Word("blue", "藍色", "🔵"), Word("green", "綠色", "🟢"),
            Word("yellow", "黃色", "🟡"), Word("one", "一", "1️⃣"), Word("two", "二", "2️⃣"),
            Word("three", "三", "3️⃣"), Word("mom", "媽媽", "👩"), Word("dad", "爸爸", "👨"),
            Word("banana", "香蕉", "🍌"), Word("pig", "豬", "🐷"), Word("duck", "鴨子", "🦆"),
        ),
        3 to listOf(
            Word("eye", "眼睛", "👁️"), Word("ear", "耳朵", "👂"), Word("nose", "鼻子", "👃"),
            Word("mouth", "嘴巴", "👄"), Word("hand", "手", "✋"), Word("foot", "腳", "🦶"),
            Word("happy", "開心", "😄"), Word("sad", "難過", "😢"), Word("cake", "蛋糕", "🍰"),
            Word("juice", "果汁", "🧃"), Word("rabbit", "兔子", "🐰"), Word("tiger", "老虎", "🐯"),
        ),
        4 to listOf(
            Word("teacher", "老師", "👩‍🏫"), Word("student", "學生", "🧑‍🎓"), Word("doctor", "醫生", "🧑‍⚕️"),
            Word("school", "學校", "🏫"), Word("park", "公園", "🏞️"), Word("rain", "下雨", "🌧️"),
            Word("snow", "雪", "❄️"), Word("hot", "熱", "🥵"), Word("cold", "冷", "🥶"),
            Word("run", "跑", "🏃"), Word("swim", "游泳", "🏊"), Word("sing", "唱歌", "🎤"),
        ),
        5 to listOf(
            Word("breakfast", "早餐", "🍳"), Word("lunch", "午餐", "🍱"), Word("dinner", "晚餐", "🍽️"),
            Word("bicycle", "腳踏車", "🚲"), Word("train", "火車", "🚆"), Word("airplane", "飛機", "✈️"),
            Word("hospital", "醫院", "🏥"), Word("library", "圖書館", "📚"), Word("birthday", "生日", "🎂"),
            Word("music", "音樂", "🎵"), Word("basketball", "籃球", "🏀"), Word("sandwich", "三明治", "🥪"),
        ),
        6 to listOf(
            Word("computer", "電腦", "💻"), Word("telephone", "電話", "☎️"), Word("umbrella", "雨傘", "☂️"),
            Word("elephant", "大象", "🐘"), Word("giraffe", "長頸鹿", "🦒"), Word("dolphin", "海豚", "🐬"),
            Word("mountain", "山", "⛰️"), Word("ocean", "海洋", "🌊"), Word("island", "島嶼", "🏝️"),
            Word("firefighter", "消防員", "🧑‍🚒"), Word("vegetable", "蔬菜", "🥦"), Word("dictionary", "字典", "📕"),
        ),
    )

    fun forGrade(grade: Int): List<Word> = byGrade[grade] ?: byGrade.getValue(1)
}

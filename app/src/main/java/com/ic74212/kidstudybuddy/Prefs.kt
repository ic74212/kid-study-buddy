package com.ic74212.kidstudybuddy

import android.content.Context

/** 存放使用者設定（目前只有年級）。 */
class Prefs(context: Context) {
    private val sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var grade: Int
        get() = sp.getInt("grade", 2)
        set(value) = sp.edit().putInt("grade", value).apply()
}

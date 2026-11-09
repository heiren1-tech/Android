package com.example.notepad

import android.content.Context

class Prefs(context: Context) {

    private val sp = context.applicationContext.getSharedPreferences("notepad_prefs", Context.MODE_PRIVATE)

    var sort: NoteSort
        get() = NoteSort.fromKey(sp.getString(KEY_SORT, null))
        set(value) {
            sp.edit().putString(KEY_SORT, value.key).apply()
        }

    var darkTheme: Boolean
        get() = sp.getBoolean(KEY_DARK_THEME, false)
        set(value) {
            sp.edit().putBoolean(KEY_DARK_THEME, value).apply()
        }

    private companion object {
        const val KEY_SORT = "sort"
        const val KEY_DARK_THEME = "dark_theme"
    }
}

package com.example.notepad

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class NoteApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val dark = Prefs(this).darkTheme
        AppCompatDelegate.setDefaultNightMode(
            if (dark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}

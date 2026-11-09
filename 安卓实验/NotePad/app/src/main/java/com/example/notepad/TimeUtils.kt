package com.example.notepad

import android.content.Context
import android.util.TypedValue
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtils {
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    fun format(timestamp: Long): String =
        if (timestamp <= 0L) "" else formatter.format(Date(timestamp))
}

object ThemeUtils {
    fun resolveColor(context: Context, attr: Int): Int {
        val value = TypedValue()
        if (context.theme.resolveAttribute(attr, value, true)) {
            return if (value.resourceId != 0) {
                ContextCompat.getColor(context, value.resourceId)
            } else {
                value.data
            }
        }
        return 0xFF000000.toInt()
    }
}

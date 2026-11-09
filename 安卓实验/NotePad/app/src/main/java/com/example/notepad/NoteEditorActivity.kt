package com.example.notepad

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class NoteEditorActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NOTE_ID = "extra_note_id"

        private val COLOR_VALUES = intArrayOf(
            0,
            0xFFFFF8E1.toInt(),
            0xFFE8F5E9.toInt(),
            0xFFE3F2FD.toInt(),
            0xFFFCE4EC.toInt(),
            0xFFF3E5F5.toInt(),
            0xFFFFF3E0.toInt()
        )
        private val COLOR_NAMES = intArrayOf(
            R.string.color_default,
            R.string.color_yellow,
            R.string.color_green,
            R.string.color_blue,
            R.string.color_pink,
            R.string.color_purple,
            R.string.color_orange
        )
    }

    private lateinit var db: NoteDbHelper
    private lateinit var root: LinearLayout
    private lateinit var titleInput: EditText
    private lateinit var contentInput: EditText
    private lateinit var timeView: TextView

    private var noteId = 0L
    private var noteColor = 0
    private var lastTitle = ""
    private var lastContent = ""
    private var lastColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)

        db = NoteDbHelper(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        root = findViewById(R.id.editor_root)
        titleInput = findViewById(R.id.title_input)
        contentInput = findViewById(R.id.content_input)
        timeView = findViewById(R.id.time_view)

        noteId = intent.getLongExtra(EXTRA_NOTE_ID, 0L)
        if (noteId > 0L) {
            val note = db.getById(noteId)
            if (note == null) {
                finish()
                return
            }
            noteColor = note.color
            titleInput.setText(note.title)
            contentInput.setText(note.content)
            timeView.text = getString(R.string.time_prefix) + TimeUtils.format(note.updated)
            supportActionBar?.title = getString(R.string.title_edit_note)
        } else {
            timeView.text = getString(R.string.time_prefix) + TimeUtils.format(System.currentTimeMillis())
            supportActionBar?.title = getString(R.string.title_new_note)
        }

        lastTitle = titleInput.text.toString().trim()
        lastContent = contentInput.text.toString()
        lastColor = noteColor
        applyThemeColor()
    }

    private fun applyThemeColor() {
        val background = if (noteColor != 0) {
            noteColor
        } else {
            ThemeUtils.resolveColor(this, android.R.attr.colorBackground)
        }
        root.setBackgroundColor(background)

        if (noteColor != 0) {
            titleInput.setTextColor(0xFF212121.toInt())
            contentInput.setTextColor(0xFF424242.toInt())
            timeView.setTextColor(0xFF757575.toInt())
        } else {
            titleInput.setTextColor(ThemeUtils.resolveColor(this, android.R.attr.textColorPrimary))
            contentInput.setTextColor(ThemeUtils.resolveColor(this, android.R.attr.textColorPrimary))
            timeView.setTextColor(ThemeUtils.resolveColor(this, android.R.attr.textColorSecondary))
        }
    }

    private fun saveIfChanged(showToast: Boolean): Boolean {
        val title = titleInput.text.toString().trim()
        val content = contentInput.text.toString()
        val changed = title != lastTitle || content != lastContent || noteColor != lastColor

        if (!changed) {
            if (showToast) {
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show()
            }
            return true
        }
        if (noteId == 0L && title.isEmpty() && content.isBlank()) {
            if (showToast) {
                Toast.makeText(this, R.string.empty_note_not_saved, Toast.LENGTH_SHORT).show()
            }
            return false
        }

        val now = System.currentTimeMillis()
        if (noteId == 0L) {
            noteId = db.insert(title, content, noteColor, now)
        } else {
            db.update(noteId, title, content, noteColor, now)
        }

        lastTitle = title
        lastContent = content
        lastColor = noteColor
        timeView.text = getString(R.string.time_prefix) + TimeUtils.format(now)
        if (showToast) {
            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show()
        }
        return true
    }

    private fun shareCurrent() {
        if (!saveIfChanged(false)) {
            Toast.makeText(this, R.string.empty_note_not_saved, Toast.LENGTH_SHORT).show()
            return
        }
        val title = titleInput.text.toString().trim().ifEmpty { getString(R.string.note_untitled) }
        val content = contentInput.text.toString()
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, "$title\n\n$content")
        }
        startActivity(Intent.createChooser(intent, getString(R.string.share_note)))
    }

    private fun chooseColor() {
        var checked = COLOR_VALUES.indexOf(noteColor)
        if (checked < 0) checked = 0
        val names = COLOR_NAMES.map { getString(it) }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle(R.string.menu_color)
            .setSingleChoiceItems(names, checked) { dialog, which ->
                noteColor = COLOR_VALUES[which]
                applyThemeColor()
                dialog.dismiss()
            }
            .show()
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_title)
            .setMessage(R.string.delete_message)
            .setPositiveButton(R.string.delete) { _, _ ->
                if (noteId > 0L) {
                    db.delete(noteId)
                    noteId = 0L
                }
                Toast.makeText(this, R.string.deleted, Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_note_editor, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                saveIfChanged(false)
                finish()
                true
            }

            R.id.action_save -> {
                saveIfChanged(true)
                true
            }

            R.id.action_share -> {
                shareCurrent()
                true
            }

            R.id.action_color -> {
                chooseColor()
                true
            }

            R.id.action_delete -> {
                confirmDelete()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
        if (!isFinishing) {
            saveIfChanged(false)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        saveIfChanged(false)
        super.onBackPressed()
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}

package com.example.notepad

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NoteListActivity : AppCompatActivity() {

    private lateinit var prefs: Prefs
    private lateinit var db: NoteDbHelper
    private lateinit var adapter: NoteAdapter
    private lateinit var searchInput: EditText
    private lateinit var emptyView: TextView
    private lateinit var noteList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)

        prefs = Prefs(this)
        db = NoteDbHelper(this)

        supportActionBar?.title = getString(R.string.app_name)

        searchInput = findViewById(R.id.search_input)
        emptyView = findViewById(R.id.empty_view)
        noteList = findViewById(R.id.note_list)

        adapter = NoteAdapter(
            onClick = { note -> openEditor(note.id) },
            onLongClick = { note -> confirmDelete(note) }
        )
        noteList.layoutManager = LinearLayoutManager(this)
        noteList.adapter = adapter

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) = loadNotes()
        })

        findViewById<ImageButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(this, NoteEditorActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }

    private fun loadNotes() {
        val keyword = searchInput.text.toString().trim()
        val notes = db.query(keyword, prefs.sort)
        adapter.submitList(notes)

        if (notes.isEmpty()) {
            emptyView.setText(
                if (keyword.isEmpty()) R.string.empty_no_notes else R.string.empty_no_result
            )
            emptyView.visibility = TextView.VISIBLE
        } else {
            emptyView.visibility = TextView.GONE
        }
    }

    private fun openEditor(id: Long) {
        startActivity(
            Intent(this, NoteEditorActivity::class.java)
                .putExtra(NoteEditorActivity.EXTRA_NOTE_ID, id)
        )
    }

    private fun confirmDelete(note: Note) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_title)
            .setMessage(R.string.delete_message)
            .setPositiveButton(R.string.delete) { _, _ ->
                db.delete(note.id)
                loadNotes()
                Toast.makeText(this, R.string.deleted, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun exportAll() {
        val notes = db.getAll(prefs.sort)
        if (notes.isEmpty()) {
            Toast.makeText(this, R.string.export_all_empty, Toast.LENGTH_SHORT).show()
            return
        }

        val builder = StringBuilder()
        builder.append(getString(R.string.export_all))
            .append("  ")
            .append(TimeUtils.format(System.currentTimeMillis()))
            .append("\n\n")

        notes.forEachIndexed { index, note ->
            if (index > 0) {
                builder.append("--------------------\n\n")
            }
            builder.append(note.title.trim().ifEmpty { getString(R.string.note_untitled) }).append('\n')
            builder.append(TimeUtils.format(note.updated)).append("\n\n")
            builder.append(note.content).append('\n')
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.export_all))
            putExtra(Intent.EXTRA_TEXT, builder.toString())
        }
        startActivity(Intent.createChooser(intent, getString(R.string.export_all)))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_note_list, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val sortItemId = when (prefs.sort) {
            NoteSort.UPDATED_DESC -> R.id.sort_updated_desc
            NoteSort.UPDATED_ASC -> R.id.sort_updated_asc
            NoteSort.TITLE_ASC -> R.id.sort_title
        }
        menu.findItem(sortItemId)?.isChecked = true
        menu.findItem(R.id.action_theme)?.title =
            getString(if (prefs.darkTheme) R.string.menu_theme_light else R.string.menu_theme_dark)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_updated_desc -> {
                changeSort(NoteSort.UPDATED_DESC)
                true
            }

            R.id.sort_updated_asc -> {
                changeSort(NoteSort.UPDATED_ASC)
                true
            }

            R.id.sort_title -> {
                changeSort(NoteSort.TITLE_ASC)
                true
            }

            R.id.action_theme -> {
                prefs.darkTheme = !prefs.darkTheme
                AppCompatDelegate.setDefaultNightMode(
                    if (prefs.darkTheme) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_NO
                    }
                )
                true
            }

            R.id.action_export_all -> {
                exportAll()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun changeSort(sort: NoteSort) {
        if (prefs.sort == sort) return
        prefs.sort = sort
        loadNotes()
        invalidateOptionsMenu()
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}

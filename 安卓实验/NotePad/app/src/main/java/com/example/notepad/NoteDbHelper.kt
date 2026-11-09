package com.example.notepad

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDbHelper(context: Context) : SQLiteOpenHelper(context.applicationContext, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "notepad.db"
        private const val DB_VERSION = 1
        private const val TABLE = "notes"

        private const val COL_ID = "_id"
        private const val COL_TITLE = "title"
        private const val COL_CONTENT = "content"
        private const val COL_CREATED = "created"
        private const val COL_UPDATED = "updated"
        private const val COL_COLOR = "color"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_TITLE TEXT NOT NULL DEFAULT '', " +
                "$COL_CONTENT TEXT NOT NULL DEFAULT '', " +
                "$COL_CREATED INTEGER NOT NULL DEFAULT 0, " +
                "$COL_UPDATED INTEGER NOT NULL DEFAULT 0, " +
                "$COL_COLOR INTEGER NOT NULL DEFAULT 0)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    fun query(keyword: String, sort: NoteSort): List<Note> {
        val selection: String?
        val args: Array<String>?
        if (keyword.isBlank()) {
            selection = null
            args = null
        } else {
            selection = "($COL_TITLE LIKE ? OR $COL_CONTENT LIKE ?)"
            args = arrayOf("%$keyword%", "%$keyword%")
        }
        readableDatabase.query(TABLE, null, selection, args, null, null, sort.orderBy).use { cursor ->
            val notes = ArrayList<Note>(cursor.count)
            while (cursor.moveToNext()) {
                notes.add(readNote(cursor))
            }
            return notes
        }
    }

    fun getAll(sort: NoteSort): List<Note> = query("", sort)

    fun getById(id: Long): Note? {
        readableDatabase.query(
            TABLE, null, "$COL_ID = ?", arrayOf(id.toString()), null, null, null
        ).use { cursor ->
            return if (cursor.moveToFirst()) readNote(cursor) else null
        }
    }

    fun insert(title: String, content: String, color: Int, now: Long): Long {
        val values = android.content.ContentValues().apply {
            put(COL_TITLE, title)
            put(COL_CONTENT, content)
            put(COL_CREATED, now)
            put(COL_UPDATED, now)
            put(COL_COLOR, color)
        }
        return writableDatabase.insert(TABLE, null, values)
    }

    fun update(id: Long, title: String, content: String, color: Int, now: Long) {
        val values = android.content.ContentValues().apply {
            put(COL_TITLE, title)
            put(COL_CONTENT, content)
            put(COL_UPDATED, now)
            put(COL_COLOR, color)
        }
        writableDatabase.update(TABLE, values, "$COL_ID = ?", arrayOf(id.toString()))
    }

    fun delete(id: Long) {
        writableDatabase.delete(TABLE, "$COL_ID = ?", arrayOf(id.toString()))
    }

    private fun readNote(cursor: android.database.Cursor): Note = Note(
        id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)),
        title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
        content = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTENT)),
        created = cursor.getLong(cursor.getColumnIndexOrThrow(COL_CREATED)),
        updated = cursor.getLong(cursor.getColumnIndexOrThrow(COL_UPDATED)),
        color = cursor.getInt(cursor.getColumnIndexOrThrow(COL_COLOR))
    )
}

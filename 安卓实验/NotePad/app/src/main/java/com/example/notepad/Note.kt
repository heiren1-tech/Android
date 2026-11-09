package com.example.notepad

data class Note(
    val id: Long = 0L,
    val title: String = "",
    val content: String = "",
    val created: Long = 0L,
    val updated: Long = 0L,
    val color: Int = 0
)

enum class NoteSort(val key: String, val label: String, val orderBy: String) {
    UPDATED_DESC("updated_desc", "按修改时间（新→旧）", "updated DESC, _id DESC"),
    UPDATED_ASC("updated_asc", "按修改时间（旧→新）", "updated ASC, _id ASC"),
    TITLE_ASC("title_asc", "按标题（A→Z）", "title COLLATE NOCASE ASC, updated DESC");

    companion object {
        fun fromKey(key: String?): NoteSort = entries.firstOrNull { it.key == key } ?: UPDATED_DESC
    }
}

package com.example.notepad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    private val onClick: (Note) -> Unit,
    private val onLongClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.Holder>() {

    private var notes: List<Note> = emptyList()

    fun submitList(list: List<Note>) {
        notes = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val note = notes[position]
        holder.bind(note)
        holder.itemView.setOnClickListener { onClick(note) }
        holder.itemView.setOnLongClickListener {
            onLongClick(note)
            true
        }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val card: CardView = itemView.findViewById(R.id.item_card)
        private val titleView: TextView = itemView.findViewById(R.id.item_title)
        private val previewView: TextView = itemView.findViewById(R.id.item_preview)
        private val timeView: TextView = itemView.findViewById(R.id.item_time)

        fun bind(note: Note) {
            val context = itemView.context
            val title = note.title.trim()
            val content = note.content.trim()

            titleView.text = title.ifEmpty { context.getString(R.string.note_untitled) }
            previewView.text = content.replace('\n', ' ').ifEmpty { context.getString(R.string.note_empty) }
            timeView.text = TimeUtils.format(note.updated)

            if (note.color != 0) {
                card.setCardBackgroundColor(note.color)
                titleView.setTextColor(0xFF212121.toInt())
                previewView.setTextColor(0xFF5F6368.toInt())
                timeView.setTextColor(0xFF757575.toInt())
            } else {
                card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_bg))
                titleView.setTextColor(ThemeUtils.resolveColor(context, android.R.attr.textColorPrimary))
                previewView.setTextColor(ThemeUtils.resolveColor(context, android.R.attr.textColorSecondary))
                timeView.setTextColor(ThemeUtils.resolveColor(context, android.R.attr.textColorSecondary))
            }
        }
    }
}

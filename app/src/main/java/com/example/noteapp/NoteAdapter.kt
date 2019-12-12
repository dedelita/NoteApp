package com.example.noteapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.note_item.view.*

class NoteAdapter(private val items: ArrayList<Note>, private val context: Context, private val dbHelper: MyDbHelper) :
    RecyclerView.Adapter<NoteAdapter.NoteItemViewHolder>() {
    private var deletedItem: Note = Note()
    private var deletedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemViewHolder {
        return NoteItemViewHolder(parent.inflate(R.layout.note_item, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NoteItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class NoteItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private var iv: View = itemView
        private var note: Note? = null


        init {
            iv.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val context = v.context
            val showNoteIntent = Intent(context, DisplayNoteActivity::class.java).apply {
                putExtra(EXTRA_NOTE, note)
            }
            context.startActivity(showNoteIntent)
        }

        fun bind(item: Note) = with(itemView) {
            note = item
            iv.ll_note.title.text = item.title
            iv.ll_note.datetime.text = item.datetime
        }
    }

    fun removeAt(position: Int, rv: RecyclerView) {
        deletedItem = items[position]
        deletedPosition = position
        items.removeAt(position)
        notifyItemRemoved(position)
        showUndoSnackbar(rv)
    }

    private fun showUndoSnackbar(rv: RecyclerView) {
        val snackBarUndo = Snackbar.make(rv, R.string.deleted, Snackbar.LENGTH_LONG)

        snackBarUndo.setActionTextColor(Color.parseColor("#FFFFFF"))
        snackBarUndo.setAction(context.getString(R.string.undo)) { undoDelete() }
        snackBarUndo.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                    dbHelper.deleteNote(deletedItem.id)
                }
                super.onDismissed(transientBottomBar, event)
            }
        })
        snackBarUndo.show()
    }

    private fun undoDelete() {
        items.add(deletedPosition, deletedItem)
        notifyItemInserted(deletedPosition)
    }
}
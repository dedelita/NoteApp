package com.example.noteapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit_note.*

class EditNoteActivity : AppCompatActivity() {
    private var dbHelper: MyDbHelper? = null
    private var note: Note = Note()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        dbHelper = MyDbHelper(this, null)

        note = intent.getParcelableExtra(EXTRA_NOTE) as Note
        etNewContent.text = Editable.Factory.getInstance().newEditable(note.content)
        etNewTitle.text = Editable.Factory.getInstance().newEditable(note.title)

        val contentView: View = findViewById(R.id.EditNote)
        contentView.setOnClickListener {
            it.hideKeyboard()
        }
    }

    fun editNote(view: View) {
        note.title = etNewTitle.text.toString()
        note.content = etNewContent.text.toString()
        val success = dbHelper!!.updateNote(note)
        if(success) {
            Toast.makeText(this, note.title + "Note updated", Toast.LENGTH_LONG).show()
            val intent = Intent(this, DisplayNoteActivity::class.java).apply {
                putExtra(EXTRA_NOTE, note)
            }
            startActivity(intent)
        }
    }
}

private fun View.hideKeyboard() {
    val inputMethodManager = context!!.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
}
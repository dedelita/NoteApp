package com.example.noteapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_new_note.*

class EditNoteActivity : AppCompatActivity() {
    private var dbHelper: MyDbHelper? = null
    private var note: Note = Note()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
        dbHelper = MyDbHelper(this, null)

        btnAddNote.setOnClickListener{
            val newtitle : String = etTitle.text.toString().trim()
            val newcontent : String = etContent.text.toString().trim()

            if(newtitle.isNotEmpty() && newcontent.isNotEmpty()) {
                editNote(it)
            }else{
                if(newtitle.isEmpty())
                    etTitle.error = "Title required"
                if(newcontent.isEmpty())
                    etContent.error = "Text required"
            }
        }

        note = intent.getParcelableExtra(EXTRA_NOTE) as Note
        etContent.text = Editable.Factory.getInstance().newEditable(note.content)
        etTitle.text = Editable.Factory.getInstance().newEditable(note.title)

        val contentView: View = findViewById(R.id.NewNote)
        contentView.setOnClickListener {
            it.hideKeyboard()
        }
    }

    private fun editNote(view: View) {
        view.requestFocus()
        note.title = etTitle.text.toString().trim()
        note.content = etContent.text.toString().trim()
        val success = dbHelper!!.updateNote(note)
        if(success) {
            Toast.makeText(this, "Note updated", Toast.LENGTH_LONG).show()
            val intent = Intent(this, DisplayNoteActivity::class.java).apply {
                putExtra(EXTRA_NOTE, note)
            }
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage(R.string.sure)
            .setPositiveButton(R.string.yes) { _, _ ->
                startActivity(Intent(this, DisplayNoteActivity::class.java).apply {
                    this.putExtra(EXTRA_NOTE, note)
                })
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

private fun View.hideKeyboard() {
    val inputMethodManager = context!!.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
}
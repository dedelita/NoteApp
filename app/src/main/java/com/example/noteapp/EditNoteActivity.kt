package com.example.noteapp

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
            //read value from EditText to a String variable
            val newtitle : String = etTitle.text.toString()
            val newcontent : String = etContent.text.toString()

            //check if the EditText have values or not
            if(newtitle.trim().isNotEmpty() && newcontent.trim().isNotEmpty()) {
                editNote(it)
            }else{
                if(newtitle.trim().isEmpty())
                    etTitle.error = "Title required"
                if(newcontent.trim().isEmpty())
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

    fun editNote(view: View) {
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
}

private fun View.hideKeyboard() {
    val inputMethodManager = context!!.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
}
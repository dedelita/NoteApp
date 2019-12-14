package com.example.noteapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_new_note.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class NewNoteActivity : AppCompatActivity() {
    private var dbHelper: MyDbHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
        dbHelper = MyDbHelper(this, null)


        btnAddNote.setOnClickListener {
            //read value from EditText to a String variable
            val newtitle: String = etTitle.text.toString()

            //check if the EditText have values or not
            if (newtitle.trim().isNotEmpty()) {
                addNewNote()
            } else {
                etTitle.error = "Title required"
            }
        }
        val contentView: View = findViewById(R.id.NewNote)
        contentView.setOnClickListener {
            it.hideKeyboard()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun addNewNote() {
        dbHelper = MyDbHelper(this, null)
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        val note = Note()
        note.title = etTitle.text.toString().trim()
        note.content = etContent.text.toString().trim()
        note.datetime = current.format(formatter)
        val success = dbHelper!!.addNote(note)

        if (success) {
            Toast.makeText(
                this, note.title + " Added at " + note.datetime,
                Toast.LENGTH_LONG
            ).show()
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
                startActivity(Intent(this, MainActivity::class.java))
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    fun View.hideKeyboard() {
        val inputMethodManager =
            context!!.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
    }
}
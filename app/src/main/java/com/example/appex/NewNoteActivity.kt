package com.example.appex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_edit_note.*
import kotlinx.android.synthetic.main.activity_new_note.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NewNoteActivity : AppCompatActivity() {
    private var dbHelper: MyDbHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
        dbHelper = MyDbHelper(this, null)


        btnAddNote.setOnClickListener{
            //read value from EditText to a String variable
            val newtitle : String = etNewTitle.text.toString()

            //check if the EditText have values or not
            if(newtitle.trim().length>0) {
                Toast.makeText(applicationContext, "Message : "+newtitle, Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, "Please enter some message! ", Toast.LENGTH_SHORT).show()
                etNewTitle.error = "Required"
            }
        }
        val contentView: View = findViewById(R.id.NewNote)
        contentView.setOnClickListener {
            it.hideKeyboard()
        }

    }
        fun addNewNote(view: View) {
            dbHelper = MyDbHelper(this, null)
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

            val note = Note()
            note.title = etTitle.text.toString()
            note.content = etContent.text.toString()
            note.datetime = current.format(formatter)
            val success = dbHelper!!.addNote(note)

            if (success) {
                Toast.makeText(
                    this,
                    note.title + " Added to database at " + note.datetime,
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(this, DisplayNoteActivity::class.java).apply {
                    putExtra(EXTRA_NOTE, note)
                }
                startActivity(intent)
            }
        }

    fun View.hideKeyboard() {
        val inputMethodManager =
            context!!.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
    }
}
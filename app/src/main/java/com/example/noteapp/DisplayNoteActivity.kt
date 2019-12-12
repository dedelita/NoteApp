package com.example.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_display_note.*

class DisplayNoteActivity : AppCompatActivity() {
    private var dbHelper: MyDbHelper? = null
    var note: Note = Note()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_note)
        dbHelper = MyDbHelper(this, null)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        val actionbar = supportActionBar!!
        actionbar.setDisplayHomeAsUpEnabled(true)

        note = intent.getParcelableExtra(EXTRA_NOTE) as Note
        title = note.title
        tv_note_datetime.text = note.datetime
        tv_note_content.text = note.content
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_display_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_edit -> {
            val intent = Intent(this, EditNoteActivity::class.java).apply {
                putExtra(EXTRA_NOTE, note)
            }
            startActivity(intent)
            true
        }

        R.id.action_delete -> {
            val success = dbHelper!!.deleteNote(note.id)
            if(success)
                Toast.makeText(this, R.string.deleted, Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
            true
        }

        R.id.action_share -> {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                //putExtra(Intent.EXTRA_TITLE, note.title)
                putExtra(Intent.EXTRA_TEXT, note.content)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
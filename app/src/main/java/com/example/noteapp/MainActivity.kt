package com.example.noteapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

const val EXTRA_NOTE = "com.example.noteapp.NOTE"
class MainActivity : AppCompatActivity() {
    private var dbHelper: MyDbHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab.setOnClickListener {
            startActivity(Intent(this, NewNoteActivity::class.java))
        }

        rv_note.layoutManager = LinearLayoutManager(this)
        rv_note.adapter = NoteAdapter(getNotes(), this, dbHelper!!)

        swipe_refresh_layout.setOnRefreshListener {
            rv_note.adapter = NoteAdapter(getNotes(), this, dbHelper!!)
                swipe_refresh_layout.isRefreshing = false
        }

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapt = rv_note.adapter as NoteAdapter
                adapt.removeAt(viewHolder.adapterPosition, rv_note)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rv_note)
    }

    private fun getNotes(): ArrayList<Note> {
        dbHelper = MyDbHelper(this, null)

        val cursor = dbHelper!!.getAllNotes()
        val listNotes = arrayListOf<Note>()
        var note = Note()

        cursor!!.moveToFirst()
        if (cursor.count > 0) {
            note.id = cursor.getInt(cursor.getColumnIndex(MyDbHelper.COLUMN_ID))
            note.title = cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_TITLE))
            note.content = cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_CONTENT))
            note.datetime = cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_DATETIME))
            listNotes.add(note)
        }
        while (cursor.moveToNext()) {
            note = Note()
            note.id = cursor.getInt(cursor.getColumnIndex(MyDbHelper.COLUMN_ID))
            note.title = cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_TITLE))
            note.content = cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_CONTENT))
            note.datetime = cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_DATETIME))
            listNotes.add(note)
        }
        cursor.close()
        return listNotes
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}
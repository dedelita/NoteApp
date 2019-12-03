package com.example.appex


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDbHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE $TABLE_NAME " +
                "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "$COLUMN_TITLE TEXT, " +
                "$COLUMN_DATETIME TEXT, " +
                "$COLUMN_CONTENT TEXT)")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }
    fun addNote(note: Note): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_TITLE, note.title)
        values.put(COLUMN_CONTENT, note.content)
        values.put(COLUMN_DATETIME, note.datetime)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    fun deleteNote(id: Int): Boolean {
        val db = this.writableDatabase  
		val _success = db.delete(TABLE_NAME, "$COLUMN_ID =?", arrayOf(id.toString())).toLong()
		db.close()  
		return Integer.parseInt("$_success") != -1  
    }

    fun updateNote(note: Note): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_TITLE, note.title)
        values.put(COLUMN_CONTENT, note.content)

        val _success = db.update(TABLE_NAME, values, "$COLUMN_ID =" + note.id, null).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun getAllNotes(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "test.db"
        val TABLE_NAME = "note"
        val COLUMN_ID = "_id"
        val COLUMN_TITLE = "title"
        val COLUMN_CONTENT = "content"
        val COLUMN_DATETIME = "datetime"
    }
}
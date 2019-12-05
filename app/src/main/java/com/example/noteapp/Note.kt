package com.example.noteapp

import android.os.Parcel
import android.os.Parcelable

class Note() : Parcelable {
    var id: Int = 0
    var title: String? = null
    var content: String? = null
    var datetime: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString()
        content = parcel.readString()
        datetime = parcel.readString()
    }

    override fun toString(): String {
        return "$id $title"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeString(datetime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }

}
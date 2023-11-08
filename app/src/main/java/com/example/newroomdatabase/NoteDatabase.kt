package com.example.newroomdatabase


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Datas::class], version = 1, exportSchema = false)
abstract class NoteDatabase:RoomDatabase() {

    companion object
    {

        private  var  INSTANCE:NoteDatabase?=null

        fun getInstance(context: Context):NoteDatabase?
        {
            if (INSTANCE==null) {
                synchronized(NoteDatabase::class.java)
                {
                    INSTANCE= Room.databaseBuilder(context,NoteDatabase::class.java,"NewDataBase") .build()
                    return INSTANCE
                }
            }
            else{
                return INSTANCE
            }
        }
    }

    abstract fun Notedao():NoteDao
}
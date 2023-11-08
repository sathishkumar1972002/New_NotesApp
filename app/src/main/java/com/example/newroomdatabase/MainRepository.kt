package com.example.newroomdatabase

import androidx.lifecycle.LiveData

class MainRepository(private val noteDao: NoteDao) {


    fun getAllData(): List<Datas> {
        return noteDao.getAllData()
    }
    fun getAllLive(): LiveData<List<Datas>> {
       return noteDao.getAllDataLive()
    }

    fun delete_noteByIds(datas: List<Int>) {
        noteDao.deletebyIds(datas)
    }

    suspend fun deleteAllnote() {
        noteDao.deleteAllData()
    }

    fun addNotes(datas: Datas) {
        noteDao.insert(datas)
    }

    fun updateNote(datas: Datas){
          noteDao.update(datas)
    }

    fun delete_UpdateNotebyId(id : Int) {
      noteDao.deletebyId(id)
    }
    fun delete(datas: Datas){
        noteDao.delete(datas)
    }
}
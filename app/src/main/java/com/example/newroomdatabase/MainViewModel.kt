package com.example.newroomdatabase

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel(val repository: MainRepository):ViewModel() {


      fun getAllData(): List<Datas> {
          return repository.getAllData()
      }

      fun getAllLive():LiveData<List<Datas>> {
          return  repository.getAllLive()
      }

      fun delete_noteByIds(tmp:ArrayList<Int>) {
        CoroutineScope(Dispatchers.IO).launch {
            val datas = tmp
            repository.delete_noteByIds(datas)
            // To clear the selected list
        }
      }

       fun deleteAllnote() {
          CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAllnote()

          }
        }

       fun addNote(datas: Datas) {
           repository.addNotes(datas)
       }

       fun updateNote(datas: Datas){
           repository.updateNote(datas)
       }

       fun delete_UpdateNotebyId(id : Int) {
           repository.delete_UpdateNotebyId(id)
       }
}
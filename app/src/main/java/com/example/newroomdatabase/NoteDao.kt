package com.example.newroomdatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface NoteDao {

    ///insert
    @Insert
    fun insert(note:Datas)

    ///Update
    @Update
    fun update(note: Datas)

    ///Delete
    @Delete
    fun delete(note: Datas)

    ///Get data or Fetch DAta
    @Query("Select * from noteTable  order by id desc")
    fun getAllData():List<Datas>

    @Query("Select * from noteTable  order by id desc")
    fun getAllDataLive():LiveData<List<Datas>>

    @Query("delete from noteTable ")
    fun deleteAllData()

    @Query("delete from noteTable where id like :id1")
    fun deletebyId(id1:Int)

    @Query("delete from noteTable where id in (:id1)")
    fun deletebyIds(id1:List<Int>)


}
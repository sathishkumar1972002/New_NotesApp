package com.example.newroomdatabase


import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "noteTable")
data class Datas(

    @PrimaryKey(autoGenerate = true)
    var Id:Int,
    var title_content:String,
    var body_content:String,
    var date:String,

)

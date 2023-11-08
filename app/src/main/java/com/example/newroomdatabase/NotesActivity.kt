package com.example.newroomdatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.newroomdatabase.databinding.ActivityNotesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotesActivity : AppCompatActivity() {

    private lateinit var bind : ActivityNotesBinding

    ///////////////////

    private lateinit var tit:String
    private lateinit var bdy:String
    lateinit var repository:MainRepository
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(bind.root)

        var noteDao = NoteDatabase.getInstance(this)?.Notedao()
        repository = MainRepository(noteDao!!)
        viewModel = MainViewModel(repository)

        bind.deleteButton.hide()

        if (intent.hasExtra("ID"))
        {
            bind.ipbody.setText(intent.getStringExtra("BODY").toString())
            bind.iptitle.setText(intent.getStringExtra("TITLE").toString())
            bind.deleteButton.show()
        }
        bind.deleteButton.setOnClickListener {
            showAlert()

        }

        bind.add.setOnClickListener{

            tit = bind.iptitle.text.toString()
            bdy = bind.ipbody.text.toString()

            if (validate()) {

                if(intent.hasExtra("ID"))
                {
                    update_note()
                }
                else {
                    saveNote()
                    // Toast.makeText(this,"Updated ", Toast.LENGTH_SHORT).show()
                    bind.iptitle.text = null
                    bind.ipbody.text = null

                    finish()
                }
            }

        }

        bind.back.setOnClickListener {
            finish()
        }

    }

    private fun delete_UpdateNoteById() {
        CoroutineScope(Dispatchers.IO).launch {
            val id = intent.getIntExtra("ID",0)
            viewModel.delete_UpdateNotebyId(id)
            withContext(Dispatchers.Main)
            {
                Toast.makeText(this@NotesActivity,"Deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun update_note() {

        var date1 =intent.getStringExtra("DATE")
        var ID = intent.getIntExtra("ID",0)
        CoroutineScope(Dispatchers.IO).launch {
            val datas = Datas(ID,tit,bdy, date1!!)
            viewModel.updateNote(datas)
            withContext(Dispatchers.Main)
            {
                Toast.makeText(this@NotesActivity,"Updated ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveNote() {
        val date = SimpleDateFormat("dd MMM yy , hh:mm a", Locale.getDefault()).format(Date())
        val datas = Datas(0,tit,bdy,date)

        GlobalScope.launch {
            viewModel.addNote(datas)
            withContext(Dispatchers.Main)
            {
                Toast.makeText(this@NotesActivity,"Added", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun validate():Boolean
    {
        if (tit.trim().isNullOrEmpty() or bdy.trim().isNullOrEmpty())
        {
            Toast.makeText(this,"Not allowed to add empty notes ", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun showAlert() {

        val alert = AlertDialog.Builder(this)
            .setTitle("Delete")
            .setMessage("Do you want to delete this note?")
            .setPositiveButton("Yes")
            { p0, p1 ->
                delete_UpdateNoteById()
            }
            .setNegativeButton("No",null)
            .show()

    }


}
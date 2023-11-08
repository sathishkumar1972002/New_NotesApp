package com.example.newroomdatabase

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newroomdatabase.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


var tmp = ArrayList<Int>()
class MainActivity : AppCompatActivity(),ToAdapter.NoteClickListener {

    lateinit var bind: ActivityMainBinding
    lateinit var adapter: ToAdapter
    lateinit var list: List<Datas>
    lateinit var viewModel : MainViewModel
    lateinit var repository : MainRepository

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind= ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        var noteDao = NoteDatabase.getInstance(this)?.Notedao()
        repository = MainRepository(noteDao!!)
        viewModel = MainViewModel(repository)


        viewModel.getAllLive()?.observe(this, Observer {
            setData()
        })


          bind.deletemultiple.hide()


        bind.recyclerView.setHasFixedSize(true)
        bind.recyclerView.layoutManager = LinearLayoutManager(this)

        bind.add.setOnClickListener {
            var intent= Intent(this,NotesActivity::class.java)
            startActivity(intent)

        }

        bind.deletemultiple.setOnClickListener {
            if(!tmp.isEmpty())
                showAlert1()
        }

        bind.menu.setOnClickListener {
            val Menu1 = PopupMenu(this, bind.menu)
            Menu1.menuInflater.inflate(R.menu.toolbar,Menu1.menu)
            Menu1.setOnMenuItemClickListener { item ->
                when(item.itemId)  //// we initiate which menu file we want to use
                {
                    R.id.deleteAll ->
                    {
                        if (list.isEmpty())
                        {
                            Toast.makeText(this,"Note is Empty", Toast.LENGTH_SHORT).show()
                            false
                        }
                        else {
                            showAlert()
                            true
                        }
                    }

                    else -> false
                }

            }
            Menu1.show()
        }


    }



    @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
    fun setData()   // get all data from room database and set to recycle view
    {
            val databass = NoteDatabase.getInstance(this)
            val dao = databass!!.Notedao()
            CoroutineScope(Dispatchers.IO).launch { //// Database operation run in coroutine not in main thread
                list = viewModel.getAllData()

            withContext(Dispatchers.Main)
            {
                adapter= ToAdapter(list,this@MainActivity)
                bind.recyclerView.adapter= adapter
                adapter.notifyDataSetChanged()
            }
        }


    }

    override fun itemClick(position: Int) {
        var intent= Intent(this,NotesActivity::class.java).also {
            it.putExtra("position",position)
            it.putExtra("ID",list[position].Id)
            it.putExtra("TITLE",list[position].title_content)
            it.putExtra("BODY",list[position].body_content)
            it.putExtra("DATE",list[position].date)
            startActivity(it)
        }
    }

    override fun itemClicktodelete(listId: ArrayList<Int>) {
        tmp.clear()
      if(listId.size!=0) {

          bind.add.hide()
          bind.deletemultiple.show()

          Log.d("sathish","$listId  size  ${listId.size}")
          for (i in listId)
              tmp.add(list[i].Id)

      }
         else
       {
          bind.add.show()
          bind.deletemultiple.hide()
       }
    }




    private fun showAlert() {

        val alert = AlertDialog.Builder(this)
            .setTitle("Delete All")
            .setMessage("Do you want to delete all notes?")
            .setPositiveButton("Yes")
            { p0, p1 ->
               viewModel.deleteAllnote()
            }
            .setNegativeButton("No",null)
            .show()

    }

    private fun showAlert1(){

        val alert = AlertDialog.Builder(this)
            .setTitle("Delete")
            .setMessage("Do you want to delete selected notes?")
            .setPositiveButton("Yes")
            { p0, p1 ->
                viewModel.delete_noteByIds(tmp)
                adapter.clearSelected()  // To clear the selected list of notes
                bind.add.show()
                bind.deletemultiple.hide()
            }
            .setNegativeButton("No",null)
            .show()

    }

    override fun onBackPressed() {
       adapter.clearSelected()
        super.onBackPressed()
    }

}
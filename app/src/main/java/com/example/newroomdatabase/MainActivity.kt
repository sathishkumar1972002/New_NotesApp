package com.example.newroomdatabase

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newroomdatabase.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


var tmp = ArrayList<Int>()
var searchlist1 = ArrayList<Datas>()
var searchlistId = ArrayList<Int>()
var selectAllBoolean = false // like a flag to check multiselect is done are not
class MainActivity : AppCompatActivity(),ToAdapter.NoteClickListener {

    lateinit var bind: ActivityMainBinding
    lateinit var adapter: ToAdapter
    lateinit var list: List<Datas>
    lateinit var viewModel : MainViewModel
    lateinit var repository : MainRepository
    lateinit var toolbar: Toolbar
    lateinit var deletemultiple:FloatingActionButton
    lateinit var deletemultipleLayout:ConstraintLayout
    lateinit var recyclerView : RecyclerView
    lateinit var add : FloatingActionButton
    lateinit var closeSelect : FloatingActionButton
    lateinit var selectAll : FloatingActionButton
    lateinit var selectText : TextView
    lateinit var searchView: SearchView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind= ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        var noteDao = NoteDatabase.getInstance(this)?.Notedao()
        repository = MainRepository(noteDao!!)
        viewModel = MainViewModel(repository)
        ////set view by ID
        val tmpView = bind.root
        toolbar = viewBind(tmpView,R.id.toolbar) as Toolbar
        deletemultiple = viewBind(tmpView,R.id.deletemultiple) as FloatingActionButton
        deletemultipleLayout = viewBind(tmpView,R.id.deletemultipleLayout) as ConstraintLayout
        recyclerView = viewBind(tmpView,R.id.recyclerView) as RecyclerView
        add = viewBind(tmpView,R.id.add) as FloatingActionButton
        closeSelect = viewBind(tmpView,R.id.closeSelect) as FloatingActionButton
        selectAll = viewBind(tmpView,R.id.selectAll) as FloatingActionButton
        selectText = viewBind(tmpView,R.id.selectText) as TextView

        setSupportActionBar(toolbar)


        viewModel.getAllLive()?.observe(this, Observer {
            setData()
        })

          deletemultiple.hide() // button visible only multiselect
        deletemultipleLayout.visibility=View.INVISIBLE

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        add.setOnClickListener {
            if (searchlistId.isEmpty()) {
                var intent = Intent(this, NotesActivity::class.java)
                startActivity(intent)
            }
            else Toast.makeText(this,"Not allowed to add note",Toast.LENGTH_SHORT).show()

        }

        deletemultiple.setOnClickListener {
            Log.d("sathish", "onCreate: $tmp")
            if(!tmp.isEmpty())
                showAlert1()
        }
        closeSelect.setOnClickListener {

            adapter.setBackgroundColor(Color.WHITE)
            tmp.clear()
            adapter.clearSelected()

        }
        selectAll.setOnClickListener {

          if(searchlist1.isEmpty())
          {
             selectAllBoolean = if(tmp.size==list.size) true
                              else  false

             if(selectAllBoolean)
             {
                 adapter.setBackgroundColor(Color.WHITE)
                 tmp.clear()
                 adapter.clearSelected()
                 selectAllBoolean = false
             }
             else {
                 adapter.setBackgroundColor(Color.BLUE)
                 adapter.setAllDataForSelectAll()
                 selectAllBoolean = true
             }
         }
            else
         {
             if(tmp.size== searchlist1.size) selectAllBoolean = true
             else selectAllBoolean = false

             if(selectAllBoolean)
             {
                 adapter.setBackgroundColor(Color.WHITE)
                 tmp.clear()
                 adapter.clearSelected()
                 selectAllBoolean = false
             }
             else {
                 adapter.setBackgroundColor(Color.BLUE)
                 adapter.setAllDataForSelectAll()
                 selectAllBoolean = true
             }
         }
        }


    }

    fun viewBind(rootView: View, id: Int):View {
        val missingView = rootView.findViewById<View>(id)
        if (missingView == null) {
            val missingId = rootView.resources.getResourceName(id)
            throw NullPointerException("Missing required view with ID: $missingId")
        }
        else
            return missingView
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

              // ItemTouchHelper(callBack).attachToRecyclerView(bind.recyclerView)   //attach swipe future to recycler view
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

      if(listId.size!=0) {
          tmp.clear()
          showSelectedLayout()

          Log.d("sathish","$listId  size  ${listId.size}")

          if(searchlistId.isEmpty()) {   //  while searching result will be new list we need to check search is active or not
              for (i in listId)
                  tmp.add(list[i].Id)

          }
          else
          {
              for (i in listId)
                  tmp.add(searchlistId[i])
          }
          if(tmp.size==1)  selectText.setText("${tmp.size} item selected") // it shows no of item selected
          else selectText.setText("${tmp.size} items selected")
      }
         else
       {
           adapter.setBackgroundColor(Color.WHITE)
           showToolbar()
       }
    }




    private fun showAlert() {

        val alert = AlertDialog.Builder(this)
            .setTitle("Delete All")
            .setMessage("Do you want to delete all notes?")
            .setPositiveButton("Yes")
            { p0, p1 ->
              CoroutineScope(Dispatchers.IO).launch {
                  viewModel.deleteAllnote()
              }
            }
            .setNegativeButton("No",null)
            .show()

    }

    private fun showAlert1(){

        if(searchlistId.isEmpty())
        {
            val alert = AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Do you want to delete selected notes?")
                .setPositiveButton("Yes")
                { p0, p1 ->
                    viewModel.delete_noteByIds(tmp)
                    adapter.clearSelected()  // To clear the selected list of notes
                    showToolbar()
                    adapter.setBackgroundColor(Color.WHITE)  // After delete the selected notes change to unselected color

                }
                .setNegativeButton("No",null)
                .show()
        }
        else
        {      // while searching perform multiselect we need to clear searchView focus
            val alert = AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Do you want to delete selected notes?")
                .setPositiveButton("Yes")
                { p0, p1 ->
                    viewModel.delete_noteByIds(tmp)
                    adapter.clearSelected()  // To clear the selected list of notes
                    showToolbar()
                    adapter.setBackgroundColor(Color.WHITE)  // After delete the selected notes change to unselected color
                    searchlist1.clear()
                    searchlistId.clear()
                    searchView.clearFocus()
                    searchView.isIconified=true
                }
                .setNegativeButton("No",null)
                .show()
        }


    }

//   private fun showAlert_whileSwipe(id:Int){
//
//        val alert = AlertDialog.Builder(this)
//            .setTitle("Delete")
//            .setMessage("Do you want to delete this note?")
//            .setPositiveButton("Yes")
//            { p0, p1 ->
//                CoroutineScope(Dispatchers.IO).launch {
//                    viewModel.delete_UpdateNotebyId(id)
//                }
//            }
//            .setNegativeButton("No",null)
//            .show()
//
//    }


   // Swipe to delete

//       private val callBack = object:ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
//        override fun onMove(
//            recyclerView: RecyclerView,
//            viewHolder: RecyclerView.ViewHolder,
//            target: RecyclerView.ViewHolder
//        ): Boolean {
//            TODO("Not yet implemented")
//        }
//
//        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//
//            if(tmp.size==0) {
//                showAlert_whileSwipe(list[viewHolder.adapterPosition].Id)
//            }
//            else
//            {
//                Toast.makeText(this@MainActivity,"Swipe delete not allowed",Toast.LENGTH_SHORT).show()
//            }
//            adapter.notifyDataSetChanged()
//        }
//
//           override fun isItemViewSwipeEnabled(): Boolean {
//               return (tmp.isEmpty() && searchlist1.isEmpty())
//           }
//
//    }


    override fun onBackPressed() { // when I close app selected items will clear
        adapter.clearSelected()
        super.onBackPressed()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

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
                    if(searchlistId.isEmpty() && searchlist1.isEmpty())
                    {
                        showAlert()
                        true
                    }
                    else
                    {
                        tmp.clear()
                        for (i in searchlist1)
                            tmp.add(i.Id)

                        Log.d("sathish", "onOptionsItemSelected: $searchlistId")
                        Log.d("sathish", "onOptionsItemSelected: $searchlist1")
                        showAlert1()
                        true
                    }

                }
            }
            R.id.search ->
            {
                searchView = item.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                         searchlistId.clear()      // list of search results iD
                        searchlist1.clear()     // list of search results
                         tmp.clear()
                        if (!newText!!.trim().isNullOrEmpty())
                        {
                            for (i in list)
                            {
                                if ((i.title_content.lowercase().contains(newText.lowercase())) or (i.body_content.lowercase().contains(newText.lowercase())))
                                {
                                      searchlist1.add(i)
                                    searchlistId.add(i.Id)
                                }
                            }
                        }
                        else
                        {
                            searchlist1.addAll(list)
                        }

                        adapter.update(searchlist1)
                        return true
                    }

                })

                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

       private fun showToolbar()
       {
           add.show()
           deletemultiple.hide()
           deletemultipleLayout.visibility=View.INVISIBLE
           toolbar.visibility =View.VISIBLE
       }
        private fun showSelectedLayout()
        {
            add.hide()
            deletemultiple.show()
            deletemultipleLayout.visibility=View.VISIBLE
            toolbar.visibility =View.INVISIBLE
        }


}


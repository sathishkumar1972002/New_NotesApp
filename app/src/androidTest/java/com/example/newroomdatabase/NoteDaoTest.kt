package com.example.newroomdatabase

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.*
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import javax.inject.Inject
import org.mockito.Mockito.*



@RunWith(AndroidJUnit4::class)
@SmallTest

class NoteDaoTest
{
    lateinit var database: NoteDatabase
    lateinit var noteDao: NoteDao
    lateinit var repository: MainRepository
    lateinit var viewModel: MainViewModel
//    @Mock
//    lateinit var recyclerview: RecyclerView
//    @Mock
//    lateinit var adapter:RecyclerView.Adapter<*>
//    @Mock
//    lateinit var toAdapter:ToAdapter

    @Before
    fun setDatabase()
    {
        database= Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),NoteDatabase::class.java).allowMainThreadQueries().build()
       noteDao=database.Notedao()
        repository= MainRepository(noteDao)
        viewModel= MainViewModel(repository)
       // toAdapter = mock(ToAdapter::class.java)

    }
    @After
    fun drop()
    {
        database.close()
    }

    @Test
    fun insertNoteToList()
    {
        noteDao.insert(Datas(1,"test","content","12-06-2002"))
        var value=noteDao.getAllData()
        assertThat(value).contains(Datas(1,"test","content","12-06-2002"))
    }
    @Test
    fun displayNullWhenListIsEmpty()
    {
        var value=noteDao.getAllData()
        assertThat(value).isEmpty()
    }
    @Test
    fun displayAllNotes()
    {
        noteDao.insert(Datas(1,"test1","content1","12-06-2002"))
        noteDao.insert(Datas(2,"test2","content2","12-06-2002"))
        var value=noteDao.getAllData()
        var dataslist =ArrayList<Datas>()
        dataslist.add (Datas(1,"test1","content1","12-06-2002"))
        dataslist.add(Datas(2,"test2","content2","12-06-2002"))
        assertThat(value).containsExactlyElementsIn(dataslist)
    }
    @Test
    fun deleteOneNotefromlist()
    {
         var datas =  Datas(1,"test","content","12-06-2002")
        noteDao.insert(datas)
        noteDao.delete(datas)
        var value=noteDao.getAllData()
        assertThat(value).doesNotContain(datas)
    }
    @Test
    fun deleteAllNotesfromlist()
    {
        noteDao.insert(Datas(1,"test1","content1","12-06-2002"))
        noteDao.insert(Datas(2,"test2","content2","12-06-2002"))
        noteDao.insert(Datas(3,"test3","content3","12-06-2002"))
        noteDao.deleteAllData()
        var value=noteDao.getAllData()
        assertThat(value).isEmpty()
    }

    @Test
    fun deleteNotesByMultipleIDs_returnTrue()= runBlocking{

        noteDao.insert(Datas(1,"test1","content1","12-06-2002"))
        noteDao.insert(Datas(2,"test2","content2","12-06-2002"))
        noteDao.insert(Datas(3,"test3","content3","12-06-2002"))
        var idList = listOf<Int>(1,2)
        noteDao.deletebyIds(idList)
        var value=noteDao.getAllData()
        val dataslist = ArrayList<Datas>()
         dataslist.add (Datas(1,"test1","content1","12-06-2002"))
         dataslist.add(Datas(2,"test2","content2","12-06-2002"))
        assertThat(value).containsNoneIn(dataslist)
    }

    @Test
    fun deleteNotesByID_returnTrue()
    {

        noteDao.insert(Datas(1,"test1","content1","12-06-2002"))
        noteDao.insert(Datas(2,"test2","content2","12-06-2002"))
        noteDao.insert(Datas(3,"test3","content3","12-06-2002"))
        noteDao.deletebyId(2)
        var datas = Datas(2,"test2","content2","12-06-2002")
        var value=noteDao.getAllData()
        assertThat(value).doesNotContain(datas)
    }
    @Test
    fun updateNotes_returnTrue()
    {

        noteDao.insert(Datas(1,"test1","content1","12-06-2002"))
        noteDao.insert(Datas(2,"test2","content2","12-06-2002"))
        noteDao.insert(Datas(3,"test3","content3","12-06-2002"))
        var datas = Datas(2,"test2edited","content2edited","12-06-2002edited")
        noteDao.update(datas)
        var value=noteDao.getAllData()
        assertThat(value).contains(datas)
    }
    @Test
    fun updateNotesMismatchID_returnFalse()
    {

        noteDao.insert(Datas(1,"test1","content1","12-06-2002"))
        noteDao.insert(Datas(2,"test2","content2","12-06-2002"))
        noteDao.insert(Datas(3,"test3","content3","12-06-2002"))
        var datas = Datas(2,"test2edited","content2edited","12-06-2002edited")
        noteDao.update(datas)
        var value=noteDao.getAllData()
        assertThat(value).doesNotContain(Datas(2,"test2","content2","12-06-2002"))
    }
    @Test
    fun displayNullWhenListIsEmpty_inRepository()
    {
        var value=repository.getAllData()
        assertThat(value).isEmpty()
    }
    @Test
    fun getAllData_inRepository()
    {
        var datas =  Datas(1,"test","content","12-06-2002")
        repository.addNotes(datas)
        var value = repository.getAllData()
        assertThat(value).isNotEmpty()
    }
    @Test
    fun getAllLiveData_inRepository()
    {
        var datas =  Datas(1,"test","content","12-06-2002")
        repository.addNotes(datas)
        var value = repository.getAllLive()
        var value1 = repository.getAllData()
        assertThat(value1).isNotEmpty()
    }
    @Test
    fun insertData_inRepository()
    {
        var datas =  Datas(1,"test","content","12-06-2002")
        repository.addNotes(datas)
        var value = repository.getAllData()
        assertThat(value).contains(datas)
    }
    @Test
    fun updateNote_inRepository()
    {
        var datas =  Datas(1,"test","content","12-06-2002")
        repository.addNotes(datas)
        var datas1 =  Datas(1,"testedit","contentedit","12-06-2002edit")
        repository.updateNote(datas1)
        var value = repository.getAllData()

        assertThat(value).contains(datas1)
    }
    @Test
    fun deleteNote_inRepository()
    {
        var datas =  Datas(1,"test","content","12-06-2002")
        repository.addNotes(datas)
        repository.delete(datas)
        var value = repository.getAllData()
        assertThat(value).doesNotContain(datas)
    }
    @Test
    fun deleteAllNote_inRepository()= runBlocking {

        var datas =  Datas(1,"test","content","12-06-2002")
        repository.addNotes(datas)
        repository.addNotes(Datas(2,"test2","content2","12-06-2002"))
        repository.addNotes(Datas(3,"test3","content3","12-06-2002"))
        repository.deleteAllnote()
        var value = repository.getAllData()
        assertThat(value).doesNotContain(datas)
    }
    @Test
    fun deleteNoteById_inRepository()
    {
        var datas =  Datas(1,"test","content","12-06-2002")
        repository.addNotes(datas)
        repository.delete_UpdateNotebyId(1)
        var value = repository.getAllData()
        assertThat(value).doesNotContain(datas)
    }
    @Test
    fun deleteNoteByMultipleIds_inRepository()= runBlocking {

        var datas =  Datas(1,"test","content","12-06-2002")
        repository.addNotes(datas)
        repository.addNotes(Datas(2,"test2","content2","12-06-2002"))
        repository.addNotes(Datas(3,"test3","content3","12-06-2002"))
        var delList = listOf<Int>(1,2)
        repository.delete_noteByIds(delList)
        var value = repository.getAllData()
        val dataslist = ArrayList<Datas>()
        dataslist.add (Datas(1,"test1","content1","12-06-2002"))
        dataslist.add(Datas(2,"test2","content2","12-06-2002"))
        assertThat(value).containsNoneIn(dataslist)
    }
    @Test
    fun insertData_inViewModel()
    {
        var datas =  Datas(1,"test","content","12-06-2002")
        viewModel.addNote(datas)
        var value = viewModel.getAllData()
        assertThat(value).contains(datas)
    }
    @Test
    fun updateNote_inViewModel()
    {
        var datas =  Datas(1,"test","content","12-06-2002")
        viewModel.addNote(datas)
        var datas1 =  Datas(1,"testedit","contentedit","12-06-2002edit")
        viewModel.updateNote(datas1)
        val rep = viewModel.repository
        var value = rep.getAllData()
        assertThat(value).contains(datas1)

    }

    @Test
    fun deleteNoteById_inViewModel()
    {
        var datas =  Datas(1,"test","content","12-06-2002")
        viewModel.addNote(datas)
        viewModel.delete_UpdateNotebyId(1)
        var value = viewModel.getAllData()
        assertThat(value).doesNotContain(datas)
    }
    @Test
    fun getAllNote_inViewModel()
    {
        var datas =  Datas(1,"test","content","12-06-2002")
        viewModel.addNote(datas)
        var value = viewModel.getAllData()
        assertThat(value).contains(datas)
    }
    @Test
    fun getAllLiveNote_inViewModel()
    {
        var datas =  Datas(1,"test","content","12-06-2002")
        viewModel.addNote(datas)
        var value1 = viewModel.getAllLive()
        var value = viewModel.getAllData()
        assertThat(value).contains(datas)
    }
   @Test
    fun deleteAllNote_inViewModel()= runBlocking {
        var datas =  Datas(1,"test","content","12-06-2002")
        viewModel.addNote(datas)
        viewModel.addNote(Datas(2,"test2","content2","12-06-2002"))
        viewModel.addNote(Datas(3,"test3","content3","12-06-2002"))
        viewModel.deleteAllnote()
        var value = viewModel.getAllData()
        assertThat(value).isEmpty()

    }
    @Test
    fun deleteNotebyIds_inViewModel()= runBlocking {
        var datas =  Datas(1,"test","content","12-06-2002")
        viewModel.addNote(datas)
        viewModel.addNote(Datas(2,"test2","content2","12-06-2002"))
        viewModel.addNote(Datas(3,"test3","content3","12-06-2002"))
        var delList = arrayListOf<Int>(1,2)
        viewModel.delete_noteByIds(delList)
        var value = viewModel.getAllData()
        val dataslist = ArrayList<Datas>()
        dataslist.add (Datas(1,"test1","content1","12-06-2002"))
        dataslist.add(Datas(2,"test2","content2","12-06-2002"))
        assertThat(value).containsNoneIn(dataslist)

    }
//    fun setDataToRecyclerView():List<Datas>
//    {
//        var datas = listOf<Datas>(
//            Datas(1,"tit1","descrip1","date"),
//            Datas(2,"tit2","descrip2","date"),
//            Datas(3,"tit3","descrip3","date")
//        )
////          adapter= ToAdapter(datas,adapter.noteClickListener)
//        return datas
//    }

}
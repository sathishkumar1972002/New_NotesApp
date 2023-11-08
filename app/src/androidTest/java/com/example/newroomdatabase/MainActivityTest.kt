package com.example.newroomdatabase

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isSystemAlertWindow
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock


class MainActivityTest{

// var notesActivity= NotesActivity()

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)
    @Mock
    lateinit var adapter: ToAdapter

        var str1 ="Not allowed to add empty notes "
        var stradd = "Added"

    @Test
    fun isMainActivityView()
    {
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }
    @Test
    fun MainActivity_HeadTitle_isVisible()
    {
        onView(withId(R.id.head)).check(matches(isDisplayed()))
    }
    @Test
    fun MainActivity_HeadMenuButton_isVisible()
    {
        onView(withId(R.id.menu)).check(matches(isDisplayed()))
    }
    @Test
    fun MainActivity_RecyclerView_isVisible()
    {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }
    @Test
    fun MainActivity_addButton_isVisible()
    {
        onView(withId(R.id.add)).check(matches(isDisplayed()))
    }

    @Test
    fun addButtonIsClicked_navToNotesActivity(){
        val activityRule = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
    }
    @Test
    fun NotesActivity_addButton_isVisible()
    {
        val activityRule = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.add)).check(matches(isDisplayed()))
    }
    @Test
    fun backButtonIsClicked_navToMainActivity(){
        val activityRule = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.back)).perform(click())
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }
    @Test
    fun mobilePressBack_navToMainActivity(){
        val activityRule = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        pressBack()
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }
    @Test
    fun inputText_WithoutNull_add_notesActivity(){
        val activityRule = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.iptitle)).perform(typeText("Title1"), closeSoftKeyboard())
        onView(withId(R.id.ipbody)).perform(typeText("Description1"), closeSoftKeyboard())
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }
    @Test
    fun inputText_TitleisNull_add_notesActivity(){
        val activityRule = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.ipbody)).perform(typeText("Description1"), closeSoftKeyboard())
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
    }
    @Test
    fun inputText_DescriptionisNull_add_notesActivity(){
        val activityRule = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.iptitle)).perform(typeText("Title1"), closeSoftKeyboard())
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
    }
    @Test
    fun inputText_isNull_add_notesActivity(){
        val activityRule = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
    }
    @Test
    fun toast_inputText_WithoutNull_add_notesActivity(){
        val activityRule = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.iptitle)).perform(typeText("Title1"), closeSoftKeyboard())
        onView(withId(R.id.ipbody)).perform(typeText("Description1"), closeSoftKeyboard())
        onView(withId(R.id.add)).perform(click())
        onView(withText("$stradd")) .inRoot(isSystemAlertWindow()).check(matches(isDisplayed()))

       // onView(withId(R.id.main)).check(matches(isDisplayed()))
    }
    @Test
    fun toast_inputText_WithNull_add_notesActivity(){
        val activityRule = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.iptitle)).perform(typeText("Title1"), closeSoftKeyboard())
        onView(withId(R.id.add)).perform(click())
        onView(withText("$str1")) .inRoot(isSystemAlertWindow()).check(matches(isDisplayed()))
    }


    @Test
    fun update_notesActivity(){
        val activityRule = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.iptitle)).perform(typeText("Title1"), closeSoftKeyboard())
        onView(withId(R.id.ipbody)).perform(typeText("Description1"), closeSoftKeyboard())
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.main)).check(matches(isDisplayed()))
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.iptitle)).perform(typeText("Title2"), closeSoftKeyboard())
        onView(withId(R.id.ipbody)).perform(typeText("Description2"), closeSoftKeyboard())
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.main)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
            click()
        ))
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))

        // onView(withId(R.id.main)).check(matches(isDisplayed()))
    }
    @Test
    fun inView_deleteMultipleData_Recycler_view_Yes()
    {
        val activityRule = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.iptitle)).perform(typeText("Title1"), closeSoftKeyboard())
        onView(withId(R.id.ipbody)).perform(typeText("Description1"), closeSoftKeyboard())
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.iptitle)).perform(typeText("Title2"), closeSoftKeyboard())
        onView(withId(R.id.ipbody)).perform(typeText("Description2"), closeSoftKeyboard())
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
            longClick()
        ))
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1,
            click()
        ))
        onView(withId(R.id.deletemultiple)).check(matches(isDisplayed()))
        onView(withId(R.id.deletemultiple)).perform(click())
        onView(withText("Yes")).perform(click())
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }
    @Test
    fun inView_deleteMultipleData_Recycler_view_clickNo()
    {
        val activityRule = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.iptitle)).perform(typeText("Title1"), closeSoftKeyboard())
        onView(withId(R.id.ipbody)).perform(typeText("Description1"), closeSoftKeyboard())
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.iptitle)).perform(typeText("Title2"), closeSoftKeyboard())
        onView(withId(R.id.ipbody)).perform(typeText("Description2"), closeSoftKeyboard())
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
            longClick()
        ))
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1,
            click()
        ))
        onView(withId(R.id.deletemultiple)).check(matches(isDisplayed()))
        onView(withId(R.id.deletemultiple)).perform(click())
        onView(withText("No")).perform(click())
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }

    @Test
    fun deleteAll_clickYesInAlert_NotEmptyList_MainActivity_Menu()
    {
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.iptitle)).perform(typeText("Title1"), closeSoftKeyboard())
        onView(withId(R.id.ipbody)).perform(typeText("Description1"), closeSoftKeyboard())
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.main)).check(matches(isDisplayed()))
        onView(withId(R.id.menu)).perform(click())
        onView(withText("Delete All")).perform(click())
        onView(withText("Yes")).perform(click())
    }
    @Test
    fun deleteAll_clickNoInAlert_NotEmptyList_MainActivity_Menu()
    {
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.iptitle)).perform(typeText("Title1"), closeSoftKeyboard())
        onView(withId(R.id.ipbody)).perform(typeText("Description1"), closeSoftKeyboard())
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.main)).check(matches(isDisplayed()))
        onView(withId(R.id.menu)).perform(click())
        onView(withText("Delete All")).perform(click())
        onView(withText("No")).perform(click())
    }
    @Test
    fun view_deleteAll_EmptyList_MainActivity_Menu()
    {

        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.iptitle)).perform(typeText("Title1"), closeSoftKeyboard())
        onView(withId(R.id.ipbody)).perform(typeText("Description1"), closeSoftKeyboard())
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.menu)).perform(click())
        onView(withText("Delete All")).perform(click())
        onView(withText("Yes")).perform(click())
        onView(withId(R.id.menu)).perform(click())
        onView(withText("Delete All")).perform(click())
        onView(withText("Note is Empty")).inRoot(isSystemAlertWindow()).check(matches(isDisplayed()))
    }
//    @Test
//    fun multiSelection_check_unSelectCheck()
//    {
//        onView(withId(R.id.add)).perform(click())
//        onView(withId(R.id.iptitle)).perform(typeText("Title1"), closeSoftKeyboard())
//        onView(withId(R.id.ipbody)).perform(typeText("Description1"), closeSoftKeyboard())
//        onView(withId(R.id.add)).perform(click())
//        onView(withId(R.id.head)).perform(click())
//        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
//            longClick()
//        ))
//        onView(withId(R.id.deletemultiple)).check(matches(isDisplayed()))
//        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
//            click()
//        ))
//        onView(withId(R.id.add)).check(matches(isDisplayed()))
//    }
       @Test
       fun updateCheck_oneNote_RecyclerView()
       {
           onView(withId(R.id.add)).perform(click())
           onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
           onView(withId(R.id.iptitle)).perform(typeText("Title1"), closeSoftKeyboard())
           onView(withId(R.id.ipbody)).perform(typeText("Description1"), closeSoftKeyboard())
           onView(withId(R.id.add)).perform(click())
           onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
               click()
           ))
           onView(withId(R.id.add)).check(matches(isDisplayed()))
           onView(withId(R.id.deleteButton)).check(matches(isDisplayed()))
           onView(withId(R.id.iptitle)).perform(click(), typeText("Edited"), closeSoftKeyboard())
           onView(withId(R.id.ipbody)).perform(click(), typeText("Edited"), closeSoftKeyboard())
           onView(withId(R.id.add)).perform(click())
           onView(withText("Updated ")).inRoot(isSystemAlertWindow()).check(matches(isDisplayed()))
       }

    @Test
    fun deleteCheck_oneNote_RecyclerView_clickYes()
    {
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.iptitle)).perform(typeText("Title1"), closeSoftKeyboard())
        onView(withId(R.id.ipbody)).perform(typeText("Description1"), closeSoftKeyboard())
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
            click()
        ))
      onView(withId(R.id.iptitle)).perform(click(), closeSoftKeyboard())

       onView(withId(R.id.deleteButton)).perform(click())
        onView(withText("Yes")).perform(click())
        onView(withText("Deleted")).inRoot(isSystemAlertWindow()).check(matches(isDisplayed()))
    }
    @Test
    fun deleteCheck_oneNote_RecyclerView_clickNo()
    {
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.notesActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.iptitle)).perform(typeText("Title1"), closeSoftKeyboard())
        onView(withId(R.id.ipbody)).perform(typeText("Description1"), closeSoftKeyboard())
        onView(withId(R.id.add)).perform(click())
        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
             click()
            ))
        onView(withText("Notes")).perform(click())
        onView(withId(R.id.deleteButton)).perform(click())
        onView(withText("No")).perform(click())
    }
//    fun setDataToRecyclerView():List<Datas>
//    {
//        var datas = listOf<Datas>(
//            Datas(1,"tit1","descrip1","date"),
//            Datas(2,"tit2","descrip2","date"),
//            Datas(3,"tit3","descrip3","date")
//        )
//
//          return datas
//    }


}
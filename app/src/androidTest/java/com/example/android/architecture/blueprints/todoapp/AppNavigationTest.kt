package com.example.android.architecture.blueprints.todoapp

import android.app.Activity
import androidx.appcompat.widget.Toolbar
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.tasks.TasksActivity
import com.example.android.architecture.blueprints.todoapp.util.DataBindingIdlingResource
import com.example.android.architecture.blueprints.todoapp.util.EspressoIdlingResource
import com.example.android.architecture.blueprints.todoapp.util.monitorActivity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AppNavigationTest
{
   
   private lateinit var tasksRepository: TasksRepository
   
   @Before
   fun init()
   {
      tasksRepository =
         ServiceLocator.provideTaskRepository(ApplicationProvider.getApplicationContext())
      runTest {
         tasksRepository.deleteAllTasks()
      }
   }
   
   @After
   fun reset()
   {
      ServiceLocator.resetRepository()
   }
   
   // An idling resource that waits for Data Binding to have no pending bindings.
   private val dataBindingIdlingResource = DataBindingIdlingResource()
   
   @Before
   fun registerIdlingResource()
   {
      IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
      IdlingRegistry.getInstance().register(dataBindingIdlingResource)
   }
   
   @After
   fun unregisterIdlingResource()
   {
      IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
      IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
      
   }
   
   
   @Test
   fun taskScreen_clickOnDrawerIcon_opensNavigation()
   {
      // Start the task screen
      val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
      dataBindingIdlingResource.monitorActivity(activityScenario)
      
      // 1. Check that left drawer is closed at startup.
      
      // 2. Open drawer by clicking drawer icon.
      
      // 3. Check if drawer is open.
      
      // When using ActivityScenario.launch(), always call close()
      activityScenario.close()
   }
   
   @Test
   fun taskDetailScreen_doubleUpButton() = runTest {
      val task = Task("Up button", "Description")
      tasksRepository.saveTask(task)
      
      // Start the Tasks screen.
      val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
      dataBindingIdlingResource.monitorActivity(activityScenario)
   }
   
   
   @Test
   fun taskDetailScreen_doubleBackButton() = runTest {
      val task = Task("Back button", "Description")
      tasksRepository.saveTask(task)
      
      // Start Tasks screen.
      val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
      dataBindingIdlingResource.monitorActivity(activityScenario)
      
      // 1. Click on the task on the list.
      
      // 2. Click on the Edit task button.
      
      // 3. Confirm that if we click Back once, we end up back at the task details page.
      
      // 4. Confirm that if we click Back a second time, we end up back at the home screen.
      
      // When using ActivityScenario.launch(), always call close()
      activityScenario.close()
   }
}

fun <T : Activity> ActivityScenario<T>.getToolbarNavigationContentDescription(): String
{
   var description = ""
   onActivity {
      description = it.findViewById<Toolbar>(R.id.toolbar).navigationContentDescription as String
   }
   
   return description
}
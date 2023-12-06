package com.example.android.architecture.blueprints.todoapp.data.source.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TaskDaoTest
{
   @get:Rule
   var instantTaskExecutorRule = InstantTaskExecutorRule()
   

   
   private lateinit var database: ToDoDatabase
   
   
   @Before
   fun initDb()
   {
      // Using an in-memory database so that the information stored here disappears when the
      // process is killed.
      database = Room.inMemoryDatabaseBuilder(
         getApplicationContext<Context>(),
         ToDoDatabase::class.java).allowMainThreadQueries().build()
   }
   
   @After
   @Throws(IOException::class)
   fun closeDb() = database.close()
   
   @Test
   @Throws(Exception::class)
   fun insertTaskAndGetById() = runTest {
      // Given add new Task in database.
      val task = Task("title", "description")
      database.taskDao().insertTask(task)
      
      // When the loaded contains the expected values.
      val loaded = database.taskDao().getTaskById(task.id)
      
      // Then the Loaded task by id from the database.
      assertThat<Task>(loaded as Task, notNullValue())
      assertThat(loaded.id, `is`(task.id))
      assertThat(loaded.title, `is`(task.title))
      assertThat(loaded.description, `is`(task.description))
      assertThat(loaded.isCompleted, `is`(task.isCompleted))
      
      
   }
   @Test
   fun updateTaskAndGetById() = runTest  {
      //  When insert a Task in database.
      val task = Task("title", "description")
      database.taskDao().insertTask(task)
      
      // When the update task and loaded contains the expected values.
      val updatedTask = Task("title2", "description2",true,   task.id)
      val id = database.taskDao().updateTask(updatedTask)
      
      // Then - the Loaded task contains the expected values.
      val loaded = database.taskDao().getTaskById(task.id)
      assertThat<Task>(loaded as Task, notNullValue())
      assertThat(loaded.id, `is`(updatedTask.id))
      assertThat(loaded.title, `is`(updatedTask.title))
      assertThat(loaded.description, `is`(updatedTask.description))
      assertThat(loaded.isCompleted, `is`(updatedTask.isCompleted))
      
      
   }
}
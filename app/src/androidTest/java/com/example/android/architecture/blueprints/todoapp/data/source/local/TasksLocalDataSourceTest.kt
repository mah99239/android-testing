package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class TasksLocalDataSourceTest
{
   
   // Execute each task synchronously using Architecture components.
   @get:Rule
   var instantExecutorRule = InstantTaskExecutorRule()
   
   private lateinit var localDataSource: TasksLocalDataSource
   private lateinit var database: ToDoDatabase
   
   @Before
   fun setup()
   {
      // using an in-memory database for testing, because it doesn't survive killing
      database = Room.inMemoryDatabaseBuilder(
         ApplicationProvider.getApplicationContext(),
         ToDoDatabase::class.java).allowMainThreadQueries()
         .build()
      
      localDataSource = TasksLocalDataSource(database.taskDao(), Dispatchers.Main)
   }
   
   @After
   fun closeDb()
   {
      database.close()
   }
   
   
   @Test
   fun saveTask_retrievesTask() = runTest {
      // GIVEN - A new task saved in the database.
      val newTask = Task("title", "description", false)
      localDataSource.saveTask(newTask)
      
      // WHEN  - Task retrieved by ID.
      val result = localDataSource.getTask(newTask.id)
      
      // THEN - Same task is returned.
      assertThat(result.succeeded, `is`(true))
      result as Result.Success
      assertThat(result.data.title, `is`("title"))
      assertThat(result.data.description, `is`("description"))
      assertThat(result.data.isCompleted, `is`(false))
   }
   
   @Test
   fun completeTask_retrievedTaskIsComplete() = runTest {
      // Given - A new Task saved in the persistent repository.
      val newTask = Task("title")
      localDataSource.saveTask(newTask)
      
      // When completed in the persistent repository
      localDataSource.completeTask(newTask)
      val result = localDataSource.getTask(newTask.id)
      
      // Then the task can be retrieved from the persistent repository and is complete
      assertThat(result.succeeded, `is`(true))
      result as Result.Success
      assertThat(result.data.title, `is`(newTask.title))
      assertThat(result.data.isCompleted, `is`(true))
      
   }
}
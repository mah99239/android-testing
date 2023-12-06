package com.example.android.architecture.blueprints.todoapp.data.source

import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DefaultTasksRepositoryTest
{
   private val task1 = Task("title1", "Description1")
   private val task2 = Task("title2", "Description2")
   private val task3 = Task("title3", "Description3")
   private val remoteTasks = listOf(task1, task2).sortedBy { it.id }
   private val localTasks = listOf(task3).sortedBy { it.id }
   private val newTasks = listOf(task3).sortedBy { it.id }
   
   private lateinit var tasksRemoteDataSource: FakeDataSource
   private lateinit var tasksLocalDataSource: FakeDataSource
   
   // Class under test
   private lateinit var tasksRepository: DefaultTasksRepository
   
   @OptIn(ExperimentalCoroutinesApi::class)
   @get:Rule
   var mainCoroutineRule =  MainCoroutineRule()
   @Before
   fun createRepository()
   {
      tasksRemoteDataSource = FakeDataSource(remoteTasks.toMutableList())
      tasksLocalDataSource = FakeDataSource(localTasks.toMutableList())
      
      tasksRepository =
         DefaultTasksRepository(tasksRemoteDataSource, tasksLocalDataSource, Dispatchers.Main)
   }
   
   @Test
   fun getTasks_requestsAllTaskFromRemoteDataSource() = runTest{
      // When tasks are requested from the tasks repository
      val tasks = tasksRepository.getTasks(true) as Result.Success
      // Then tasks are loaded from remote data source.
      MatcherAssert.assertThat(tasks.data , IsEqual(remoteTasks))
   }
   
}
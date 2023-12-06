package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.CoroutinesTestExtension
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.InstantTaskExecutorExtension
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExperimentalCoroutinesApi
//@ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
class TasksViewModelTest
{
   @get:Rule
   var mainCoroutineRule = MainCoroutineRule()
   
   
   // Subject under test
   private lateinit var tasksViewModel: TasksViewModel
   
   // Use a fake repository to be injected into the viewModel
   private lateinit var tasksRepository: FakeTestRepository
   
   // Executes each task synchronously using Architecture Components.
   @get:Rule
   var instantExecutorRule = InstantTaskExecutorRule()
   
   @Before
   fun setupViewModel()
   {
      tasksRepository = FakeTestRepository()
      val tasks1 = Task("Title1", "Description1")
      val tasks2 = Task("Title2", "Description2", true)
      val tasks3 = Task("Title3", "Description3", true)
      tasksRepository.addTasks(tasks1, tasks2, tasks3)
      tasksViewModel = TasksViewModel(tasksRepository)
   }
   
   @Test
   fun addNewTask_setsNewTaskEvent()
   {
      
      
      // When adding a new task.
      tasksViewModel.addNewTask()
      
      // Then the new task event is triggered
      val value = tasksViewModel.newTaskEvent.getOrAwaitValue()
      assertThat(value.getContentIfNotHandled(), (not(nullValue())))
      
   }
   
   
   @Test
   fun setFilterAllTasks_tasksAddViewVisible()
   {
      
      
      // When the filter type is All Tasks.
      tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)
      
      //Then add task action is visible.
      MatcherAssert.assertThat(tasksViewModel.tasksAddViewVisible.getOrAwaitValue(), `is`(true))
      
   }
   
   @Test
   fun completeTask_dataAndSnackbarUpdated()
   {
      
      
      // With a repository that has an active task.
      val task = Task("Title", "Description")
      tasksRepository.addTasks(task)
      
      // Complete task
      tasksViewModel.completeTask(task, true)
      
      // Verify the task is completed.
      assertThat(tasksRepository.tasksServiceData[task.id]?.isCompleted, `is`(true))
      
      // Assert that the snackbar has been updated with the correct text.
      val snackbarText: Event<Int> = tasksViewModel.snackbarText.getOrAwaitValue()
      assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.task_marked_complete))
      
   }
}
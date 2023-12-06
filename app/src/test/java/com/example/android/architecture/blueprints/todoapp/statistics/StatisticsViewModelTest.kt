package com.example.android.architecture.blueprints.todoapp.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StatisticsViewModelTest
{
   // Executes each task synchronously using Architecture Components.
   @get:Rule
   var instantExecutorRule = InstantTaskExecutorRule()
   
   // Set the main coroutines dispatcher for unit testing.
   @get:Rule
   var mainCoroutineRule = MainCoroutineRule()
   
   // Subject under test
   private lateinit var statisticsViewModel: StatisticsViewModel
   
   // Use a fake repository to be injected into the viewModel
   private lateinit var tasksRepository: FakeTestRepository
   
   
   @Before
   fun setupStatisticsViewModel()
   {
      // initialize the repository with no tasks.
      tasksRepository = FakeTestRepository()
      
      statisticsViewModel = StatisticsViewModel(tasksRepository)
   }
   
   
   @Test
   fun loadTasks_loading()
   {
      // Load the task in the viewModel
      statisticsViewModel.refresh()
      
      // Then progress indicator is shown.
      assertThat(statisticsViewModel.dataLoading.getOrAwaitValue(), `is`(true))
      
      // Execute pending coroutines actions.
      runTest {}
      
      // Then progress indicator is hidden.
      assertThat(statisticsViewModel.dataLoading.getOrAwaitValue(), `is`(false))
      
   }
   
   @Test
   fun `load statistics when tasks are unavailable call error to display`()
   {
      // Make the repository return errors.
      tasksRepository.setReturnError(true)
      
      // when load the task in the view model.
      runTest {
         statisticsViewModel.refresh()
      }
      // Then an error message is shown
      assertThat(statisticsViewModel.empty.getOrAwaitValue(), `is`(true))
      assertThat(statisticsViewModel.error.getOrAwaitValue(), `is`(true))
   }
   
   
}
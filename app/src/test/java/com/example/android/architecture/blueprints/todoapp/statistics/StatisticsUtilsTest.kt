package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class StatisticsUtilsTest{

   @Test
   fun getActiveAndCompletedStats_noCompleted_returnZeroHundred(){
      // Given a list of tasks with a single, active, task
      val tasks = listOf<Task>(
         Task("title", "description", isCompleted =  false)
                              )
      // When the list of tasks is computed with an active task
      val result = getActiveAndCompletedStats(tasks)
      
      // then the percentages are 0 and 100
      assertThat(result.completedTasksPercent, `is`(0f))
      assertThat(result.activeTasksPercent, `is`(100f))
     
   }
   
 
   @Test
   fun getActiveAndCompletedStats_both_returnFortySixty(){
      // Given 3 completed tasks and 2 active tasks
      val tasks = listOf<Task>(
         Task("title", "description", isCompleted =  true),
         Task("title", "description", isCompleted =  true),
         Task("title", "description", isCompleted =  false),
         Task("title", "description", isCompleted =  false),
         Task("title", "description", isCompleted =  false)
                              )
      // When the list of tasks is computed
      val result = getActiveAndCompletedStats(tasks)
      
      // Then the result is 40-60
      assertThat( result.completedTasksPercent, `is`(40f))
      assertThat( result.activeTasksPercent, `is`(60f))
   }
   
   // If there's one completed task and no active tasks.
   @Test
   fun getActiveAndCompletedStats_Completed_returnHundredZero(){
      // Given a list of task with single, completed, task
      val tasks = listOf<Task>(
         Task("title", "description", isCompleted =  true)
       
                              )
      // When the list of task is computed
      val result = getActiveAndCompletedStats(tasks)
      // Then the result is 0
      assertThat( result.completedTasksPercent, `is`(100f))
      assertThat( result.activeTasksPercent, `is`(0f))
      
   }
   
   
   @Test
   fun getActiveAndCompletedStats_empty_returnZeros(){
      //Given a list of task is empty,  when computed
      val result = getActiveAndCompletedStats(emptyList<Task>())
      
      // Then  the result is 0
      assertThat( result.completedTasksPercent, `is`(0f))
      assertThat( result.activeTasksPercent, `is`(0f))
      
      
   }
   
   @Test
   fun getActiveAndCompletedStats_error_returnZeros(){
      // Given a list is null, and when computed is null
      val result = getActiveAndCompletedStats(null)
      
      // Then the result is 0
      assertThat( result.completedTasksPercent, `is`(0f))
      assertThat( result.completedTasksPercent, `is`(0f))
      
     
   }
}
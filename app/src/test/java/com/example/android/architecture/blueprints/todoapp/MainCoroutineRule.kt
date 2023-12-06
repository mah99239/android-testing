package com.example.android.architecture.blueprints.todoapp

import androidx.room.util.copyAndClose
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
class MainCoroutineRule(
                        val dispatcher: TestDispatcher = StandardTestDispatcher(),
                       ) : TestWatcher(),  CoroutineScope by TestScope(dispatcher)
{
  
   init
   {
      Dispatchers.setMain(dispatcher)
      
   }
 /*   override fun starting(description: Description?)
   {
      super.starting(description)
      Dispatchers.setMain(dispatcher)
      
      
   } */
   

   override fun finished(description: Description?)
   {
      super.finished(description)
      
      Dispatchers.resetMain()
      
   }
   
   
}
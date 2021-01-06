package com.example.ToDo


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*


//import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
   private lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val actionbar =supportActionBar
// all the main part here, for toolbar

       val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController() // to set tool bar

       // tool_barMain.title = "hhh"//
        setSupportActionBar(tool_barMain)
      //  setupActionBarWithNavController(navController)  // to set action bar title as LABEL


       // actionbar!!.title = "Main"
       // setSupportActionBar(tool_barMain)

    }

    override fun onSupportNavigateUp(): Boolean {
       // return  navController.navigateUp()|| super.onSupportNavigateUp()   // to go back fragment with <- toolbar view
   return super.onSupportNavigateUp()
    }
}
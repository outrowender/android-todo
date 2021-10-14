package com.outrowender.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.outrowender.todo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var activity: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activity.root)

        //connects NavController with main fragment
        setupActionBarWithNavController(getMainNavController())
    }

    private fun getMainNavController(): NavController {
        //https://stackoverflow.com/a/62612502
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragmentContainerView) as NavHostFragment
        return navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        val nav = getMainNavController()
        return nav.navigateUp() || super.onSupportNavigateUp()
    }
}
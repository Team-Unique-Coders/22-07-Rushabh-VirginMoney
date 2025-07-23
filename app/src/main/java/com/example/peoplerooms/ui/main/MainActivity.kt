package com.example.peoplerooms.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.peoplerooms.R
import com.example.peoplerooms.databinding.ActivityMainBinding
import com.example.peoplerooms.ui.people.PeopleFragment
import com.example.peoplerooms.ui.room.RoomFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Set up BottomNavigationView with NavController
        binding.bottomNav.setupWithNavController(navController)

        // Set Toolbar
        setSupportActionBar(binding.topAppBar)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val topLevelDestinations = setOf(
                R.id.loginFragment,
                R.id.signupFragment,
                R.id.peopleFragment,
                R.id.roomFragment
            )
            val isTopLevel = destination.id in topLevelDestinations
            supportActionBar?.setDisplayHomeAsUpEnabled(!isTopLevel)
            supportActionBar?.title = "PeopleRooms"

            val hideBottomNavFragments = setOf(
                R.id.loginFragment,
                R.id.signupFragment,
                R.id.userDetailsFragment
            )
            val showBottomNav = destination.id !in hideBottomNavFragments
            binding.bottomNav.isVisible = showBottomNav
        }


        // Handle toolbar navigation click
        binding.topAppBar.setNavigationOnClickListener {
            navController.navigateUp()
        }


    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}
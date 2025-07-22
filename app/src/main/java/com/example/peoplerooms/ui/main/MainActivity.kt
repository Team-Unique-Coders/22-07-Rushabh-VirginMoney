package com.example.peoplerooms.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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


        replaceFragment(PeopleFragment())

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
               R.id.people_menu -> replaceFragment(PeopleFragment())
                R.id.room_menu -> replaceFragment(RoomFragment())
            }
            true
        }
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.frameLayout.id, fragment).commit()
    }
}
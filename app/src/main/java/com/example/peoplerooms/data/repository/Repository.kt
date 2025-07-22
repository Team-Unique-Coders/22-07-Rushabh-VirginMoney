package com.example.peoplerooms.data.repository

import com.example.peoplerooms.data.model.people.People
import com.example.peoplerooms.data.model.rooms.Rooms

interface Repository {
    suspend fun getPeople() : People
    suspend fun getRooom() : Rooms
}
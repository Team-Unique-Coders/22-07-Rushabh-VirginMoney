package com.project.peoplerooms.data.repository

import com.project.peoplerooms.data.model.people.People
import com.project.peoplerooms.data.model.rooms.Rooms

interface Repository {
    suspend fun getPeople() : People
    suspend fun getRooom() : Rooms
}
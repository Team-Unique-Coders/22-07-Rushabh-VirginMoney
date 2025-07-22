package com.example.peoplerooms.data.repository

import com.example.peoplerooms.data.model.people.People
import com.example.peoplerooms.data.model.rooms.Rooms
import com.example.peoplerooms.data.service.UserService

class RepositoryImpl(
    val userService: UserService
): Repository {
    override suspend fun getPeople(): People {
       return userService.getPeople()
    }

    override suspend fun getRooom(): Rooms {
        return userService.getRooms()
    }

}
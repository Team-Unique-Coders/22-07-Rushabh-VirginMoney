package com.project.peoplerooms.data.repository

import com.project.peoplerooms.data.model.people.People
import com.project.peoplerooms.data.model.rooms.Rooms
import com.project.peoplerooms.data.service.UserService

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
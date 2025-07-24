package com.project.peoplerooms.data.service

import com.project.peoplerooms.data.model.people.People
import com.project.peoplerooms.data.model.rooms.Rooms
import retrofit2.http.GET

interface UserService {
    @GET(ApiDetails.PEOPLE)
    suspend fun getPeople(): People

    @GET(ApiDetails.ROOMS)
    suspend fun getRooms(): Rooms
}
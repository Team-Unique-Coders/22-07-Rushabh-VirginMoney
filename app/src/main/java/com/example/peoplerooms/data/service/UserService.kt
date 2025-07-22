package com.example.peoplerooms.data.service

import com.example.peoplerooms.data.model.people.People
import com.example.peoplerooms.data.model.rooms.Rooms
import retrofit2.http.GET

interface UserService {
    @GET(ApiDetails.PEOPLE)
    suspend fun getPeople(): People

    @GET(ApiDetails.ROOMS)
    suspend fun getRooms(): Rooms
}
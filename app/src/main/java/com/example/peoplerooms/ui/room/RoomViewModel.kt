package com.example.peoplerooms.ui.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peoplerooms.data.model.rooms.Rooms
import com.example.peoplerooms.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel

import jakarta.inject.Inject
import kotlinx.coroutines.launch
import retrofit2.http.Header

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _text = MutableLiveData<ApiResponse>()
    val text: LiveData<ApiResponse> = _text

    init {
        getRoom()
    }

    fun getRoom(){
        _text.postValue(ApiResponse.Loading)

        viewModelScope.launch {
            try {
                val result: Rooms = repository.getRooom()
                // If People is a List<Person>
                if (result.isEmpty()) {
                    _text.postValue(ApiResponse.Error("No people found"))
                } else {
                    _text.postValue(ApiResponse.Success(result))
                }
                println("Current state: ${_text.value}")
            } catch (e: Exception) {
                _text.postValue(ApiResponse.Error(e.message ?: "Unknown error"))
                println("Exception occurred: ${e.message}")
            }
        }
    }
}

sealed class ApiResponse {
    object Loading : ApiResponse()
    data class Success(val data: Rooms) : ApiResponse()
    data class Error(val error: String) : ApiResponse()
}

package com.example.peoplerooms.ui.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peoplerooms.data.model.rooms.Rooms
import com.example.peoplerooms.databinding.FragmentRoomBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomFragment : Fragment() {

    private var _binding : FragmentRoomBinding?=null
    private val binding get() = _binding!!

    private lateinit var viewModel: RoomViewModel
    private lateinit var adapter: RoomAdapter
    private lateinit var analytics: FirebaseAnalytics
    private var isSortByOccupied = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentRoomBinding.inflate(inflater,container,false)
        analytics = Firebase.analytics
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabRoom.setOnClickListener {
            sortRooms(isSortByOccupied)
            isSortByOccupied = !isSortByOccupied

                analytics.logEvent(
                    FirebaseAnalytics.Event.SELECT_CONTENT,
                    bundleOf(
                        FirebaseAnalytics.Param.ITEM_ID to "Product",
                        FirebaseAnalytics.Param.ITEM_NAME to "Protien",
                        FirebaseAnalytics.Param.CONTENT_TYPE to "text",

//                    Pair("","")
                    ))
//                throw NullPointerException("You crashed!")

        }

        viewModel = ViewModelProvider(this)[RoomViewModel::class.java]

        viewModel.text.observe(viewLifecycleOwner){
                response ->
            when(response){
                is com.example.peoplerooms.ui.room.ApiResponse.Loading -> {
                    binding.apply {
                        pbLoading.visibility = View.VISIBLE
                        tvError.visibility = View.GONE
                        recyclerViewRoom.visibility = View.GONE
                    }
                }
                is com.example.peoplerooms.ui.room.ApiResponse.Error ->{
                    binding.apply {
                        tvError.visibility = View.VISIBLE
                        pbLoading.visibility = View.GONE
                        recyclerViewRoom.visibility = View.GONE
                        tvError.text = response.error
                    }
                }
                is com.example.peoplerooms.ui.room.ApiResponse.Success ->{
                    binding.apply {
                        tvError.visibility = View.GONE
                        pbLoading.visibility = View.GONE
                        recyclerViewRoom.visibility = View.VISIBLE
                    }
                    setupUI(response.data as Rooms)
                }
            }

        }

    }

    private fun setupUI(models: Rooms) {
        adapter = RoomAdapter(models.toMutableList()) { item ->
            Toast.makeText(context, "${item.maxOccupancy} Clicked!", Toast.LENGTH_SHORT).show()
        }
        binding.recyclerViewRoom.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewRoom.adapter = adapter
    }

    private fun sortRooms(occupiedFirst: Boolean) {
        val sortedList = if (occupiedFirst) {
            adapter.list.sortedByDescending { it.isOccupied == true }
        } else {
            adapter.list.sortedBy { it.isOccupied == true }
        }

        adapter.list = sortedList.toMutableList()
        adapter.notifyDataSetChanged()
    }

}
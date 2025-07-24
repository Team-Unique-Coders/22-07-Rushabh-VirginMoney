package com.project.peoplerooms.ui.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.project.peoplerooms.data.model.people.People
import com.project.peoplerooms.databinding.FragmentPeopleBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.OnBackPressedCallback

@AndroidEntryPoint
class PeopleFragment : Fragment() {

    private var _binding : FragmentPeopleBinding?=null
    private val binding get() = _binding!!

    private lateinit var viewModel: PeopleViewModel
    private lateinit var adapter: PeopleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        super.onCreate(savedInstanceState)
        _binding = FragmentPeopleBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Close the app
                    requireActivity().finishAffinity()
                }
            })

        viewModel = ViewModelProvider(this)[PeopleViewModel::class.java]

        viewModel.text.observe(viewLifecycleOwner){
                response ->
            when(response){
                is ApiResponse.Loading -> {
                    binding.apply {
                        pbLoading.visibility = View.VISIBLE
                        tvError.visibility = View.GONE
                        recyclerViewPeople.visibility = View.GONE
                    }
                }
                is ApiResponse.Error ->{
                    binding.apply {
                        tvError.visibility = View.VISIBLE
                        pbLoading.visibility = View.GONE
                        recyclerViewPeople.visibility = View.GONE
                        tvError.text = response.error
                    }
                }
                is ApiResponse.Success ->{
                    binding.apply {
                        tvError.visibility = View.GONE
                        pbLoading.visibility = View.GONE
                        recyclerViewPeople.visibility = View.VISIBLE
                    }
                    setupUI(response.data as People)
                }
            }

        }

    }

    private fun setupUI(models : People) {
        adapter = PeopleAdapter(models) { item ->
            Toast.makeText(context, "${item.firstName + " " + item.lastName} Clicked!", Toast.LENGTH_SHORT).show()
            val action = PeopleFragmentDirections
                .actionPeopleFragmentToUserDetailsFragment(item)
            println("Action is : ${action}")
            findNavController().navigate(action)
        }
        binding.recyclerViewPeople.layoutManager = GridLayoutManager(requireContext(),2)
        binding.recyclerViewPeople.adapter = adapter
    }

}
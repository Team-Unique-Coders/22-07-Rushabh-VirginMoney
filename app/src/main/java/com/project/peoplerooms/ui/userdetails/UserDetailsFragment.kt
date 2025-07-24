package com.project.peoplerooms.ui.userdetails

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.project.peoplerooms.R
import com.project.peoplerooms.data.model.people.PeopleItemModel
import com.project.peoplerooms.databinding.FragmentUserDetailsBinding
import kotlin.getValue


class UserDetailsFragment : Fragment() {
    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: UserDetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val people: PeopleItemModel = args.item

        (activity as? AppCompatActivity)?.supportActionBar?.title = people.firstName ?: "User"

        Glide.with(this)
            .load(people.avatar)
            .placeholder(R.drawable.img_1)
            .into(binding.ivUserImage)


        binding.tvUserName.text = people.firstName + " " + people.lastName
        binding.tvUserEmail.text = "$ ${people.email}"
        binding.tvUserJobTitle.text = people.jobtitle
        binding.tvUserColor.text = people.favouriteColor
        binding.tvuserSize.text = people.size
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
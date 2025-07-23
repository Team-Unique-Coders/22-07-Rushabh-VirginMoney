package com.example.peoplerooms.ui.signup

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.peoplerooms.R
import com.example.peoplerooms.databinding.FragmentLoginBinding
import com.example.peoplerooms.databinding.FragmentSignupBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignupFragment : Fragment() {

    private var _binding : FragmentSignupBinding?=null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth



    private val viewModel: SignupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater,container,false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginNow.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
        binding.apply {
            signupBtn.setOnClickListener {
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()
                val confirmPassword = passwordInputConfirm.text.toString()

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(context,"Enter valid details", Toast.LENGTH_SHORT).show()
                }else if (password != confirmPassword){
                    Toast.makeText(context,"Passwords do not match", Toast.LENGTH_SHORT).show()
                }
                else{
                    auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener {
                                task -> if(task.isSuccessful){
                            val user = auth.currentUser
                            Toast.makeText(context,"${user?.email} Created!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                        }else{
                            Toast.makeText(context,"Something went wrong! No user created", Toast.LENGTH_SHORT).show()
                        }
                        }
                }
            }
        }
    }
}
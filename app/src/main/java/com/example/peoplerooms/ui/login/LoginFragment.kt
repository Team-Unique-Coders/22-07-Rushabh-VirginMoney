package com.example.peoplerooms.ui.login

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
import com.example.peoplerooms.databinding.FragmentPeopleBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding?=null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val viewModel: LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registerNow.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.forgotPassword.setOnClickListener {
            Toast.makeText(context,"Eat Almonds", Toast.LENGTH_SHORT).show()
        }
        binding.apply {
            loginBtn.setOnClickListener {
                val email = emailInputLogin.text.toString()
                val password = passwordInputLogin.text.toString()

                if(email.isEmpty() || password.isNullOrEmpty()){
                    Toast.makeText(context,"Enter valid details", Toast.LENGTH_SHORT).show()
                }else{
                    auth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener {
                                task -> if(task.isSuccessful){
                            val user = auth.currentUser
                            Toast.makeText(context,"${user?.email} I welcome you!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_loginFragment_to_peopleFragment)
                        }else{
                            Toast.makeText(context,"Nope Not Allowed", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }

    }

}
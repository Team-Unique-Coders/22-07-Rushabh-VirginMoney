package com.project.peoplerooms.ui.login

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.project.peoplerooms.R
import com.project.peoplerooms.databinding.FragmentLoginBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.login.widget.LoginButton
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch



class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding?=null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val viewModel: LoginViewModel by viewModels()

    private val TAG: String = "FirebaseAuth"
    private lateinit var credentialManager: CredentialManager

    private lateinit var callbackManager: CallbackManager
    private lateinit var buttonFacebookLogin: LoginButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        auth = Firebase.auth
        credentialManager = CredentialManager.create(requireContext())
        callbackManager = CallbackManager.Factory.create()


        // ✅ Manual initialization of Facebook SDK (very important)
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id))
        FacebookSdk.sdkInitialize(requireContext())
        AppEventsLogger.activateApp(requireActivity().application)


        buttonFacebookLogin = binding.facebookIcon
        buttonFacebookLogin.setPermissions("email", "public_profile")
        buttonFacebookLogin.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d(TAG, "facebook:onSuccess:$loginResult")
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d(TAG, "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d(TAG, "facebook:onError", error)
                }
            },
        )
        return binding.root
    }

    fun github() {
        val provider = OAuthProvider.newBuilder("github.com")
        provider.addCustomParameter("login", "your-email@example.com")
        provider.scopes = listOf("user:email")

        val pendingResultTask = auth.pendingAuthResult
        if (pendingResultTask != null) {
            // There's something already in progress
            pendingResultTask
                .addOnSuccessListener { authResult ->
                    val user = authResult.user
                    Toast.makeText(context, "Welcome ${user?.displayName} from GitHub", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_peopleFragment)
                }
                .addOnFailureListener {
                    Toast.makeText(context, "GitHub Sign-in Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Start the sign-in flow
            auth.startActivityForSignInWithProvider(requireActivity(), provider.build())
                .addOnSuccessListener { authResult ->
                    val user = authResult.user
                    Toast.makeText(context, "Welcome ${user?.displayName} from GitHub", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_peopleFragment)
                }
                .addOnFailureListener {
                    Toast.makeText(context, "GitHub Sign-in Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun signOut() {
        // [START auth_sign_out]
        Firebase.auth.signOut()
        // [END auth_sign_out]
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
            googleIcon.setOnClickListener {
                initFirebaseAuth()
            }
            githubIcon.setOnClickListener {
                github()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Toast.makeText(context,"Hello ${user?.displayName} from Facebook", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_peopleFragment)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    Toast.makeText(context,"Fail from Facebook", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun initFirebaseAuth() {
        // Instantiate a Google sign-in request
        val googleIdOption = GetGoogleIdOption.Builder()
            // Your server's client ID, not your Android client ID.
            .setServerClientId(getString(R.string.default_web_client_id))
            // Only show accounts previously used to sign in.
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .build()

// Create the Credential Manager request
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                // Launch Credential Manager UI
                val result = credentialManager.getCredential(
                    context = requireContext(),
                    request = request
                ).credential

                // Extract credential from the result returned by Credential Manager
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.e(TAG, "Couldn't retrieve user's credentials: ${e.localizedMessage}")
            }
        }
    }

    private fun handleSignIn(credential: Credential) {
        // Check if credential is of type Google ID
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            // Create Google ID Token
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            // Sign in to Firebase with using the token
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {
            Log.w(TAG, "Credential is not of type Google ID!")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Toast.makeText(context,"Hello ${user?.email}", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_peopleFragment)
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(context,"Error", Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                }
            }
    }



}
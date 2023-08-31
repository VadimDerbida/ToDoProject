package com.example.todoproject.presentation.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoproject.databinding.FragmentLogInBinding
import com.google.firebase.auth.FirebaseAuth

class LogInFragment : Fragment() {
    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setupUi()
    }


    private fun setupUi() {
        binding.signUpLink.setOnClickListener {
            findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToSigUpFragment())
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailTextInput.text.toString()
            val password = binding.passwordTextInput.text.toString()


            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToProfileFragment())
                    } else {
                        Toast.makeText(
                            requireContext(),
                            it.exception.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Empty fields are not allowed ",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

// on start screen life checking current user and navigating to his dashboard
    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToProfileFragment())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
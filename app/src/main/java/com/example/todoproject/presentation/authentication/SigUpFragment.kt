package com.example.todoproject.presentation.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoproject.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SigUpFragment:Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        setupToolbar()
        setupUi()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupUi(){

        binding.signUpButton.setOnClickListener{
            val email = binding.emailTextInput.text.toString()
            val password = binding.passwordTextInput.text.toString()
            val confirmPassword = binding.confirmPasswordTextInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if(confirmPassword == password){
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if(it.isSuccessful){
                            findNavController().navigate(SigUpFragmentDirections.actionSigUpFragmentToProfileFragment())

                        } else{
                            Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }else {
                    Toast.makeText(requireContext(), "Password is not matching ", Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(requireContext(), "Empty fields are not allowed ", Toast.LENGTH_SHORT).show()

            }
        }

        binding.loginLink.setOnClickListener{
            findNavController().navigate(SigUpFragmentDirections.actionSigUpFragmentToLogInFragment())
        }
    }









    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.example.progr3ssapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.progr3ssapp.R
import com.example.progr3ssapp.databinding.FragmentLoginBinding
import com.example.progr3ssapp.ui.navigateSafely

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // Csak onCreateView és onDestroyView között érvényes
    private val binding get() = _binding!!

    // Jelzi, hogy a felhasználó megpróbált-e már belépni (hogy ne mutasson hibát gépelés előtt)
    private var isSubmitted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Az első próbálkozás után gépelés közben frissülnek a hibaüzenetek
        binding.etUsername.doAfterTextChanged { if (isSubmitted) validate() }
        binding.etPassword.doAfterTextChanged { if (isSubmitted) validate() }

        // Bejelentkezés gomb: sikeres validáció után a főoldalra navigál
        binding.btnLogin.setOnClickListener {
            isSubmitted = true
            if (validate()) {
                navigateSafely(R.id.action_loginFragment_to_homeFragment)
            }
        }

        binding.btnGoRegister.setOnClickListener {
            navigateSafely(R.id.action_loginFragment_to_registerFragment)
        }
    }

    /** Validáció (min. 6 karakter mindkét mezőben); a hibát a mező alatt jeleníti meg. */
    private fun validate(): Boolean {
        val isUsernameValid = (binding.etUsername.text?.length ?: 0) >= MIN_LENGTH
        val isPasswordValid = (binding.etPassword.text?.length ?: 0) >= MIN_LENGTH

        binding.tilUsername.error = if (isUsernameValid) null else getString(R.string.error_username)
        binding.tilPassword.error = if (isPasswordValid) null else getString(R.string.error_password)

        return isUsernameValid && isPasswordValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        const val MIN_LENGTH = 6
    }
}

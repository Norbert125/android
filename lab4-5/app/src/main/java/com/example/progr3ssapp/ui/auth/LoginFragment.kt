package com.example.progr3ssapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.progr3ssapp.R
import com.example.progr3ssapp.databinding.FragmentLoginBinding
import com.example.progr3ssapp.ui.navigateSafely
import kotlinx.coroutines.launch

/** Csak megjeleníti az állapotot és továbbítja az eseményeket; a Retrofithoz nem nyúl. */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // Csak onCreateView és onDestroyView között érvényes
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Események a ViewModel felé
        binding.etEmail.doAfterTextChanged { viewModel.onEmailChange(it?.toString().orEmpty()) }
        binding.etPassword.doAfterTextChanged { viewModel.onPasswordChange(it?.toString().orEmpty()) }
        binding.btnLogin.setOnClickListener { viewModel.login() }

        binding.btnGoRegister.setOnClickListener {
            navigateSafely(R.id.action_loginFragment_to_registerFragment)
        }

        // Az állapot gyűjtése csak akkor fut, amikor a képernyő látható
        // (a Compose-os collectAsStateWithLifecycle() megfelelője)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::render)
            }
        }
    }

    private fun render(state: LoginUiState) {
        // Töltés közben minden le van tiltva, így nem lehet kétszer elküldeni
        binding.tilEmail.isEnabled = !state.isLoading
        binding.tilPassword.isEnabled = !state.isLoading
        binding.btnLogin.isEnabled = !state.isLoading
        binding.btnLogin.text = if (state.isLoading) null else getString(R.string.btn_login)
        binding.progressLogin.isVisible = state.isLoading

        // Mellékhatások (Toast, navigáció): egyszer futnak le, amikor az állapot megváltozik
        state.error?.let { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            viewModel.onErrorShown()
        }
        if (state.isLoggedIn) {
            Toast.makeText(requireContext(), R.string.login_success, Toast.LENGTH_SHORT).show()
            navigateSafely(R.id.action_loginFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

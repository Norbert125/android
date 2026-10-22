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
import androidx.navigation.fragment.findNavController
import com.example.progr3ssapp.R
import com.example.progr3ssapp.databinding.FragmentRegisterBinding
import com.example.progr3ssapp.ui.navigateSafely
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    // Csak onCreateView és onDestroyView között érvényes
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etUsername.doAfterTextChanged { viewModel.onUsernameChange(it?.toString().orEmpty()) }
        binding.etEmail.doAfterTextChanged { viewModel.onEmailChange(it?.toString().orEmpty()) }
        binding.etPassword.doAfterTextChanged { viewModel.onPasswordChange(it?.toString().orEmpty()) }
        binding.btnRegister.setOnClickListener { viewModel.register() }

        // Vissza a Login-ra. A navigateUp() az utolsó képernyőt sosem veszi le a stackről,
        // így dupla koppintásnál sem marad üres a NavHost (a popBackStack() ezt megtenné).
        binding.btnGoLogin.setOnClickListener {
            findNavController().navigateUp()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::render)
            }
        }
    }

    private fun render(state: RegisterUiState) {
        binding.tilUsername.isEnabled = !state.isLoading
        binding.tilEmail.isEnabled = !state.isLoading
        binding.tilPassword.isEnabled = !state.isLoading
        binding.btnRegister.isEnabled = !state.isLoading
        binding.btnRegister.text = if (state.isLoading) null else getString(R.string.btn_register)
        binding.progressRegister.isVisible = state.isLoading

        state.error?.let { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            viewModel.onErrorShown()
        }
        // Regisztráció után már be vagyunk lépve, így egyből a főoldalra megyünk
        if (state.isLoggedIn) {
            Toast.makeText(requireContext(), R.string.register_success, Toast.LENGTH_SHORT).show()
            navigateSafely(R.id.action_registerFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

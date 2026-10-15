package com.example.progr3ssapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.progr3ssapp.R
import com.example.progr3ssapp.databinding.FragmentRegisterBinding
import com.example.progr3ssapp.ui.navigateSafely

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    // Csak onCreateView és onDestroyView között érvényes
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            navigateSafely(R.id.action_registerFragment_to_homeFragment)
        }

        // Vissza a Login-ra. A navigateUp() az utolsó képernyőt sosem veszi le a stackről,
        // így dupla koppintásnál sem marad üres a NavHost (a popBackStack() ezt megtenné).
        binding.btnGoLogin.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

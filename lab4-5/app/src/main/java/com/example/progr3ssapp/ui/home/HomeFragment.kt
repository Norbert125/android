package com.example.progr3ssapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.progr3ssapp.R
import com.example.progr3ssapp.databinding.FragmentHomeBinding
import com.example.progr3ssapp.ui.navigateSafely

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // Csak onCreateView és onDestroyView között érvényes
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // A Profil a mentett session-ből olvassa a felhasználót, argumentum nem kell
        binding.btnViewProfile.setOnClickListener {
            navigateSafely(R.id.action_homeFragment_to_profileFragment)
        }

        // Kijelentkezés: a ViewModel törli a session-t, a navigációs action pedig
        // a teljes back stacket (lásd nav_graph.xml)
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            navigateSafely(R.id.action_homeFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

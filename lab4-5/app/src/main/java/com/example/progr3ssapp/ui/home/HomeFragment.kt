package com.example.progr3ssapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.progr3ssapp.R
import com.example.progr3ssapp.databinding.FragmentHomeBinding
import com.example.progr3ssapp.ui.navigateSafely
import com.example.progr3ssapp.ui.profile.ProfileFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // Csak onCreateView és onDestroyView között érvényes
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Argumentumok átadása: a kulcsok megegyeznek a nav_graph.xml <argument> neveivel
        binding.btnViewProfile.setOnClickListener {
            val args = bundleOf(
                ProfileFragment.ARG_USER_ID to "user_456",
                ProfileFragment.ARG_USER_NAME to "Jane Doe"
            )
            navigateSafely(R.id.action_homeFragment_to_profileFragment, args)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

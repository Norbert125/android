package com.example.progr3ssapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.progr3ssapp.R
import com.example.progr3ssapp.databinding.FragmentProfileBinding
import com.example.progr3ssapp.ui.navigateSafely
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // Csak onCreateView és onDestroyView között érvényes
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Belépéskor frissít, így mindig az aktuálisan bejelentkezett felhasználót mutatja
        viewModel.refresh()

        binding.btnBackHome.setOnClickListener {
            navigateSafely(R.id.action_profileFragment_to_homeFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::render)
            }
        }
    }

    private fun render(state: ProfileUiState) {
        binding.tvUserName.text =
            getString(R.string.profile_name, state.username ?: getString(R.string.profile_guest))
        binding.tvUserEmail.text = getString(R.string.profile_email, state.email ?: "-")
        binding.tvUserId.text = getString(R.string.profile_id, state.userId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

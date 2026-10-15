package com.example.progr3ssapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progr3ssapp.R
import com.example.progr3ssapp.databinding.FragmentProfileBinding
import com.example.progr3ssapp.ui.navigateSafely

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // Csak onCreateView és onDestroyView között érvényes
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Az argumentumok mindig jelen vannak: ha a hívó nem adta meg őket (alsó menü),
        // a NavController a nav_graph.xml-ben megadott alapértékekkel tölti fel
        val args = requireArguments()
        val userId = args.getString(ARG_USER_ID).orEmpty()
        val userName = args.getString(ARG_USER_NAME).orEmpty()

        binding.tvUserId.text = getString(R.string.profile_id, userId)
        binding.tvUserName.text = getString(R.string.profile_name, userName)

        binding.btnBackHome.setOnClickListener {
            navigateSafely(R.id.action_profileFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        // Meg kell egyezniük a nav_graph.xml <argument android:name="..."> értékeivel
        const val ARG_USER_ID = "userId"
        const val ARG_USER_NAME = "userName"
    }
}

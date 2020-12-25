package com.example.androidknowledge_dunets_l11

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidknowledge_dunets_l11.databinding.FragmentIntentsBinding

class IntentsFragment : Fragment() {

    private var _binding: FragmentIntentsBinding? = null
    private val binding get() = requireNotNull(_binding)

    companion object {
        @JvmStatic
        fun newInstance() = IntentsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClickListeners() {
        // Opens activity and send int, string extras
        binding.iExtras.setOnClickListener {
            openActivitiesWithExtras()
        }
        // Opens map with coordinates
        binding.iMaps.setOnClickListener {
            openMapWithCoordinates()
        }
    }

    private fun openMapWithCoordinates() {
        val gmmIntentUri = Uri.parse(
            "geo:50.4474,30.5437?q="
                    + Uri.encode("Parimatch Tech, вулиця Михайла Грушевського, 9б, Київ, 02000")
        )

        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(requireActivity().packageManager)?.let {
            startActivity(mapIntent)
        }
    }

    private fun openActivitiesWithExtras() {
        ActivityWithExtras.start(requireContext(), "Custom Param", 331)
    }

}
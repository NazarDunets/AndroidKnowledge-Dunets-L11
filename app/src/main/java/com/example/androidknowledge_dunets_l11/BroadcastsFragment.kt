package com.example.androidknowledge_dunets_l11

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidknowledge_dunets_l11.databinding.FragmentBroadcastsBinding


private const val TAP_ACTION = "TAP_ACTION"

class BroadcastsFragment : Fragment() {

    private var _binding: FragmentBroadcastsBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val userTapReceiver = UserTapReceiver()

    companion object {
        @JvmStatic
        fun newInstance() = BroadcastsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBroadcastsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    override fun onResume() {
        super.onResume()
        activity?.registerReceiver(userTapReceiver, IntentFilter(TAP_ACTION))
        activity?.registerReceiver(
            connectionReceiver,
            IntentFilter("android.net.wifi.WIFI_STATE_CHANGED")
        )
    }

    override fun onPause() {
        super.onPause()
        activity?.unregisterReceiver(userTapReceiver)
        activity?.unregisterReceiver(connectionReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val connectionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val wifiStateExtra = intent?.getIntExtra(
                WifiManager.EXTRA_WIFI_STATE,
                WifiManager.WIFI_STATE_UNKNOWN
            )
            when (wifiStateExtra) {
                WifiManager.WIFI_STATE_ENABLED -> {
                    binding.connectionText.text = getString(R.string.wifi_on)
                }
                WifiManager.WIFI_STATE_DISABLED -> {
                    binding.connectionText.text = getString(R.string.wifi_off)
                }
            }
        }
    }

    private fun setOnClickListeners() {
        binding.bSender.setOnClickListener {
            sendTapBroadcast()
        }
    }

    private fun sendTapBroadcast() {
        Intent().also {
            it.action = TAP_ACTION
            activity?.sendBroadcast(it)
        }
    }
}
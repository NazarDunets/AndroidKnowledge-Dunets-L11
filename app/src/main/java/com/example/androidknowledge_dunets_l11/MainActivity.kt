package com.example.androidknowledge_dunets_l11

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.example.androidknowledge_dunets_l11.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        createNotificationChannels()
        replaceContainers()
    }

    override fun onStart() {
        super.onStart()
        cancelActionNotification()
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun replaceContainers() {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.containerNotifications.id, NotificationsFragment.newInstance())
            .replace(binding.containerIntents.id, IntentsFragment.newInstance())
            .replace(binding.containerBroadcasts.id, BroadcastsFragment.newInstance())
            .commit()
    }

    private fun cancelActionNotification() {
        NotificationManagerCompat.from(this)
            .cancel(1)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            // Create the main NotificationChannel
            var name = getString(R.string.channel_name)
            var descriptionText = getString(R.string.channel_description)
            var importance = NotificationManager.IMPORTANCE_DEFAULT

            val mChannel = NotificationChannel(MAIN_CHANNEL_ID, name, importance)
            mChannel.description = descriptionText

            notificationManager.createNotificationChannel(mChannel)

            // Create secondary NotificationChannel for silent notifications
            name = getString(R.string.s_channel_name)
            descriptionText = getString(R.string.s_channel_description)
            importance = NotificationManager.IMPORTANCE_LOW

            val sChannel = NotificationChannel(SECONDARY_CHANNEL_ID, name, importance)
            sChannel.description = descriptionText

            notificationManager.createNotificationChannel(sChannel)
        }
    }
}
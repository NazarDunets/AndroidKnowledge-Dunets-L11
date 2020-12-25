package com.example.androidknowledge_dunets_l11

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import androidx.fragment.app.Fragment
import com.example.androidknowledge_dunets_l11.databinding.FragmentNotificationsBinding
import java.util.concurrent.Executors

private const val KEY_TEXT_REPLY = "KEY_TEXT_REPLY"
private const val NR_ACTION = "NOTIFICATION_REPLY"

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = requireNotNull(_binding)

    private var prgrsNotifIsActive = false

    companion object {
        @JvmStatic
        fun newInstance() = NotificationsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    override fun onResume() {
        super.onResume()
        activity?.registerReceiver(notificationMsgReceiver, IntentFilter(NR_ACTION))
    }

    override fun onPause() {
        super.onPause()
        activity?.unregisterReceiver(notificationMsgReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val notificationMsgReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            binding.msgText.text =
                RemoteInput.getResultsFromIntent(intent)?.getCharSequence(KEY_TEXT_REPLY)

            val repliedNotification = NotificationCompat.Builder(requireContext(), MAIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(getString(R.string.reply_sent_label))
                .build()

            NotificationManagerCompat.from(requireContext())
                .notify(2, repliedNotification)
        }
    }

    private fun setOnClickListeners() {
        // Simple notification
        binding.nSimple.setOnClickListener {
            sendSimpleNotification()
        }
        // Notification with action button, that opens main activity
        binding.nAction.setOnClickListener {
            sendActionNotification()
        }
        // Notification with reply
        binding.nReply.setOnClickListener {
            sendReplyNotification()
        }
        // Notification with progress bar
        binding.nProgress.setOnClickListener {
            sendProgressNotification()
        }
    }

    private fun sendSimpleNotification() {
        val notificationSimple = NotificationCompat.Builder(requireContext(), MAIN_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.placeholder_title))
            .setContentText(getString(R.string.placeholder_content))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(requireContext())
            .notify(0, notificationSimple)
    }

    private fun sendActionNotification() {
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent
            .getActivity(requireContext(), 0, intent, 0)

        val notificationWithAction =
            NotificationCompat.Builder(requireContext(), MAIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.placeholder_title))
                .setContentText(getString(R.string.placeholder_content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(
                    R.drawable.ic_launcher_foreground,
                    getString(R.string.notification_button_text),
                    pendingIntent
                )
                .setAutoCancel(true)
                .build()

        NotificationManagerCompat.from(requireContext())
            .notify(1, notificationWithAction)
    }

    private fun sendReplyNotification() {
        val replyLabel: String = resources.getString(R.string.reply_label)
        val remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
            setLabel(replyLabel)
            build()
        }

        val intent = Intent(NR_ACTION)
        val replyPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(
                requireContext(),
                228,
                intent,
                0
            )

        val action: NotificationCompat.Action =
            NotificationCompat.Action.Builder(
                R.drawable.ic_launcher_foreground,
                getString(R.string.reply_action_title),
                replyPendingIntent
            )
                .addRemoteInput(remoteInput)
                .build()

        val notificationWithReply =
            NotificationCompat.Builder(requireContext(), MAIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.placeholder_title))
                .setContentText(getString(R.string.placeholder_content))
                .addAction(action)
                .setAutoCancel(true)
                .build()

        NotificationManagerCompat.from(requireContext())
            .notify(2, notificationWithReply)
    }

    private fun sendProgressNotification() {
        if (!prgrsNotifIsActive) {
            prgrsNotifIsActive = true
            val builder =
                NotificationCompat.Builder(requireContext(), SECONDARY_CHANNEL_ID).apply {
                    setContentTitle(getString(R.string.placeholder_title))
                    setContentText(getString(R.string.placeholder_content))
                    setSmallIcon(R.drawable.ic_launcher_foreground)
                    setAutoCancel(true)
                    priority = NotificationCompat.PRIORITY_LOW
                }
            // Usually Service should be used in scenario like this, but I've used  executor
            // not to complicated the app too much
            val executor = Executors.newSingleThreadExecutor()

            executor.execute {
                NotificationManagerCompat.from(requireContext()).apply {
                    repeat(11) {
                        builder.setProgress(100, 10 * it, false)
                        builder.setContentText("Downloading... ${it * 10}%")
                        notify(3, builder.build())
                        Thread.sleep(1000)
                    }
                    builder.setContentText("Download complete")
                        .setProgress(0, 0, false)
                    notify(3, builder.build())
                }
                prgrsNotifIsActive = false
            }
        }
    }
}
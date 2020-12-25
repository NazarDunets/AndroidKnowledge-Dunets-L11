package com.example.androidknowledge_dunets_l11

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class UserTapReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(HOMEWORK_TAG, "$this --- button tap detected!")
    }
}
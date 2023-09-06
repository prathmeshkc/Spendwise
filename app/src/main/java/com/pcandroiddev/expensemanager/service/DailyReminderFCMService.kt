package com.pcandroiddev.expensemanager.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class DailyReminderFCMService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "onMessageReceived: From: ${message.from}")
        message.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }
    }


    companion object {
        const val TAG = "DailyReminderFCMService"
    }
}
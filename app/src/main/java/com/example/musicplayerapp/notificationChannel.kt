package com.example.musicplayerapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class notificationChannel: Application() {
    public var CHANNEL1="Channel1"
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel= NotificationChannel(CHANNEL1,"Music", NotificationManager.IMPORTANCE_HIGH)

            var manager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }
    }
}
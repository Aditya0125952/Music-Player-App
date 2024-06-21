package com.example.musicplayerapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlin.system.exitProcess

class broadcasting : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {

            "PreviousSong" -> {
                previoussong(context!!)
                musicService.startService(context)
            }

            "PlayBtn" -> {
                playpauseDunction()
                musicService.startService(context!!)
            }

            "nextSong" -> {
                nextsong(context!!)
                musicService.startService(context)
            }

            "Exit" -> {
                musicService.MusicPlayer = null
                musicService.stopService(context!!)
                exitProcess(1)
            }
        }

    }

}
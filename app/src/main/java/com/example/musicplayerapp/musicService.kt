package com.example.musicplayerapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media.app.NotificationCompat.MediaStyle
import kotlin.properties.Delegates

class musicService : Service(), MediaPlayer.OnCompletionListener{

    companion object{
        fun startService(context:Context) {
            val startIntent = Intent(context, musicService::class.java)
            ContextCompat.startForegroundService(context, startIntent)
        }
        fun stopService(context: Context) {
            val stopIntent = Intent(context, musicService::class.java)
            context.stopService(stopIntent)
        }
        var MusicPlayer: MediaPlayer?=null
        var musicIsPlaying= SongPlaying.isplaying
        val notificationChannel ="Music"
        var position by Delegates.notNull<Int>()


    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MusicPlayer!!.setOnCompletionListener(this)
        showNotification()
        return START_NOT_STICKY
    }

    fun CreateChannel() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel=
                NotificationChannel(notificationChannel,"musicNotification", NotificationManager.IMPORTANCE_DEFAULT)
            val manager =getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
    @SuppressLint("ForegroundServiceType", "UnspecifiedImmutableFlag")
    fun showNotification(){
        CreateChannel()
        val playIntent= Intent(baseContext,broadcasting::class.java).setAction("PlayBtn")
        val playPendingIntent= PendingIntent.getBroadcast(
            baseContext,
            0,
            playIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val nextIntent= Intent(baseContext,broadcasting::class.java).setAction("nextSong")
        val nextPendingIntent= PendingIntent.getBroadcast(
            baseContext,
            0,
            nextIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val prevIntent= Intent(baseContext,broadcasting::class.java).setAction("PreviousSong")
        val prevPendingIntent= PendingIntent.getBroadcast(
            baseContext,
            0,
            prevIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val exitIntent= Intent(baseContext,broadcasting::class.java).setAction("Exit")
        val exitPendingIntent= PendingIntent.getBroadcast(
            baseContext,
            0,
            exitIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

         position=SongPlaying.songPostion
        val ImageArt= getImageArt(SongPlaying.songsList[position].address)
        val NSongImage=if(ImageArt!=null){
            BitmapFactory.decodeByteArray(ImageArt,0,ImageArt.size)
        }else{
            BitmapFactory.decodeResource(resources,R.drawable.india)
        }
        val playpauseBtn= if(musicIsPlaying){
            R.drawable.baseline_pause_24
        }else{
            R.drawable.play_icon
        }




        val NotificationIntent= Intent(this,musicService::class.java)
        val pendingIntent= PendingIntent.getActivity(this,0,NotificationIntent, PendingIntent.FLAG_MUTABLE)
        val notification= NotificationCompat.Builder(this, notificationChannel)
            .setContentTitle(SongPlaying.songsList[position].Sname)
            .setContentText(SongPlaying.songsList[position].artistName)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(NSongImage)
            .setContentIntent(pendingIntent)
            .setStyle(MediaStyle())
            .addAction(R.drawable.skip_previous,"previous",prevPendingIntent)
            .addAction(playpauseBtn,"play/pause",playPendingIntent)
            .addAction(R.drawable.skip_next,"next",nextPendingIntent)
            .addAction(R.drawable.exit_icon,"EXIT",exitPendingIntent)
            .setOnlyAlertOnce(true)
            .build()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            startForeground(1, notification)
        } else {
            startForeground(1, notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        }
    }



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onCompletion(mp: MediaPlayer?) {
        nextsong(this)
        showNotification()
    }

}


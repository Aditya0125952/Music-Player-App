package com.example.musicplayerapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.util.Log
import androidx.core.graphics.drawable.toIcon
import com.bumptech.glide.Glide
import com.example.musicplayerapp.MyFragment.Companion
import com.example.musicplayerapp.SongPlaying.Companion.binding
import com.example.musicplayerapp.SongPlaying.Companion.songPostion
import com.example.musicplayerapp.SongPlaying.Companion.songsList
import com.example.musicplayerapp.musicService.Companion.position
import java.util.concurrent.TimeUnit

data class Music(val id :String,val Sname:String,val songimage : String,val address :String,val artistName:String,val duration: Long)

fun formateDuration(duration: Long):String{
    val minutes=TimeUnit.MINUTES.convert(duration,TimeUnit.MILLISECONDS)
    val seconds=(TimeUnit.SECONDS.convert(duration,TimeUnit.MILLISECONDS))-minutes*TimeUnit.SECONDS.convert(1,TimeUnit.MINUTES)

  return String.format("%02d:%02d",minutes,seconds)
}

fun getImageArt(path:String): ByteArray? {
    val reteriver= MediaMetadataRetriever()
    reteriver.setDataSource(path)

    return reteriver.embeddedPicture
}
fun selectedsong(){
    if(musicService.MusicPlayer ==null){
        musicService.MusicPlayer= MediaPlayer()
    }
    musicService.MusicPlayer!!.reset()
    musicService.MusicPlayer!!.setDataSource(songsList[songPostion].address)
    musicService.MusicPlayer!!.prepare()
    musicService.MusicPlayer!!.start()
    binding.Songstart.text= formateDuration(musicService.MusicPlayer!!.currentPosition.toLong())
    binding.songending.text= formateDuration(musicService.MusicPlayer!!.duration.toLong())
    SongPlaying.runnableseekbar()
    if(!SongPlaying.isplaying){
        SongPlaying.isplaying =true
        binding.palyPause.setIconResource(R.drawable.baseline_pause_24)
    }
}



fun playpauseDunction(context: Context){
    if(musicService.musicIsPlaying){
        binding.palyPause.setIconResource(R.drawable.play_icon)
        MyFragment.binding.fpausplay.setImageResource(R.drawable.fplay)
        SongPlaying.isplaying =false
        musicService.musicIsPlaying=false
        musicService.MusicPlayer!!.pause()
    }else{
        binding.palyPause.setIconResource(R.drawable.baseline_pause_24)
        MyFragment.binding.fpausplay.setImageResource(R.drawable.fpause)
        SongPlaying.isplaying =true
        musicService.musicIsPlaying=true
        musicService.MusicPlayer!!.start()
    }

    musicService.startService(context)
}

fun nextsong(context : Context){
    if(songsList.size-1== songPostion){
        songPostion =0
    }else{
        songPostion++
    }
    imageforsongfunction(context)
    musicService.startService(context)
    MyFragment.binding.fsngName.text= songsList[songPostion].Sname
    fragmentplayPauseCheck()
    fragmentImage()
    selectedsong()
}

fun previoussong(context: Context){
    if(songPostion ==0){
        songPostion = songsList.size-1
    }else{
        --songPostion
    }

    musicService.startService(context)
    imageforsongfunction(context)
    MyFragment.binding.fsngName.text= songsList[songPostion].Sname
    fragmentplayPauseCheck()
    fragmentImage()
    selectedsong()
}

fun fragmentplayPauseCheck(){
    if(musicService.musicIsPlaying==false){
        MyFragment.binding.fpausplay.setImageResource(R.drawable.fpause)
        musicService.musicIsPlaying=true
    }
}

fun imageforsongfunction(context: Context){
    Glide.with(context)
        .load(songsList[songPostion].songimage)
        .placeholder(R.drawable.india)
        .into(binding.selectedsongImage)
}

fun fragmentImage(){
    Glide.with(MyFragment.binding.root)
        .load(songsList[songPostion].songimage)
        .placeholder(R.drawable.india)
        .into(Companion.binding.fimg)
}


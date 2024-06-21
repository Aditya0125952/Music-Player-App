package com.example.musicplayerapp

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import com.bumptech.glide.Glide
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
    musicService.MusicPlayer!!.setDataSource(SongPlaying.songsList[SongPlaying.songPostion].address)
    musicService.MusicPlayer!!.prepare()
    musicService.MusicPlayer!!.start()
    SongPlaying.binding.Songstart.text= formateDuration(musicService.MusicPlayer!!.currentPosition.toLong())
    SongPlaying.binding.songending.text= formateDuration(musicService.MusicPlayer!!.duration.toLong())
    SongPlaying.runnableseekbar()
    if(!SongPlaying.isplaying){
        SongPlaying.isplaying =true
        SongPlaying.binding.palyPause.setIconResource(R.drawable.baseline_pause_24)
    }
}



fun playpauseDunction(){
    if(SongPlaying.isplaying){
        SongPlaying.binding.palyPause.setIconResource(R.drawable.play_icon)
        SongPlaying.isplaying =false
        musicService.MusicPlayer!!.pause()
    }else{
        SongPlaying.binding.palyPause.setIconResource(R.drawable.baseline_pause_24)
        SongPlaying.isplaying =true
        musicService.MusicPlayer!!.start()
    }

}

fun nextsong(context : Context){
    if(SongPlaying.songsList.size-1== SongPlaying.songPostion){
        SongPlaying.songPostion =0
    }else{
        ++SongPlaying.songPostion
    }
    Glide.with(context)
        .load(SongPlaying.songsList[SongPlaying.songPostion].songimage)
        .placeholder(R.drawable.india)
        .into(SongPlaying.binding.selectedsongImage)
    selectedsong()
}

fun previoussong(context: Context){
    if(SongPlaying.songPostion ==0){
        SongPlaying.songPostion = SongPlaying.songsList.size-1
    }else{
        --SongPlaying.songPostion
    }
    Glide.with(context)
        .load(SongPlaying.songsList[SongPlaying.songPostion].songimage)
        .placeholder(R.drawable.india)
        .into(SongPlaying.binding.selectedsongImage)
    selectedsong()
}
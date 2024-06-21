package com.example.musicplayerapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.musicplayerapp.databinding.ActivitySongPlayingBinding

class SongPlaying : AppCompatActivity() {

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivitySongPlayingBinding
        lateinit var songsList:ArrayList<Music>
        var songPostion:Int=0
        var isplaying=true
        private lateinit var runnable:Runnable

        fun runnableseekbar(){
            binding.seekbar.progress=0
            binding.seekbar.max=musicService.MusicPlayer!!.duration
            binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if(fromUser){
                        musicService.MusicPlayer!!.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }

            })

            runnable=Runnable{
                binding.Songstart.text= formateDuration(musicService.MusicPlayer!!.currentPosition.toLong())
                binding.seekbar.progress=musicService.MusicPlayer!!.currentPosition

                Handler(Looper.getMainLooper()).postDelayed(runnable,300)
            }
            Handler(Looper.getMainLooper()).postDelayed(runnable,0)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySongPlayingBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        songPostion=intent.getIntExtra("position",0)
        when(intent.getStringExtra("class")){
            "Music"->{
                songsList= ArrayList()
                songsList.addAll(MainActivity.MusicList_in_MA)
                imageforsongfunction()
                selectedsong()
                musicService.startService(this)

                binding.palyPause.setOnClickListener {
                    playpauseDunction()
                    musicService.startService(this)
                }
                binding.next.setOnClickListener {
                    nextsong(this)
                    imageforsongfunction()
                    musicService.startService(this)
                }
                binding.previous.setOnClickListener {
                    previoussong(this)
                    imageforsongfunction()
                    musicService.startService(this)
                }

            }
        }

    }

    fun imageforsongfunction(){
        Glide.with(this)
            .load(songsList[songPostion].songimage)
            .placeholder(R.drawable.india)
            .into(binding.selectedsongImage)
    }




}
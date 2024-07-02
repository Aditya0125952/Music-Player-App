package com.example.musicplayerapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toIcon
import com.bumptech.glide.Glide
import com.example.musicplayerapp.SongPlaying.Companion
import com.example.musicplayerapp.SongPlaying.Companion.songPostion
import com.example.musicplayerapp.SongPlaying.Companion.songsList
import com.example.musicplayerapp.databinding.FragmentMyBinding
import com.example.musicplayerapp.musicService.Companion.position

class MyFragment : Fragment() {
@SuppressLint("StaticFieldLeak")
companion object{
    lateinit var binding:FragmentMyBinding

}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_my, container, false)
        binding= FragmentMyBinding.bind(view)
        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
              binding.root.visibility=View.GONE
              view.setBackgroundColor(Color.GRAY)

        binding.fnext.setOnClickListener{
            context?.let { it1 -> nextsong(it1) }
        }

        binding.fprev.setOnClickListener{
            context?.let { it1 -> previoussong(it1) }
        }
        binding.fpausplay.setOnClickListener {
            context?.let { it1 -> playpauseDunction(it1) }

        }


    }

    override fun onResume() {
        super.onResume()
        if(musicService.MusicPlayer!=null){
            if (musicService.musicIsPlaying!=null) {
                binding.root.visibility = View.VISIBLE
                binding.fsngName.text= songsList[songPostion].Sname
                binding.fsngName.isSelected=true
               fragmentImage()
            }
        }


    }


}
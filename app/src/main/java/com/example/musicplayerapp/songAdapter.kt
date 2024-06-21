package com.example.musicplayerapp

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

@Suppress("DEPRECATION")
class songAdapter(val context : Activity, val songsList:ArrayList<Music>)
    : RecyclerView.Adapter<songAdapter.voewHolder>()
{
    class voewHolder (singleMusic : View): RecyclerView.ViewHolder(singleMusic){
        val songNamefromAdapter =singleMusic.findViewById<TextView>(R.id.songName)
        val songImagefromAdapter=singleMusic.findViewById<ShapeableImageView>(R.id.songeImage)
        val songDuration=singleMusic.findViewById<TextView>(R.id.songDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): voewHolder {
        val singleitem= LayoutInflater.from(context).inflate(R.layout.singlesongview,parent,false)
        return voewHolder(singleitem)
    }

    override fun onBindViewHolder(holder: voewHolder, position: Int) {
        val currentSong =songsList[position]
        holder.songNamefromAdapter.text=currentSong.Sname
        holder.songDuration.text= formateDuration(currentSong.duration)
        Glide.with(context)
            .load(currentSong.songimage)
            .placeholder(R.drawable.india)
            .into(holder.songImagefromAdapter)
        holder.itemView.setOnClickListener {
            val swapingActivity = Intent(context, SongPlaying::class.java)
            swapingActivity.putExtra("position",position)
            swapingActivity.putExtra("class","Music")
            ContextCompat.startActivity(context, swapingActivity, null)

        }

    }

    override fun getItemCount(): Int {
        return songsList.size
    }



}
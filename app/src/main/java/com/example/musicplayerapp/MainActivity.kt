package com.example.musicplayerapp

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var myrecycler : RecyclerView
    companion object{
        lateinit var MusicList_in_MA :ArrayList<Music>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getPermission()
        fragment()
        myrecycler=findViewById<RecyclerView>(R.id.recyclerView)
        myrecycler.layoutManager= LinearLayoutManager(this)
        MusicList_in_MA=retriveAllSongsFromStorage()
        myrecycler.adapter=songAdapter(this, MusicList_in_MA)

        binding.fragment.setOnClickListener{
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment,MyFragment())
            .commit()
    }

    @SuppressLint("InlinedApi")
    private fun getPermission(){
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_MEDIA_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO,android.Manifest.permission.POST_NOTIFICATIONS,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),38)
        }

    }

    @SuppressLint("InlinedApi")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==38){
            if(grantResults.isNotEmpty()&&grantResults[0]== PackageManager.PERMISSION_GRANTED&&grantResults[1]== PackageManager.PERMISSION_GRANTED&&grantResults[2]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "permisson granted", Toast.LENGTH_SHORT).show()
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO,android.Manifest.permission.POST_NOTIFICATIONS,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),38)
            }
        }
    }

    @SuppressLint("Range")
    private fun retriveAllSongsFromStorage(): ArrayList<Music> {
        val tempSongList=ArrayList<Music>()
        val selection= MediaStore.Audio.Media.IS_MUSIC+"!=0"
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION)

        applicationContext.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            MediaStore.Audio.Media.DATE_ADDED,
            null
        )?.use { cursor ->
            while(cursor.moveToNext()) {
                val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val album = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                val uri = Uri.parse("content://media/external/audio/albumart")
                // for getting the song pic in the recycler view
                val songImageC = Uri.withAppendedPath(uri, album).toString()
                val adressC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val CArtistName=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val cduration=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))

                val singlesong = Music(idC, titleC, songImageC, adressC,CArtistName,cduration)
                tempSongList.add(singlesong)
            }
        }

        return tempSongList
    }



}
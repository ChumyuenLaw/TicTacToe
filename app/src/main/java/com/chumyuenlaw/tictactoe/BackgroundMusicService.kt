package com.chumyuenlaw.tictactoe

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi


class BackgroundMusicService : Service() {

    private lateinit var player: MediaPlayer

    val ACTION_STOP = "stop_music_service"
    val ACTION_START = "start_music_service"

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate() {
        super.onCreate()

        player = MediaPlayer.create(this, R.raw.bgm)
        //val assetFileDescriptor = applicationContext.assets.openFd("res/raw/bgm.mp3") as AssetFileDescriptor
        //player.setDataSource(assetFileDescriptor)
        player.isLooping = true // Set looping
        player.setVolume(50f, 50f)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if(intent.action == ACTION_STOP)
            player.stop()
        else
            player.start()
        return startId
    }

    override fun onStart(intent: Intent, startId: Int) {
        // TO DO
    }

    fun onUnBind(arg0: Intent): IBinder? {
        // TO DO Auto-generated method
        return null
    }

    fun onStop() {
        player.stop()
    }

    fun onPause() {

    }

    override fun onDestroy() {
        player.stop()
        player.release()
    }

    override fun onLowMemory() {

    }
}
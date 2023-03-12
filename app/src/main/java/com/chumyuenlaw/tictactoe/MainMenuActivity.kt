package com.chumyuenlaw.tictactoe

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import android.widget.Button

class MainMenuActivity : AppCompatActivity() {

    private lateinit var startNewGameBtn : Button
    private lateinit var statisticsBtn : Button
    private lateinit var settingsBtn : Button
    private lateinit var exitBtn : Button
    private var soundEffectVolume = 1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        startNewGameBtn = findViewById(R.id.menu_start_game_button)
        statisticsBtn = findViewById(R.id.menu_statistics_button)
        settingsBtn = findViewById(R.id.menu_settings_button)
        exitBtn = findViewById(R.id.menu_exit_button)

        val soundPool = SoundPool.Builder().build()
        val clickCommon = soundPool.load(applicationContext, R.raw.click3, 1)

        loadSoundEffectSettings()
        val soundPref = getSharedPreferences("sound_settings", Context.MODE_PRIVATE)
        if(soundPref.getBoolean("bgm_settings", true)) {
            val bgmService = Intent(this, BackgroundMusicService::class.java)
            bgmService.action = "start_music_service"
            startService(bgmService)
        }

        startNewGameBtn.setOnClickListener {
            soundPool.play(clickCommon, soundEffectVolume, soundEffectVolume, 1, 0, 1f)
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        statisticsBtn.setOnClickListener {
            soundPool.play(clickCommon, soundEffectVolume, soundEffectVolume, 1, 0, 1f)
            val intent = Intent(applicationContext, StatisticsActivity::class.java)
            startActivity(intent)
        }

        settingsBtn.setOnClickListener {
            soundPool.play(clickCommon, soundEffectVolume, soundEffectVolume, 1, 0, 1f)
            val intent = Intent(applicationContext, SettingsActivity::class.java)

            startActivity(intent)
        }

        exitBtn.setOnClickListener {
            soundPool.play(clickCommon, soundEffectVolume, soundEffectVolume, 1, 0, 1f)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, BackgroundMusicService::class.java))
    }

    override fun onResume() {
        super.onResume()
        loadSoundEffectSettings()
    }

    private fun loadSoundEffectSettings() {
        val soundPref = getSharedPreferences("sound_settings", Context.MODE_PRIVATE)
        if (soundPref.getBoolean("sound_effects_settings", true))
            soundEffectVolume = 1f
        else
            soundEffectVolume = 0f
    }


}
package com.chumyuenlaw.tictactoe

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private lateinit var radioGroup : RadioGroup

    private lateinit var soundEffectSwitch : SwitchMaterial

    private lateinit var backgroundMusicSwitch : SwitchMaterial

    private lateinit var clearCacheBtn : MaterialButton

    private lateinit var aboutMeBtn : MaterialButton

    private var gameDifficulty = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        radioGroup = findViewById(R.id.radioGroup)
        soundEffectSwitch = findViewById(R.id.effect_music_switch)
        backgroundMusicSwitch = findViewById(R.id.background_music_switch)
        clearCacheBtn = findViewById(R.id.clear_cache_button)
        aboutMeBtn = findViewById(R.id.about_me_button)

        val pref = getSharedPreferences("game_difficulty", Context.MODE_PRIVATE)
        val editor = pref.edit()

        val soundPref = getSharedPreferences("sound_settings", Context.MODE_PRIVATE)
        val soundEditor = soundPref.edit()

        radioGroup.setOnCheckedChangeListener { _, i ->
            when(i) {
            R.id.easy -> {
                gameDifficulty = 0
            }
            R.id.moderate -> {
                gameDifficulty = 1
            }
            R.id.hard -> {
                gameDifficulty = 2
            }
            }
            editor.putInt("game_difficulty", gameDifficulty)
            editor.commit()
        }

        soundEffectSwitch.isChecked = soundPref.getBoolean("sound_effects_settings", true)
        soundEffectSwitch.setOnCheckedChangeListener { compoundButton, b ->
            soundEditor.putBoolean("sound_effects_settings", compoundButton.isChecked)
            soundEditor.commit()
        }

        backgroundMusicSwitch.isChecked = soundPref.getBoolean("bgm_settings", true)
        backgroundMusicSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) {
                startMusicService()
            } else {
                stopMusicService()
            }
            soundEditor.putBoolean("bgm_settings", compoundButton.isChecked)
            soundEditor.commit()
        }

        clearCacheBtn.setOnClickListener {
            val pref = getSharedPreferences("battle_statistics", Context.MODE_PRIVATE)
            pref.edit().putInt("user_won", 0).apply()
            pref.edit().putInt("computer_won", 0).apply()
            pref.edit().putInt("tie", 0).apply()
            Toast.makeText(applicationContext, "Success!", Toast.LENGTH_SHORT).show()
        }

        aboutMeBtn.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.settings_about_me)
                .setMessage(R.string.settings_about_me_info)
                .setPositiveButton("OK") { _, _ -> }
                .show()
        }
    }

    fun stopMusicService() {
        val musicIntent = Intent(this, BackgroundMusicService::class.java)
        musicIntent.action = "stop_music_service"
        stopService(musicIntent)
    }

    fun startMusicService() {
        val bgmService = Intent(this, BackgroundMusicService::class.java)
        bgmService.action = "start_music_service"
        startService(bgmService)
    }
}
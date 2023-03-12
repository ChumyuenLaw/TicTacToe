package com.chumyuenlaw.tictactoe

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.RadioGroup

class SettingsActivity : AppCompatActivity() {
    private lateinit var radioGroup : RadioGroup

    private var gameDifficulty = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        radioGroup = findViewById(R.id.radioGroup)
        val pref = getSharedPreferences("game_difficulty", Context.MODE_PRIVATE)
        val editor = pref.edit();

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
    }
}
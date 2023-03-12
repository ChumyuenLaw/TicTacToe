package com.chumyuenlaw.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class MainMenuActivity : AppCompatActivity() {

    private lateinit var startNewGameBtn : Button
    private lateinit var statisticsBtn : Button
    private lateinit var settingsBtn : Button
    private lateinit var exitBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        startNewGameBtn = findViewById(R.id.menu_start_game_button)
        statisticsBtn = findViewById(R.id.menu_statistics_button)
        settingsBtn = findViewById(R.id.menu_settings_button)
        exitBtn = findViewById(R.id.menu_exit_button)

        startNewGameBtn.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        statisticsBtn.setOnClickListener {
            val intent = Intent(applicationContext, StatisticsActivity::class.java)
            startActivity(intent)
        }

        settingsBtn.setOnClickListener {
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            startActivity(intent)
        }

        exitBtn.setOnClickListener {
            finish()
        }

    }


}
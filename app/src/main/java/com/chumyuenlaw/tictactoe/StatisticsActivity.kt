package com.chumyuenlaw.tictactoe

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class StatisticsActivity : AppCompatActivity() {

    private lateinit var playerWinTW : TextView
    private lateinit var aiWinTW : TextView
    private lateinit var tieTW : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        playerWinTW = findViewById(R.id.textView_player_number)
        aiWinTW = findViewById(R.id.textView_ai_number)
        tieTW = findViewById(R.id.textView_tie_number)

        val pref = getSharedPreferences("battle_statistics", Context.MODE_PRIVATE)
        if (pref != null) {
            val userWon = pref.getInt("user_won", 0)
            val computerWon = pref.getInt("computer_won", 0)
            val tie = pref.getInt("tie", 0)
            playerWinTW.text = userWon.toString()
            aiWinTW.text = computerWon.toString()
            tieTW.text = tie.toString()
        }
    }
}
package com.chumyuenlaw.tictactoe

import android.content.Context
import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorCompat
import com.google.android.material.button.MaterialButton

class MainMenuActivity : AppCompatActivity() {

    private lateinit var startNewGameBtn : MaterialButton
    private lateinit var statisticsBtn : MaterialButton
    private lateinit var settingsBtn : MaterialButton
    private lateinit var exitBtn : MaterialButton
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

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (!hasFocus)
            return
        animate()
        super.onWindowFocusChanged(hasFocus)
    }

    private fun loadSoundEffectSettings() {
        val soundPref = getSharedPreferences("sound_settings", Context.MODE_PRIVATE)
        if (soundPref.getBoolean("sound_effects_settings", true))
            soundEffectVolume = 1f
        else
            soundEffectVolume = 0f
    }

    private fun animate() {
        val imageLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)
        val buttonLayout = findViewById<ConstraintLayout>(R.id.constraintLayout4)
        val titleTextView = findViewById<TextView>(R.id.fullscreen_content)

        ViewCompat.animate(imageLayout)
            .translationY(-800f)
            .setStartDelay(300)
            .setDuration(1000)
            .setInterpolator(DecelerateInterpolator(1.2f))
            .start()

        ViewCompat.animate(titleTextView)
            .scaleX(1f)
            .scaleY(1f)
            .setStartDelay(800)
            .setDuration(500)
            .setInterpolator(DecelerateInterpolator())
            .start()


        for (i in 0 until buttonLayout.childCount) {
            val view = buttonLayout.getChildAt(i)
            lateinit var viewAnimator: ViewPropertyAnimatorCompat

            if(view !is MaterialButton){
                viewAnimator = ViewCompat.animate(view)
                    .translationY(50f).alpha(1f)
                    .setStartDelay((300 * i + 1300).toLong())
                    .setDuration(800)
            } else {
                viewAnimator = ViewCompat.animate(view)
                    .scaleY(1f)
                    .scaleX(1f)
                    .setStartDelay((300 * i + 1300).toLong())
                    .setDuration(500)
            }
            viewAnimator.setInterpolator(DecelerateInterpolator()).start()
        }
    }
}
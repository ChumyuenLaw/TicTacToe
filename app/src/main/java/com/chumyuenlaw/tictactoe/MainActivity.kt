package com.chumyuenlaw.tictactoe

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Represents the internal state of the game
    var mGame = TicTacToeGame()

    // Buttons making up the board
    private lateinit var mBoardButtons : Array<Button>

    // TextView
    private lateinit var information : TextView
    private lateinit var aiWinTW : TextView
    private lateinit var playerWinTW : TextView
    private lateinit var gameResultTW : TextView

    // Sound Effects
    private lateinit var soundPool : SoundPool
    private var clickCross = -1
    private var clickCircle = -1
    private var clickCommon = -1

    // Game Over
    var mGameOver = false

    var userWon: Int = 0

    var computerWon: Int = 0

    var tie: Int = 0

    private var selectedPlayer = 0

    private var gameDifficulty = 1

    private var soundEffectVolume = 1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        information = findViewById(R.id.information)

        aiWinTW = findViewById(R.id.textView_circle)

        playerWinTW = findViewById(R.id.textView_cross)

        gameResultTW = findViewById(R.id.textView_game_result)

        soundPool = SoundPool.Builder().build()
        clickCircle = soundPool.load(this, R.raw.click, 1)
        clickCross = soundPool.load(this, R.raw.click2, 1)
        clickCommon = soundPool.load(this, R.raw.click3, 1)

        val soundPref = getSharedPreferences("sound_settings", Context.MODE_PRIVATE)
        if (soundPref.getBoolean("sound_effects_settings", true))
            soundEffectVolume = 1f
        else
            soundEffectVolume = 0f

        val button0 = findViewById<Button>(R.id.button0)
        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)
        val button7 = findViewById<Button>(R.id.button7)
        val button8 = findViewById<Button>(R.id.button8)
        // Buttons making up the board
        mBoardButtons = arrayOf(button0, button1, button2, button3, button4, button5, button6, button7, button8)

        loadBattleStatistics()

        loadGameDifficulty()

        startNewGame()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val buttonsText = arrayListOf<CharSequence>()
        val buttonsColor = arrayListOf<ColorStateList>()
        val buttonsIsEnable = arrayListOf<Boolean>()
        for (i in mBoardButtons.indices) {
            buttonsText.add(mBoardButtons[i].text)
            buttonsColor.add(mBoardButtons[i].textColors)
            buttonsIsEnable.add(mBoardButtons[i].isEnabled)
        }

        outState.putSerializable("button_text", buttonsText)
        outState.putSerializable("button_colors", buttonsColor)
        outState.putSerializable("button_is_enable", buttonsIsEnable)
        outState.putSerializable("info", information.text.toString())
        outState.putSerializable("info_color", information.currentTextColor)
        outState.putSerializable("game_result", gameResultTW.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val buttonsText = savedInstanceState.getSerializable("button_text") as ArrayList<CharSequence>
        val buttonsColor = savedInstanceState.getSerializable("button_colors") as ArrayList<ColorStateList>
        val buttonsIsEnable = savedInstanceState.getSerializable("button_is_enable") as ArrayList<Boolean>

        for (i in mBoardButtons.indices) {
            mBoardButtons[i].text = buttonsText[i]
            mBoardButtons[i].setTextColor(buttonsColor[i])
            mBoardButtons[i].isEnabled = buttonsIsEnable[i]
            if(!mBoardButtons[i].isEnabled) {
                if(mBoardButtons[i].text == "X")
                    mGame.setMove(TicTacToeGame.HUMAN_PLAYER, i)
                else
                    mGame.setMove(TicTacToeGame.COMPUTER_PLAYER, i)
            }
        }
        information.text = savedInstanceState.getSerializable("info") as String
        information.setTextColor(savedInstanceState.getSerializable("info_color") as Int)
        gameResultTW.text = savedInstanceState.getSerializable("game_result") as String

        val winner = mGame.checkForWinner()
        if (winner != 0)
            mGameOver = true
    }

    private fun saveBattleStatistics(userWon : Int, computerWon : Int, tie : Int) {
        val pref = getSharedPreferences("battle_statistics", Context.MODE_PRIVATE)
        pref.edit().putInt("user_won", userWon).apply()
        pref.edit().putInt("computer_won", computerWon).apply()
        pref.edit().putInt("tie", tie).apply()
    }

    private fun loadBattleStatistics() {
        val pref = getSharedPreferences("battle_statistics", Context.MODE_PRIVATE)
        if (pref != null) {
            userWon = pref.getInt("user_won", 0)
            computerWon = pref.getInt("computer_won", 0)
            tie = pref.getInt("tie", 0)
        }
        updateBattleStatistics()
    }

    private fun updateBattleStatistics() {
        playerWinTW.text = userWon.toString()
        aiWinTW.text = computerWon.toString()
    }

    private fun loadGameDifficulty() {
        val pref = getSharedPreferences("game_difficulty", Context.MODE_PRIVATE)
        if (pref != null) {
            gameDifficulty = pref.getInt("game_difficulty", 0)
        }
        mGame.difficulty = gameDifficulty
    }

    private fun disableAllButtons(): ArrayList<Int> {
        val availableList = ArrayList<Int>()
        for (i in mBoardButtons.indices) {
            if(mBoardButtons[i].isEnabled) {
                mBoardButtons[i].isEnabled = false
                availableList.add(i)
            }
        }
        return availableList;
    }

    private fun activateAvailableButtons(availableList : ArrayList<Int>) {
        for (i in availableList.indices) {
            mBoardButtons[availableList[i]].isEnabled = true
        }
    }

    private fun gameResult(winner : Int) {
        if (winner == 0) {
            information.text = getString(R.string.gameboard_info_player_term)
        } else if (winner == 1) {
            gameResultTW.text = getString(R.string.gameboard_gameresult_draw)
            information.text = getString(R.string.gameboard_info_think_more)
            tie++
            mGameOver = true
            saveBattleStatistics(userWon, computerWon, tie)
        } else if (winner == 2) {
            gameResultTW.text = getString(R.string.gameboard_gameresult_victory)
            information.text = getString(R.string.gameboard_info_congratulation)
            userWon++
            mGameOver = true
            saveBattleStatistics(userWon, computerWon, tie)
        } else {
            gameResultTW.text = getString(R.string.gameboard_gameresult_defeat)
            information.text = getString(R.string.gameboard_info_encougarement)
            computerWon++
            mGameOver = true
            saveBattleStatistics(userWon, computerWon, tie)
        }
        updateBattleStatistics()
    }

    //--- Set up the game board.
    private fun startNewGame() {
        mGameOver = false
        mGame.clearBoard()
        //---Reset all buttons
        for (i in mBoardButtons.indices) {
            mBoardButtons[i].text = ""
            mBoardButtons[i].isEnabled = true
        }

        gameResultTW.text = ""

        if (selectedPlayer == 0) {
            information.text = getString(R.string.gameboard_info_player_term)
        }
        else if (selectedPlayer == 1){
            information.text = getString(R.string.gameboard_info_ai_term)
            val availableList = disableAllButtons()
            Handler().postDelayed({
                val move = mGame.computerMove

                runOnUiThread {
                    soundPool.play(clickCircle, soundEffectVolume, soundEffectVolume, 1, 0, 1f)
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move)
                    information.text = getString(R.string.gameboard_info_player_term)
                }
                availableList.remove(move)
                activateAvailableButtons(availableList)
                availableList.clear()
            }, 1000)
        }
    }

    // multiple button click method
    fun onButtonClicked(view: View) {
        val buSelected: Button = view as Button
        var location = 0
        when (buSelected.id) {
            R.id.button0 -> location = 0
            R.id.button1 -> location = 1
            R.id.button2 -> location = 2
            R.id.button3 -> location = 3
            R.id.button4 -> location = 4
            R.id.button5 -> location = 5
            R.id.button6 -> location = 6
            R.id.button7 -> location = 7
            R.id.button8 -> location = 8
        }
        if (!mGameOver) {
            if (mBoardButtons[location].isEnabled) {

                soundPool.play(clickCross, soundEffectVolume, soundEffectVolume, 1, 0, 1f)
                setMove(TicTacToeGame.HUMAN_PLAYER, location)

                //--- If no winner yet, let the computer make a move
                var winner = mGame.checkForWinner()
                if (winner == 0) {
                    val availableList = disableAllButtons()
                    information.setText(R.string.gameboard_info_ai_term)
                    Handler().postDelayed({
                        val move = mGame.computerMove
                        runOnUiThread {
                            setMove(TicTacToeGame.COMPUTER_PLAYER, move)
                            winner = mGame.checkForWinner()
                            gameResult(winner)
                        }
                        availableList.remove(move)
                        activateAvailableButtons(availableList)
                        availableList.clear()
                        soundPool.play(clickCircle, soundEffectVolume, soundEffectVolume, 1, 0, 1f)
                    },1000)
                } else {
                    gameResult(winner)
                }
            }
        }
    }

    private fun whoPlayFirstDialog() {
        val players = arrayOf(getString(R.string.gameboard_play_first_dialog_player), getString(R.string.gameboard_play_first_dialog_ai))
        AlertDialog.Builder(this)
            .setTitle(R.string.gameboard_play_first_dialog_header)
            .setSingleChoiceItems(players, selectedPlayer) { _, which ->
                selectedPlayer = which
            }
            .setPositiveButton(R.string.gameboard_play_first_dialog_button_confirm) { _,_ ->
                startNewGame()
            }
            .show()
    }

    private fun setMove(player: Char, location: Int) {
        mGame.setMove(player, location)
        mBoardButtons[location].isEnabled = false
        mBoardButtons[location].text = player.toString()
        if (player == TicTacToeGame.HUMAN_PLAYER) {
            mBoardButtons[location].setTextColor(Color.parseColor("#E7AB9A"))
        } else {
            mBoardButtons[location].setTextColor(Color.parseColor("#DF7857"))
        }
    }

    //--- OnClickListener for Restart a New Game Button
    fun newGame(v: View) {
        soundPool.play(clickCommon, soundEffectVolume, soundEffectVolume, 1, 0, 1f)
        whoPlayFirstDialog()
    }
}
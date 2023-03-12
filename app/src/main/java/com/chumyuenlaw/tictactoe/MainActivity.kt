package com.chumyuenlaw.tictactoe

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import android.os.Build.VERSION_CODES.S
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    // Represents the internal state of the game
    var mGame = TicTacToeGame()

    // Buttons making up the board
    private lateinit var mBoardButtons : Array<Button>

    // TextView
    private lateinit var information : TextView

    // Game Over
    var mGameOver = false

    var userWon: Int = 0

    var computerWon: Int = 0

    var tie: Int = 0

    private var selectedPlayer = 0

    private var gameDifficulty = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        information = findViewById(R.id.information)

        val button0 = findViewById(R.id.button0) as Button
        val button1 = findViewById(R.id.button1) as Button
        val button2 = findViewById(R.id.button2) as Button
        val button3 = findViewById(R.id.button3) as Button
        val button4 = findViewById(R.id.button4) as Button
        val button5 = findViewById(R.id.button5) as Button
        val button6 = findViewById(R.id.button6) as Button
        val button7 = findViewById(R.id.button7) as Button
        val button8 = findViewById(R.id.button8) as Button
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
    }

    private fun loadGameDifficulty() {
        val pref = getSharedPreferences("game_difficulty", Context.MODE_PRIVATE)
        if (pref != null) {
            gameDifficulty = pref.getInt("game_difficulty", 0)
        }
        mGame.difficulty = gameDifficulty
    }

//    private fun loadBattleStatistics() {
//        val pref = getSharedPreferences("battle_statistics", Context.MODE_PRIVATE)
//        if (pref != null) {
//            userWon = pref.getInt("user_won", 0)
//            computerWon = pref.getInt("computer_won", 0)
//            tie = pref.getInt("tie", 0)
//        }
//        updateBattleStatistics()
//    }

//    private fun updateBattleStatistics() {
//        userWonTV.setText(R.string.user_won)
//        userWonTV.append(userWon.toString())
//        computerWonTV.setText(R.string.computer_won)
//        computerWonTV.append(computerWon.toString())
//        tieTV.setText(R.string.tie_num)
//        tieTV.append(tie.toString())
//    }

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
            information.text = getString(R.string.your_term)
        } else if (winner == 1) {
            information.text = getString(R.string.tie)
            tie++
            mGameOver = true
            saveBattleStatistics(userWon, computerWon, tie)
        } else if (winner == 2) {
            information.text = getString(R.string.you_won)
            userWon++
            mGameOver = true
            saveBattleStatistics(userWon, computerWon, tie)
        } else {
            information.text = getString(R.string.android_won)
            computerWon++
            mGameOver = true
            saveBattleStatistics(userWon, computerWon, tie)
        }
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

        if (selectedPlayer == 0) {
            information.text = getString(R.string.your_term)
        }
        else if (selectedPlayer == 1){
            information.text = getString(R.string.android_term)
            val availableList = disableAllButtons()
            Handler().postDelayed({
                val move = mGame.computerMove
                runOnUiThread {
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move)
                    information.text = getString(R.string.your_term)
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
                setMove(TicTacToeGame.HUMAN_PLAYER, location)
                //--- If no winner yet, let the computer make a move
                var winner = mGame.checkForWinner()
                if (winner == 0) {
                    val availableList = disableAllButtons()
                    information.setText(R.string.android_term)
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
                    },1000)
                } else {
                    gameResult(winner)
                }
            }
        }
    }

    private fun whoPlayFirstDialog() {
        val players = arrayOf(getString(R.string.player), getString(R.string.computer))
        AlertDialog.Builder(this)
            .setTitle(R.string.who_play_first)
            .setSingleChoiceItems(players, selectedPlayer) { _, which ->
                selectedPlayer = which
            }
            .setPositiveButton(R.string.confirm) { _,_ ->
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
        whoPlayFirstDialog()
    }

//    // --- Option Menu ---
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu to use in the action bar
//        val inflater = menuInflater
//        inflater.inflate(R.menu.menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle presses on the action bar menu items
//        when (item.itemId) {
//            R.id.menu_difficulty -> {
//                difficultySelection()
//                return true
//            }
//            R.id.menu_clear_stat -> {
//                clearCache()
//                return true
//            }
//            R.id.menu_exit -> {
//                finish()
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

//    private fun clearCache () {
//        val pref = getSharedPreferences("battle_statistics", Context.MODE_PRIVATE)
//        pref.edit().putInt("user_won", 0).apply()
//        pref.edit().putInt("computer_won", 0).apply()
//        pref.edit().putInt("tie", 0).apply()
//        loadBattleStatistics()
//    }

//    private fun difficultySelection (){
//        val players = arrayOf(getString(R.string.easy), getString(R.string.harder), getString(R.string.expert) )
//        AlertDialog.Builder(this)
//            .setTitle(R.string.difficulty)
//            .setSingleChoiceItems(players, gameDifficulty) { _, which ->
//                gameDifficulty = which
//            }
//            .setPositiveButton(R.string.confirm) { _,_ ->
//                mGame.difficulty = gameDifficulty
//                startNewGame()
//            }
//            .show()
//    }
}
package com.chumyuenlaw.tictactoe;

/* TicTacToeConsole.java
 * By Frank McCown (Harding University)
 *
 * This is a tic-tac-toe game that runs in the console window.  The human
 * is X and the computer is O.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToeGame {

    // Characters used to represent the human, computer, and open spots
    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    public static final char OPEN_SPOT = ' ';
    public static final int BOARD_SIZE = 9;
    private char mBoard[] = { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    private Random mRand;
    public int difficulty = 1;

    /** The constructor of the TicTacToeGame have to be remove some code as follows */
    public TicTacToeGame() {
        // Seed the random number generator
        mRand = new Random();
        char turn = HUMAN_PLAYER; // Human starts first
        int win = 0; // Set to 1, 2, or 3 when game is over
    }

    /** Clear the board of all X's and O's. */
    public void clearBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            mBoard[i] = OPEN_SPOT;
        }
    }

    /** Set the given player at the given location on the game board * */
    public void setMove(char player, int location) {
        mBoard[location] = player;
    }

    // Check for a winner.  Return
    //  0 if no winner or tie yet
    //  1 if it's a tie
    //  2 if X won
    //  3 if O won
    public int checkForWinner() {
        // Check horizontal wins
        for (int i = 0; i <= 6; i += 3)	{
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i+1] == HUMAN_PLAYER &&
                    mBoard[i+2]== HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i+1]== COMPUTER_PLAYER &&
                    mBoard[i+2] == COMPUTER_PLAYER)
                return 3;
        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++) {
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i+3] == HUMAN_PLAYER &&
                    mBoard[i+6]== HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i+3] == COMPUTER_PLAYER &&
                    mBoard[i+6]== COMPUTER_PLAYER)
                return 3;
        }

        // Check for diagonal wins
        if ((mBoard[0] == HUMAN_PLAYER &&
                mBoard[4] == HUMAN_PLAYER &&
                mBoard[8] == HUMAN_PLAYER) ||
                (mBoard[2] == HUMAN_PLAYER &&
                        mBoard[4] == HUMAN_PLAYER &&
                        mBoard[6] == HUMAN_PLAYER))
            return 2;
        if ((mBoard[0] == COMPUTER_PLAYER &&
                mBoard[4] == COMPUTER_PLAYER &&
                mBoard[8] == COMPUTER_PLAYER) ||
                (mBoard[2] == COMPUTER_PLAYER &&
                        mBoard[4] == COMPUTER_PLAYER &&
                        mBoard[6] == COMPUTER_PLAYER))
            return 3;

        // Check for tie
        for (int i = 0; i < BOARD_SIZE; i++) {
            // If we find a number, then no one has won yet
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER)
                return 0;
        }

        // If we make it through the previous loop, all places are taken, so it's a tie
        return 1;
    }

    public int checkForWinner(char[] board){
        // Check horizontal wins
        for (int i = 0; i <= 6; i += 3)	{
            if (board[i] == HUMAN_PLAYER &&
                    board[i+1] == HUMAN_PLAYER &&
                    board[i+2]== HUMAN_PLAYER)
                return 2;
            if (board[i] == COMPUTER_PLAYER &&
                    board[i+1]== COMPUTER_PLAYER &&
                    board[i+2] == COMPUTER_PLAYER)
                return 3;
        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++) {
            if (board[i] == HUMAN_PLAYER &&
                    board[i+3] == HUMAN_PLAYER &&
                    board[i+6]== HUMAN_PLAYER)
                return 2;
            if (board[i] == COMPUTER_PLAYER &&
                    board[i+3] == COMPUTER_PLAYER &&
                    board[i+6]== COMPUTER_PLAYER)
                return 3;
        }

        // Check for diagonal wins
        if ((board[0] == HUMAN_PLAYER &&
                board[4] == HUMAN_PLAYER &&
                board[8] == HUMAN_PLAYER) ||
                (board[2] == HUMAN_PLAYER &&
                        board[4] == HUMAN_PLAYER &&
                        board[6] == HUMAN_PLAYER))
            return 2;
        if ((board[0] == COMPUTER_PLAYER &&
                board[4] == COMPUTER_PLAYER &&
                board[8] == COMPUTER_PLAYER) ||
                (board[2] == COMPUTER_PLAYER &&
                        board[4] == COMPUTER_PLAYER &&
                        board[6] == COMPUTER_PLAYER))
            return 3;

        // Check for tie
        for (int i = 0; i < BOARD_SIZE; i++) {
            // If we find a number, then no one has won yet
            if (board[i] != HUMAN_PLAYER && board[i] != COMPUTER_PLAYER)
                return 0;
        }

        // If we make it through the previous loop, all places are taken, so it's a tie
        return 1;
    }

    public int getComputerMove() {
        int move;
        if (difficulty == 0)
            move = easyMode();
        else if (difficulty == 1)
            move = harderMode();
        else
            move = expertMode();

        return move;
    }

    public int easyMode() {
        int move;
        do
        {
            move = mRand.nextInt(BOARD_SIZE);
        } while (mBoard[move] == HUMAN_PLAYER || mBoard[move] == COMPUTER_PLAYER);
        mBoard[move] = COMPUTER_PLAYER;
        return move;
    }

    public int harderMode() {
        int move;
        // First see if there's a move O can make to win
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                char curr = mBoard[i];
                mBoard[i] = COMPUTER_PLAYER;
                if (checkForWinner() == 3) {
                    return i;
                }
                else
                    mBoard[i] = curr;
            }
        }
        // See if there's a move O can make to block X from winning
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                char curr = mBoard[i]; // Save the current number
                mBoard[i] = HUMAN_PLAYER;
                if (checkForWinner() == 2) {
                    mBoard[i] = COMPUTER_PLAYER;
                    return i;
                }
                else
                    mBoard[i] = curr;
            }
        }
        // Generate random move
        do
        {
            move = mRand.nextInt(BOARD_SIZE);
        } while (mBoard[move] == HUMAN_PLAYER || mBoard[move] == COMPUTER_PLAYER);
        mBoard[move] = COMPUTER_PLAYER;
        return move;
    }

    public int expertMode() {
        int move = minimax(mBoard, COMPUTER_PLAYER).index;
        mBoard[move] = COMPUTER_PLAYER;
        return move;
    }

    public Move minimax(char[] newBoard, char player) {

        // Get all the available spots
        List<Integer> availSpots = emptyIndexes(newBoard);

        // Check the game board for the winner
        int winner = checkForWinner(newBoard);
        if(winner == 3) // Computer wins, get 10 marks
            return new Move(10);
        else if (winner == 2) // Human wins, get -10 marks
            return new Move(-10);
        else if(winner == 1) // Tie, get 0 marks
            return new Move(0);
        // 游戏还没结束，继续执行下面的代码

        List<Move> moves = new ArrayList<>();

        // 遍历所有空位，最后添加到 moves 内的 move 是下在对应格子的所能获得的最大分数
        for (int i = 0; i < availSpots.size(); i++) {
            Move move = new Move();
            move.index = availSpots.get(i);

            // 将棋子下到该格子中，player 有可能是 COMPUTER 或 HUMAN
            newBoard[availSpots.get(i)] = player;

            // 如果当前是 COMPUTER，则下一轮轮到 HUMAN 下，不断递归，最终会将棋盘下满
            Move result;
            if(player == COMPUTER_PLAYER)
                result = minimax(newBoard, HUMAN_PLAYER);
            else
                result = minimax(newBoard, COMPUTER_PLAYER);
            // 当所有格子都被下了之后，会返回最终 Move 对象，对象里面存有本次递归的结果 (-10, 0, 10)
            move.score = result.score;

            // 回溯，将格子重新设为空
            newBoard[availSpots.get(i)] = (char) (move.index + '0');

            // 此时对于下在 availSpots.get(i) 的棋子，已经有了一个答案 move，将其加入到结果中。
            moves.add(move);
        }

        // 对于所有可以下的空格，对 COMPUTER 来说要找出一个最大的分数才能赢；对 HUMAN 来说，要找出最小的分数才能赢
        int bestMove = 0;
        if (player == COMPUTER_PLAYER) {
            int bestScore = -10000;
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).score > bestScore) {
                    bestScore = moves.get(i).score;
                    bestMove = i;
                }
            }
        } else {
            int bestScore = 10000;
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).score < bestScore) {
                    bestScore = moves.get(i).score;
                    bestMove = i;
                }
            }
        }

        return moves.get(bestMove);
    }

    public List<Integer> emptyIndexes(char[] board) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            if(board[i] != 'X' && board[i] != 'O')
                indexes.add(i);
        }
        return indexes;
    }
}

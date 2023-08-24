package com.example.tictactoe.classes;

import com.example.tictactoe.Exceptions.GameOverException;
import com.example.tictactoe.Exceptions.InvalidMoveException;
import com.example.tictactoe.model.BoardCell;
import com.example.tictactoe.model.MoveRequest;
import com.example.tictactoe.model.MoveResponse;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeGame {
    private char[][] board;
    private char currentPlayer;
    private boolean gameOver;
    private char winner;

    public TicTacToeGame() {
        board = new char[3][3]; // Initialize the board with empty cells
        currentPlayer = 'X'; // Player 'X' starts
        gameOver = false;
        winner = '\0'; // No winner initially
    }

    public MoveResponse makeMove(MoveRequest move) {
        int row = move.getRow();
        int col = move.getCol();
         if(gameOver){
             throw new GameOverException("The game is over");
         }
        if ( row < 0 || row >= 3 || col < 0 || col >= 3 || board[row][col] != '\0') {
             throw new InvalidMoveException("Invalid move");
        }

        board[row][col] = currentPlayer;
          // to make it a linked list
        List<BoardCell> boardCells = new ArrayList<>();
        for (int i = 0; i < 3; i++) {  //row
            for (int j = 0; j < 3; j++) { // column
                BoardCell cell = new BoardCell();// the object of that list
                cell.setCellValue(board[i][j]);
                boardCells.add(cell); // the whole list
            }
        }

        // Checking for winning conditions
        if (checkForWin()) {
            gameOver = true;
            winner = currentPlayer;
        } else if (checkForDraw()) {
            gameOver = true;
        }

        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';

        return new MoveResponse(boardCells, currentPlayer, gameOver, winner);
    }



    ////////////////////////


    private boolean checkForWin() {
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == currentPlayer && board[row][1] == currentPlayer && board[row][2] == currentPlayer) {
                return true;
            }
        }

        // Check columns
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == currentPlayer && board[1][col] == currentPlayer && board[2][col] == currentPlayer) {
                return true;
            }
        }

        // Check diagonals
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
            return true;
        }
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
            return true;
        }

        return false;
    }

  //////////////////




    private boolean checkForDraw() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == '\0') { // empty value
                    return false; // If any cell is empty the game is not a draw
                }
            }
        }
        return true;
    }


    ///////////

    public char[][] getBoardState() {
        return board;
    }


    ///////////////
    public boolean isGameOver() {
        return gameOver;
    }




    //////////////////
    public char getWinner() {
        return winner;
    }


    ///////////////////
    public char getCurrentPlayer() {
        return currentPlayer;
    }

    ///////////////////////

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public void setCurrentPlayer(char currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setWinner(char winner) {
        this.winner = winner;
    }

    // Reset the game to its initial state
    public void resetGame() {
        board = new char[3][3];
        currentPlayer = 'X';
        gameOver = false;
        winner = '\0';
    }
}

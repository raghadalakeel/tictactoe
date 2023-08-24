package com.example.tictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MoveResponse {
    private Long gameId;
    private List<BoardCell> board; // Use List<BoardCell> instead of List<String>
    private char currentPlayer;
    private boolean gameOver;
    private char winner;

    public MoveResponse(List<BoardCell> board, char currentPlayer, boolean gameOver, char winner) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.gameOver = gameOver;
        this.winner = winner;
    }
}




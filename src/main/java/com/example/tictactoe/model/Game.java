package com.example.tictactoe.model;

import com.example.tictactoe.Exceptions.GameNotFoundException;
import com.example.tictactoe.classes.TicTacToeGame;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BoardCell> board = new ArrayList<>();

    @ManyToOne

    private User playerX;

    @ManyToOne
    private User playerO;

    private char currentPlayer;
    private boolean gameOver;
    private char winner;
    private boolean active;
//    public void updateGameState(List<BoardCell> board, char currentPlayer, boolean gameOver, char winner) {
//        this.board = board;
//        this.currentPlayer = currentPlayer;
//        this.gameOver = gameOver;
//        this.winner = winner;
//    }


}

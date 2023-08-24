package com.example.tictactoe.Exceptions;

public class GameOverException extends RuntimeException {
    public GameOverException(String message) {
        super(message);
    }
}

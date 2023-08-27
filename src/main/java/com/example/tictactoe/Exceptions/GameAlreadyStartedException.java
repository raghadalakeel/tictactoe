package com.example.tictactoe.Exceptions;

public class GameAlreadyStartedException extends RuntimeException{
    public GameAlreadyStartedException(String message) {
        super(message);
    }
}

package com.example.tictactoe.Exceptions;

public class GameNotActiveException extends RuntimeException{
    public GameNotActiveException(String message) {
        super(message);
    }
}

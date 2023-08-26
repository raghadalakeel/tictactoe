package com.example.tictactoe.service;

import com.example.tictactoe.Exceptions.GameNotFoundException;
import com.example.tictactoe.Exceptions.InvalidMoveException;
import com.example.tictactoe.Exceptions.UserNotFoundException;
import com.example.tictactoe.Repository.GameRepository;
import com.example.tictactoe.Repository.UserRepository;
import com.example.tictactoe.classes.TicTacToeGame;
import com.example.tictactoe.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class GameService {
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    @Autowired
    public GameService(GameRepository gameRepository,UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository=userRepository;
    }
    //new
    public Game startNewGame(String playerXUsername) {
        User playerX = userRepository.findByUserName(playerXUsername);


        Game newGame = new Game();
        newGame.setPlayerX(playerX);
        newGame.setCurrentPlayer('X');

        List<BoardCell> boardCells = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                BoardCell cell = new BoardCell();
                cell.setGame(newGame);
                cell.setCellValue('\0');
                boardCells.add(cell);
            }
        }
        newGame.setBoard(boardCells);
        gameRepository.save(newGame);
        return newGame;
    }
    public Game joinGame(Long gameId, String playerOUsername) {
        User playerO = userRepository.findByUserName(playerOUsername);


        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));

        if (game.getPlayerO() != null) {
            throw new GameNotFoundException("Game has already started");
        }

        game.setPlayerO(playerO);
        game.setActive(true);

        return gameRepository.save(game);
    }




    //handle moves
    public Game makeMove(Long id, MoveRequest moveRequest,String authenticatedUsername) {

        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isEmpty()) {
            throw new GameNotFoundException("Game not found");
        }

        Game game = optionalGame.get();
        List<BoardCell> boardCells = game.getBoard();
        char currentPlayer = game.getCurrentPlayer();

        char[][] boardArray = convertListToArray(boardCells);

        TicTacToeGame ticTacToeGame = new TicTacToeGame(currentPlayer);
        ticTacToeGame.setBoard(boardArray);


        try {
            MoveResponse moveResponse = ticTacToeGame.makeMove(moveRequest,authenticatedUsername);

            updateBoardCells(game, moveResponse.getBoard());
            game.setCurrentPlayer(moveResponse.getCurrentPlayer());
            game.setGameOver(moveResponse.isGameOver());
            game.setWinner(moveResponse.getWinner());

            gameRepository.save(game);
            return game;

        } catch (InvalidMoveException e) {
            throw new InvalidMoveException("Invalid move: " + e.getMessage());
        }
    }



    //get state of game
    public Game getGame(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));
    }

///////////////////////////////
    //hnalding with the list of board cells to proceess it  well in tictactoegame class as char[][]
    private char[][] convertListToArray(List<BoardCell> boardCells) {
        char[][] boardArray = new char[3][3];
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardArray[i][j] = boardCells.get(index).getCellValue();
                index++;
            }
        }
        return boardArray;
    }
/////

    private void updateBoardCells(Game game, List<BoardCell> updatedBoardCells) {
    game.getBoard().clear();
    game.getBoard().addAll(updatedBoardCells);
    for (BoardCell cell : updatedBoardCells) {
        cell.setGame(game);
    }
}
    public void saveGame(Game game) {
  gameRepository.save(game);
    }


    public Game UpdateGame(Long gameId, String inviterUsername, String invitedUsername) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));
        User inviter = userRepository.findByUserName(inviterUsername);
        User invitedUser = userRepository.findByUserName(invitedUsername);

        game.setPlayerX(inviter);
        game.setPlayerO(invitedUser);
        game.setCurrentPlayer('X');


        return gameRepository.save(game);
    }
}

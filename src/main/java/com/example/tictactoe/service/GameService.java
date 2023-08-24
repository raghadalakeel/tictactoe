package com.example.tictactoe.service;

import com.example.tictactoe.Exceptions.GameNotFoundException;
import com.example.tictactoe.Exceptions.InvalidMoveException;
import com.example.tictactoe.Repository.GameRepository;
import com.example.tictactoe.classes.TicTacToeGame;
import com.example.tictactoe.model.BoardCell;
import com.example.tictactoe.model.Game;
import com.example.tictactoe.model.MoveRequest;
import com.example.tictactoe.model.MoveResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class GameService {
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }
    //new
    public Game startNewGame() {
        Game newGame = new Game();
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

    //handle moves
    public Game makeMove(Long id, MoveRequest moveRequest) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isEmpty()) {
            throw new GameNotFoundException("Game not found");
        }

        Game game = optionalGame.get();
        List<BoardCell> boardCells = game.getBoard();
        char currentPlayer = game.getCurrentPlayer();

        char[][] boardArray = convertListToArray(boardCells);

        TicTacToeGame ticTacToeGame = new TicTacToeGame();
        ticTacToeGame.setBoard(boardArray);
        ticTacToeGame.setCurrentPlayer(currentPlayer);

        try {
            MoveResponse moveResponse = ticTacToeGame.makeMove(moveRequest);

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
}

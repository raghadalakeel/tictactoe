package com.example.tictactoe.controller;

import com.example.tictactoe.Exceptions.InvalidMoveException;
import com.example.tictactoe.classes.TicTacToeGame;
import com.example.tictactoe.model.BoardCell;
import com.example.tictactoe.model.Game;
import com.example.tictactoe.model.MoveRequest;
import com.example.tictactoe.model.MoveResponse;
import com.example.tictactoe.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final GameService gameService;

    public WebSocketController(SimpMessagingTemplate messagingTemplate, GameService gameService) {
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
    }

    @MessageMapping("/game/{id}/move")
    @SendTo("/topic/game/{id}")
    public MoveResponse makeMove(@DestinationVariable Long id, MoveRequest moveRequest) {

        Game game = gameService.getGame(id);
        List<BoardCell> boardList = game.getBoard();
        char currentPlayer = game.getCurrentPlayer();

        char[][] boardArray = convertListToArray(boardList);
        TicTacToeGame ticTacToeGame = new TicTacToeGame();
        ticTacToeGame.setBoard(boardArray);
        ticTacToeGame.setCurrentPlayer(currentPlayer);

        try {
            MoveResponse moveResponse = ticTacToeGame.makeMove(moveRequest);
            game.updateGameState(moveResponse.getBoard(), moveResponse.getCurrentPlayer(), moveResponse.isGameOver(), moveResponse.getWinner());
            gameService.saveGame(game);

            messagingTemplate.convertAndSend("/topic/game/" + id, moveResponse);
            return moveResponse;
        } catch (InvalidMoveException e) {

            throw new RuntimeException("Invalid move: " + e.getMessage());
        }
    }
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
}

package com.example.tictactoe.controller;

import com.example.tictactoe.Exceptions.GameNotFoundException;
import com.example.tictactoe.Exceptions.InvalidMoveException;
import com.example.tictactoe.Repository.GameRepository;
import com.example.tictactoe.Repository.UserRepository;
import com.example.tictactoe.classes.TicTacToeGame;
import com.example.tictactoe.model.Game;
import com.example.tictactoe.model.MoveRequest;
import com.example.tictactoe.model.MoveResponse;
import com.example.tictactoe.model.User;
import com.example.tictactoe.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
@RequestMapping("/api/game")
public class GameController {
    private final GameService gameService;
    private final UserRepository userRepository;
    public GameController(GameService gameService,UserRepository userRepository) {

        this.gameService = gameService;
        this.userRepository=userRepository;
    }

    @PostMapping("/new")
    public ResponseEntity<Game> startNewGame(Authentication authentication) {
        String username = authentication.getName();
        Game newGame = gameService.startNewGame(username);
        return ResponseEntity.status(HttpStatus.CREATED).body(newGame);
    }

//    @PostMapping("/{id}/move")
//    public ResponseEntity<?> makeMove(@PathVariable Long id, @RequestBody MoveRequest moveRequest) {
//        try {
//            Game game = gameService.makeMove(id, moveRequest);
//            return ResponseEntity.ok(game);
//        } catch (GameNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        } catch (InvalidMoveException e) {
//            return ResponseEntity.badRequest().body( e.getMessage());
//        }
//    }



    @GetMapping("/{id}")
    public ResponseEntity<Game> getGame(@PathVariable Long id) {
        try {
            Game game = gameService.getGame(id);
            return ResponseEntity.ok(game);
        } catch (GameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinGame(@RequestParam Long gameId, @RequestParam String playerOUsername) {
        User playerO = userRepository.findByUserName(playerOUsername);
        if (playerO == null) {
            return ResponseEntity.badRequest().body("Player O not found");
        }

        try {
            Game joinedGame = gameService.joinGame(gameId, playerO.getUserName());
            return ResponseEntity.ok(joinedGame);
        } catch (GameNotFoundException e) {
            return ResponseEntity.notFound().build();
  }
//        catch (GameNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("Game has already started");
//        }
    }
}


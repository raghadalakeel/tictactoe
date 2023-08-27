package com.example.tictactoe.controller;

import com.example.tictactoe.Exceptions.InvalidMoveException;
import com.example.tictactoe.classes.TicTacToeGame;
import com.example.tictactoe.model.*;
import com.example.tictactoe.service.GameService;
import com.example.tictactoe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final GameService gameService;
    private final UserService userService;
    public WebSocketController(SimpMessagingTemplate messagingTemplate, GameService gameService,UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
        this.userService=userService;
    }

    @MessageMapping("/game/{id}/move")
    @SendTo("/topic/game/{id}")
    public Game makeMove(@DestinationVariable Long id, MoveRequest moveRequest, Authentication authentication) {
        String authenticatedUsername = authentication.getName();
        return gameService.makeMove(id, moveRequest,authenticatedUsername);
    }

    @MessageMapping("/connect")
    public void connectUser(@Payload String username) {
       userService.setUserOnlineStatus(username, true);
    }

    @MessageMapping("/disconnect")
    public void disconnectUser(@Payload String username) {
       userService.setUserOnlineStatus(username, false);
    }

    @MessageMapping("/invite")
    public void sendGameInvitation(GameInvitationRequest invitationRequest) {
        Long gameId = invitationRequest.getGameId();
        String inviter = invitationRequest.getInviter();
        String invitedUser = invitationRequest.getInvitedUser();

        if (userService.isUserOnline(invitedUser)) {
            Game game = gameService.UpdateGame(gameId, inviter, invitedUser);
            messagingTemplate.convertAndSend("/topic/game/" + gameId, game);


            String invitationMessage = "You have been invited to a game by " + inviter;
            Map<String, Object> invitationPayload = new HashMap<>();
            invitationPayload.put("message", invitationMessage);
            invitationPayload.put("gameId", gameId);
            messagingTemplate.convertAndSendToUser(invitedUser, "/queue/invitations", invitationPayload);
        } else {
            // Handle user not online case
        }
    }


}

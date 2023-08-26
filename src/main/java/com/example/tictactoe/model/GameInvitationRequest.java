package com.example.tictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameInvitationRequest {
    private Long gameId;
    private String inviter;
    private String invitedUser;


}

package com.example.tictactoe.controller;

import com.example.tictactoe.model.User;
import com.example.tictactoe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/online-players")
    public ResponseEntity<List<User>> getOnlinePlayers() {
        List<User> onlinePlayers = userService.getOnlinePlayers();
        return ResponseEntity.ok(onlinePlayers);
    }
}

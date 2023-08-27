package com.example.tictactoe.controller;

import com.example.tictactoe.Repository.UserRepository;
import com.example.tictactoe.classes.JwtTokenProvider;
import com.example.tictactoe.model.AuthenticationResponse;
import com.example.tictactoe.model.User;
import com.example.tictactoe.model.UserLoginRequest;
import com.example.tictactoe.model.UserRegistrationRequest;
import com.example.tictactoe.service.TokenInvalidationServiceImpl;
import com.example.tictactoe.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private TokenInvalidationServiceImpl tokenInvalidationService;
    @Autowired
    private UserService userService;



    @Autowired
    private  JwtTokenProvider jwtTokenProvider;


    private  AuthenticationManager authenticationManager;




    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody UserLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );

        String token = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new AuthenticationResponse(token, request.getUserName()));
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            tokenInvalidationService.invalidateToken(token);
            return ResponseEntity.ok("Logged out successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid token");
        }
    }


}

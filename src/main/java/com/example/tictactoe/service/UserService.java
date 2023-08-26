package com.example.tictactoe.service;

import com.example.tictactoe.Repository.UserRepository;
import com.example.tictactoe.model.Role;
import com.example.tictactoe.model.User;
import com.example.tictactoe.model.UserRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(UserRegistrationRequest request) {
        User newUser = new User();
        newUser.setUserName(request.getUserName());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole = new Role();
        userRole.setName("USER");
        newUser.setRoles(Collections.singletonList(userRole));
        userRepository.save(newUser);
    }

    public boolean isUserOnline(String invitedUser) {
       User user= userRepository.findByUserName(invitedUser);
        return user.isOnline();
    }

    private Set<User> onlineUsers = new HashSet<>();

    public List<User> getOnlinePlayers() {
        return onlineUsers.stream().collect(Collectors.toList());
    }

    public void setUserOnlineStatus(String username, boolean isOnline) {
        User user = userRepository.findByUserName(username);


        if (isOnline) {
            onlineUsers.add(user);
        } else {
            onlineUsers.remove(user);
        }
    }
}

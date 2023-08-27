package com.example.tictactoe.Repository;

import com.example.tictactoe.model.Game;
import com.example.tictactoe.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository  extends CrudRepository<User,Long> {
    User findByUserName(String username);
    List<User> findAllByIsOnlineTrue();
}

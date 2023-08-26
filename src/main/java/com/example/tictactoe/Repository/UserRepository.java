package com.example.tictactoe.Repository;

import com.example.tictactoe.model.Game;
import com.example.tictactoe.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository  extends CrudRepository<User,Long> {
    User findByUserName(String username);
}

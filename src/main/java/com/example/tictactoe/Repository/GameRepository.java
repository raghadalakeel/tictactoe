package com.example.tictactoe.Repository;

import com.example.tictactoe.model.Game;
import org.springframework.data.repository.CrudRepository;



public interface GameRepository extends CrudRepository<Game,Long> {
 Game findGameById(Long Id);
}

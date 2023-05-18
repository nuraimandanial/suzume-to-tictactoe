package com.tictactoe;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface LeaderBoardRepository extends CrudRepository<LeaderBoard, Integer> {
  List<LeaderBoard> findByUser(User user);

  List<LeaderBoard> findByDifficulty(String difficulty);

  List<LeaderBoard> findByGame(String game);

}
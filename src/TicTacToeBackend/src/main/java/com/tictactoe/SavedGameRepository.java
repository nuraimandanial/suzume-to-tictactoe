package com.tictactoe;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface SavedGameRepository extends CrudRepository<SavedGame, Integer> {

  List<SavedGame> findByUserEmail(String userEmail);

  List<SavedGame> findById(String id);

  List<SavedGame> findByGame(String game);

}
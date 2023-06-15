package com.tictactoe;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface CTDHistoryRepository extends CrudRepository<CTDHistory, Integer> {

  List<CTDHistory> findByUser(User user);

}
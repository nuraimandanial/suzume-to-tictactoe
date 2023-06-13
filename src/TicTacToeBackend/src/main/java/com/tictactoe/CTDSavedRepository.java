package com.tictactoe;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface CTDSavedRepository extends CrudRepository<CTDSaved, Integer> {

  List<CTDSaved> findByUserEmail(String userEmail);

  List<CTDSaved> findById(String id);

}
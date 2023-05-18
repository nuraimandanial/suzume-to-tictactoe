package com.tictactoe;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
  List<User> findByEmail(String email);

  List<User> findByUserName(String userName);

}
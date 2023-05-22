package com.tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/database")
@CrossOrigin
public class DatabaseControl {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SavedGameRepository savedGameRepository;

  @Autowired
  private LeaderBoardRepository leaderBoardRepository;

  @PostMapping("/register")
  public ResponseEntity<String> Register(@RequestBody RegisterCredential registerCredential) {
    String message = checkUserExistence(registerCredential.getEmail(), registerCredential.getUserName());

    if (message.equals("Can be registered!")) {
      User user = new User();
      user.setUserName(registerCredential.getUserName());
      user.setEmail(registerCredential.getEmail());
      user.setPassword(registerCredential.getPassword());
      userRepository.save(user);
      return ResponseEntity.ok("{\"message\": \"Successfully registered!\"}");
    } else {
      return ResponseEntity.ok("{\"message\": \"" + message + "\"}");
    }

  }

  public String checkUserExistence(String email, String userName) {
    List<User> user_email = userRepository.findByEmail(email);
    List<User> user_userName = userRepository.findByUserName(userName);

    if (!user_email.isEmpty() && !user_userName.isEmpty()) {
      return "The email and user name have already existed!";
    } else if (!user_email.isEmpty()) {
      return "The email has already existed!";
    } else if (!user_userName.isEmpty()) {
      return "The user name has already existed!";
    } else {
      return "Can be registered!";
    }
  }

  @PostMapping("/login")
  public ResponseEntity<String> Login(@RequestBody LoginCredentials loginCredentials) {
    List<User> user = userRepository.findByUserName(loginCredentials.getUsername());
    if (user.isEmpty()) {
      return ResponseEntity.ok("{\"message\": \"Invalid Email!\"}");
    } else if (!user.get(0).getPassword().equals(loginCredentials.getPassword())) {
      return ResponseEntity.ok("{\"message\": \"Incorrect Password!\"}");
    } else {
      return ResponseEntity.ok("{\"message\": \"Successfully Login!\"}");
    }
  }

  @PostMapping("/findemail")
  public ResponseEntity<String> findEmail(@RequestBody EmailFind userEmail) {
    List<User> user = userRepository.findByUserName(userEmail.getUsername());
    System.out.println("Hi find mail");
    if (!user.isEmpty()) {
      return ResponseEntity.ok("{\"email\": \"" + user.get(0).getEmail() + "\"}");
    } else {
      return ResponseEntity.ok("{\"email\": \"No such User!\"}");
    }
  }

  @PostMapping("/savegame")
  public ResponseEntity<String> saveGame(@RequestBody BoardData game) {
    List<User> user = userRepository.findByEmail(game.getUserEmail());

    if (user.isEmpty()) {
      return ResponseEntity.badRequest().body("{\"message\": \"Some error occurs!\"}");
    } else {
      SavedGame saved = new SavedGame();
      saved.setUser(user.get(0));
      saved.setBoard(game.getBoard());
      saved.setDifficulty(game.getDifficulty());
      saved.setGame(game.getGame());
      savedGameRepository.save(saved);
      return ResponseEntity.ok("{\"message\": \"Successfully Saved!\"}");
    }

  }

  @PostMapping("/loadgame")
  public ResponseEntity<String> loadGame(@RequestBody LastGameData loadGame) {
    List<User> user = userRepository.findByEmail(loadGame.getUserEmail());

    if (!user.isEmpty()) {
      List<SavedGame> savedGame = savedGameRepository.findByUserEmail(user.get(0).getEmail());
      List<SavedGame> savedGameByGame = savedGameRepository.findByGame(loadGame.getGame());
      List<SavedGame> intersected = intersection(savedGame, savedGameByGame);
      ArrayList<String> savedGameString = new ArrayList<>();
      ArrayList<Integer> savedGameId = new ArrayList<>();
      ArrayList<String> savedGameDifficulty = new ArrayList<>();
      ArrayList<String> savedGameType = new ArrayList<>();
      for (int i = 0; i < intersected.size(); i++) {
        savedGameString.add(Arrays.deepToString(intersected.get(i).getBoard()));
        savedGameId.add(intersected.get(i).getId());
        savedGameDifficulty.add(intersected.get(i).getDifficulty());
        savedGameType.add(intersected.get(i).getGame());
      }
      return ResponseEntity.ok("{\"board\": \"" + savedGameString + "\", \"id\": \"" + savedGameId
          + "\", \"difficulty\": \"" + savedGameDifficulty + "\", \"game\": \"" + savedGameType + "\"}");
    } else {
      return ResponseEntity.badRequest().body("{\"message\": \"Fail to load!\"}");
    }

  }

  @PostMapping("/delete")
  public ResponseEntity<String> deleteSavedGame(@RequestBody DeleteGame deleteGame) {
    List<SavedGame> savedGame = savedGameRepository.findById(deleteGame.getId() + "");
    if (!savedGame.isEmpty()) {
      savedGameRepository.deleteById(deleteGame.getId());
      return ResponseEntity.ok("{\"message\": \"Successfully Deleted!\"}");
    } else {
      return ResponseEntity.badRequest().body("{\"message\": \"Fail to delete!\"}");
    }

  }

  @PostMapping("/getleaderboard")
  public ResponseEntity<String> getLeaderBoard(@RequestBody LeaderBoardData leaderBoardData) {
    List<LeaderBoard> listByGame = leaderBoardRepository.findByGame(leaderBoardData.getGame());
    List<LeaderBoard> listByDif = leaderBoardRepository.findByDifficulty(leaderBoardData.getDifficulty());
    List<LeaderBoard> intersectedList = intersection(listByGame, listByDif);
    ArrayList<String> userName = new ArrayList<>();
    ArrayList<Integer> score = new ArrayList<>();
    ArrayList<Integer> win = new ArrayList<>();
    ArrayList<Integer> lose = new ArrayList<>();

    if (!intersectedList.isEmpty()) {
      Collections.sort(intersectedList, (a, b) -> a.getScore().compareTo(b.getScore()));
      Collections.reverse(intersectedList);
      for (int i = 0; i < intersectedList.size(); i++) {
        userName.add(intersectedList.get(i).getUserName());
        score.add(intersectedList.get(i).getScore());
        win.add(intersectedList.get(i).getWin());
        lose.add(intersectedList.get(i).getLose());
      }
      return ResponseEntity.ok("{\"userName\": \"" + userName + "\", \"score\": \"" + score + "\", \"win\": \"" + win
          + "\", \"lose\": \"" + lose + "\"}");
    } else {
      return ResponseEntity.ok("{\"userName\": \"" + userName + "\", \"score\": \"" + score + "\", \"win\": \"" + win
          + "\", \"lose\": \"" + lose + "\"}");
    }
  }

  public <E> List<E> intersection(List<E> emailList, List<E> gameList) {
    List<E> intersection_list = new ArrayList<E>();

    for (int i = 0; i < gameList.size(); i++) {
      if (emailList.contains(gameList.get(i))) {
        intersection_list.add(gameList.get(i));
      }
    }

    return intersection_list;

  }

}

class MessageJSON {
  private String message;

  public MessageJSON(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}

class RegisterCredential {
  private String email;
  private String userName;
  private String password;

  public RegisterCredential(String email, String userName, String password) {
    this.email = email;
    this.userName = userName;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public String getUserName() {
    return userName;
  }

  public String getPassword() {
    return password;
  }
}

class LoginCredentials {
  private String username;
  private String password;

  public LoginCredentials(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

}

class EmailFind {
  private String username;

  public EmailFind() {

  }

  public EmailFind(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}

class BoardData {
  private String userEmail;
  private String[][] board;
  private String difficulty;
  private String game;

  public BoardData(String[][] board, String userEmail, String difficulty, String game) {
    this.board = board;
    this.userEmail = userEmail;
    this.difficulty = difficulty;
    this.game = game;
  }

  public BoardData() {

  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public String[][] getBoard() {
    return board;
  }

  public void setBoard(String[][] board) {
    this.board = board;
  }

  public String getDifficulty() {
    return this.difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public String getGame() {
    return game;
  }

  public void setGame(String game) {
    this.game = game;
  }
}

class LastGameData {
  private String email;
  private String game;

  public LastGameData() {

  }

  public LastGameData(String email, String game) {
    this.email = email;
    this.game = game;
  }

  public String getUserEmail() {
    return email;
  }

  public void setUserEmail(String userEmail) {
    this.email = userEmail;
  }

  public String getGame() {
    return game;
  }

  public void setGame(String game) {
    this.game = game;
  }

}

class DeleteGame {
  private int id;

  public DeleteGame() {

  }

  public DeleteGame(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}

class LeaderBoardData {
  private String game;
  private String difficulty;

  public LeaderBoardData(String game, String difficulty) {
    this.game = game;
    this.difficulty = difficulty;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public String getGame() {
    return game;
  }
}
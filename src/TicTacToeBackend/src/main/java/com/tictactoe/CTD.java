package com.tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/connectingthedots")
@CrossOrigin
public class CTD {

  @Autowired
  CTDSavedRepository ctdSavedRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  LeaderBoardRepository leaderBoardRepository;

  @Autowired
  CTDHistoryRepository ctdHistoryRepository;

  // index 0 = Y, index 1 = X
  private String difficulty; // save
  private double score; // save
  private int win; // save
  private int lose;// save
  private int[] position; // save
  private int[] previousMove; // save
  private int beforeMove; // save
  private ArrayList<int[]> stations; // save
  private ArrayList<Integer> moveIndexBeforeStation; // save
  private int[][] map; // save
  private int pathNumber; // save
  private final String[][] options = {
      { "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "R", "R",
          "R", "R", "R", "D", "D", "D", "R", "R", "R", "D", "D", "D", "D", "D", "L", "D", "D", "R", "D", "D", "R", "D",
          "D", "R", "D", "D", "D", "D", "R", "R", "R", "D", "R", "R", "U", "R", "R", "R", "D", "R" },
      { "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "R", "R",
          "R", "R", "R", "D", "D", "D", "R", "R", "R", "D", "D", "D", "D", "D", "L", "D", "D", "R", "D", "D", "R", "D",
          "R", "D", "D", "D", "D", "D", "R", "R", "R", "D", "R", "R", "U", "R", "R", "R", "D", "R" },
      { "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "R", "R",
          "R", "R", "R", "D", "D", "D", "R", "R", "R", "D", "D", "D", "D", "D", "L", "D", "D", "R", "D", "D", "R", "R",
          "D", "D", "D", "D", "D", "D", "R", "R", "R", "D", "R", "R", "U", "R", "R", "R", "D", "R" },
      { "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "R", "R",
          "R", "R", "R", "D", "R", "R", "D", "D", "R", "D", "D", "D", "D", "D", "L", "D", "D", "R", "D", "D", "R", "D",
          "D", "R", "D", "D", "D", "D", "R", "R", "R", "D", "R", "R", "U", "R", "R", "R", "D", "R" },
      { "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "R", "R",
          "R", "R", "R", "D", "R", "R", "D", "D", "R", "D", "D", "D", "D", "D", "L", "D", "D", "R", "D", "D", "R", "D",
          "R", "D", "D", "D", "D", "D", "R", "R", "R", "D", "R", "R", "U", "R", "R", "R", "D", "R" },
      { "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "R", "R",
          "R", "R", "R", "D", "R", "R", "D", "D", "R", "D", "D", "D", "D", "D", "L", "D", "D", "R", "D", "D", "R", "R",
          "D", "D", "D", "D", "D", "D", "R", "R", "R", "D", "R", "R", "U", "R", "R", "R", "D", "R" } };
  private String[] selectedOptions; // save
  private int moveIndex; // save
  private Map<String, CTD> gameInstances = new HashMap<>();

  public CTD() {
    difficulty = "";
    score = 0;
    win = 0;
    lose = 0;
    position = new int[] { 0, 0 };
    previousMove = new int[] { 0, 0 };
    beforeMove = 0;
    stations = new ArrayList<int[]>();
    selectedOptions = options[0];
    moveIndex = 0;
    pathNumber = 0;
    moveIndexBeforeStation = new ArrayList<Integer>();
    map = new int[][] { { 9, 0, 2, 0, 0, 0, 1, 1, 1, 1, 0, 0, 2, 0, 0, 0, 1, 1, 1, 1 },
        { 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1 },
        { 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1 },
        { 0, 1, 0, 1, 1, 0, 0, 2, 0, 1, 0, 1, 0, 1, 1, 0, 0, 2, 0, 1 },
        { 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
        { 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
        { 0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1 },
        { 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
        { 0, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 1, 2, 1, 0, 0, 1 },
        { 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1 },
        { 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1 },
        { 0, 1, 1, 0, 0, 2, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 2, 0, 1 },
        { 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0 },
        { 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0 },
        { 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0 },
        { 2, 0, 0, 0, 1, 1, 0, 0, 0, 1, 2, 0, 0, 0, 1, 1, 0, 0, 0, 1 },
        { 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
        { 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
        { 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 },
        { 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 1 },
        { 0, 0, 2, 0, 0, 0, 1, 1, 1, 1, 0, 0, 2, 0, 0, 0, 1, 1, 1, 1 },
        { 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 2, 0, 0, 1, 1 },
        { 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1 },
        { 0, 1, 0, 1, 1, 0, 0, 2, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1 },
        { 0, 1, 0, 1, 1, 2, 0, 1, 0, 1, 0, 1, 0, 1, 1, 2, 0, 0, 0, 1 },
        { 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
        { 0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1 },
        { 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1 },
        { 0, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1 },
        { 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1 },
        { 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 1 },
        { 0, 1, 1, 0, 0, 2, 1, 1, 0, 1, 0, 1, 1, 0, 0, 2, 1, 1, 0, 1 },
        { 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0 },
        { 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0 },
        { 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0 },
        { 2, 0, 0, 0, 1, 1, 0, 0, 0, 1, 2, 0, 0, 0, 1, 1, 0, 0, 0, 1 },
        { 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
        { 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 },
        { 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 3 } };
  }

  // Print out the map in console
  public void printMap() {
    System.out.println();
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[i].length; j++) {
        System.out.print(map[i][j] + " ");
      }
      System.out.println();
    }
    System.out.println();
  }

  @GetMapping("/{email}/newGame")
  public void newGame(@PathVariable String email) {
    CTD gameInstance = new CTD();
    gameInstances.put(email, gameInstance);
  }

  @GetMapping("/{email}/restart")
  public void restart(@PathVariable String email) {
    CTD gameInstance = gameInstances.get(email);
    gameInstance.setScore(0);
    gameInstance.setWin(0);
    gameInstance.setLose(0);
    gameInstance.setMoveIndex(0);
    gameInstance.setPosition(new int[] { 0, 0 });
    gameInstance.setSelectedOptions(gameInstance.getOptions()[0]);
    gameInstance.setStations(new ArrayList<int[]>());
    gameInstance.setmoveIndexBeforeStation(new ArrayList<Integer>());
    gameInstance.setMap(new int[][] { { 9, 0, 2, 0, 0, 0, 1, 1, 1, 1, 0, 0, 2, 0, 0, 0, 1, 1, 1, 1 },
        { 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1 },
        { 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1 },
        { 0, 1, 0, 1, 1, 0, 0, 2, 0, 1, 0, 1, 0, 1, 1, 0, 0, 2, 0, 1 },
        { 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
        { 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
        { 0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1 },
        { 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
        { 0, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 1, 2, 1, 0, 0, 1 },
        { 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1 },
        { 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1 },
        { 0, 1, 1, 0, 0, 2, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 2, 0, 1 },
        { 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0 },
        { 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0 },
        { 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0 },
        { 2, 0, 0, 0, 1, 1, 0, 0, 0, 1, 2, 0, 0, 0, 1, 1, 0, 0, 0, 1 },
        { 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
        { 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
        { 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 },
        { 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 1 },
        { 0, 0, 2, 0, 0, 0, 1, 1, 1, 1, 0, 0, 2, 0, 0, 0, 1, 1, 1, 1 },
        { 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 2, 0, 0, 1, 1 },
        { 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1 },
        { 0, 1, 0, 1, 1, 0, 0, 2, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1 },
        { 0, 1, 0, 1, 1, 2, 0, 1, 0, 1, 0, 1, 0, 1, 1, 2, 0, 0, 0, 1 },
        { 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
        { 0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1 },
        { 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1 },
        { 0, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1 },
        { 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1 },
        { 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 1 },
        { 0, 1, 1, 0, 0, 2, 1, 1, 0, 1, 0, 1, 1, 0, 0, 2, 1, 1, 0, 1 },
        { 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0 },
        { 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0 },
        { 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0 },
        { 2, 0, 0, 0, 1, 1, 0, 0, 0, 1, 2, 0, 0, 0, 1, 1, 0, 0, 0, 1 },
        { 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
        { 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 },
        { 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 3 } });
    previousMove = new int[] { 0, 0 };
    gameInstance.setPreviousMove(previousMove);
    gameInstance.setDifficulty("");
    gameInstance.setBeforeMove(0);
  }

  // Suzume Move
  @GetMapping("/{email}/move")
  public ResponseEntity<Integer> move(@PathVariable String email) {
    CTD gameInstance = gameInstances.get(email);
    String direction = gameInstance.getSelectedOptions()[gameInstance.getMoveIndex()];
    int newMoveIndex = gameInstance.getMoveIndex() + 1;
    gameInstance.setMoveIndex(newMoveIndex);
    int currentX = gameInstance.getPosition()[1];
    int currentY = gameInstance.getPosition()[0];
    int tempX = currentX;
    int tempY = currentY;
    switch (direction) {
      case "U":
        currentY--;
        gameInstance.setPosition(new int[] { currentY, currentX });
        break;
      case "D":
        currentY++;
        gameInstance.setPosition(new int[] { currentY, currentX });
        break;
      case "L":
        currentX--;
        gameInstance.setPosition(new int[] { currentY, currentX });
        break;
      case "R":
        currentX++;
        gameInstance.setPosition(new int[] { currentY, currentX });
        break;
      default:
        break;
    }

    // code 200 = no station/end
    // code 1,2,3 = TTT code
    // code 0 = end
    int[] temp = { tempX, tempY };
    gameInstance.setPreviousMove(temp);

    if (gameInstance.getMap()[currentY][currentX] == 2) {
      int[][] tempMap1 = gameInstance.getMap();
      tempMap1[tempY][tempX] = gameInstance.getBeforeMove();
      gameInstance.setMap(tempMap1);
      gameInstance.setBeforeMove(gameInstance.getMap()[currentY][currentX]);
      int[][] tempMap2 = gameInstance.getMap();
      tempMap2[currentY][currentX] = 9;
      gameInstance.setMap(tempMap2);
      System.out.println("Arrived station!");
      gameInstance.printMap();

      if (gameInstance.getmoveIndexBeforeStation().contains(gameInstance.getMoveIndex() - 1)) {
        return new ResponseEntity<>(200, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(chooseTTT(), HttpStatus.OK);
      }
    } else {
      int[][] tempMap1 = gameInstance.getMap();
      tempMap1[tempY][tempX] = gameInstance.getBeforeMove();
      gameInstance.setMap(tempMap1);
      gameInstance.setBeforeMove(gameInstance.getMap()[currentY][currentX]);
      int[][] tempMap2 = gameInstance.getMap();
      int nextStep = tempMap2[currentY][currentX];
      tempMap2[currentY][currentX] = 9;
      gameInstance.setMap(tempMap2);
      System.out.println("Successfully Move!");
      gameInstance.printMap();
      if (nextStep == 0) {
        return new ResponseEntity<>(200, HttpStatus.OK);
      } else {
        System.out.println("You Win!");
        gameInstance.saveLeaderboard(email, userRepository, leaderBoardRepository);
        List<User> users = userRepository.findByEmail(email);
        gameInstance.saveHistory(userRepository, ctdHistoryRepository, 1, gameInstance, users.get(0));

        return new ResponseEntity<>(0, HttpStatus.OK);
      }

    }

  }

  // Randomly choose a TTT
  public int chooseTTT() {
    Random random = new Random();
    int randomNum = random.nextInt(4) + 1;
    if (randomNum >= 3) {
      return 3;
    }
    return randomNum;
  }

  @GetMapping("/{email}/takeBackStep")
  public void takeBackStep(@PathVariable String email) {
    CTD gameInstance = gameInstances.get(email);
    if (gameInstance.getMoveIndex() != 0) {
      gameInstance.setMoveIndex(gameInstance.getMoveIndex() - 1);
      String direction = gameInstance.getSelectedOptions()[gameInstance.getMoveIndex()];
      int currentX = gameInstance.getPosition()[1];
      int currentY = gameInstance.getPosition()[0];
      int tempX = currentX;
      int tempY = currentY;

      switch (direction) {
        case "U":
          currentY++;
          gameInstance.setPosition(new int[] { currentY, currentX });
          break;
        case "D":
          currentY--;
          gameInstance.setPosition(new int[] { currentY, currentX });
          break;
        case "L":
          currentX++;
          gameInstance.setPosition(new int[] { currentY, currentX });
          break;
        case "R":
          currentX--;
          gameInstance.setPosition(new int[] { currentY, currentX });
          break;
        default:
          break;
      }

      int[] temp = { tempX, tempY };
      gameInstance.setPreviousMove(temp);

      int[][] tempMap1 = gameInstance.getMap();
      tempMap1[tempY][tempX] = gameInstance.getBeforeMove();
      gameInstance.setMap(tempMap1);
      gameInstance.setBeforeMove(gameInstance.getMap()[currentY][currentX]);
      int[][] tempMap2 = gameInstance.getMap();
      tempMap2[currentY][currentX] = 9;
      gameInstance.setMap(tempMap2);
      System.out.println("Successfully Back Move!");
      gameInstance.printMap();
    } else {
      System.out.println("You are at the start point!");
    }
  }

  // Action after TTT win lose
  @GetMapping("/{email}/action/{code}")
  public ResponseEntity<Integer> ActionAfterGame(@PathVariable int code, @PathVariable String email) {
    CTD gameInstance = gameInstances.get(email);
    int currentX = gameInstance.getPosition()[1];
    int currentY = gameInstance.getPosition()[0];

    if (code == 1) {
      gameInstance.getStations().add(gameInstance.getPreviousMove());
      gameInstance.getmoveIndexBeforeStation().add(gameInstance.getMoveIndex() - 1);
      gameInstance.setWin(gameInstance.getWin() + 1);
      gameInstance.setScore(
          (double) gameInstance.getWin() / ((double) gameInstance.getLose() + (double) gameInstance.getWin()));

      return new ResponseEntity<>(200, HttpStatus.OK);
    } else if (code == -1) {
      if (gameInstance.getStations().isEmpty()) {
        System.out.println("You Lose!");
        List<User> users = userRepository.findByEmail(email);
        gameInstance.saveHistory(userRepository, ctdHistoryRepository, -1, gameInstance, users.get(0));
        return new ResponseEntity<>(-1, HttpStatus.OK);
      } else {
        gameInstance.setBeforeMove(0);
        int[][] tempMap = gameInstance.getMap();
        tempMap[currentY][currentX] = 2;
        gameInstance.setMap(tempMap);
        currentX = gameInstance.getStations().get(gameInstance.getStations().size() - 1)[0];
        currentY = gameInstance.getStations().get(gameInstance.getStations().size() - 1)[1];
        gameInstance.setPosition(new int[] { currentY, currentX });
        gameInstance.setMoveIndex(
            gameInstance.getmoveIndexBeforeStation().get(gameInstance.getmoveIndexBeforeStation().size() - 1));
        gameInstance.getmoveIndexBeforeStation()
            .remove(gameInstance.getmoveIndexBeforeStation().size() - 1);
        gameInstance.getStations().remove(gameInstance.getStations().size() - 1);
        int[][] tempMap2 = gameInstance.getMap();
        tempMap2[currentY][currentX] = 9;
        gameInstance.setMap(tempMap2);
        gameInstance.printMap();
        gameInstance.setLose(gameInstance.getLose() + 1);
        gameInstance.setScore(
            (double) gameInstance.getWin() / ((double) gameInstance.getLose() + (double) gameInstance.getWin()));

        return new ResponseEntity<>(200, HttpStatus.OK);
      }
    } else {
      System.out.println("Tie!");
      takeBackStep(email);
      return new ResponseEntity<>(200, HttpStatus.OK);
    }
  }

  public void saveLeaderboard(String email, UserRepository userRepository,
      LeaderBoardRepository leaderBoardRepository) {
    List<User> users = userRepository.findByEmail(email);
    if (!users.isEmpty()) {
      List<LeaderBoard> leaderBoards = leaderBoardRepository.findByUser(users.get(0));
      List<LeaderBoard> leaderBoardsByDif = leaderBoardRepository.findByDifficulty(getDifficulty());
      List<LeaderBoard> leaderBoardsByGame = leaderBoardRepository.findByGame("CTD");
      List<LeaderBoard> intersectedList_1 = intersection(leaderBoards, leaderBoardsByDif);
      List<LeaderBoard> intersectedList_2 = intersection(leaderBoards, leaderBoardsByGame);
      List<LeaderBoard> intersectedList = intersection(intersectedList_2, intersectedList_1);
      if (!intersectedList.isEmpty()) {
        LeaderBoard leaderBoard = intersectedList.get(0);
        if (leaderBoard.getScore() < getScore()) {
          leaderBoard.setScore(getScore());
          leaderBoard.setWin(getWin());
          leaderBoard.setLose(getLose());
          leaderBoardRepository.save(leaderBoard);
        }
      } else {
        LeaderBoard leaderBoard = new LeaderBoard();
        leaderBoard.setScore(getScore());
        leaderBoard.setUser(users.get(0));
        leaderBoard.setDifficulty(getDifficulty());
        leaderBoard.setWin(getWin());
        leaderBoard.setLose(getLose());
        leaderBoard.setGame("CTD");
        leaderBoard.setUserName(users.get(0).getUserName());
        leaderBoardRepository.save(leaderBoard);
      }
    }
  }

  public void saveHistory(UserRepository userRepository,
      CTDHistoryRepository ctdHistoryRepository, int status, CTD gameInstance, User user) {
    CTDHistory history = new CTDHistory();

    history.setDifficulty(gameInstance.getDifficulty());
    history.setWin(gameInstance.getWin());
    history.setLose(gameInstance.getLose());
    history.setScore(gameInstance.getScore());
    history.setPathNumber(gameInstance.getPathNumber());
    history.setUser(user);
    history.setStatus(status);
    ctdHistoryRepository.save(history);

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

  public int getBeforeMove() {
    return beforeMove;
  }

  @GetMapping("/{email}/map")
  public ResponseEntity<String> getMapResponse(@PathVariable String email) {
    CTD gameInstance = gameInstances.get(email);
    return ResponseEntity.ok().body("{ \"Map\" : \"" + Arrays.deepToString(gameInstance.getMap()) + "\"}");
  }

  @GetMapping("/{email}/selectPath/{optionNumber}")
  public void selectPath(@PathVariable String email, @PathVariable int optionNumber) {
    CTD gameInstance = gameInstances.get(email);
    gameInstance.setPathNumber(optionNumber);
    gameInstance.setSelectedOptions(gameInstance.getOptions()[optionNumber]);
  }

  @GetMapping("/{email}/save/{name}")
  public void saveGame(@PathVariable String email, @PathVariable String name) {
    CTD gameInstance = gameInstances.get(email);
    List<User> users = userRepository.findByEmail(email);

    if (!users.isEmpty()) {
      CTDSaved savedGame = new CTDSaved();

      savedGame.setPosition(gameInstance.getPosition());
      savedGame.setPreviousMove(gameInstance.getPreviousMove());
      savedGame.setPathNumber(gameInstance.getPathNumber());
      ObjectMapper objectMapper = new ObjectMapper();
      String JsonStationsString = "";
      String JsonMoveIndexBeforeStationString = "";
      String JsonMap = "";
      String JsonSelectedOptions = "";
      try {
        JsonStationsString = objectMapper.writeValueAsString(gameInstance.getStations());
        JsonMoveIndexBeforeStationString = objectMapper
            .writeValueAsString(gameInstance.getmoveIndexBeforeStation());
        JsonMap = objectMapper.writeValueAsString(gameInstance.getMap());
        JsonSelectedOptions = objectMapper.writeValueAsString(gameInstance.getSelectedOptions());
      } catch (JsonProcessingException e) {
        System.out.println(e.getMessage());
      }
      savedGame.setMap(JsonMap);
      savedGame.setStations(JsonStationsString);
      savedGame.setSelectedOptions(JsonSelectedOptions);
      savedGame.setMoveIndex(gameInstance.getMoveIndex());
      savedGame.setMoveIndexBeforeStation(JsonMoveIndexBeforeStationString);
      savedGame.setDifficulty(gameInstance.getDifficulty());
      savedGame.setBeforeMove(gameInstance.getBeforeMove());
      savedGame.setUser(users.get(0));
      savedGame.setName(name);
      savedGame.setLose(gameInstance.getLose());
      savedGame.setWin(gameInstance.getWin());
      savedGame.setScore(gameInstance.getScore());
      ctdSavedRepository.save(savedGame);
    }

  }

  public int[][] getMap() {
    return map;
  }

  public int[] getPosition() {
    return position;
  }

  public int[] getPreviousMove() {
    return previousMove;
  }

  public ArrayList<int[]> getStations() {
    return stations;
  }

  public String[][] getOptions() {
    return options;
  }

  public int getPathNumber() {
    return pathNumber;
  }

  @GetMapping("/{email}/getPathNumber")
  public ResponseEntity<String> getPathNumberRequest(@PathVariable String email) {
    CTD gameInstance = gameInstances.get(email);
    return ResponseEntity.ok("{\"path\": \"" + gameInstance.getPathNumber() + "\"}");
  }

  public String[] getSelectedOptions() {
    return selectedOptions;
  }

  public int getWin() {
    return win;
  }

  @GetMapping("/{email}/getwin")
  public ResponseEntity<String> getWinRequest(@PathVariable String email) {
    CTD gameInstance = gameInstances.get(email);
    return ResponseEntity.ok("{ \"win\": \"" + gameInstance.getWin() + "\"}");
  }

  @GetMapping("/{email}/loadGame/{id}")
  public void loadGame(@PathVariable String email, @PathVariable String id) {
    CTD gameInstance = new CTD();
    gameInstances.put(email, gameInstance);
    List<CTDSaved> savedGames = ctdSavedRepository.findById(id);
    if (!savedGames.isEmpty()) {
      CTDSaved savedGame = savedGames.get(0);
      gameInstance.setPosition(savedGame.getPosition());
      gameInstance.setBeforeMove(savedGame.getBeforeMove());
      gameInstance.setDifficulty(savedGame.getDifficulty());
      gameInstance.setLose(savedGame.getLose());
      gameInstance.setWin(savedGame.getWin());
      gameInstance.setScore(savedGame.getScore());
      gameInstance.setPreviousMove(savedGame.getPreviousMove());
      gameInstance.setMoveIndex(savedGame.getMoveIndex());
      gameInstance.setPathNumber(savedGame.getPathNumber());

      ObjectMapper objectMapper = new ObjectMapper();
      try {
        int[][] map;
        ArrayList<int[]> stations;
        ArrayList<Integer> MoveIndexBeforeStation;
        String[] selectedOptions;

        map = objectMapper.readValue(savedGame.getMap(),
            new TypeReference<int[][]>() {
            });
        stations = objectMapper.readValue(savedGame.getStations(),
            new TypeReference<ArrayList<int[]>>() {
            });
        MoveIndexBeforeStation = objectMapper.readValue(savedGame.getMoveIndexBeforeStation(),
            new TypeReference<ArrayList<Integer>>() {
            });
        selectedOptions = objectMapper.readValue(savedGame.getSelectedOptions(),
            new TypeReference<String[]>() {
            });
        gameInstance.setStations(stations);
        gameInstance.setMap(map);
        gameInstance.setmoveIndexBeforeStation(MoveIndexBeforeStation);
        gameInstance.setSelectedOptions(selectedOptions);
      } catch (JsonProcessingException e) {
        System.out.println(e.getMessage());
      }

    }
  }

  @GetMapping("/{email}/deleteGame/{id}")
  public void deleteGame(@PathVariable String email, @PathVariable String id) {
    List<CTDSaved> savedGames = ctdSavedRepository.findById(id);
    if (!savedGames.isEmpty()) {
      ctdSavedRepository.delete(savedGames.get(0));
    }
  }

  public int getLose() {
    return lose;
  }

  @GetMapping("/{email}/getlose")
  public ResponseEntity<String> getLoseRequest(@PathVariable String email) {
    CTD gameInstance = gameInstances.get(email);
    return ResponseEntity.ok("{ \"lose\": \"" + gameInstance.getLose() + "\"}");
  }

  public double getScore() {
    return score;
  }

  @GetMapping("/{email}/getscore")
  public ResponseEntity<String> getScoreRequest(@PathVariable String email) {
    CTD gameInstance = gameInstances.get(email);
    return ResponseEntity.ok("{ \"score\": \"" + gameInstance.getScore() + "\"}");
  }

  public int getMoveIndex() {
    return moveIndex;
  }

  public ArrayList<Integer> getmoveIndexBeforeStation() {
    return moveIndexBeforeStation;
  }

  public String getDifficulty() {
    return difficulty;
  }

  @GetMapping("/{email}/difficulty/{difficulty}")
  public void setDifficultyRequest(@PathVariable String email, @PathVariable String difficulty) {
    CTD gameInstance = gameInstances.get(email);
    gameInstance.setDifficulty(difficulty);
  }

  @GetMapping("/{email}/getdifficulty")
  public ResponseEntity<String> getDifficultyRequest(@PathVariable String email) {
    CTD gameInstance = gameInstances.get(email);
    if (gameInstance.getDifficulty() == "") {
      return ResponseEntity.ok().body("{ \"difficulty\" : \"" + "None" + "\"}");
    } else {
      return ResponseEntity.ok().body("{ \"difficulty\" : \"" + gameInstance.getDifficulty() + "\"}");
    }

  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public void setmoveIndexBeforeStation(ArrayList<Integer> moveIndexBeforeStation) {
    this.moveIndexBeforeStation = moveIndexBeforeStation;
  }

  public void setMoveIndex(int moveIndex) {
    this.moveIndex = moveIndex;
  }

  public void setSelectedOptions(String[] selectedOptions) {
    this.selectedOptions = selectedOptions;
  }

  public void setBeforeMove(int beforeMove) {
    this.beforeMove = beforeMove;
  }

  public void setWin(int win) {
    this.win = win;
  }

  public void setLose(int lose) {
    this.lose = lose;
  }

  public void setScore(double score) {
    this.score = score;
  }

  public void setPathNumber(int pathNumber) {
    this.pathNumber = pathNumber;
  }

  public void setMap(int[][] map) {
    this.map = map;
  }

  public void setPosition(int[] position) {
    this.position = position;
  }

  public void setPreviousMove(int[] previousMove) {
    this.previousMove = previousMove;
  }

  public void setStations(ArrayList<int[]> stations) {
    this.stations = stations;
  }

}

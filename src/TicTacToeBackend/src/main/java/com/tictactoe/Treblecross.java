package com.tictactoe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/treblecross")
@CrossOrigin
public class Treblecross {
  private String board[][];
  final private String SYMBOL = "X";
  private String turn;
  private String difficulty;
  private Stack<String[][]> previousMoveStack = new Stack<>();
  private Map<String, Treblecross> gameInstances = new HashMap<>();

  @Autowired
  SavedGameRepository savedGameRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  LeaderBoardRepository leaderBoardRepository;

  public Treblecross() {
    board = new String[][] { { "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-" } };
    difficulty = "easy";
    turn = "PLAYER";
  }

  @GetMapping("/{email}/board")
  public String[][] getBoard(@PathVariable String email) {
    Treblecross gameInstance = gameInstances.get(email);
    return gameInstance.getBoard2();
  }

  public String[][] getBoard2() {
    return this.board;
  }

  public void setBoard(String[][] board) {
    this.board = board;
  }

  public void setTurn(String turn) {
    this.turn = turn;
  }

  public String getTurn() {
    return turn;
  }

  public Stack<String[][]> getPreviousMoveStack() {
    return previousMoveStack;
  }

  public void setPreviousMoveStack(Stack<String[][]> previousMoveStack) {
    this.previousMoveStack = previousMoveStack;
  }

  public void printBoard() {

    System.out.println("---------------------------------------------");
    for (int box = 0; box < board[0].length; box++) {
      if (box != board[0].length - 1) {
        System.out.print("| " + board[0][box] + " ");
      } else {
        System.out.print("| " + board[0][box] + " |");
      }
    }
    System.out.println("\n---------------------------------------------");
  }

  @GetMapping("/{email}/backMove")
  public void backMove(@PathVariable String email) {
    Treblecross gameInstance = gameInstances.get(email);
    if (!gameInstance.getPreviousMoveStack().isEmpty()) {
      gameInstance.setBoard(gameInstance.getPreviousMoveStack().pop());
      gameInstance.printBoard();
      System.out.println("You take a back move!");
    } else {
      System.out.println("No more previous step!");
    }

  }

  @PostMapping("/{email}/playerMove")
  public ResponseEntity<String> playerMove(@RequestBody PlayerMoveClass move, @PathVariable String email) {
    Treblecross gameInstance = gameInstances.get(email);
    gameInstance.setTurn("PLAYER");
    double suboptimalProb = 0;

    if (gameInstance.getDifficultyString().equals("hard")) {
      suboptimalProb = 0.5;
    } else if (gameInstance.getDifficultyString().equals("medium")) {
      suboptimalProb = 0.7;
    } else
      suboptimalProb = 0.9;

    if (gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository, leaderBoardRepository,
        false) == 200) {

      if (!gameInstance.checkValidMove(move.getWhichRow())) {
        System.out.println("Invalid Move!");
        return ResponseEntity.ok("{ \"status\": \"Invalid Move!\"}");
      } else {
        String[][] board = gameInstance.getBoard2();
        String[][] previousMove = new String[board.length][board[0].length];
        System.arraycopy(board[0], 0, previousMove[0], 0, board[0].length);
        gameInstance.getPreviousMoveStack().push(previousMove);
        board[0][move.getWhichRow()] = SYMBOL;
        gameInstance.setBoard(board);
        int checkWinAfterAIMove = 404;
        int checkWinAfterPlayerMove = gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository,
            leaderBoardRepository, false);
        if (checkWinAfterPlayerMove == 1) {
          gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository, leaderBoardRepository, true);
        }

        gameInstance.printBoard();

        if (checkWinAfterPlayerMove == 200) {

          gameInstance.aiMove(suboptimalProb);
          gameInstance.printBoard();
          checkWinAfterAIMove = gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository,
              leaderBoardRepository, false);
          if (checkWinAfterAIMove == -1) {
            gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository, leaderBoardRepository, true);
          }
        }

        if (checkWinAfterAIMove != 404) {
          return ResponseEntity.ok("{\"status\": "
              + checkWinAfterAIMove
              + " }");
        } else {
          return ResponseEntity.ok("{\"status\": "
              + checkWinAfterPlayerMove
              + " }");
        }

      }

    }
    return ResponseEntity.ok("Player Move successfully!");
  }

  @PostMapping("/{email}/playerMoveStory")
  public ResponseEntity<String> playerMoveStory(@RequestBody PlayerMoveClass move, @PathVariable String email) {
    Treblecross gameInstance = gameInstances.get(email);
    gameInstance.setTurn("PLAYER");
    double suboptimalProb = 0;

    if (move.getDifficulty().equals("hard")) {
      suboptimalProb = 0;
    } else if (move.getDifficulty().equals("medium")) {
      suboptimalProb = 0.5;
    } else
      suboptimalProb = 0.7;

    if (gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository, leaderBoardRepository,
        false) == 200) {

      if (!gameInstance.checkValidMove(move.getWhichRow())) {
        System.out.println("Invalid Move!");
        return ResponseEntity.ok("{\"status\": \"Invalid Move!\"}");
      } else {
        String[][] board = gameInstance.getBoard2();
        String[][] previousMove = new String[board.length][board[0].length];
        System.arraycopy(board[0], 0, previousMove[0], 0, board[0].length);
        gameInstance.getPreviousMoveStack().push(previousMove);
        board[0][move.getWhichRow()] = SYMBOL;
        gameInstance.setBoard(board);
        int checkWinAfterAIMove = 404;
        int checkWinAfterPlayerMove = gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository,
            leaderBoardRepository, false);
        if (checkWinAfterPlayerMove == 1) {
          gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository, leaderBoardRepository, false);
        }

        gameInstance.printBoard();

        if (checkWinAfterPlayerMove == 200) {
          gameInstance.aiMove(suboptimalProb);
          gameInstance.printBoard();
          checkWinAfterAIMove = gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository,
              leaderBoardRepository, false);
          if (checkWinAfterAIMove == -1) {
            gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository, leaderBoardRepository, false);
          }
        }

        if (checkWinAfterAIMove != 404) {
          return ResponseEntity.ok("{\"status\": "
              + checkWinAfterAIMove
              + " }");
        } else {
          return ResponseEntity.ok("{\"status\": "
              + checkWinAfterPlayerMove
              + " }");
        }

      }

    }
    return ResponseEntity.ok("Player Move successfully!");
  }

  @PostMapping("/{email}/PVPMove")
  public ResponseEntity<String> PVPMove(@RequestBody PvPClassTreblecross move, @PathVariable String email) {
    Treblecross gameInstance = gameInstances.get(email);

    if (gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository, leaderBoardRepository,
        false) == 200) {
      gameInstance.setTurn(move.getTurn());

      if (!gameInstance.checkValidMove(move.getWhichRow())) {
        System.out.println("Invalid Move!");
        return ResponseEntity.ok("Invalid Move!");
      } else {
        String[][] board = gameInstance.getBoard2();
        board[0][move.getWhichRow()] = SYMBOL;
        gameInstance.setBoard(board);
        int checkWinAfterPlayerMove = gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository,
            leaderBoardRepository, false);
        gameInstance.printBoard();

        return ResponseEntity.ok("{\"status\": "
            + checkWinAfterPlayerMove
            + " }");

      }

    }
    return ResponseEntity.ok("Player Move successfully!");
  }

  public boolean checkValidMove(int whichRow) {
    return board[0][whichRow].equals("-");
  }

  @PostMapping("/{email}/difficulty")
  public ResponseEntity<String> setDifficulty(@RequestBody SettingDifficulty difficult, @PathVariable String email) {
    Treblecross gameInstance = gameInstances.get(email);
    gameInstance.setDifficulty2(difficult.getDifficulty());
    return ResponseEntity.ok("{\"difficulty\": \"" + gameInstance.getDifficultyString() + "\"}");
  }

  public void setDifficulty2(String difficulty) {
    this.difficulty = difficulty;
  }

  @GetMapping("/{email}/getdifficulty")
  public ResponseEntity<String> getDifficulty(@PathVariable String email) {
    Treblecross gameInstance = gameInstances.get(email);
    return ResponseEntity.ok("{\"difficulty\": \"" + gameInstance.getDifficultyString() + "\"}");
  }

  public String getDifficultyString() {
    return this.difficulty;
  }

  @GetMapping("/{email}/restart")
  public void restartGame(@PathVariable String email) {
    Treblecross gameInstance = gameInstances.get(email);
    String[][] board = gameInstance.getBoard2();
    for (int row = 0; row < board[0].length; row++) {
      board[0][row] = "-";
    }
    gameInstance.setBoard(board);
    gameInstance.getPreviousMoveStack().clear();
    gameInstance.printBoard();
  }

  @GetMapping("/{email}/newGame")
  public void startNewGame(@PathVariable String email) {
    Treblecross gameInstance = new Treblecross();
    gameInstances.put(email, gameInstance);
    gameInstance.initializeGame();
    gameInstance.printBoard();

  }

  private void initializeGame() {
    for (int row = 0; row < board.length; row++) {
      board[0][row] = "-";
    }
    this.difficulty = "easy";
  }

  public int evaluate(String b[][], String turn) {
    if (turn.equals("PLAYER")) {
      return checkTripleCross(b) == 1 ? -10 : 0;
    } else {
      return checkTripleCross(b) == 1 ? 10 : 0;
    }
  }

  public int minimax(String board[][], String turn, int depth, boolean isMax, double suboptimalProb) {

    int score = evaluate(board, turn);

    // If Maximizer has won the game
    // return his/her evaluated score
    if (score == 10)
      return score;

    // If Minimizer has won the game
    // return his/her evaluated score
    if (score == -10)
      return score;

    // If this maximizer's move
    if (isMax) {
      int best = Integer.MIN_VALUE;

      // Traverse all cells
      for (int i = 0; i < board[0].length; i++) {

        // Check if cell is empty
        if (board[0][i].equals("-")) {
          // Make the move
          board[0][i] = SYMBOL;

          // Call minimax recursively and choose
          // the maximum value
          best = Math.max(best, minimax(board, "AI", depth + 1, !isMax, suboptimalProb));

          // Undo the move
          board[0][i] = "-";
        }

      }
      // Add randomness to the decision
      if (depth == 0) {
        if (Math.random() < suboptimalProb) {
          best -= (int) (Math.random() * 10) - 5;
        }
      }

      return best - depth;
    }

    // If this minimizer's move
    else {
      int best = Integer.MAX_VALUE;

      // Traverse all cells
      for (int i = 0; i < board[0].length; i++) {
        // Check if cell is empty
        if (board[0][i].equals("-")) {
          // Make the move
          board[0][i] = SYMBOL;

          // Call minimax recursively and choose
          // the minimum value
          best = Math.min(best, minimax(board, "PLAYER", depth + 1, !isMax, suboptimalProb));

          // Undo the move
          board[0][i] = "-";
        }

      }
      if (depth == 0) {
        if (Math.random() < suboptimalProb) {
          best += (int) (Math.random() * 10) + 5;
        }
      }

      return best + depth;
    }

  }

  public Move findBestMove(String board[][], double suboptimalProb, String turn, boolean isMax) {
    int bestVal = Integer.MIN_VALUE;
    Move bestMove = new Move();
    bestMove.row = -1;

    // Traverse all cells, evaluate minimax function
    // for all empty cells. And return the cell
    // with optimal value.
    for (int i = 0; i < board[0].length; i++) {
      // Check if cell is empty
      if (board[0][i].equals("-")) {
        // Make the move
        board[0][i] = SYMBOL;

        // compute evaluation function for this
        // move.
        int moveVal = minimax(board, "AI", 0, false, suboptimalProb);

        // Undo the move
        board[0][i] = "-";

        // If the value of the current move is
        // more than the best value, then update
        // best

        if (moveVal > bestVal) {
          bestMove.row = i;
          bestVal = moveVal;
        }
      }
    }

    return bestMove;
  }

  public void aiMove(double suboptimalProb) {
    setTurn("AI");
    Move bestMove = new Move();
    bestMove = findBestMove(board, suboptimalProb, "AI", false);
    board[0][bestMove.row] = SYMBOL;
  }

  @PostMapping("/{email}/EvEAiMove")
  public ResponseEntity<String> EvEAiMove(@RequestBody EveMove eve, @PathVariable String email) {
    Treblecross gameInstance = gameInstances.get(email);
    String[][] board = gameInstance.getBoard2();
    Move bestMove = new Move();
    double suboptimalProb = 0;

    if (gameInstance.getDifficultyString().equals("hard")) {
      suboptimalProb = 0.5;
    } else if (gameInstance.getDifficultyString().equals("medium")) {
      suboptimalProb = 0.7;
    } else
      suboptimalProb = 0.9;
    if (gameInstance.checkTripleCross(board) == 200) {
      bestMove = findBestMove(board, suboptimalProb, eve.getTurn(), eve.getIsMax());
      if (gameInstance.checkValidMove(bestMove.row)) {
        board[0][bestMove.row] = SYMBOL;
        gameInstance.setBoard(board);
        gameInstance.printBoard();
        int winCode = gameInstance.checkTripleCross(board);
        if (winCode == 1 && turn.equals("AI")) {
          System.out.println("Computer 2 Win!");
        } else if (winCode == 1 && turn.equals("PLAYER")) {
          System.out.println("Computer 1 Win!");
        }
        return ResponseEntity.ok("{ \"winCode\": \" " + winCode + " \"}");
      } else {
        System.out.println("Invalid Move!");
        return ResponseEntity.ok("Invalid Move");
      }
    }
    return ResponseEntity.ok("{ \"winCode\": \" " + 0 + " \"}");

  }

  public int checkTripleCross(String[][] board) {
    Stack<String> stack = new Stack<>();
    for (int i = 0; i < board[0].length; i++) {
      if (board[0][i].equals(SYMBOL)) {
        stack.push(board[0][i]);
        if (stack.size() == 3) {
          return 1;
        }
      } else if (!stack.isEmpty() && !board[0][i].equals(SYMBOL)) {
        stack.clear();
      }
    }
    return 200;
  }

  @GetMapping("/{email}/checkWin")
  public int checkWin(String email, String difficulty, UserRepository userRepository,
      LeaderBoardRepository leaderBoardRepository, boolean willSave) {

    if (getTurn().equals("PLAYER")) {
      if (willSave) {
        saveWinLoseDatabase(email, true, difficulty, userRepository, leaderBoardRepository);
      }
      return checkTripleCross(board);
    } else {
      if (willSave) {
        saveWinLoseDatabase(email, false, difficulty, userRepository, leaderBoardRepository);
      }

      return checkTripleCross(board) == 1 ? -1 : 200;
    }
  }

  @PostMapping("/{email}/savegame")
  public ResponseEntity<String> saveGame(@RequestBody BoardSaved boardToSave, @PathVariable String email) {
    Treblecross gameInstance = gameInstances.get(email);
    List<User> user = userRepository.findByEmail(boardToSave.getEmail());
    if (user.isEmpty()) {
      return ResponseEntity.badRequest().body("{\"message\": \"Some error occurs!\"}");
    } else {
      SavedGame saved = new SavedGame();
      saved.setUser(user.get(0));
      saved.setBoard(gameInstance.getBoard2());
      saved.setDifficulty(gameInstance.getDifficultyString());
      saved.setGame("treblecross");
      saved.setName(boardToSave.getName());
      ObjectMapper objectMapper = new ObjectMapper();
      String JsonString = "";
      try {
        JsonString = objectMapper.writeValueAsString(gameInstance.getPreviousMoveStack());
      } catch (JsonProcessingException e) {
        System.out.println(e.getMessage());
      }
      saved.setPreviousMove(JsonString);
      savedGameRepository.save(saved);
      return ResponseEntity.ok("{\"message\": \"Successfully Saved!\"}");
    }
  }

  @PostMapping("/{email}/loadgame")
  public ResponseEntity<String> loadGame(@RequestBody BoardLoaded loadedBoard, @PathVariable String email) {
    Treblecross gameInstance = gameInstances.get(email);
    List<SavedGame> savedGame = savedGameRepository.findById(loadedBoard.getId());
    if (!savedGame.isEmpty()) {
      gameInstance.setBoard(savedGame.get(0).getBoard());
      gameInstance.setDifficulty2(savedGame.get(0).getDifficulty());
      ObjectMapper objectMapper = new ObjectMapper();
      Stack<String[][]> PreviousMoveStack;
      try {
        PreviousMoveStack = objectMapper.readValue(savedGame.get(0).getPreviousMove(),
            new TypeReference<Stack<String[][]>>() {
            });
        gameInstance.setPreviousMoveStack(PreviousMoveStack);
      } catch (JsonProcessingException e) {
        System.out.println(e.getMessage());
      }

      return ResponseEntity.ok("{\"message\": \"Game Loaded Successfully!\"}");
    } else {
      return ResponseEntity.badRequest().body("{\"message\": \"Some error occurs!\"}");
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

  public void saveWinLoseDatabase(String email, boolean isWin, String difficulty, UserRepository userRepository,
      LeaderBoardRepository leaderBoardRepository) {

    List<User> user = userRepository.findByEmail(email);
    if (isWin) {
      if (!user.isEmpty()) {
        List<LeaderBoard> user2 = leaderBoardRepository.findByUser(user.get(0));
        List<LeaderBoard> userByDif = leaderBoardRepository.findByDifficulty(difficulty);
        List<LeaderBoard> userByGame = leaderBoardRepository.findByGame("treblecross");
        List<LeaderBoard> intersect1 = intersection(user2, userByDif);
        List<LeaderBoard> intersected = intersection(intersect1, userByGame);
        if (!intersected.isEmpty()) {
          int winTime = intersected.get(0).getWin();
          double previousScore = intersected.get(0).getScore();
          intersected.get(0).setWin(winTime + 1);
          intersected.get(0).setScore(previousScore + 5);
          leaderBoardRepository.save(intersected.get(0));
        } else {
          LeaderBoard leaderboard = new LeaderBoard();
          leaderboard.setUser(user.get(0));
          leaderboard.setUserName(user.get(0).getUserName());
          leaderboard.setDifficulty(difficulty);
          leaderboard.setWin(1);
          leaderboard.setLose(0);
          leaderboard.setScore(5);
          leaderboard.setGame("treblecross");
          leaderBoardRepository.save(leaderboard);
        }
      }
    } else {
      if (!user.isEmpty()) {
        List<LeaderBoard> user2 = leaderBoardRepository.findByUser(user.get(0));
        List<LeaderBoard> userByDif = leaderBoardRepository.findByDifficulty(difficulty);
        List<LeaderBoard> userByGame = leaderBoardRepository.findByGame("treblecross");
        List<LeaderBoard> intersect1 = intersection(user2, userByDif);
        List<LeaderBoard> intersected = intersection(intersect1, userByGame);
        if (!intersected.isEmpty()) {
          int loseTime = intersected.get(0).getLose();
          double previousScore = intersected.get(0).getScore();
          intersected.get(0).setLose(loseTime + 1);
          intersected.get(0).setScore(previousScore - 3);
          leaderBoardRepository.save(intersected.get(0));
        } else {
          LeaderBoard leaderboard = new LeaderBoard();
          leaderboard.setUser(user.get(0));
          leaderboard.setUserName(user.get(0).getUserName());
          leaderboard.setDifficulty(difficulty);
          leaderboard.setWin(0);
          leaderboard.setLose(1);
          leaderboard.setScore(-3);
          leaderboard.setGame("treblecross");
          leaderBoardRepository.save(leaderboard);
        }
      }
    }

  }
}

class Move {
  public int row;
  public int col;

}

class PlayerMoveClass {
  private int whichRow;
  private int whichCol;
  private String email;
  private String difficulty;

  public PlayerMoveClass(int whichCol, int whichRow, String email, String difficulty) {
    this.whichCol = whichCol;
    this.whichRow = whichRow;
    this.email = email;
    this.difficulty = difficulty;
  }

  public int getWhichRow() {
    return whichRow;
  }

  public int getWhichCol() {
    return whichCol;
  }

  public String getEmail() {
    return email;
  }

  public String getDifficulty() {
    return difficulty;
  }

}

class PvPClassTreblecross {
  private int whichRow;
  private String email;
  private String difficulty;
  private String turn;

  public PvPClassTreblecross(int whichRow, String email, String difficulty, String turn) {
    this.whichRow = whichRow;
    this.email = email;
    this.difficulty = difficulty;
    this.turn = turn;
  }

  public int getWhichRow() {
    return whichRow;
  }

  public String getEmail() {
    return email;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public String getTurn() {
    return turn;
  }

}

class BoardSaved {
  private String email;
  private String name;

  public BoardSaved() {

  }

  public BoardSaved(String email, String name) {
    this.email = email;
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }
}

class BoardLoaded {
  private String id;

  public BoardLoaded() {

  }

  public BoardLoaded(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}

class SettingDifficulty {
  private String difficulty;

  public SettingDifficulty() {

  }

  public SettingDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public String getDifficulty() {
    return difficulty;
  }
}

class EveMove {
  private String turn;
  private boolean isMax;

  public EveMove(String turn, boolean isMax) {
    this.turn = turn;
    this.isMax = isMax;
  }

  public EveMove() {

  }

  public String getTurn() {
    return turn;
  }

  public boolean getIsMax() {
    return isMax;
  }
}

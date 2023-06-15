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
@RequestMapping("/mtictactoe")
@CrossOrigin
public class MisereTicTacToe {
  private String board[][];
  final private String PLAYER = "X";
  final private String AI = "O";
  private String difficulty;
  private Stack<String[][]> previousMoveStack = new Stack<>();
  private Map<String, MisereTicTacToe> gameInstances = new HashMap<>();

  @Autowired
  SavedGameRepository savedGameRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  LeaderBoardRepository leaderBoardRepository;

  public MisereTicTacToe() {
    board = new String[][] { { "-", "-", "-" }, { "-", "-", "-" }, { "-", "-", "-" } };
    difficulty = "easy";
  }

  @GetMapping("/{email}/board")
  public String[][] getBoard(@PathVariable String email) {
    MisereTicTacToe gameInstance = gameInstances.get(email);
    return gameInstance.getBoard2();
  }

  public String[][] getBoard2() {
    return this.board;
  }

  public void setBoard(String[][] board) {
    this.board = board;
  }

  public Stack<String[][]> getPreviousMoveStack() {
    return previousMoveStack;
  }

  public void setPreviousMoveStack(Stack<String[][]> previousMoveStack) {
    this.previousMoveStack = previousMoveStack;
  }

  public void printBoard() {
    System.out.println("-------------");
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        if (col == 0) {
          System.out.print("| " + board[row][col] + " |");
        } else if (col == 2 && row != 2) {
          System.out.print(" " + board[row][col] + " |\n");
        } else {
          System.out.print(" " + board[row][col] + " |");
        }
      }
    }
    System.out.println("\n-------------");
  }

  @GetMapping("/{email}/backMove")
  public void backMove(@PathVariable String email) {
    MisereTicTacToe gameInstance = gameInstances.get(email);
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
    MisereTicTacToe gameInstance = gameInstances.get(email);
    double suboptimalProb = 0;

    if (gameInstance.getDifficultyString().equals("hard")) {
      suboptimalProb = 0.0;
    } else if (gameInstance.getDifficultyString().equals("medium")) {
      suboptimalProb = 0.3;
    } else
      suboptimalProb = 0.5;

    if (gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository, leaderBoardRepository,
        true) == 200) {

      if (!gameInstance.checkValidMove(move.getWhichRow(), move.getWhichCol())) {
        System.out.println("Invalid Move!");
        return ResponseEntity.ok("{ \"status\": \"Invalid Move!\"}");
      } else {
        String[][] board = gameInstance.getBoard2();
        String[][] previousMove = new String[3][3];
        for (int i = 0; i < 3; i++) {
          System.arraycopy(board[i], 0, previousMove[i], 0, board[i].length);
        }
        gameInstance.getPreviousMoveStack().push(previousMove);
        board[move.getWhichRow()][move.getWhichCol()] = PLAYER;
        gameInstance.setBoard(board);
        int checkWinAfterAIMove = 404;
        int checkWinAfterPlayerMove = gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository,
            leaderBoardRepository, true);
        gameInstance.printBoard();

        if (checkWinAfterPlayerMove == 200) {
          gameInstance.aiMove(suboptimalProb);
          gameInstance.printBoard();

          checkWinAfterAIMove = gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository,
              leaderBoardRepository, true);
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
    MisereTicTacToe gameInstance = gameInstances.get(email);
    double suboptimalProb = 0;

    if (move.getDifficulty().equals("hard")) {
      suboptimalProb = 0.02;
    } else if (move.getDifficulty().equals("medium")) {
      suboptimalProb = 0.2;
    } else
      suboptimalProb = 0.5;

    if (gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository, leaderBoardRepository,
        false) == 200) {

      if (!gameInstance.checkValidMove(move.getWhichRow(), move.getWhichCol())) {
        System.out.println("Invalid Move!");
        return ResponseEntity.ok("{\"status\": \"Invalid Move!\"}");
      } else {
        String[][] board = gameInstance.getBoard2();
        String[][] previousMove = new String[3][3];
        for (int i = 0; i < 3; i++) {
          System.arraycopy(board[i], 0, previousMove[i], 0, board[i].length);
        }
        gameInstance.getPreviousMoveStack().push(previousMove);
        board[move.getWhichRow()][move.getWhichCol()] = PLAYER;
        gameInstance.setBoard(board);
        int checkWinAfterAIMove = 404;
        int checkWinAfterPlayerMove = gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository,
            leaderBoardRepository, false);
        gameInstance.printBoard();

        if (checkWinAfterPlayerMove == 200) {
          gameInstance.aiMove(suboptimalProb);
          gameInstance.printBoard();

          checkWinAfterAIMove = gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository,
              leaderBoardRepository, false);
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
  public ResponseEntity<String> PVPMove(@RequestBody PvPClass move, @PathVariable String email) {
    MisereTicTacToe gameInstance = gameInstances.get(email);

    if (gameInstance.checkWin(move.getEmail(), move.getDifficulty(), userRepository, leaderBoardRepository,
        false) == 200) {

      if (!gameInstance.checkValidMove(move.getWhichRow(), move.getWhichCol())) {
        System.out.println("Invalid Move!");
        return ResponseEntity.ok("Invalid Move!");
      } else {
        String[][] board = gameInstance.getBoard2();
        board[move.getWhichRow()][move.getWhichCol()] = move.getSymbol();
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

  @PostMapping("/{email}/EvEAiMove")
  public ResponseEntity<String> EvEAiMove(@RequestBody EveMove eve, @PathVariable String email) {
    MisereTicTacToe gameInstance = gameInstances.get(email);
    String[][] board = gameInstance.getBoard2();
    Move bestMove = new Move();
    double suboptimalProb = 0;

    if (gameInstance.getDifficultyString().equals("hard")) {
      suboptimalProb = 0.5;
    } else if (gameInstance.getDifficultyString().equals("medium")) {
      suboptimalProb = 0.7;
    } else
      suboptimalProb = 0.9;
    if (gameInstance.checkWin(email, difficulty, userRepository, leaderBoardRepository, false) == 200) {
      bestMove = findBestMove(board, suboptimalProb, eve.getTurn(), eve.getIsMax());
      if (gameInstance.checkValidMove(bestMove.row, bestMove.col)) {
        board[bestMove.row][bestMove.col] = eve.getTurn();
        gameInstance.setBoard(board);
        gameInstance.printBoard();
        int winCode = gameInstance.checkWin(email, difficulty, userRepository, leaderBoardRepository, false);
        if (winCode == 1 && eve.getTurn().equals("X")) {
          System.out.println("Computer 1 Win!");
        } else if (winCode == 1 && eve.getTurn().equals("O")) {
          System.out.println("Computer 2 Win!");
        }
        return ResponseEntity.ok("{ \"winCode\": \" " + winCode + " \"}");
      } else {
        System.out.println("Invalid Move!");
        return ResponseEntity.ok("Invalid Move");
      }
    }
    return ResponseEntity.ok("{ \"winCode\": \" " + 0 + " \"}");

  }

  public boolean checkValidMove(int whichRow, int whichCol) {
    return board[whichRow][whichCol].equals("-");
  }

  public boolean fullBoard() {
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        if (board[row][col].equals("-"))
          return false;
      }
    }
    return true;
  }

  @PostMapping("/{email}/difficulty")
  public ResponseEntity<String> setDifficulty(@RequestBody SettingDifficulty difficult, @PathVariable String email) {
    MisereTicTacToe gameInstance = gameInstances.get(email);
    gameInstance.setDifficulty2(difficult.getDifficulty());
    return ResponseEntity.ok("{\"difficulty\": \"" + gameInstance.getDifficultyString() + "\"}");
  }

  public void setDifficulty2(String difficulty) {
    this.difficulty = difficulty;
  }

  @GetMapping("/{email}/getdifficulty")
  public ResponseEntity<String> getDifficulty(@PathVariable String email) {
    MisereTicTacToe gameInstance = gameInstances.get(email);
    return ResponseEntity.ok("{\"difficulty\": \"" + gameInstance.getDifficultyString() + "\"}");
  }

  public String getDifficultyString() {
    return this.difficulty;
  }

  @GetMapping("/{email}/restart")
  public void restartGame(@PathVariable String email) {
    MisereTicTacToe gameInstance = gameInstances.get(email);
    String[][] board = gameInstance.getBoard2();
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        board[row][col] = "-";
      }
    }
    gameInstance.setBoard(board);
    gameInstance.getPreviousMoveStack().clear();
    gameInstance.printBoard();
  }

  @GetMapping("/{email}/newGame")
  public void startNewGame(@PathVariable String email) {
    MisereTicTacToe gameInstance = new MisereTicTacToe();
    gameInstances.put(email, gameInstance);

    gameInstance.initializeGame();
    gameInstance.printBoard();
  }

  private void initializeGame() {
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        board[row][col] = "-";
      }
    }
    this.difficulty = "easy";
  }

  public int evaluate(String b[][]) {
    // Checking for Rows for X or O victory.
    for (int row = 0; row < 3; row++) {
      if (b[row][0].equals(b[row][1]) &&
          b[row][1].equals(b[row][2])) {
        if (b[row][0].equals(PLAYER))
          return -10;
        else if (b[row][0].equals(AI))
          return +10;
      }
    }

    // Checking for Columns for X or O victory.
    for (int col = 0; col < 3; col++) {
      if (b[0][col].equals(b[1][col]) &&
          b[1][col].equals(b[2][col])) {
        if (b[0][col].equals(PLAYER))
          return -10;

        else if (b[0][col].equals(AI))
          return +10;
      }
    }

    // Checking for Diagonals for X or O victory.
    if (b[0][0].equals(b[1][1]) && b[1][1].equals(b[2][2])) {
      if (b[0][0].equals(PLAYER))
        return -10;
      else if (b[0][0].equals(AI))
        return +10;
    }

    if (b[0][2].equals(b[1][1]) && b[1][1].equals(b[2][0])) {
      if (b[0][2].equals(PLAYER))
        return -10;
      else if (b[0][2].equals(AI))
        return +10;
    }

    // Else if none of them have won then return 0
    return 0;
  }

  public int minimax(String board[][], int depth, boolean isMax, double suboptimalProb) {

    int score = evaluate(board);

    // If Maximizer has won the game
    // return his/her evaluated score
    if (score == 10)
      return score;

    // If Minimizer has won the game
    // return his/her evaluated score
    if (score == -10)
      return score;

    // If there are no more moves and
    // no winner then it is a tie
    if (fullBoard())
      return 0;

    // If this maximizer's move
    if (isMax) {
      int best = Integer.MIN_VALUE;

      // Traverse all cells
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          // Check if cell is empty
          if (board[i][j].equals("-")) {
            // Make the move
            board[i][j] = PLAYER;

            // Call minimax recursively and choose
            // the maximum value
            best = Math.max(best, minimax(board, depth + 1, isMax, suboptimalProb));

            // Undo the move
            board[i][j] = "-";
          }
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
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          // Check if cell is empty
          if (board[i][j].equals("-")) {
            // Make the move
            board[i][j] = AI;

            // Call minimax recursively and choose
            // the minimum value
            best = Math.min(best, minimax(board, depth + 1, !isMax, suboptimalProb));

            // Undo the move
            board[i][j] = "-";
          }
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

  public Move findBestMove(String board[][], double suboptimalProb, String symbol, boolean isMax) {
    int bestVal = Integer.MAX_VALUE;
    Move bestMove = new Move();
    bestMove.row = -1;
    bestMove.col = -1;

    // Traverse all cells, evaluate minimax function
    // for all empty cells. And return the cell
    // with optimal value.
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        // Check if cell is empty
        if (board[i][j].equals("-")) {
          // Make the move
          board[i][j] = symbol;

          // compute evaluation function for this
          // move.
          int moveVal = minimax(board, 0, isMax, suboptimalProb);

          // Undo the move
          board[i][j] = "-";

          // If the value of the current move is
          // more than the best value, then update
          // best/

          if (moveVal < bestVal) {
            bestMove.row = i;
            bestMove.col = j;
            bestVal = moveVal;
          }

        }
      }
    }

    return bestMove;
  }

  public void aiMove(double suboptimalProb) {

    Move bestMove = new Move();
    bestMove = findBestMove(board, suboptimalProb, AI, true);
    board[bestMove.row][bestMove.col] = AI;

  }

  @GetMapping("/{email}/checkWin")
  public int checkWin(String email, String difficulty, UserRepository userRepository,
      LeaderBoardRepository leaderBoardRepository, boolean willSave) {

    if (equalsLinePlayer(0, 0, 0, 1, 0, 2) || equalsLinePlayer(1, 0, 1, 1, 1, 2)
        || equalsLinePlayer(2, 0, 2, 1, 2, 2) ||
        equalsLinePlayer(0, 0, 1, 0, 2, 0) || equalsLinePlayer(0, 1, 1, 1, 2, 1)
        || equalsLinePlayer(0, 2, 1, 2, 2, 2) ||
        equalsLinePlayer(0, 0, 1, 1, 2, 2) || equalsLinePlayer(0, 2, 1, 1, 2, 0)) {
      System.out.println("You Lose!");
      if (willSave) {
        saveWinLoseDatabase(email, false, difficulty, userRepository, leaderBoardRepository);
      }

      return -1;
    } else if (equalsLineAI(0, 0, 0, 1, 0, 2) || equalsLineAI(1, 0, 1, 1, 1, 2)
        || equalsLineAI(2, 0, 2, 1, 2, 2) ||
        equalsLineAI(0, 0, 1, 0, 2, 0) || equalsLineAI(0, 1, 1, 1, 2, 1)
        || equalsLineAI(0, 2, 1, 2, 2, 2) ||
        equalsLineAI(0, 0, 1, 1, 2, 2) || equalsLineAI(0, 2, 1, 1, 2, 0)) {
      System.out.println("You Win!");
      if (willSave) {
        saveWinLoseDatabase(email, true, difficulty, userRepository, leaderBoardRepository);
      }

      return 1;
    } else if (fullBoard()) {
      System.out.println("Tie!");
      return 0;
    } else {
      return 200;
    }
  }

  public boolean equalsLinePlayer(int row1, int col1, int row2, int col2, int row3, int col3) {
    return board[row1][col1].equals(PLAYER) && board[row1][col1].equals(board[row2][col2])
        && board[row1][col1].equals(board[row3][col3]);
  }

  public boolean equalsLineAI(int row1, int col1, int row2, int col2, int row3, int col3) {
    return board[row1][col1].equals(AI) && board[row1][col1].equals(board[row2][col2])
        && board[row1][col1].equals(board[row3][col3]);
  }

  @PostMapping("/{email}/savegame")
  public ResponseEntity<String> saveGame(@RequestBody BoardSaved boardToSave, @PathVariable String email) {
    MisereTicTacToe gameInstance = gameInstances.get(email);
    List<User> user = userRepository.findByEmail(boardToSave.getEmail());
    if (user.isEmpty()) {
      return ResponseEntity.badRequest().body("{\"message\": \"Some error occurs!\"}");
    } else {
      SavedGame saved = new SavedGame();
      saved.setUser(user.get(0));
      saved.setBoard(gameInstance.getBoard2());
      saved.setDifficulty(gameInstance.getDifficultyString());
      saved.setGame("mttt");
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
    MisereTicTacToe gameInstance = gameInstances.get(email);
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
        List<LeaderBoard> userByGame = leaderBoardRepository.findByGame("mttt");
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
          leaderboard.setGame("mttt");
          leaderBoardRepository.save(leaderboard);
        }
      }
    } else {
      if (!user.isEmpty()) {
        List<LeaderBoard> user2 = leaderBoardRepository.findByUser(user.get(0));
        List<LeaderBoard> userByDif = leaderBoardRepository.findByDifficulty(difficulty);
        List<LeaderBoard> userByGame = leaderBoardRepository.findByGame("mttt");
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
          leaderboard.setGame("mttt");
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
    this.whichRow = whichRow;
    this.whichCol = whichCol;
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

class PvPClass {
  private int whichRow;
  private int whichCol;
  private String email;
  private String difficulty;
  private String symbol;

  public PvPClass(int whichCol, int whichRow, String email, String difficulty, String symbol) {
    this.whichRow = whichRow;
    this.whichCol = whichCol;
    this.email = email;
    this.difficulty = difficulty;
    this.symbol = symbol;
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

  public String getSymbol() {
    return symbol;
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
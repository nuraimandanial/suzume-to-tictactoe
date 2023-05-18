package com.tictactoe;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fftictactoe")
@CrossOrigin
public class FFtictactoe {
  private String board[][];
  final private String PLAYER = "X";
  final private String AI = "O";
  private String difficulty;

  @Autowired
  SavedGameRepository savedGameRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  LeaderBoardRepository leaderBoardRepository;

  public FFtictactoe() {
    board = new String[][] { { "-", "-", "-", "-", "-" }, { "-", "-", "-", "-", "-" }, { "-", "-", "-", "-", "-" },
        { "-", "-", "-", "-", "-" }, { "-", "-", "-", "-", "-" } };
    difficulty = "easy";
  }

  @GetMapping("/board")
  public String[][] getBoard() {
    return board;
  }

  public void setBoard(String[][] board) {
    this.board = board;
  }

  @GetMapping
  public void printBoard() {
    System.out.println("---------------------");
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        if (col == 0) {
          System.out.print("| " + board[row][col] + " |");
        } else if (col == 4 && row != 4) {
          System.out.print(" " + board[row][col] + " |\n");
        } else {
          System.out.print(" " + board[row][col] + " |");
        }
      }
    }
    System.out.println("\n---------------------");
  }

  @PostMapping("/playerMove")
  public ResponseEntity<String> playerMove(@RequestBody PlayerMoveClass move) {
    double suboptimalProb = 0;

    if (difficulty.equals("hard")) {
      suboptimalProb = 0;
    } else if (difficulty.equals("medium")) {
      suboptimalProb = 0.2;
    } else
      suboptimalProb = 0.5;

    if (checkWin(move.getEmail(), move.getDifficulty()) == 200) {

      if (!checkValidMove(move.getWhichRow(), move.getWhichCol())) {
        System.out.println("Invalid Move!");
        return ResponseEntity.ok("Invalid Move!");
      } else {
        board[move.getWhichRow()][move.getWhichCol()] = PLAYER;

        printBoard();
        if (checkWin(move.getEmail(), move.getDifficulty()) == 200) {
          aiMove(suboptimalProb);
          checkWin(move.getEmail(), move.getDifficulty());
        }

      }

    }
    return ResponseEntity.ok("Player Move successfully!");
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

  @PostMapping("/difficulty")
  public ResponseEntity<String> setDifficulty(@RequestBody SettingDifficulty difficult) {
    this.difficulty = difficult.getDifficulty();
    return ResponseEntity.ok("{\"difficulty\": \"" + this.difficulty + "\"}");
  }

  @GetMapping("/getdifficulty")
  public ResponseEntity<String> getDifficulty() {
    return ResponseEntity.ok("{\"difficulty\": \"" + this.difficulty + "\"}");
  }

  public String getDifficultyString() {
    return this.difficulty;
  }

  @GetMapping("/restart")
  public void restartGame() {
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        board[row][col] = "-";
      }
    }
    printBoard();
  }

  @GetMapping("/newGame")
  public void startNewGame() {
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        board[row][col] = "-";
      }
    }
    this.difficulty = "easy";
    printBoard();
  }

  public int evaluate(String b[][]) {
    // Checking for Rows for X or O victory.
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 3; col++) {
        if (b[row][col].equals(b[row][col + 1]) &&
            b[row][col + 1].equals(b[row][col + 2])) {
          if (b[row][col].equals(PLAYER))
            return +10;
          else if (b[row][col].equals(AI))
            return -10;
        }
      }
    }

    // Checking for Columns for X or O victory.
    for (int col = 0; col < 5; col++) {
      for (int row = 0; row < 3; row++) {
        if (b[row][col].equals(b[row + 1][col]) &&
            b[row + 1][col].equals(b[row + 2][col])) {
          if (b[row][col].equals(PLAYER))
            return +10;

          else if (b[row][col].equals(AI))
            return -10;
        }
      }
    }

    // Checking for Diagonals for X or O victory.
    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 3; col++) {
        if (b[row][col].equals(b[row + 1][col + 1]) && b[row + 1][col + 1].equals(b[row + 2][col + 2])) {
          if (b[row][col].equals(PLAYER))
            return +10;
          else if (b[row][col].equals(AI))
            return -10;
        }
      }
    }

    for (int row = 0; row < 3; row++) {
      for (int col = 4; col > 1; col--) {
        if (b[row][col].equals(b[row + 1][col - 1]) && b[row + 1][col - 1].equals(b[row + 2][col - 2])) {
          if (b[row][col].equals(PLAYER))
            return +10;
          else if (b[row][col].equals(AI))
            return -10;
        }
      }
    }

    // Else if none of them have won then return 0
    return 0;
  }

  public int minimax(String board[][], int depth, int maxDepth, int alpha, int beta, boolean isMax,
      double suboptimalProb) {

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
    if (fullBoard() || depth == maxDepth)
      return 0;

    // If this maximizer's move
    if (isMax) {
      int best = Integer.MIN_VALUE;

      // Traverse all cells
      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
          // Check if cell is empty
          if (board[i][j].equals("-")) {
            // Make the move
            board[i][j] = PLAYER;

            // Call alpha-beta recursively and choose
            // the maximum value
            best = Math.max(best, minimax(board, depth + 1, maxDepth, alpha, beta, !isMax, suboptimalProb));
            alpha = Math.max(alpha, best);

            // Undo the move
            board[i][j] = "-";

            // Alpha-beta pruning
            if (beta <= alpha) {
              break;
            }
          }
        }
      }

      if (depth == 0) {
        if (Math.random() < suboptimalProb) {
          best += (int) (Math.random() * 10) - 5;
        }
      }

      return best - depth;
    }

    // If this minimizer's move
    else {
      int best = Integer.MAX_VALUE;

      // Traverse all cells
      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
          // Check if cell is empty
          if (board[i][j].equals("-")) {
            // Make the move
            board[i][j] = AI;

            // Call alpha-beta recursively and choose
            // the minimum value
            best = Math.min(best, minimax(board, depth + 1, maxDepth, alpha, beta, !isMax, suboptimalProb));
            beta = Math.min(beta, best);

            // Undo the move
            board[i][j] = "-";

            // Alpha-beta pruning
            if (beta <= alpha) {
              break;
            }
          }
        }
      }

      if (depth == 0) {
        if (Math.random() < suboptimalProb) {
          best += (int) (Math.random() * 10) - 5;
        }
      }

      return best + depth;
    }
  }

  public Move findBestMove(String board[][], double suboptimalProb) {
    int bestVal = Integer.MIN_VALUE;
    int alpha = Integer.MIN_VALUE;
    int beta = Integer.MAX_VALUE;
    Move bestMove = new Move();
    bestMove.row = -1;
    bestMove.col = -1;

    // Traverse all cells, evaluate minimax function
    // for all empty cells. And return the cell
    // with optimal value.
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        // Check if cell is empty
        if (board[i][j].equals("-")) {
          // Make the move
          board[i][j] = PLAYER;

          // compute evaluation function for this
          // move.
          int moveVal = minimax(board, 0, 5, alpha, beta, false, suboptimalProb);

          // Undo the move
          board[i][j] = "-";

          // If the value of the current move is
          // more than the best value, then update
          // best/

          if (moveVal > bestVal) {
            bestMove.row = i;
            bestMove.col = j;
            bestVal = moveVal;
          }

          // Update alpha value if the moveVal is greater than alpha
          alpha = Math.max(alpha, moveVal);

          // If beta becomes less than or equal to alpha, prune the remaining subtree
          if (beta <= alpha) {
            break;
          }
        }
      }
    }

    return bestMove;
  }

  public void aiMove(double suboptimalProb) {

    Move bestMove = new Move();
    bestMove = findBestMove(board, suboptimalProb);
    board[bestMove.row][bestMove.col] = AI;
    printBoard();

  }

  @GetMapping("/checkWin")
  public int checkWin(String email, String difficulty) {
    // Checking for Rows for X or O victory.
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 3; col++) {
        if (board[row][col].equals(board[row][col + 1]) &&
            board[row][col + 1].equals(board[row][col + 2])) {
          if (board[row][col].equals(PLAYER)) {
            System.out.println("You Win!");
            saveWinLoseDatabase(email, true, difficulty);
            return 1;
          }

          else if (board[row][col].equals(AI)) {
            System.out.println("You Lose!");
            saveWinLoseDatabase(email, false, difficulty);
            return -1;
          }

        }
      }
    }

    // Checking for Columns for X or O victory.
    for (int col = 0; col < 5; col++) {
      for (int row = 0; row < 3; row++) {
        if (board[row][col].equals(board[row + 1][col]) &&
            board[row + 1][col].equals(board[row + 2][col])) {
          if (board[row][col].equals(PLAYER)) {
            System.out.println("You Win!");
            saveWinLoseDatabase(email, true, difficulty);
            return 1;
          }

          else if (board[row][col].equals(AI)) {
            System.out.println("You Lose!");
            saveWinLoseDatabase(email, false, difficulty);
            return -1;
          }
        }
      }
    }

    // Checking for Diagonals for X or O victory.
    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 3; col++) {
        if (board[row][col].equals(board[row + 1][col + 1])
            && board[row + 1][col + 1].equals(board[row + 2][col + 2])) {
          if (board[row][col].equals(PLAYER)) {
            System.out.println("You Win!");
            saveWinLoseDatabase(email, true, difficulty);
            return 1;
          }

          else if (board[row][col].equals(AI)) {
            System.out.println("You Lose!");
            saveWinLoseDatabase(email, false, difficulty);
            return -1;
          }
        }
      }
    }

    for (int row = 0; row < 3; row++) {
      for (int col = 4; col > 1; col--) {
        if (board[row][col].equals(board[row + 1][col - 1])
            && board[row + 1][col - 1].equals(board[row + 2][col - 2])) {
          if (board[row][col].equals(PLAYER)) {
            System.out.println("You Win!");
            saveWinLoseDatabase(email, true, difficulty);
            return 1;
          } else if (board[row][col].equals(AI)) {
            System.out.println("You Lose!");
            saveWinLoseDatabase(email, false, difficulty);
            return -1;
          }
        }
      }
    }

    // Else if none of them have won then return 0
    if (fullBoard()) {
      return 0;
    }

    return 200;

  }

  @PostMapping("/savegame")
  public ResponseEntity<String> saveGame(@RequestBody BoardSaved boardToSave) {
    List<User> user = userRepository.findByEmail(boardToSave.getEmail());
    if (user.isEmpty()) {
      return ResponseEntity.badRequest().body("{\"message\": \"Some error occurs!\"}");
    } else {
      SavedGame saved = new SavedGame();
      saved.setUser(user.get(0));
      saved.setBoard(getBoard());
      saved.setDifficulty(getDifficultyString());
      saved.setGame("ffttt");
      savedGameRepository.save(saved);
      return ResponseEntity.ok("{\"message\": \"Successfully Saved!\"}");
    }
  }

  @PostMapping("/loadgame")
  public ResponseEntity<String> loadGame(@RequestBody BoardLoaded loadedBoard) {
    setBoard(loadedBoard.getBoard());
    this.difficulty = loadedBoard.getDifficulty();

    return ResponseEntity.ok("{\"message\": \"Game Loaded Successfully!\"}");
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

  public void saveWinLoseDatabase(String email, boolean isWin, String difficulty) {
    List<User> user = userRepository.findByEmail(email);
    System.out.println(email);
    if (isWin) {
      if (!user.isEmpty()) {
        List<LeaderBoard> user2 = leaderBoardRepository.findByUser(user.get(0));
        List<LeaderBoard> userByDif = leaderBoardRepository.findByDifficulty(difficulty);
        List<LeaderBoard> userByGame = leaderBoardRepository.findByGame("ffttt");
        List<LeaderBoard> intersect1 = intersection(user2, userByDif);
        List<LeaderBoard> intersected = intersection(intersect1, userByGame);
        if (!intersected.isEmpty()) {
          int winTime = intersected.get(0).getWin();
          int loseTime = intersected.get(0).getLose();
          intersected.get(0).setWin(winTime + 1);
          intersected.get(0).setWinLoseRatio(!(loseTime == 0) ? (winTime + 1.0) / loseTime : winTime + 1.0);
          leaderBoardRepository.save(intersected.get(0));
        } else {
          LeaderBoard leaderboard = new LeaderBoard();
          leaderboard.setUser(user.get(0));
          leaderboard.setUserName(user.get(0).getUserName());
          leaderboard.setDifficulty(difficulty);
          leaderboard.setWin(1);
          leaderboard.setLose(0);
          leaderboard.setGame("ffttt");
          leaderBoardRepository.save(leaderboard);
        }
      }
    } else {
      System.out.println(user);
      if (!user.isEmpty()) {
        List<LeaderBoard> user2 = leaderBoardRepository.findByUser(user.get(0));
        List<LeaderBoard> userByDif = leaderBoardRepository.findByDifficulty(difficulty);
        List<LeaderBoard> userByGame = leaderBoardRepository.findByGame("ffttt");
        List<LeaderBoard> intersect1 = intersection(user2, userByDif);
        List<LeaderBoard> intersected = intersection(intersect1, userByGame);
        if (!intersected.isEmpty()) {
          int winTime = intersected.get(0).getWin();
          int loseTime = intersected.get(0).getLose();
          intersected.get(0).setLose(loseTime + 1);
          intersected.get(0).setWinLoseRatio(winTime / (loseTime + 1.0));
          leaderBoardRepository.save(intersected.get(0));
        } else {
          LeaderBoard leaderboard = new LeaderBoard();
          leaderboard.setUser(user.get(0));
          leaderboard.setUserName(user.get(0).getUserName());
          leaderboard.setDifficulty(difficulty);
          leaderboard.setWin(0);
          leaderboard.setLose(1);
          leaderboard.setGame("ffttt");
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

class BoardSaved {
  private String email;

  public BoardSaved() {

  }

  public BoardSaved(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}

class BoardLoaded {
  private String[][] board;
  private String difficulty;

  public BoardLoaded() {

  }

  public BoardLoaded(String[][] board, String difficulty) {
    this.board = board;
    this.difficulty = difficulty;
  }

  public String[][] getBoard() {
    return board;
  }

  public void setBoard(String[][] board) {
    this.board = board;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
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
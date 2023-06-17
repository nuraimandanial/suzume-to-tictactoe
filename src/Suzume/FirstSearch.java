package Suzume;

import java.util.*;

public class FirstSearch {

  private int targetStations;
  private final int[][] DIRECTIONS = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
  private int pathCount;

  public FirstSearch(int targetStations) {
    this.targetStations = targetStations;
  }

  public int dfsPath(Node[][] map) {
    pathCount = dfs(map, 0, 0, 0);

    return pathCount;
  }

  private int dfs(Node[][] map, int row, int column, int stationVisited) {
    if (row < 0 || row >= map.length || column < 0 || column >= map[0].length ||
        map[row][column].isVisited() || map[row][column].isObstacle() || stationVisited > targetStations) {
      return 0;
    }

    Node node = map[row][column];

    if (node.getValue() == 3 && stationVisited == targetStations) {
      return 1;
    }

    if (stationVisited > targetStations) {
      return 0;
    }

    node.setVisited(true);

    int paths = dfs(map, row - 1, column, stationVisited + (node.getValue() == 2 ? 1 : 0)) +
        dfs(map, row + 1, column, stationVisited + (node.getValue() == 2 ? 1 : 0)) +
        dfs(map, row, column - 1, stationVisited + (node.getValue() == 2 ? 1 : 0)) +
        dfs(map, row, column + 1, stationVisited + (node.getValue() == 2 ? 1 : 0));

    node.setVisited(false);
    return paths;
  }

  public int bfsPath(int[][] map) {
    pathCount = 0;

    Queue<int[]> queue = new LinkedList<>();
    boolean[][] visited = new boolean[map.length][map[0].length];
    visited[0][0] = true;
    queue.add(new int[] { 0, 0, 0, 0 });

    List<boolean[][]> visitedList = new ArrayList<>();
    visitedList.add(visited);

    while (!queue.isEmpty()) {
      int[] current = queue.poll();
      int row = current[0];
      int column = current[1];
      int stationVisited = current[2];
      int index = current[3];
      boolean[][] currentVisited = visitedList.get(index);

      if (map[row][column] == 3 && stationVisited == targetStations) {
        pathCount++;
        continue;
      }

      for (int[] direction : DIRECTIONS) {
        int newRow = row + direction[0];
        int newColumn = column + direction[1];

        if (newRow < 0 || newRow >= map.length || newColumn < 0 || newColumn >= map[0].length
            || map[newRow][newColumn] == 1 || currentVisited[newRow][newColumn]) {
          continue;
        }

        int newStationVisited = stationVisited;
        if (map[newRow][newColumn] == 2 && stationVisited < targetStations) {
          newStationVisited++;
        } else if (map[newRow][newColumn] == 2) {
          continue;
        }

        if (map[newRow][newColumn] == 3 && newStationVisited < targetStations) {
          continue;
        }

        boolean[][] newVisited = new boolean[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
          System.arraycopy(currentVisited[i], 0, newVisited[i], 0, map[0].length);
        }
        newVisited[newRow][newColumn] = true;

        visitedList.add(newVisited);
        int newIndex = visitedList.size() - 1;
        queue.add(new int[] { newRow, newColumn, newStationVisited, newIndex });
      }
    }

    return pathCount;
  }

  public int bdsPath(Node[][] map) {
    Set<Node> visitedForward = new HashSet<>();
    Set<Node> visitedBackward = new HashSet<>();
    Queue<Node> queueForward = new LinkedList<>();
    Queue<Node> queueBackward = new LinkedList<>();

    Node startNode = map[0][0];
    Node endNode = map[map.length - 1][map[0].length - 1];

    queueForward.add(startNode);
    queueBackward.add(endNode);
    visitedForward.add(startNode);
    visitedBackward.add(endNode);

    int pathCount = 0;

    while (!queueForward.isEmpty() && !queueBackward.isEmpty()) {
      if (searchStep(map, queueForward, visitedForward, visitedBackward)) {
        pathCount += getPathCount(visitedForward, visitedBackward);
      }
      if (searchStep(map, queueBackward, visitedBackward, visitedForward)) {
        pathCount += getPathCount(visitedForward, visitedBackward);
      }
    }

    return pathCount;
  }

  private boolean searchStep(Node[][] map, Queue<Node> queue, Set<Node> visited, Set<Node> oppositeVisited) {
    Node currentNode = queue.poll();

    if (oppositeVisited.contains(currentNode)) {
      return true;
    }

    int row = currentNode.getRow();
    int column = currentNode.getColumn();

    if (row > 0) {
      if (!currentNode.getUp().isObstacle() && visited.add(currentNode.getUp())) {
        queue.add(currentNode.getUp());
      }
    }
    if (row < map.length - 1) {
      if (!currentNode.getDown().isObstacle() && visited.add(currentNode.getDown())) {
        queue.add(currentNode.getDown());
      }
    }
    if (column > 0) {
      if (!currentNode.getLeft().isObstacle() && visited.add(currentNode.getLeft())) {
        queue.add(currentNode.getLeft());
      }
    }
    if (column < map[0].length - 1) {
      if (!currentNode.getRight().isObstacle() && visited.add(currentNode.getRight())) {
        queue.add(currentNode.getRight());
      }
    }

    return false;
  }

  private int getPathCount(Set<Node> visitedForward, Set<Node> visitedBackward) {
    int count = 0;

    for (Node node : visitedForward) {
      if (visitedBackward.contains(node)) {
        count++;
      }
    }

    return count;
  }

  int minDepth = Integer.MAX_VALUE;
  int maxDepth = Integer.MIN_VALUE;

  public void iddfsPath(Node[][] map) {
    boolean[][] visited = new boolean[map.length][map[0].length];

    int lock = 0; int limit = dfsPath(map);
    
    for (int i = 50; i < 200; i+=2) {
      int test = dfsDepth(map, 0, 0, 0, i, visited);
      if (test > 0 && lock == 0) {
        minDepth = i;
        lock++;
      }
      int compare = dfsDepth(map, 0, 0, 0, i - 1, visited);
      if (test == limit && test == compare && lock == 1 && minDepth != i - 1) {
        maxDepth = i - 2;
        lock++;
      }
      if (lock == 2) {
        break;
      }
    }
    pathCount = dfsDepth(map, 0, 0, 0, minDepth, visited);
    
    System.out.println("    Iterative Deepening DFS (Shortest Path)  : " + pathCount + " path(s)");
    System.out.println("        Minimum Steps : " + minDepth);
    System.out.println("        Maximum Steps : " + maxDepth);
  }

  private int dfsDepth(Node[][] map, int row, int column, int stations, int depthLimit, boolean[][] visited) {
    Node node = map[row][column];

    if (node.getValue() == 3 && stations == targetStations) {
      return 1;
    }

    if (stations > targetStations) {
      return 0;
    }

    int pathCount = 0;
    node.setVisited(true);
    visited[row][column] = true;

    if (depthLimit > 0) {
      if (row > 0) {
        Node upNode = node.getUp();
        if (!upNode.isObstacle() && !visited[row - 1][column]) {
          pathCount += dfsDepth(map, row - 1, column, stations + (upNode.getValue() == 2 ? 1 : 0),
              depthLimit - 1, visited);
        }
      }

      if (row < map.length - 1) {
        Node downNode = node.getDown();
        if (!downNode.isObstacle() && !visited[row + 1][column]) {
          pathCount += dfsDepth(map, row + 1, column, stations + (downNode.getValue() == 2 ? 1 : 0),
              depthLimit - 1, visited);
        }
      }

      if (column > 0) {
        Node leftNode = node.getLeft();
        if (!leftNode.isObstacle() && !visited[row][column - 1]) {
          pathCount += dfsDepth(map, row, column - 1, stations + (leftNode.getValue() == 2 ? 1 : 0),
              depthLimit - 1, visited);
        }
      }

      if (column < map[0].length - 1) {
        Node rightNode = node.getRight();
        if (!rightNode.isObstacle() && !visited[row][column + 1]) {
          pathCount += dfsDepth(map, row, column + 1, stations + (rightNode.getValue() == 2 ? 1 : 0),
              depthLimit - 1, visited);
        }
      }
    }

    node.setVisited(false);
    visited[row][column] = false;
    return pathCount;
  }
}

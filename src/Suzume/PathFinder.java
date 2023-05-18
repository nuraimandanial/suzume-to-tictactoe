package Suzume;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

/*
 * author @nuraimandanial
 */
public class PathFinder {
    private final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
    // private final int TARGET_STATIONS = 3;
    // private final int COMBINED_TARGET_STATIONS = 4;
    private int target_stations;
    
    private int pathCount;

    private Queue<int[]> queue = new LinkedList<>();

    public int findPaths(int[][] array, int targetStation, boolean type) {
        //System.out.println("Method findPaths(array, " + targetStation + ")");
        pathCount = 0;
        boolean[][] visited = new boolean[array.length][array[0].length];
        target_stations = targetStation;
        if (type) {depthFS(array, visited, 0, 0, 0);}
        else { /*queue.offer(new int[]{0, 0, 0}); */ breadthFS(array);}
        return pathCount;
    }

    public void depthFS(int[][] map, boolean[][] visited, int row, int column, int stationCount) {
        int rows = map.length;
        int columns = map[0].length;

        if (row < 0 || row >= rows || column < 0 || column >= columns || visited[row][column] || map[row][column] == 1) {
            return;
        }

        if (map[row][column] == 3 && stationCount == target_stations) {
            pathCount++;
            return;
        }

        if (map[row][column] == 2) {
            stationCount++;
        }

        visited[row][column] = true;

        for (int[] direction : DIRECTIONS) {
            rows = row + direction[0];
            columns = column + direction[1];
            depthFS(map, visited, rows, columns, stationCount);
        }

        visited[row][column] = false;
    }

    public void breadthFS(int[][] map, boolean[][] visited) {
        int rows = map.length;
        int columns = map[0].length;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int column = current[1];
            int stationCount = current[2];
            
            if (row < 0 || row >= rows || column < 0 || column >= columns || visited[row][column] || map[row][column] == 1) {
                continue;
            }

            if (map[row][column] == 3 && stationCount == target_stations) {
                pathCount++;
            }

            if (map[row][column] == 2) {
                stationCount++;
            }

            visited[row][column] = true;

            for (int[] direction : DIRECTIONS) {
                rows = row + direction[0];
                columns = column + direction[1];
                queue.offer(new int[]{rows, columns, stationCount});
                breadthFS(map, visited);
            }

            visited[row][column] = false;
        }
    }

    class Node {
        int row;
        int column;
        int value;
        boolean visited;

        Node(int row, int column, int value) {
            this.row = row;
            this.column = column;
            this.value = value;
            visited = false;
        }
    }

    public void breadthFS(int[][] map) {
        int rows = map.length;
        int columns = map[0].length;

        Node[][] maps = new Node[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int value = map[i][j];
                maps[i][j] = new Node(i, j, value);
            }
        }

        Queue<Node> queue = new ArrayDeque<>();
        int count = 0;
        
        Node startNode = maps[0][0];
        queue.add(startNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();

            if (currentNode.value == 3) {
                count++;
            }

            if (currentNode.value == 2 && !currentNode.visited) {
                currentNode.visited = true;

                if (countStations(maps) == target_stations) {
                    count++;
                }

                for (int[] direction : DIRECTIONS) {
                    int nextRow = currentNode.row + direction[0];
                    int nextColumn = currentNode.column + direction[1];

                    if (isValidPosition(nextRow, nextColumn, maps)) {
                        Node nextNode = maps[nextRow][nextColumn];
                        queue.add(nextNode);
                    }
                }
            }
        }

        pathCount = count;
    }

    private boolean isValidPosition(int row, int column, Node[][] maps) {
        int rows = maps.length;
        int columns = maps[0].length;

        if (row >= 0 && row < rows && column >= 0 && column < columns) {
            Node node = maps[row][column];
            return (node.value != 1) && !node.visited;
        }

        return false;
    }

    private int countStations(Node[][] maps) {
        int count = 0;

        for (Node[] map : maps) {
            for (Node node : map) {
                if (node.value == 2 && node.visited) {
                    count++;
                }
            }
        }

        return count;
    }
}
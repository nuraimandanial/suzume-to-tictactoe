package Suzume;

import java.util.ArrayDeque;
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

    public int findPaths(int[][] array, int targetStation) {
        //System.out.println("Method findPaths(array, " + targetStation + ")");
        pathCount = 0;
        boolean[][] visited = new boolean[array.length][array[0].length];
        target_stations = targetStation;
        depthFS(array, visited, 0, 0, 0);
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

    public void breadthFS(int[][] map, boolean[][] visited, int row, int column) {
        int rows = map.length;
        int columns = map[0].length;

        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{row, column, 0});
        visited[row][column] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currentRow = current[0];
            int currentCol = current[1];
            int stationCount = current[2];

            stationCount++;

            if (map[currentRow][currentCol] == 3 && stationCount == target_stations) {
                pathCount++;
            } else {
                for (int[] direction : DIRECTIONS) {
                    int newRow = currentRow + direction[0];
                    int newCol = currentCol + direction[1];

                    if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < columns && !visited[newRow][newCol] && map[newRow][newCol] != 1) {
                        visited[newRow][newCol] = true;
                        queue.offer(new int[]{newRow, newCol, stationCount});
                    }
                }
            }
        }
    }
}
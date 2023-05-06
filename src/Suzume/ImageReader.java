package Suzume;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class ImageReader {
    private int[][] bitData;

    public int[][] imageReader(File directory, int i) throws IOException { // Actually can just directly scale down the bits value into range of 0 - 3, but lets make it longer process.
        File imagePath = new File(directory, "image " + i + ".png");
        BufferedImage image = ImageIO.read(imagePath);

        int width = image.getWidth();
        int height = image.getHeight();
        bitData = new int[height][width];

        for (int j = 0; j < height; j++) {
            for (int k = 0; k < width; k++) {
                int pixel = image.getRGB(k, j);
                int grey = (pixel >> 16) & 0xff;
                bitData[j][k] = grey;
            }
        }

        return bitData;
    }

    public int[][] scaleDown(int[][] bitData) {
        for (int i = 0; i < bitData.length; i++) {
            for (int j = 0; j < bitData[i].length; j++) {
                bitData[i][j] = bitData[i][j] / 64;
            }
        }

        return bitData;
    }

    public JFileChooser getPath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setDialogTitle("Select Directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        return chooser;
    }

    public static void main(String[] args) throws IOException {
        PathFinder pf = new PathFinder();

        JFileChooser chooser = new ImageReader().getPath();
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File directory = chooser.getSelectedFile();
            System.out.println(directory);
            
            for (int i = 1; i <= 4; i++) {
                int[][] map = new ImageReader().scaleDown(new ImageReader().imageReader(directory, i));   
                
                System.out.println("\nImage " + i);
                for (int j = 0; j < map.length; j++) {
                    for (int k = 0; k < map[j].length; k++) {
                        System.out.printf("%3d", map[j][k]);
                    }
                    System.out.println();
                }

                System.out.println("Possible path map " + i + " : " + pf.findPaths(map, 3));
            }
        }
    }
}

class PathFinder {
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Possible directions: up, down, left, right
    private static int TARGET_STATIONS = 3; // Number of stations to pass through
    private int pathCount;

    public int findPaths(int[][] array, int targetStation) {
        TARGET_STATIONS = targetStation;
        pathCount = 0;
        boolean[][] visited = new boolean[array.length][array[0].length];
        dfs(array, visited, 0, 0, 0);
        return pathCount;
    }

    public int findPaths(int[][] array, boolean combined) {
        int pathCount = 0;
        boolean[][] visited = new boolean[array.length][array[0].length];

        Queue<int[]> queue = new LinkedList<>();
        int stationCount = 0;

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j] == 2) {
                    queue.offer(new int[]{i, j, stationCount});
                }
            }
        }

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int row = curr[0];
            int col = curr[1];
            int count = curr[2];

            if (count == 4) {
                pathCount++; // Found a valid path passing through exactly 4 stations
                continue;
            }

            for (int[] direction : DIRECTIONS) {
                int newRow = row + direction[0];
                int newCol = col + direction[1];

                if (isValidMove(array, visited, newRow, newCol)) {
                    visited[newRow][newCol] = true;
                    queue.offer(new int[]{newRow, newCol, count + 1});
                }
            }
        }

        return pathCount;
    }

    private boolean isValidMove(int[][] array, boolean[][] visited, int row, int col) {
        return row >= 0 && row < array.length && col >= 0 && col < array[0].length &&
                !visited[row][col] && array[row][col] != 1;
    }

    private void dfs(int[][] array, boolean[][] visited, int row, int col, int stationCount) {
        if (row < 0 || row >= array.length || col < 0 || col >= array[0].length || visited[row][col] || array[row][col] == 1) {
            return; // Out of bounds, already visited, or obstacle encountered
        }

        if (array[row][col] == 3 && stationCount == TARGET_STATIONS) {
            pathCount++; // Found a valid path passing through exactly 3 stations
            //printPath(array, visited);
            return;
        }

        if (array[row][col] == 2) {
            stationCount++; // Increment station count if passing through a station
        }

        visited[row][col] = true;

        for (int[] direction : DIRECTIONS) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            dfs(array, visited, newRow, newCol, stationCount);
        }

        visited[row][col] = false; // Reset visited flag for backtracking
    }
    
    /*
    private void printPath(int[][] array, boolean[][] visited) {
        System.out.println("Path: " + pathCount);
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (visited[i][j]) {
                    System.out.print("X ");
                } else {
                    System.out.print(array[i][j] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    } */
}

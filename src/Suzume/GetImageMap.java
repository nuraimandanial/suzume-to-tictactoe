package Suzume;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

public class GetImageMap {
    private int number;
    private int[][] map;
    private int path;

    public GetImageMap() {}

    public GetImageMap(int number, int[][] map, int path) {
        this.number = number;
        this.map = map;
        this.path = path;
    }

    public int getNumber() {
        return number;
    }

    public int[][] getMap(int number) {
        if (this.number == number) return map;
        else return null;
    }

    public int getPath() {
        return path;
    }
}

class CombineMap {
    public static void main(String[] args) throws IOException {
        ImageReader ir = new ImageReader();
        PathFinder pf = new PathFinder();

        GetImageMap[] combinedMap = new GetImageMap[5];

        JFileChooser chooser = ir.getPath();
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File directory = chooser.getSelectedFile();
            
            for (int i = 1; i <= 4; i++) {
                int[][] map = ir.scaleDown(ir.imageReader(directory, i));
                combinedMap[i] = new GetImageMap(i, map, pf.findPaths(map, 3));

                int[][] getMap = combinedMap[i].getMap(i);
                System.out.println("\nImage Map " + i + " : " + combinedMap[i].getPath() + " possible path.");
                for (int j = 0; j < getMap.length; j++) {
                    for (int k = 0; k < getMap[j].length; k++) 
                        System.out.print(getMap[j][k] + " ");
                    System.out.println();
                }
            }
        }
    }
}

class Test {
    public static void main(String[] args) throws IOException {
        ImageReader ir = new ImageReader();
        PathFinder pf = new PathFinder();

        GetImageMap[] combinedMap = new GetImageMap[5];

        JFileChooser chooser = ir.getPath();
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File directory = chooser.getSelectedFile();

            // Create the combined map array
            int[][] combinedArray = new int[40][20];

            // Define the positions of the maps in the combined array
            int[][] positions = {
                {0, 0},   // Position for map 16
                {20, 0},  // Position for map 41
                {0, 10},  // Position for map 38
                {20, 10}  // Position for map 27
            };

            for (int i = 1; i <= 4; i++) {
                int[][] map = ir.scaleDown(ir.imageReader(directory, i));
                combinedMap[i] = new GetImageMap(i, map, pf.findPaths(map, 3));

                int[][] getMap = combinedMap[i].getMap(i);

                // Get the position for the current map
                int[] position = positions[i - 1];
                int startX = position[0];
                int startY = position[1];

                // Copy the map into the combined array
                for (int j = 0; j < getMap.length; j++) {
                    for (int k = 0; k < getMap[j].length; k++) {
                        // Change the value of 3 to 1
                        if (getMap[j][k] == 3 && combinedMap[i].getPath() != 27) {
                            combinedArray[startX + j][startY + k] = 1;
                        } else {
                            combinedArray[startX + j][startY + k] = getMap[j][k];
                        }
                    }
                }
            }

            // Print the combined array
            System.out.println("\nCombined Map:" + pf.findPaths(combinedArray, 4));
            for (int i = 0; i < combinedArray.length; i++) {
                for (int j = 0; j < combinedArray[i].length; j++) {
                    System.out.print(combinedArray[i][j] + " ");
                }
                System.out.println();
            }
        }
    }
}


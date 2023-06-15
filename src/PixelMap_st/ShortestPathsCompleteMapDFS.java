import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShortestPathsCompleteMapDFS {

    public static void main(String[] args) {
        String[] imagePaths = {
            "C:\\Users\\USER\\Documents\\GitHub\\suzume-to-tictactoe\\bin\\assets\\image 1.png",
            "C:\\Users\\USER\\Documents\\GitHub\\suzume-to-tictactoe\\bin\\assets\\image 2.png",
            "C:\\Users\\USER\\Documents\\GitHub\\suzume-to-tictactoe\\bin\\assets\\image 3.png",
            "C:\\Users\\USER\\Documents\\GitHub\\suzume-to-tictactoe\\bin\\assets\\image 4.png"
        };

        // Create an array to store the 2D arrays for each image
        int[][][] imageArrays = new int[4][][];

        for (int i = 0; i < imagePaths.length; i++) {
            String imagePath = imagePaths[i];

            try {
                // Read the grayscale image
                BufferedImage image = ImageIO.read(new File(imagePath));

                // Get the dimensions of the image
                int width = image.getWidth();
                int height = image.getHeight();

                // Create a 2D array to store the pixel values
                int[][] pixelValues = new int[height][width];

                // Iterate over the image and store the pixel values
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int pixel = image.getRGB(x, y);
                        int grayValue = (pixel & 0xFF);
                        pixelValues[y][x] = grayValue;
                    }
                }

                // Convert the pixel values to the range of 0-3
                int[][] convertedValues = convertToRange(pixelValues);

                // Store the converted values in the array
                imageArrays[i] = convertedValues;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Create the combined image
        int[][] combinedImage = createCombinedImage(imageArrays);

        // Find all the shortest paths
        List<Path> shortestPaths = findAllPaths(combinedImage);

        // Print the combined image
        printArray(combinedImage);

        // Print all the shortest paths
        System.out.println("All Shortest Paths");
        int i = 1;
        for (Path path : shortestPaths) {
            System.out.println("Shortest Paths " + i);
            System.out.println("Steps: " + path.steps);
            System.out.println("Directions: " + path.directions);
            System.out.println();
            i++;
        }
    }

    // Create the combined image by copying the individual images to their respective positions
    public static int[][] createCombinedImage(int[][][] imageArrays) {
        int combinedHeight = 40;
        int combinedWidth = 20;
        int[][] combinedImage = new int[combinedHeight][combinedWidth];

        // Copy the individual images to their respective positions in the combined image
        copyImageToPosition(imageArrays[0], combinedImage, 0, 0);
        copyImageToPosition(imageArrays[1], combinedImage, 10, 0);
        copyImageToPosition(imageArrays[2], combinedImage, 0, 20);
        copyImageToPosition(imageArrays[3], combinedImage, 10, 20);

        // Change all values in the combined image to 1 except for the most bottom right corner
        for (int y = 0; y < combinedImage.length; y++) {
            for (int x = 0; x < combinedImage[0].length; x++) {
                if (y != combinedImage.length - 1 || x != combinedImage[0].length - 1) {
                    if (combinedImage[y][x] == 3) {
                        combinedImage[y][x] = 1;
                    }
                }
            }
        }

        return combinedImage;
    }

    public static void copyImageToPosition(int[][] source, int[][] destination, int offsetX, int offsetY) {
        int sourceHeight = source.length;
        int sourceWidth = source[0].length;

        for (int y = 0; y < sourceHeight; y++) {
            for (int x = 0; x < sourceWidth; x++) {
                destination[offsetY + y][offsetX + x] = source[y][x];
            }
        }
    }

    // Convert the pixel values to the range of 0-3
    public static int[][] convertToRange(int[][] values) {
        int[][] convertedValues = new int[values.length][values[0].length];

        for (int y = 0; y < values.length; y++) {
            for (int x = 0; x < values[0].length; x++) {
                int oldValue = values[y][x];
                int newValue;

                if (oldValue < 64) {
                    newValue = 0;
                } else if (oldValue < 128) {
                    newValue = 1;
                } else if (oldValue < 192) {
                    newValue = 2;
                } else {
                    newValue = 3;
                }

                convertedValues[y][x] = newValue;
            }
        }

        return convertedValues;
    }

    // Find all paths starting from the top left corner
    public static List<Path> findAllPaths(int[][] array) {
        List<Path> allPaths = new ArrayList<>();

        int rows = array.length;
        int cols = array[0].length;

        // Find the starting position: first column and first row with value 0
        int startX = -1;
        int startY = -1;

        // Explore all possible paths starting from the top left corner
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (array[y][x] == 0) {
                    startX = x;
                    startY = y;
                    break;
                }
            }
            if (startX != -1 && startY != -1) {
                break;
            }
        }

        Set<String> shortestPaths = new HashSet<>(); // Set to store the unique shortest paths
        int shortestSteps = findShortestPaths(array, startX, startY, 0, "", Integer.MAX_VALUE, shortestPaths);

        // Add the unique shortest paths to the allPaths list
        for (String path : shortestPaths) {
            allPaths.add(new Path(shortestSteps, path));
        }

        return allPaths;
    }

    // Recursively find the shortest paths starting from a position
    public static int findShortestPaths(int[][] array, int x, int y, int stationCount, String directions, int shortestSteps, Set<String> shortestPaths) {
        int rows = array.length;
        int cols = array[0].length;

        // Base case: If the current position is out of bounds or it is an obstacle (value 1),
        // or we have already passed through more than 4 stations, return the current shortestSteps.
        if (x < 0 || x >= cols || y < 0 || y >= rows || array[y][x] == 1 || stationCount > 4 || array[y][x] == -1) {
            return shortestSteps;
        }

        // If the current position is the final destination (value 3)
        // and we have passed through exactly 4 stations, update the shortestSteps if this path is shorter.
        if (array[y][x] == 3 && stationCount == 4 && x == array[0].length - 1 && y == array.length - 1) {
            if (directions.length() / 2 < shortestSteps) {
                shortestSteps = directions.length() / 2;
                shortestPaths.clear();
                shortestPaths.add(directions);
            } else if (directions.length() / 2 == shortestSteps) {
                shortestPaths.add(directions);
            }
            return shortestSteps;
        }

        // If the current position is a station (value 2), increment the station count.
        if (array[y][x] == 2) {
            stationCount++;
        }

        // Mark the current position as visited by changing its value to -1.
        int originalValue = array[y][x];
        array[y][x] = -1;

        // Explore paths in all four directions: up, down, left, right.
        shortestSteps = findShortestPaths(array, x, y - 1, stationCount, directions + "U ", shortestSteps, shortestPaths); // Up
        shortestSteps = findShortestPaths(array, x, y + 1, stationCount, directions + "D ", shortestSteps, shortestPaths); // Down
        shortestSteps = findShortestPaths(array, x - 1, y, stationCount, directions + "L ", shortestSteps, shortestPaths); // Left
        shortestSteps = findShortestPaths(array, x + 1, y, stationCount, directions + "R ", shortestSteps, shortestPaths); // Right

        // Restore the original value at the current position.
        array[y][x] = originalValue;

        return shortestSteps;
    }

    // Inner class to represent a path
    public static class Path {
        public int steps;
        public String directions;

        public Path(int steps, String directions) {
            this.steps = steps;
            this.directions = directions;
        }
    }

    // Print a 2D array
    public static void printArray(int[][] array) {
        for (int y = 0; y < array.length; y++) {
            System.out.print("[ ");
            for (int x = 0; x < array[0].length; x++) {
                System.out.print(array[y][x] + " ");
            }
            System.out.println("]");
        }
    }
}

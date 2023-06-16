
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.LinkedList;
import java.util.Queue;

public class PossiblePathIndividualImages {

    public static void main(String[] args) {
        String[] imagePaths = {
            "C:\\Users\\HP\\Documents\\GitHub\\suzume-to-tictactoe\\src\\assets\\image 1.png",
            "C:\\Users\\HP\\Documents\\GitHub\\suzume-to-tictactoe\\src\\assets\\image 2.png",
            "C:\\Users\\HP\\Documents\\GitHub\\suzume-to-tictactoe\\src\\assets\\image 3.png",
            "C:\\Users\\HP\\Documents\\GitHub\\suzume-to-tictactoe\\src\\assets\\image 4.png"
        };

        // Process each image individually
        for (String imagePath : imagePaths) {
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

                // Count the possible paths that pass through exactly 3 stations and reach the final destination
                int pathCount = countPaths(convertedValues,0,0,19,9,2);

                // Print the pixel values
                System.out.println("Pixel values for " + imagePath + ":");
                printArray(convertedValues);

                System.out.println("Number of possible paths for " + imagePath + ": " + pathCount);
                System.out.println();

            } catch (IOException e) {
                e.printStackTrace();
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

    public static int countPaths(int[][] pixelMap, int startX, int startY, int destX, int destY, int stationCount) {
        int rows = pixelMap.length;
        int cols = pixelMap[0].length;

        int[] dx = {0, 1, -1, 0};  // Changes in X coordinate for each direction
        int[] dy = {-1, 0, 0, 1};  // Changes in Y coordinate for each direction

        // Create a 3D array to keep track of the path counts for each pixel, station, and station count
        int[][][] pathCounts = new int[rows][cols][stationCount+1];

        // Create a queue for BFS traversal
        Queue<int[]> queue = new LinkedList<>();

        // Initialize the count for the starting pixel and station 0 with station count 0
        pathCounts[startX][startY][0] = 1;

        // Add the starting pixel and station 0 with station count 0 to the queue
        queue.offer(new int[]{startX, startY, 0});

        int numPaths = 0; // Variable to count the number of paths

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int currX = curr[0];
            int currY = curr[1];
            int currStation = curr[2];

        // If the current station count exceeds the maximum station count, break the loop
        if (currStation > stationCount) {
            break;
        }

        if (currX == destX && currY == destY && currStation == stationCount) {
            numPaths += pathCounts[currX][currY][currStation];
            continue;
        }

            // Explore the neighboring pixels
            for (int i = 0; i < 4; i++) {
                int nextX = currX + dx[i];
                int nextY = currY + dy[i];

                // Check if the next pixel is within the bounds of the pixel map
                if (nextX >= 0 && nextX < rows && nextY >= 0 && nextY < cols) {
                    int nextStation = currStation;

                    // If the next pixel is a station and the maximum number of stations hasn't been reached,
                    // increment the station count and add the pixel to the queue with the updated count
                    if (pixelMap[nextX][nextY] == 2 && currStation < stationCount) {
                        nextStation++;
                        queue.offer(new int[]{nextX, nextY, nextStation});
                    }

                    // If the next pixel is not an obstacle and has not been visited by this station before
                    if (pixelMap[nextX][nextY] != 1 && pathCounts[nextX][nextY][nextStation] == 0) {
                        // Update the count for the next pixel and station with the current station count
                        pathCounts[nextX][nextY][nextStation] =
                                pathCounts[currX][currY][currStation];

                        // Add the next pixel and station to the queue
                        queue.offer(new int[]{nextX, nextY, nextStation});
                    }
                    
                    // If the next pixel has been visited by this station before,
                    // add the count to the existing count
                    else if (pixelMap[nextX][nextY] != 1) {
                        pathCounts[nextX][nextY][nextStation] +=
                                pathCounts[currX][currY][currStation];
                    }
                }
            }
        }

        return numPaths;
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







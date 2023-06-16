import java.util.LinkedList;
import java.util.Queue;

public class PixelMapPaths {

    public static int countPaths(int[][] pixelMap, int startX, int startY, int destX, int destY, int stationCount) {
        int rows = pixelMap.length;
        int cols = pixelMap[0].length;

        int[] dx = {-1, 1, 0, 0};  // Changes in X coordinate for each direction
        int[] dy = {0, 0, -1, 1};  // Changes in Y coordinate for each direction

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

            // If we have reached the destination and passed through the required number of stations
            if (currStation == stationCount && currX == destX && currY == destY) {
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

    public static void main(String[] args) {
        // Example usage
        int[][] pixelMap1 = {
            {0, 0, 2, 0, 0, 0, 1, 1, 1, 1},
            {0, 1, 0, 1, 1, 0, 0, 0, 1, 1},
            {0, 1, 0, 1, 1, 0, 1, 0, 1, 1},
            {0, 1, 0, 1, 1, 0, 0, 2, 0, 1},
            {0, 1, 0, 1, 1, 0, 0, 1, 0, 1},
            {0, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {0, 1, 0, 0, 0, 0, 1, 1, 0, 1},
            {0, 1, 0, 1, 1, 1, 1, 1, 0, 1},
            {0, 1, 0, 1, 1, 1, 1, 0, 0, 1},
            {0, 0, 0, 1, 1, 1, 1, 0, 1, 1},
            {0, 1, 1, 1, 1, 0, 0, 0, 0, 1},
            {0, 1, 1, 0, 0, 2, 1, 1, 0, 1},
            {0, 1, 1, 0, 1, 0, 0, 1, 0, 0},
            {0, 1, 1, 0, 1, 1, 0, 1, 1, 0},
            {0, 1, 1, 0, 1, 1, 0, 1, 0, 0},
            {2, 0, 0, 0, 1, 1, 0, 0, 0, 1},
            {0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {0, 1, 1, 1, 1, 1, 1, 1, 0, 3}
        };

        int[][] pixelMap2 = {
            {0, 0, 2, 0, 0, 0, 1, 1, 1, 1},
            {0, 1, 0, 1, 1, 0, 0, 0, 1, 1},
            {0, 1, 0, 1, 1, 0, 1, 0, 1, 1},
            {0, 1, 0, 1, 1, 0, 0, 2, 0, 1},
            {0, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {0, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {0, 1, 0, 0, 0, 0, 1, 1, 0, 1},
            {0, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {0, 1, 0, 1, 1, 2, 1, 0, 0, 1},
            {0, 0, 0, 1, 1, 0, 1, 0, 1, 1},
            {0, 1, 1, 1, 1, 0, 0, 0, 0, 1},
            {0, 1, 1, 0, 0, 0, 1, 2, 0, 1},
            {0, 1, 1, 0, 1, 0, 0, 1, 0, 0},
            {0, 1, 1, 0, 1, 1, 0, 1, 1, 0},
            {0, 1, 1, 0, 1, 1, 0, 1, 0, 0},
            {2, 0, 0, 0, 1, 1, 0, 0, 0, 1},
            {0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 1, 1, 0, 0, 0, 1, 1, 0, 3}
        };

        int[][] pixelMap3 = {
            {0, 0, 2, 0, 0, 0, 1, 1, 1, 1},
            {0, 1, 0, 1, 1, 0, 0, 0, 1, 1},
            {0, 1, 0, 1, 1, 0, 1, 0, 1, 1},
            {0, 1, 0, 1, 1, 0, 0, 2, 0, 1},
            {0, 1, 0, 1, 1, 2, 0, 1, 0, 1},
            {0, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {0, 1, 0, 0, 0, 0, 1, 1, 0, 1},
            {0, 1, 0, 1, 1, 1, 1, 1, 0, 1},
            {0, 1, 0, 1, 1, 1, 1, 0, 0, 1},
            {0, 0, 0, 1, 1, 1, 1, 0, 1, 1},
            {0, 1, 1, 1, 1, 0, 0, 0, 0, 1},
            {0, 1, 1, 0, 0, 2, 1, 1, 0, 1},
            {0, 1, 1, 0, 1, 0, 0, 1, 0, 0},
            {0, 1, 1, 0, 1, 1, 0, 1, 1, 0},
            {0, 1, 1, 0, 1, 1, 0, 1, 0, 0},
            {2, 0, 0, 0, 1, 1, 0, 0, 0, 1},
            {0, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {0, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 0, 3}
        };

        int[][] pixelMap4 = {
            {0, 0, 2, 0, 0, 0, 1, 1, 1, 1},
            {0, 1, 0, 1, 1, 0, 0, 0, 1, 1},
            {0, 1, 0, 1, 1, 0, 1, 0, 1, 1},
            {0, 1, 0, 1, 1, 0, 0, 2, 0, 1},
            {0, 1, 0, 1, 1, 0, 0, 1, 0, 1},
            {0, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {0, 1, 0, 0, 0, 0, 1, 1, 0, 1},
            {0, 1, 0, 1, 1, 1, 1, 1, 0, 1},
            {0, 1, 0, 1, 1, 1, 1, 0, 0, 1},
            {0, 0, 0, 1, 1, 1, 1, 0, 1, 1},
            {0, 1, 1, 1, 1, 0, 0, 0, 0, 1},
            {0, 1, 1, 0, 0, 2, 1, 1, 0, 1},
            {0, 1, 1, 0, 1, 0, 0, 1, 0, 0},
            {0, 1, 1, 0, 1, 1, 0, 1, 1, 0},
            {0, 1, 1, 0, 1, 1, 0, 1, 0, 0},
            {2, 0, 0, 0, 1, 1, 0, 0, 0, 1},
            {0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 1, 1, 0, 0, 0, 1, 1, 0, 3}
        };

        int startX = 0;
        int startY = 0;
        int destX = 19;
        int destY = 9;
        int stationCount = 2;

        int numPaths = countPaths(pixelMap1, startX, startY, destX, destY, stationCount);
        System.out.println("Number of paths 1: " + numPaths);

        int numPaths2 = countPaths(pixelMap2, startX, startY, destX, destY, stationCount);
        System.out.println("Number of paths 2: " + numPaths2);

        int numPaths3 = countPaths(pixelMap3, startX, startY, destX, destY, stationCount);
        System.out.println("Number of paths 3: " + numPaths3);

        int numPaths4 = countPaths(pixelMap4, startX, startY, destX, destY, stationCount);
        System.out.println("Number of paths 4: " + numPaths4);


    }
}

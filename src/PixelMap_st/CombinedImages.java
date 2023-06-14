import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class CombinedImages {

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

        // Create a new big image of size 40x20
        int[][] combinedImage = new int[40][20];

        // Copy image 1 to the top left corner
        copyImageToPosition(imageArrays[0], combinedImage, 0, 0); // Y: row, X: column

        // Copy image 2 to the top right corner
        copyImageToPosition(imageArrays[1], combinedImage, 10, 0);

        // Copy image 3 to the bottom left corner
        copyImageToPosition(imageArrays[2], combinedImage, 0, 20);

        // Copy image 4 to the bottom right corner
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

        // Print the combined image
        printArray(combinedImage);
    }

    // Copy an image to a specified position in the big image
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

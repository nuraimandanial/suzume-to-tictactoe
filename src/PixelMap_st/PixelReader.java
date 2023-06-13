import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PixelReader {

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

        // Print the converted pixel values for each image
        for (int i = 0; i < imageArrays.length; i++) {
            System.out.println("Image " + (i + 1) + ":");
            printArray(imageArrays[i]);
            System.out.println();
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
package Suzume;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class ImageReader {
    public static void main(String[] args) throws IOException {
        for (int i = 1; i <= 4; i++) {
            File imagePath = new File("C:\\Users\\User\\OneDrive\\Documents\\UNIMALAYA\\SEMESTER 2\\WIA1002 - DATA STRUCTURE\\ASSIGNMENT\\Pieces of Map\\Pieces of Map\\image " + i + ".png");
            System.out.println(imagePath);
            BufferedImage image = ImageIO.read(imagePath);

            int width = image.getWidth();
            int height = image.getHeight();
            int[][] bitData = new int[width][height];

            for (int j = 0; j < width; j++) {
                for (int k = 0; k < height; k++) {
                    int pixel = image.getRGB(j, k);
                    int grey = (pixel >> 16) & 0xff;
                    bitData[j][k] = grey;
                }
            }

            System.out.println("Image " + i);
            for (int j = 1; j < bitData.length; j++) {
                System.out.println(Arrays.toString(bitData[j]));
            }
        }
    }
}

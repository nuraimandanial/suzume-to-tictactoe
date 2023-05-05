package Suzume;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class ImageReader {
    public static void main(String[] args) throws IOException {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setDialogTitle("Select Directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File directory = chooser.getSelectedFile();
            
            for (int i = 1; i <= 4; i++) {
                File imagePath = new File(directory, "image " + i + ".png");
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
}

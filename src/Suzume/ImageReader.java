package Suzume;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class ImageReader {
    private int[][] bitData;

    public int[][] imageReader(File directory, int i) throws IOException { // Actually can just directly scale down the bits value into range of 0 - 3, but lets make it longer process.
        File imagePath = new File(directory, "image " + i + ".png");
        BufferedImage image = ImageIO.read(imagePath);

        int width = image.getWidth();
        int height = image.getHeight();
        bitData = new int[width][height];

        for (int j = 0; j < width; j++) {
            for (int k = 0; k < height; k++) {
                int pixel = image.getRGB(j, k);
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

    public static void main(String[] args) throws IOException {
        
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setDialogTitle("Select Directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File directory = chooser.getSelectedFile();
            
            for (int i = 1; i <= 4; i++) {
                int[][] map = new ImageReader().scaleDown(new ImageReader().imageReader(directory, i));   
                
                System.out.println("\nImage " + i);
                for (int j = 0; j < map.length; j++) {
                    for (int k = 0; k < map[j].length; k++) {
                        System.out.printf("%3d,", map[j][k]);
                    }
                    System.out.println();
                }
            }
        }
    }
}

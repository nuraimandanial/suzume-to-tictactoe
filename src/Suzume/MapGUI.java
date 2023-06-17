package Suzume;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

class MapGUI extends JFrame {
  private static final int TILE_SIZE = 18;

  public MapGUI(Node[][] map, File directory) {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Map GUI");

    JPanel panel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < map.length; row++) {
          for (int col = 0; col < map[0].length; col++) {
            int value = map[row][col].getValue();
            BufferedImage image = getImageForValue(value, directory);

            g.drawImage(image, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
          }
        }
      }

      @Override
      public Dimension getPreferredSize() {
        return new Dimension(map[0].length * TILE_SIZE, map.length * TILE_SIZE);
      }
    };

    getContentPane().add(panel);
    pack();
    setLocationRelativeTo(null);
  }

  private BufferedImage getImageForValue(int value, File directory) {
    String imagePath = "\\Texture\\" + value + ".jpg";

    try {
      return ImageIO.read(new File(directory, imagePath));
    } catch (IOException e) {
      System.out.println("Err");
      e.printStackTrace();
      return null;
    }
  }

  public void delay(int second) {
    try {
      Thread.sleep(second * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

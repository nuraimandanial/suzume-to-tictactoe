package Suzume;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class ImageReader {
  private int[][] bitData;
  private int[][][] map = new int[5][20][10];
  private int[][] combined = new int[40][20];

  private File directory;

  private Node[][][] nodeMap = new Node[5][20][10];
  private Node[][] combinedMap = new Node[40][20];

  public ImageReader() {
    JFileChooser chooser = getPath();
    int result = chooser.showOpenDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      directory = chooser.getSelectedFile();

      for (int i = 1; i < 5; i++) {
        try {
          bitData = scaleDown(imageReader(directory, i));
        } catch (IOException e) {
          e.printStackTrace();
        }

        map[i] = bitData;
      }
    }
    saveNode(map);
    combineNode(); combineMap();
  }

  private int[][] imageReader(File directory, int i) throws IOException {
    File imagePath = new File(directory, "image " + i + ".png");
    BufferedImage image = ImageIO.read(imagePath);

    int width = image.getWidth();
    int height = image.getHeight();
    bitData = new int[height][width];

    for (int j = 0; j < height; j++) {
      for (int k = 0; k < width; k++) {
        int pixel = image.getRGB(k, j);
        int grey = (pixel >> 16) & 0xff;
        bitData[j][k] = grey;
      }
    }

    return bitData;
  }

  private int[][] scaleDown(int[][] bitData) {
    for (int i = 0; i < bitData.length; i++) {
      for (int j = 0; j < bitData[i].length; j++) {
        bitData[i][j] = bitData[i][j] / 64;
      }
    }
    return bitData;
  }

  private JFileChooser getPath() {
    JFileChooser chooser = new JFileChooser();
    chooser.setCurrentDirectory(new File("."));
    chooser.setDialogTitle("Select Directory");
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    return chooser;
  }

  private void combineMap() {
    int[][] position = {
        { 0, 0 },
        { 0, 0 },
        { 0, 10 },
        { 20, 0 },
        { 20, 10 }
    };

    for (int i = 1; i < map.length; i++) {
      int startX = position[i][0];
      int startY = position[i][1];

      for (int j = 0; j < map[i].length; j++) {
        for (int k = 0; k < map[i][j].length; k++) {
          if (i != 4 && map[i][j][k] == 3) {
            combined[startX + j][startY + k] = 1;
            continue;
          }
          combined[startX + j][startY + k] = map[i][j][k];
        }
      }
    }
  }

  private void saveNode(int[][][] map) {
    for (int i = 0; i < nodeMap.length; i++) {
      for (int j = 0; j < nodeMap[i].length; j++) {
        for (int k = 0; k < nodeMap[i][j].length; k++) {
          int value = map[i][j][k];
          Node node = new Node(j, k, value);

          nodeMap[i][j][k] = node;

          if (j > 0) {
            Node up = nodeMap[i][j - 1][k];
            node.setUp(up);
            up.setDown(node);
          }
          if (k > 0) {
            Node left = nodeMap[i][j][k - 1];
            node.setLeft(left);
            left.setRight(node);
          }
        }
      }
    }
  }

  private void combineNode() {
    int[][] position = {
        { 0, 0 },
        { 0, 0 },
        { 0, 10 },
        { 20, 0 },
        { 20, 10 }
    };

    for (int i = 1; i < nodeMap.length; i++) {
      int startX = position[i][0];
      int startY = position[i][1];

      for (int j = 0; j < nodeMap[i].length; j++) {
        for (int k = 0; k < nodeMap[i][j].length; k++) {
          int value = nodeMap[i][j][k].getValue();
          Node node;

          if (i != 4 && value == 3) {
            node = new Node(startX + j, startY + k, 1);
          } else {
            node = new Node(startX + j, startY + k, value);
          }

          combinedMap[startX + j][startY + k] = node;

          if (startX + j > 0) {
            Node up = combinedMap[startX + j - 1][startY + k];
            node.setUp(up);
            up.setDown(node);
          }
          if (startY + k > 0) {
            Node left = combinedMap[startX + j][startY + k - 1];
            node.setLeft(left);
            left.setRight(node);
          }
        }
      }
    }
  }

  public File getDirectory() {
    return directory;
  }

  public int[][] getBitData(int i) {
    if (i == 0) {
      return combined;
    }
    return map[i];
  }

  public Node[][] getNodeMap(int i) {
    return nodeMap[i];
  }

  public Node[][] getCombinedMap() {
    return combinedMap;
  }
}

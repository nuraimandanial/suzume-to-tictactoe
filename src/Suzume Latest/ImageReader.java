package Suzume;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

/*
 * author @nuraimandanial
 */
 public class ImageReader {
    private int[][] bitData;
    private int[][][] map = new int[5][20][10];
    private Node[][][] nodeMap;

    public ImageReader() {
        JFileChooser chooser = getPath();
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File directory = chooser.getSelectedFile();

            for (int i = 1; i < 5; i++) {
                try {
                    bitData = scaleDown(imageReader(directory, i));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                map[i] = bitData;
            }
        }

        saveInNode(map);
    }

    public int[][] imageReader(File directory, int i) throws IOException {
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

    public int[][] scaleDown(int[][] bitData) {
        for (int i = 0; i < bitData.length; i++) {
            for (int j = 0; j < bitData[i].length; j++) {
                bitData[i][j] = bitData[i][j] / 64;
            }
        }

        return bitData;
    }

    public JFileChooser getPath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setDialogTitle("Select Directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        return chooser;
    }

    public void saveInNode(int[][][] map) {
        nodeMap = new Node[5][20][10];
        for (int i = 1; i < 5; i++) {
            for (int j = 0; j < map[i].length; j++) {
                for (int k = 0; k < map[i][j].length; k++) {
                    int value = map[i][j][k];
                    Node node = new Node(j, k, value);
                    Queue<String> path = new LinkedList<>();
                    
                    nodeMap[i][j][k] = node;

                    if (j > 0) {
                        Node upNode = nodeMap[i][j - 1][k];
                        node.setUp(upNode);
                        upNode.setDown(node);
                        path.add(new FirstSearch().getDirection(j, k, j - 1, k));
                    }

                    if (k > 0) {
                        Node leftNode = nodeMap[i][j][k - 1];
                        node.setLeft(leftNode);
                        leftNode.setRight(node);
                        path.add(new FirstSearch().getDirection(j, k, j, k - 1));
                    }

                    node.setPath(path);
                }
            }
        }
    }

    public Node[][][] getNodeMap() {
        return nodeMap;
    }
 }
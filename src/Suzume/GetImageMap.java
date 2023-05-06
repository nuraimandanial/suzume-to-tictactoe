package Suzume;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

public class GetImageMap {
    private int number;
    private int[][] map;
    private int path;

    public GetImageMap() {}

    public GetImageMap(int number, int[][] map, int path) {
        this.number = number;
        this.map = map;
        this.path = path;
    }

    public int getNumber() {
        return number;
    }

    public int[][] getMap(int number) {
        if (this.number == number) return map;
        else return null;
    }

    public int getPath() {
        return path;
    }
}

class CombineMap {
    public static void main(String[] args) throws IOException {
        ImageReader ir = new ImageReader();
        PathFinder pf = new PathFinder();

        GetImageMap[] combinedMap = new GetImageMap[5];

        JFileChooser chooser = ir.getPath();
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File directory = chooser.getSelectedFile();
            
            for (int i = 1; i <= 4; i++) {
                int[][] map = ir.scaleDown(ir.imageReader(directory, i));
                combinedMap[i] = new GetImageMap(i, map, pf.findPaths(map));

                int[][] getMap = combinedMap[i].getMap(i);
                System.out.println("\nImage Map " + i + " : " + combinedMap[i].getPath() + " possible path.");
                for (int j = 0; j < getMap.length; j++) {
                    for (int k = 0; k < getMap[j].length; k++) 
                        System.out.print(getMap[j][k] + " ");
                    System.out.println();
                }
            }
        }
    }
}

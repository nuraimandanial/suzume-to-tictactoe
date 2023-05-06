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

    public int[][] getMap() {
        return map;
    }

    public int getPath() {
        return path;
    }
}

class CombineMap {
    public static void main(String[] args) throws IOException {
        ImageReader ir = new ImageReader();
        PathFinder pf = new PathFinder();

        GetImageMap[] combinedMap = new GetImageMap[4];
        int result = ir.getPath().showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File directory = ir.getPath().getSelectedFile();
            
            for (int i = 1; i <= 4; i++) {
                int[][] map = ir.scaleDown(ir.imageReader(directory, i));
                combinedMap[i - 1] = new GetImageMap(i, map, pf.findPaths(map));
                System.out.println(combinedMap[i - 1].getMap());
            }
        }
    }
}

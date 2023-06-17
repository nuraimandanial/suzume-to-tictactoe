package Suzume;

import javax.swing.SwingUtilities;

public class Test {
  public static void main(String[] args) {
    ImageReader imageReader = new ImageReader();

    FirstSearch firstSearch = new FirstSearch(3);
    int[] pathCount = new int[5];

    for (int i = 1; i < pathCount.length; i+=2) {
      int[][] map = imageReader.getBitData(i);
      pathCount[i] = firstSearch.bfsPath(map);
      
      int[][] maps = imageReader.getBitData(i + 1);
      pathCount[i + 1] = firstSearch.bfsPath(maps);

      System.out.print("    Map " + i + " : " + pathCount[i] + " path(s)");
      System.out.println("\t\t   Map " + (i + 1) + " : " + pathCount[i + 1] + " path(s)");
      for (int j = 0; j < map.length; j++) {
        for (int k = 0; k < map[j].length; k++) {
          if (k == 0) {
            System.out.print(" [ ");
          }
          System.out.print(map[j][k] + " ");
        }
        System.out.print("]       ");

        for (int k = 0; k < maps[j].length; k++) {
          if (k == 0) {
            System.out.print(" [ ");
          }
          System.out.print(maps[j][k] + " ");
        }
        System.out.println("]");
      }
      System.out.println();
    }

    Node[][] combinedMap = imageReader.getCombinedMap();
    FirstSearch fs = new FirstSearch(4);
    System.out.println("\t    Combined Map : " + fs.dfsPath(combinedMap) + " path(s)");
    for (int i = 0; i < 40; i++) {
      for (int j = 0; j < 20; j++) {
        if (j == 0) {
          System.out.print("     [ ");
        }
        System.out.print(combinedMap[i][j].getValue() + " ");
      }
      System.out.println("]");
    }
    System.out.println();

    System.out.println("Additional First Search Algorithm :-");
    System.out.println("    Bidirectional Search                     : " + fs.bdsPath(combinedMap) + " path(s)");
    fs.iddfsPath(combinedMap);

    SwingUtilities.invokeLater(() -> {
      MapGUI mapGUI = new MapGUI(combinedMap, imageReader.getDirectory());
      mapGUI.delay(3);
      mapGUI.setVisible(true);
    });
  }
}

// Java program to print all paths of source to
// destination in given graph

import java.io.*;
import java.util.*;

class Graph1 {

    // utility function for printing
// the found path in graph
    static int[][] total = new int[50][20];
    static int count = 0;
    static int countPath =0;
    private static void printPath(List<Integer> path) {
        int size = path.size();
        int i= 0;
        for (Integer v : path) {
//            System.out.print(v + " ");
            total[count][i] = v;
            i++;
        }
        count++;
//        System.out.println();
    }

    // Utility function to check if current
// vertex is already present in path
    private static boolean isNotVisited(int x,
                                        List<Integer> path) {
        int size = path.size();
        for (int i = 0; i < size; i++)
            if (path.get(i) == x)
                return false;

        return true;
    }

    // Utility function for finding paths in graph
// from source to destination
    private static void findpaths(List<List<Integer>> g,
                                  int src, int dst, int v) {

        // Create a queue which stores
        // the paths
        Queue<List<Integer>> queue = new LinkedList<>();

        // Path vector to store the current path
        List<Integer> path = new ArrayList<>();
        path.add(src);
        queue.offer(path);

        while (!queue.isEmpty()) {
            path = queue.poll();
            int last = path.get(path.size() - 1);

            // If last vertex is the desired destination
            // then print the path
            if (last == dst) {
                printPath(path);
            }

            // Traverse to all the nodes connected to
            // current vertex and push new path to queue
            List<Integer> lastNode = g.get(last);
            for (int i = 0; i < lastNode.size(); i++) {
                if (isNotVisited(lastNode.get(i), path)) {
                    List<Integer> newpath = new ArrayList<>(path);
                    newpath.add(lastNode.get(i));
                    queue.offer(newpath);
                }
            }
        }
    }

    // Driver code
    public static void main(String[] args) {
        List<List<Integer>> g = new ArrayList<>();
        int v = 4;
        for (int i = 0; i < 16; i++) {
            g.add(new ArrayList<>());
        }

        // Construct a graph
        g.get(0).add(3);
        g.get(0).add(1);

        g.get(1).add(5);
        g.get(1).add(4);
        g.get(1).add(0);



        g.get(3).add(4);
        g.get(3).add(6);
        g.get(3).add(0);

        g.get(4).add(12);
        g.get(4).add(1);
        g.get(4).add(3);

        g.get(5).add(1);
        g.get(5).add(15);
        g.get(5).add(11);
        g.get(5).add(12);

        g.get(6).add(3);
        g.get(6).add(7);

        g.get(7).add(6);
        g.get(7).add(8);
        g.get(7).add(9);

        g.get(8).add(7);
        g.get(8).add(9);
        g.get(8).add(11);

        g.get(9).add(7);
        g.get(9).add(8);
        g.get(9).add(10);

        g.get(10).add(9);

        g.get(11).add(5);
        g.get(11).add(15);
        g.get(11).add(8);

        g.get(12).add(4);
        g.get(12).add(14);
        g.get(12).add(5);

        g.get(14).add(12);
        g.get(14).add(15);

        g.get(15).add(5);
        g.get(15).add(14);
        g.get(15).add(11);


        int src = 0, dst = 10;


        System.out.println("path from src " + src +
                " to dst " + dst + " are ");

        // Function for finding the paths
        findpaths(g, src, dst, v);


        for(int[] x: Graph1.total) {
            boolean one, six, seven, eleven;
            one = six = seven = eleven = false;
            String s = "";
            for(int y: x) {
                if(y == 1) one = true;
                if(y == 6) six = true;
                if(y == 7) seven = true;
                if(y == 11) eleven = true;

                s += y + " ";
            }
            if((one && six && seven || one && six && eleven || six && seven && eleven || one && seven && eleven)
                    && !(one && seven && eleven && six)){
                System.out.println(s);
                countPath++;
                    }
//            System.out.println();
        }
         System.out.println("Possible paths: " + countPath);
    }
}
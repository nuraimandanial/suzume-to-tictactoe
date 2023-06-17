
import java.util.*;

class Graph4 {

    // utility function for printing
// the found path in graph
    static int[][] total = new int[200][20];
    static int count = 0;
    static int countPath =0 ;

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
                                  int src, int dst) {

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
        for (int i = 0; i < 17; i++) {
            g.add(new ArrayList<>());
        }

        // Construct a graph
        g.get(1).add(2);
        g.get(1).add(10);

        g.get(2).add(3);
        g.get(2).add(9);
   

        g.get(3).add(2);
        g.get(3).add(8);
        g.get(3).add(4);

        g.get(4).add(3);
        g.get(4).add(12);
        g.get(4).add(8);

        g.get(5).add(13);
        g.get(5).add(15);
        g.get(5).add(14);

        g.get(6).add(10);
        g.get(6).add(13);
        g.get(6).add(7);

        g.get(7).add(6);
        g.get(7).add(15);

        g.get(8).add(2);
        g.get(8).add(14);
        g.get(8).add(4);

        g.get(9).add(12);
        g.get(9).add(2);
        g.get(9).add(11);

        g.get(10).add(11);
        g.get(10).add(6);
        g.get(10).add(1);

        g.get(11).add(9);
        g.get(11).add(10);
        g.get(11).add(16);

        g.get(12).add(4);
        g.get(12).add(9);
        g.get(12).add(16);

        g.get(13).add(16);
        g.get(13).add(5);
        g.get(13).add(6);

        g.get(14).add(15);
        g.get(14).add(8);
        g.get(14).add(5);

        g.get(15).add(7);
        g.get(15).add(14);
        g.get(15).add(5);

        
        g.get(16).add(11);
        g.get(16).add(12);
        g.get(16).add(13);

    int src = 1, dst = 7;

        System.out.println("Paths from src " + src +
                " to dst " + dst + " are: ");

        findpaths(g, src, dst);

        for (int[] x : Graph4.total) {
            boolean two, three, four, five, six;
            two = three = four = five = six = false;
            String s = "";
            for (int y : x) {
                if (y == 2) two = true;
                if (y == 3) three = true;
                if (y == 4) four = true;
                if (y == 5) five = true;
                if (y == 6) six = true;

                s += y + " ";
            }
            if (((two && three && four) || (two && four && five) || (two && five && six) ||
                    (three && four && five) || (three && five && six) || (four && five && six) ||
                    (five && two && three) || (six && two && three) || (six && three && four) ||
                    (two && four && six)) &&!((two&&three&&four&&five&&six)||(two&&three&&four&&five)||(two&&three&&four&&six)||(two&&three&&five&&six)||(two&&four&&five&&six)||(three&&four&&five&&six))) {
                System.out.println(s);
                countPath++;
            }
        }
        System.out.println("Possible paths: " + countPath);
    }
}
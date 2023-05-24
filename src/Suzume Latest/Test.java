package Suzume;

import java.awt.Font;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/*
 * author @nuraimandanial
 */
public class Test {
    public static void main(String[] args) {
        ImageReader imageReader = new ImageReader();

        Node[][][] nodeMap = imageReader.getNodeMap();
        FirstSearch fs = new FirstSearch(nodeMap);
        int[] pathCount = new int[5];

        for (int i = 1; i < 5; i++) {
            pathCount[i] = fs.numberOfPath(i, 3, true);
            System.out.println("Map " + i + " : " + pathCount[i] + " possible path (" + fs.numberOfPath(3, nodeMap[i], 1) + ")");
            for (int j = 0; j < 20; j++) {
                for (int k = 0; k < 10; k++) {
                    System.out.print(nodeMap[i][j][k].getValue() + " ");
                }
                System.out.println();
            }
            System.out.println();
        }

        int[][] positions = {
            {0, 0},
            {20, 0},
            {0, 10},
            {20, 10}
        };

        Node[][] combinedMap = new Node[40][20];
        for (int i = 1; i < 5; i++) {
            int[] position = positions[i - 1];
            int sX = position[0];
            int sY = position[1];

            for (int j = 0; j < nodeMap[i].length; j++) {
                for (int k = 0; k < nodeMap[i][j].length; k++) {
                    int value = nodeMap[i][j][k].getValue();
                    Node node;
                    Queue<String> path = new LinkedList<>();
                    
                    if (value == 3 && pathCount[i] != 27) {
                        node = new Node(sX + j, sY + k, 1);
                    }
                    else {
                        node = new Node(sX + j, sY + k, value);
                    }
                    combinedMap[sX + j][sY + k] = node;

                    if (sX + j > 0) {
                        Node upNode = combinedMap[sX + j - 1][sY + k];
                        node.setUp(upNode);
                        upNode.setDown(node);
                    }
                    if (sY + k > 0) {
                        Node leftNode = combinedMap[sX + j][sY + k - 1];
                        node.setLeft(leftNode);
                        leftNode.setRight(node);
                    }

                    if (j > 0 && k > 0) {path.add(fs.getDirection(j, k, sX + j, sY + k));}
                    node.setPath(path);
                }
            }
        }

        System.out.println("Combined Map : ");
        for (int i = 0; i < combinedMap.length; i++) {
            for (int j = 0; j < combinedMap[i].length; j++) {
                System.out.print(combinedMap[i][j].getValue() + " ");
            }
            System.out.println();
        }
        System.out.println("Path Count : ");
        System.out.println("BDS : " + fs.numberOfPath(4, combinedMap, 4));
        System.out.println("BFS (bool) : " + fs.numberOfPath(4, combinedMap, 3));
        System.out.println("BFS (int): " + fs.numberOfPath(4, combinedMap, 2));
        // System.out.println("DFS : " + fs.numberOfPath(4, combinedMap, 1));

        List<List<String>> shortestPath = fs.getShortestPath(4, combinedMap);
        
        if (shortestPath.isEmpty()) {
            System.out.println("No Path Found.");
        }
        else {
            System.out.println("Shortest Path : " + shortestPath.size());

            for (List<String> path : shortestPath) {
                System.out.println("Path : " + path.toString());
                System.out.println("Total Steps : " + path.size());
                System.out.println();
            }
        }
        
        // SwingUtilities.invokeLater(new Runnable() {
        //     @Override
        //     public void run() {
        //         new NodeFrame(combinedMap);
        //     }
        // });
    }
}

class NodeFrame extends JFrame {
    private Node[][] nodes;

    public NodeFrame(Node[][] Nodes) {
        this.nodes = Nodes;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Node Values");

        JPanel panel = new JPanel();

        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                Node node = nodes[i][j];
                JLabel label = new JLabel(getNodeValueWithAdjacent(node));
                label.setFont(new Font("Courier New", Font.PLAIN, 6));
                panel.add(label);
            }
        }

        add(panel);
        pack();
        setVisible(true);
    }

    private String getNodeValueWithAdjacent(Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append(node.getValue()).append(" [");

        if (node.getUp() != null) {
            sb.append(node.getUp().getValue());
        }
        else sb.append(" ");
        sb.append(", ");

        if (node.getDown() != null) {
            sb.append(node.getDown().getValue());
        }
        else sb.append(" ");
        sb.append(", ");

        if (node.getLeft() != null) {
            sb.append(node.getLeft().getValue());
        }
        else sb.append(" ");
        sb.append(", ");

        if (node.getRight() != null) {
            sb.append(node.getRight().getValue());
        }
        else sb.append(" ");

        sb.append("]");

        return sb.toString();
    }

    public static void main(String[] args) {
        // Example usage
        Node[][] nodes = new ImageReader().getNodeMap()[1];

        SwingUtilities.invokeLater(() -> {
            new NodeFrame(nodes);
        });
    }
}

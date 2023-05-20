package Suzume_Ahmed.MyTree;

import Suzume_Ahmed.MyStack.GenericStack;
import Suzume_Ahmed.Pixel;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.Graph;

import java.util.*;
import java.io.IOException;




public class MyTree<E, U> {
    public static int height=0;
    public final NodeA<E, U> head;
    public NodeA<E, U> current;

    public MyTree(E element, U prevEdge) {
        NodeA<E, U> newEle = new NodeA<>(element, prevEdge);
        head = newEle;
        current = head;
        head.element = element;
        height = 0;
    }

    static int i = 0; //to circulate between the next nodes
    //Adds element with edge name
    public void addNodeAuto(E element, U prevEdge) {
        NodeA<E, U> newEle = new NodeA<>(element, prevEdge);


        //create nodes and set current to travrsed element later
        String[] labels = {"next1", "next2", "next3"};
        if(i >= 2) i = 0;

        try {
            switch (labels[i++]) {
                case "next1":
                    current.next1 = newEle;
                    break;
                case "next2":
                    current.next2 = newEle;
                    break;
                case "next3":
                    current.next3 = newEle;
                    break;
            }
        } catch (NullPointerException ignored) {}
    }

   /* public NodeA<E, U> getNode(NodeA<E, U> root, E element) {
        if (root == null) {
            return null;
        }

        Deque<NodeA<E, U>> stack = new LinkedList<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            NodeA<E, U> node = stack.pop();
            if (node.element.equals(element)) {
                return node;
            }
            if (node.next1 != null) {
                stack.push(node.next1);
            }
            if (node.next2 != null) {
                stack.push(node.next2);
            }
            if (node.next3 != null) {
                stack.push(node.next3);
            }
        }
        System.out.println("REsult not found when searched element " + element);
        return null;
    }
*/
    public NodeA<E, U> getNode(NodeA<E, U> root, E element) {
        if (root == null) {
            return null;
        }

        Deque<NodeA<E, U>> stack = new LinkedList<>();
        Set<NodeA<E, U>> visited = new HashSet<>(); // keep track of visited nodes
        stack.push(root);

        while (!stack.isEmpty()) {
            NodeA<E, U> node = stack.pop();
            if (node.element.equals(element)) {
                return node;
            }
            visited.add(node); // mark current node as visited
            if (node.next1 != null && !visited.contains(node.next1)) {
                stack.push(node.next1);
            }
            if (node.next2 != null && !visited.contains(node.next2)) {
                stack.push(node.next2);
            }
            if (node.next3 != null && !visited.contains(node.next3)) {
                stack.push(node.next3);
            }
        }
        System.out.println("Result not found when searched element " + element);
        return null;
    }


    /*public void addPrev(E element, U prevEdge) {
        NodeA<E, U> newEle = new NodeA<>(element, prevEdge);

        //create nodes and set current to travrsed element later
        String[] labels = {"prev1", "prev2"};
        if(i >= 2) i = 0;

        switch (labels[i++]) {
            case "prev1":
                current.prev1 = newEle;
                break;
            case "prev2":
                current.prev2 = newEle;
                break;
        }
    }
*/
    //Enter value and edge you want to traverse
    public void traverse(E e, U prevEdge) {
        //try 5 times
        for (int i = 0; i < 5; i++) {
            try {
                if (current.element != null) {
                    if(i == 0)
                      if (Objects.equals(current.next1.element, e) && Objects.equals(current.next1.prevEdge, prevEdge) && i ==0) {
                        current = current.next1;
                        height++;
                        System.out.println("Traversed into " + e);
                        return;
                    }
                    if(i == 1)
                        if (Objects.equals(current.next2.element, e) && Objects.equals(current.next2.prevEdge, prevEdge) && i ==1) {
                        current = current.next2;
                        height++;
                        current.height++;
                        System.out.println("Traversed into " + e);
                        return;
                    }
                    if(i == 2)
                        if (current.next3.element.equals(e) && Objects.equals(current.next3.prevEdge, prevEdge)) {
                        current = current.next3;
                        height++;
                        current.height++;
                        System.out.println("Traversed into " + e);
                        return;
                    }
                    if(i == 3)
                        if (current.prev1.element.equals(e) && Objects.equals(current.prev1.prevEdge, prevEdge))  {
                        current = current.prev1;
                        height++;
                        current.height++;

                        System.out.println("Traversed into prev1");
                            return;
                    }
                    if(i == 4)
                        if (current.prev2.element.equals(e) && Objects.equals(current.prev2.prevEdge, prevEdge)&& i==4) {
                        current = current.prev2;
                        height++;
                        current.height++;

                        System.out.println("Traversed into prev2");
                        return;
                    } else System.out.println("Edge and element not found");
                }
            } catch (NullPointerException ignored) {
//                System.out.println("Edge and element not found");
            }
        }

    }

    //lays out the edges
    public String[][] nieghbors() {
        String[][] nieghbors = new String[5][];

        E v;
        try {
            if((v=current.next1.element) != null) {
                nieghbors[0] = new String[]{(String) v, (String) current.next1.prevEdge};
            } } catch(NullPointerException ignored) {
        }

        try{
            if((v=current.next2.element) != null) {
                nieghbors[1] = new String[]{(String) v, (String) current.next2.prevEdge};
            } } catch(NullPointerException ignored) {
        }

        try{
            if((v=current.next3.element) != null) {
                nieghbors[2] = new String[]{(String) v, (String) current.next3.prevEdge};
            } } catch(NullPointerException ignored) {
        }

        try{
            if((v=current.prev1.element) != null) {
                nieghbors[3] = new String[]{(String) v, (String) current.prev1.prevEdge};
            } } catch(NullPointerException ignored) {
        }
        try {
            if((v=current.prev2.element) != null) {
                nieghbors[4] = new String[]{(String) v, (String) current.prev2.prevEdge};
            } } catch(NullPointerException ignored) {
        }

        return nieghbors;
    }
    public Graph tree = new SingleGraph("ID01");
    GenericStack<String> visited = new GenericStack<>();
    String x="a"; int iterations = 0; boolean End = false;
    public void displayTree(NodeA node) throws EdgeRejectedException {
        iterations++;
        if (node != null) {
            //if the node is repeating itself
            if(!visited.contains((String) node.element+node.prevEdge)) {
                if (tree.getNode(String.valueOf(node.element)) != null) {
                    double x1 = Math.random(); //random number per recurssion | add unique value if same found
                    x = Double.toString(x1);
                    tree.addNode((String) node.element +node.prevEdge+ x).setAttribute("ui.label", node.element);
                } else {//add node as long as no repetition
                    tree.addNode((String) node.element+node.prevEdge).setAttribute("ui.label", node.element);
                }
            }

//            if(Pixel.node_coordinates.contains((String) node.element))
//                tree.setAttribute("ui.stylesheet", "node#\"" +node.element+node.prevEdge+"\" {fill-color: yellow;}");

            if(node == head) {
                StringBuilder sheet = new StringBuilder();
                for(int i = 1; i<Pixel.node_coordinates.getSize(); i++) {
                    String coor = Pixel.node_coordinates.get(i);
                    //only add style if its a station
                    if(Pixel.tree.getNode(Pixel.tree.head, coor).station)
                      sheet.append("node#\"").append(coor).append(Pixel.tree.getNode(Pixel.tree.head, coor).prevEdge).append("\" {fill-color: yellow;}");
                }

                System.out.println("head accessed");
                tree.setAttribute("ui.stylesheet", "node { text-mode: normal; text-size: 20; text-offset: 25px, 5px; text-alignment: justify;} edge {text-size: 20;} node#\""+node.element+node.prevEdge+"\" {fill-color: red; text-size: 20;}" +
                        "node#\"9,-19RIGHT\" {fill-color: yellow;}" + sheet +
                        "edge {arrow-shape: arrow; arrow-size: 9px,5px; text-alignment: along;}");

            }
            if(!visited.contains((String) node.element+node.prevEdge))
                visited.push((String) node.element+node.prevEdge);

            System.out.print(node.element + " "); // print current node's value
            if(iterations >= 500) return;

            //if(visited.contains((String) node.element)) {
                displayTree(node.next1); // recursively display next1 node
                if (node.next1 != null) {
                    try {
                        Node node1 = tree.getNode((String) node.element+node.prevEdge); //node 1
                        Node node2 = tree.getNode((String) node.next1.element+node.next1.prevEdge); //node 2
                        tree.addEdge((String) node.element + Math.random() + 5, node1, node2, true).setAttribute("ui.label", node.next1.prevEdge);
                    } catch (EdgeRejectedException e) {
                        Node node1 = tree.getNode((String) node.element); //node 1
                        Node node2 = tree.getNode((String) node.next1.element + x); //node 2
                        //System.out.println("Edge not found");
                        try {
                            tree.addEdge((String) node.element + Math.random() + 11, node1, node2, true).setAttribute("ui.label", node.next1.prevEdge);
                        } catch (NullPointerException | EdgeRejectedException ignored) {
                        }
                    }
                }
                displayTree(node.next2); // recursively display next2 node
                if (node.next2 != null) {

                    try {
                        Node node1 = tree.getNode((String) node.element+node.prevEdge); //node 1
                        Node node2 = tree.getNode((String) node.next2.element+node.next2.prevEdge); //node 2
                        tree.addEdge((String) node.element + Math.random() + 1, node1, node2, true).setAttribute("ui.label", node.next2.prevEdge);
                    } catch (EdgeRejectedException e) {
                        Node node1 = tree.getNode((String) node.element); //node 1
                        Node node2 = tree.getNode((String) node.next2.element+node.prevEdge + x); //node 2
                        //System.out.println("Edge not found");

                        try {
                            tree.addEdge((String) node.element + Math.random() + 1, node1, node2, true).setAttribute("ui.label", node.next2.prevEdge);
                        } catch (NullPointerException j) {
                            System.out.println(j + "values of node1 & two are " + node.element + " ");
                        }
                    }
                }
                displayTree(node.next3); // recursively display next3 node
                if (node.next3 != null ) {

                    try {
                        Node node1 = tree.getNode((String) node.element+node.prevEdge); //node 1
                        Node node2 = tree.getNode((String) node.next3.element+node.next3.prevEdge); //node 2
                        tree.addEdge((String) node.element + Math.random() + 2, node1, node2, true).setAttribute("ui.label", node.next3.prevEdge);
                    } catch (EdgeRejectedException e) {
                        Node node1 = tree.getNode((String) node.element); //node 1
                        Node node2 = tree.getNode((String) node.next3.element + x); //node 2
                        //System.out.println("Edge not found");

                        try{
                        tree.addEdge((String) node.element + Math.random() + 2, node1, node2, true).setAttribute("ui.label", node.next3.prevEdge);
                    } catch (NullPointerException j) {
                        System.out.println(j + "values of node1 & two are " + node.element + " ");
                    }
                    }
                }
                displayTree(node.prev1); // recursively display prev1 node
                if (node.prev1 != null) {

                    try {
                        Node node1 = tree.getNode((String) node.element+node.prevEdge); //node 1
                        Node node2 = tree.getNode((String) node.prev1.element+node.prev1.prevEdge); //node 2
                        tree.addEdge((String) node.element + Math.random() + 3, node1, node2, true).setAttribute("ui.label", node.prev1.prevEdge);
                    } catch (EdgeRejectedException e) {
                        Node node1 = tree.getNode((String) node.element); //node 1
                        Node node2 = tree.getNode((String) node.prev1.element + x); //node 2
                        //System.out.println("Edge not found");

                        try {
                        tree.addEdge((String) node.element + Math.random() + 3, node1, node2, true).setAttribute("ui.label", node.prev1.prevEdge);
                    } catch (NullPointerException j) {
                        System.out.println(j + "values of node1 & two are " + node.element + " ");
                    }
                    }
                }
                displayTree(node.prev2); // recursively display prev2 node
                if (node.prev2 != null) {

                    try {
                        Node node1 = tree.getNode((String) node.element+node.prevEdge); //node 1
                        Node node2 = tree.getNode((String) node.prev2.element+node.prev2.prevEdge); //node 2
                        tree.addEdge((String) node.element + Math.random() + 4, node1, node2, true).setAttribute("ui.label", node.prev2.prevEdge);
                    } catch (EdgeRejectedException e) {
                        Node node1 = tree.getNode((String) node.element); //node 1
                        Node node2 = tree.getNode((String) node.prev2.element + x); //node 2
                        //System.out.println("Node not found");
                        try {
                        tree.addEdge((String) node.element + Math.random() + 4, node1, node2, true).setAttribute("ui.label", node.prev2.prevEdge);
                    } catch (NullPointerException j) {
                        System.out.println(j + "values of node1 & two are " + node.element + " ");
                    }
                    }
                }

            System.out.println("End of all nodes");
            System.out.println("visitad coordinates " + visited);
        }
    }

    public int countPaths(NodeA root, E target, NodeA node1, NodeA node2,NodeA node3, NodeA node4) {
        Set<NodeA> visited = new HashSet<>();
        return countPathsRecursive(root, target, node1, node2, node3, node4, visited);
    }

    private int countPathsRecursive(NodeA node, E target, NodeA node1, NodeA node2, NodeA node3, NodeA node4, Set<NodeA> visited) {
        if (node == null)
            return 0;

        // If the current node is the target, return 1
        if (node.element.equals(target))
            return 1;

        // Check if the node has already been visited
        if (visited.contains(node))
            return 0;

        // Add the node to the visited set
        visited.add(node);

        // Check if the current node is one of the three flagged nodes
        boolean isNode1 = (node == node1);
        boolean isNode2 = (node == node2);
        boolean isNode3 = (node == node3);
        boolean isNode4 = (node == node4);

        // Recursively count paths in the next1, next2, and next3 nodes
        int next1Paths = countPathsRecursive(node.next1, target, node1, node2, node3, node4, visited);
        int next2Paths = countPathsRecursive(node.next2, target, node1, node2, node3, node4, visited);
        int next3Paths = countPathsRecursive(node.next3, target, node1, node2, node3, node4, visited);

        // Recursively count paths in the prev1 and prev2 nodes
        int prev1Paths = countPathsRecursive(node.prev1, target, node1, node2, node3, node4, visited);
        int prev2Paths = countPathsRecursive(node.prev2, target, node1, node2, node3, node4, visited);

        // Total paths = paths in next1, next2, and next3 + paths in prev1 and prev2
        int totalPaths = next1Paths + next2Paths + next3Paths + prev1Paths + prev2Paths;

        // If the current node is one of the flagged nodes, increase the count
        int v;
        if((v=((isNode1 ? 1 : 0) + (isNode2 ? 1 : 0) + (isNode3 ? 1 : 0) + (isNode4 ? 1 : 0))) == 3)
            totalPaths++;

        // Remove the node from the visited set
        visited.remove(node);

        return totalPaths;
    }



    public static void main(String[] args) throws IOException {
        MyTree<String, String> tree = new MyTree<>("start", "none");

        //Adding nodes to the master one
        tree.addNodeAuto("20", "left"); //add to first
        tree.addNodeAuto("5", "right"); //add to second
        tree.addNodeAuto("6", "down"); //add to second

        System.out.println("Tree height " + tree.current.height);

        tree.traverse("5", "right");
        tree.current.station = true;
        System.out.println("Tree height " + tree.current.height);

        tree.addNodeAuto("50", "down");
        System.out.println("height is " +  tree.current.height);

        tree.addNodeAuto("100","up");
        tree.addNodeAuto("101", "up");




        tree.traverse("100", "up");
        System.out.println("height is " + MyTree.height);
        tree.addNodeAuto("12", "newone");
        tree.traverse("12", "newone");

        tree.current.prev1 = tree.head;

        tree.addNodeAuto("120", "down");
        tree.traverse("120", "down");



        //Print the tree
        tree.displayTree(tree.head);

        System.setProperty("org.graphstream.ui", "swing");
        tree.tree.display();


        //Find paths

//        System.out.println("\nOne path" + tree.findPath(tree.head, "120"));
//        System.out.println("Total paths from start to target: " +tree.countPaths(tree.head, "120"));


    }
}
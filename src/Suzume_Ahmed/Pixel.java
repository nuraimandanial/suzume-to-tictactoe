package Suzume_Ahmed;

import Suzume_Ahmed.MyStack.GenericStack;
import Suzume_Ahmed.MyTree.MyTree;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import javax.imageio.ImageIO;

import static java.lang.Math.abs;

public class Pixel {
    BufferedImage image;
    int width, height;
    int[][] image_arr; //Stores the array of the map while scaling down the values

    //Reads the Suzume Maps
    Pixel(String filename) {
        try {
            image = ImageIO.read(new File(filename));
            width = image.getWidth();
            height = image.getHeight();

            image_arr = new int[height][width]; //20 by 10

            for(int i=0; i<height; i++) {
                for(int j=0; j<width; j++) {
                    Color c = new Color(image.getRGB(j, i));
                    image_arr[i][j] = c.getGreen()/64; //I need create an array for different images
                }
            }

        } catch (Exception ignored) {}
    }

    //note: change to try & catch blocks
    public static void main(String[] args) throws IOException {
        Pixel image1 = new Pixel("C:\\Users\\A7M1ST\\Desktop\\suzume-to-tictactoe\\src\\Suzume_Ahmed\\map\\image 1.png");
        Pixel image2 = new Pixel("C:\\Users\\A7M1ST\\Desktop\\suzume-to-tictactoe\\src\\Suzume_Ahmed\\map\\image 2.png");
        Pixel image3 = new Pixel("C:\\Users\\A7M1ST\\Desktop\\suzume-to-tictactoe\\src\\Suzume_Ahmed\\map\\image 3.png");
        Pixel image4 = new Pixel("C:\\Users\\A7M1ST\\Desktop\\suzume-to-tictactoe\\src\\Suzume_Ahmed\\map\\image 4.png");
        int[][] combined_map = new int[40][20];

        int[][] image_arr = {
                {0, 0, 0, 0, 2, 0, 0, 0, 1, 1, 1, 1},
                {0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1},
                {0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 3}
        };

        for(int i=0; i<20; i++) {
            for(int j=0; j<10; j++) {
                combined_map[i][j] = image1.image_arr[i][j] == 3 ? 1: image1.image_arr[i][j];
                combined_map[i][j+10] = image2.image_arr[i][j] == 3 ? 1: image2.image_arr[i][j];

                combined_map[i+20][j] = image3.image_arr[i][j] == 3 ? 1: image3.image_arr[i][j];
                combined_map[i+20][j+10] = image4.image_arr[i][j];

            }

        }

        //Display the Image Array
        for(int[] x: combined_map) {
            for(int y: x) System.out.printf("%2s ", y);
            System.out.println();
        }


        // //Find the paths taken
        // for(boolean[] x: pathFinder.arr_tree) {
        //     for(boolean y: x) {
        //         System.out.print((y? 0: 1) + "  ");
        //     }
        //     System.out.println("");
        // }

        // pathFinder(image2.image_arr);

        pathFinder(combined_map); //combined map coordinates



        System.out.println("The coordinates are " + coordinates);
        System.out.println("the stack is " + path);

        System.out.println("END OF PROGRAM");

        //Display the tree in store
        tree.displayTree(tree.head);
        System.setProperty("org.graphstream.ui", "swing");
        tree.tree.display();






        System.out.println(tree.countPaths(tree.head, "19,-39", null, null, null, null));
        System.out.println("Total paths from start to target: " +tree.countPaths(tree.head, "9, -19", tree.head.next2, tree.getNode(tree.head, "7,-3"), tree.getNode(tree.head, "5,-11"), tree.getNode(tree.head, "0,-15")));

//        System.out.println("Paths is ");
//        System.out.println("Number of paths is " + new pathFinder().findPaths(image1.image_arr, 3));
//        System.out.println("Number of paths is " + new pathFinder().findPaths2(combined_map, 4));

    }

    //DIRECTIONS
    enum DIR {
        LEFT, RIGHT, UP, DOWN
    }

    static int x =0;
    static int y =0;
    static int level = 0;
    static GenericStack<DIR> path = new GenericStack<>();
    public static MyTree<String, String> tree = new MyTree<>("0,0", "none");
    static ArrayList<String> coordinates = new ArrayList<>();
    public static GenericStack<String> node_coordinates = new GenericStack<>();
    static GenericStack<String> connected_nodes = new GenericStack<>();
    static boolean first = true; //if its first node, don't create a new node

    public static void pathFinder(int[][] map) {
        if(first) { //if its first add, 0,0 as coordinates !!ADD ONCE ONLY
            coordinates.add("0,0");
            //connected_nodes.push("0,0");
            node_coordinates.push("0,0");
            first = false;
        }
        //X and Y values for the recurssed method | holder previous value
         int x_holder = x;
         int y_holder = y;


        //moveTos the axis into new location
        boolean l, r, u, d;
        l = false; r = false; u = false; d = false;
        //check the surrounding
        //qtns: what if its going back and forth


        int v;
        {
            if ((v = moveTo(DIR.RIGHT, map)) != 1 && v != -10) {
                r = true;
                moveTo(DIR.LEFT, map);
            } else if (v == 1) moveTo(DIR.LEFT, map);

            if ((v = moveTo(DIR.LEFT, map)) != 1 && v != -10) {
                l = true;
                moveTo(DIR.RIGHT, map);
            } else if (v == 1) moveTo(DIR.RIGHT, map); //getback

            if ((v = moveTo(DIR.UP, map)) != 1 && v != -10) {
                u = true;
                moveTo(DIR.DOWN, map);
            } else if (v == 1) moveTo(DIR.DOWN, map);

            if ((v = moveTo(DIR.DOWN, map)) != 1 && v != -10) {
                d = true;
                moveTo(DIR.UP, map); //moveTo it back up
            } else if (v == 1) moveTo(DIR.UP, map);
        }


        //Look at previous moveToment & don't consider the way back as new way
        if(Objects.equals(path.peek(), DIR.RIGHT)) l = false;
        if(Objects.equals(path.peek(), DIR.LEFT)) r = false;
        if(Objects.equals(path.peek(), DIR.UP)) d = false;
        if(Objects.equals(path.peek(), DIR.DOWN)) u = false;


//            System.out.println("Coordinates are " + coordinates);
//            System.out.println("Nodes are " + node_coordinates);

        boolean branching = true, gate = false;
        //repeated resetter to previous information when getting back from recursion, not going in
        if(node_coordinates.contains(x_holder+","+y_holder)) { //reset the coordinates ||I need to get the latest direction before coordinate
            x = x_holder;
            y = y_holder;
            gate = true;
            tree.current = tree.getNode(tree.head, x+","+y); //if I am back in the node, then get back to that node
        }
        String direction; //Stores the Direction taken, makes code readable
        if (r) {
            if(u|d|l && (level != 0) && branching) {//if there is other path
                    node_coordinates.push(x + "," + y);
                    //now I need a direction from node IF resetted...
                    if(gate)
                        direction =  path.get(coordinates.indexOf(x_holder+","+y_holder)).name();
                    else direction = path.peek().name();


                    tree.addNodeAuto(x + "," + y, direction);
                    tree.traverse(x + "," + y, direction);
                    if(map[abs(y)][x] == 2) //if current value is true; make it a station
                        tree.current.station = true;
                    branching = false;
                }
                //on each moveTo call the method
//                boolean old = false;
                while((v=moveTo(DIR.RIGHT, map)) != 1 && v!=-10 && v!=3) {
                    //on each step && node creatin
                    if(!coordinates.contains(x+","+y)) {
                        path.push(DIR.RIGHT);
                        coordinates.add(x + "," + y);
                        level++;
                        pathFinder(map);
                        if(node_coordinates.contains(x_holder+","+y_holder)) { //reset the coordinates ||I need to get the latest direction before coordinate
                            x = x_holder;
                            y = y_holder;
                            gate = true;
                            tree.current = tree.getNode(tree.head, x+","+y); //if I am back in the node, then get back to that node
                        }
                    } else if(node_coordinates.contains(x+","+y) && !connected_nodes.contains(x+","+y) && !(tree.current.element.equals(x+","+y))){ //as long as its a node & it isnt connected before
                        System.out.println("Gone into else while "+ x + ", " +y);
                        connected_nodes.push(x+","+y);
                        tree.current.prev1 = tree.getNode(tree.head, x+","+y); //prev1 from new node
                        tree.current.prev1.next3 = tree.current;   //from old node
                        tree.current = tree.current.prev1;
                        break;
                    }

                } if(v == 1) moveTo(DIR.LEFT, map); //if its 1, moveTo back

                if(v == 3) { //if last location reached
                    coordinates.add(x + "," + y); //add the coordinantes
                    path.push(DIR.RIGHT); //this is always to the right
                    tree.addNodeAuto(x+","+y, "RIGHT");
                }

//                if((node=tree.getNode(tree.head, x_holder+","+y_holder)) != null)
//                    tree.current = node;
        }

        //if the x & y coordinates equals node then set position it to a node coordinate
        //x_h & y_h => contains the changing values per level
        //x & y => is the current location of player.
        if (l) {
            if(u|d|r && (level != 0) && branching) {//if there is other path
                node_coordinates.push(x + "," + y);
                if(gate)
                    direction =  path.get(coordinates.indexOf(x_holder+","+y_holder)).name();
                else direction = path.peek().name();
                tree.addNodeAuto(x + "," + y, direction);
                tree.traverse(x + "," + y, direction);
                if(map[abs(y)][x] == 2) //if current value is true; make it a station
                    tree.current.station = true;
                branching = false;
            }
            //on each moveTo call the method
            while((v=moveTo(DIR.LEFT, map)) != 1 && v!=-10 && v!=3) {
                //on each step && node creatin
                if(!coordinates.contains(x+","+y)) {
                    path.push(DIR.LEFT);
                    coordinates.add(x + "," + y);
                    level++;
                    pathFinder(map);
                    if(node_coordinates.contains(x_holder+","+y_holder)) { //reset the coordinates ||I need to get the latest direction before coordinate
                        x = x_holder;
                        y = y_holder;
                        gate = true;
                        tree.current = tree.getNode(tree.head, x+","+y); //if I am back in the node, then get back to that node
                    }

                }
                else if(node_coordinates.contains(x+","+y)&& !connected_nodes.contains(x+","+y) && !(tree.current.element.equals(x+","+y))){
                    connected_nodes.push(x+","+y);

                    System.out.println("Gone into else while "+ x + ", " +y);
                    tree.current.prev1 = tree.getNode(tree.head, x+","+y); //prev1 from new node
                    tree.current.prev1.next3 = tree.current;   //from old node
                    tree.current = tree.current.prev1;
                    break;
                }

            } if(v == 1) moveTo(DIR.RIGHT, map); //if its 1, moveTo back

            if(v == 3) { //if last location reached
                coordinates.add(x + "," + y); //add the coordinantes
                path.push(DIR.LEFT);
                tree.addNodeAuto(x + ","+y, "LEFT");
            }

//                if((node=tree.getNode(tree.head, node_coordinates.peek())) != null)
//                    tree.current = node;
        }

        //
        if (u) {
            if((r|l|d) && (level != 0) && branching) {//if there is other path
                node_coordinates.push(x + "," + y);
                if(gate)
                    direction =  path.get(coordinates.indexOf(x_holder+","+y_holder)).name();
                else
                    direction = path.peek().name();

                tree.addNodeAuto(x + "," + y, direction);
                tree.traverse(x + "," + y, direction);
                if(map[abs(y)][x] == 2) //if current value is true; make it a station
                    tree.current.station = true;
                branching = false;
            }
            //on each moveTo call the method
            while((v=moveTo(DIR.UP, map)) != 1 && v!=-10 && v!=3) {
                //on each step && node creatin
                if(!coordinates.contains(x+","+y)) {
                    path.push(DIR.UP);
                    coordinates.add(x + "," + y);
                    level++;
                    pathFinder(map);
                    if(node_coordinates.contains(x_holder+","+y_holder)) { //reset the coordinates ||I need to get the latest direction before coordinate
                        x = x_holder;
                        y = y_holder;
                        gate = true;
                        tree.current = tree.getNode(tree.head, x+","+y); //if I am back in the node, then get back to that node
                    }
                }
                else if(node_coordinates.contains(x+","+y) && !connected_nodes.contains(x+","+y) && !(tree.current.element.equals(x+","+y))) {
                    connected_nodes.push(x+","+y);
                    System.out.println("Gone into else while "+ x + ", " +y);
                    tree.current.prev1 = tree.getNode(tree.head, x+","+y); //prev1 from new node
                    tree.current.prev1.next3 = tree.current;   //from old node
                    tree.current = tree.current.prev1;
                    break;
                }

            } if(v == 1) moveTo(DIR.DOWN, map); //if its 1, moveTo back

            if(v == 3) { //if last location reached
                coordinates.add(x + "," + y); //add the coordinantes
                path.push(DIR.UP);
                tree.addNodeAuto(x + ","+y, "UP");
            }

//                if((node=tree.getNode(tree.head, x_holder+","+y_holder)) != null)
//                    tree.current = node;
        }


        if (d) {
            if((r|l|u) && (level != 0) && branching) {//if there is other path
                node_coordinates.push(x + "," + y);
                if(gate)
                    direction =  path.get(coordinates.indexOf(x_holder+","+y_holder)).name();
                else direction = path.peek().name();

                tree.addNodeAuto(x + "," + y, direction);
                tree.traverse(x + "," + y, direction);
                if(map[abs(y)][x] == 2) //if current value is true; make it a station
                  tree.current.station = true;

                branching = false;
            }
            //on each moveTo call the method
            while((v=moveTo(DIR.DOWN, map)) != 1 && v!=-10 && v!=3) {
                //on each step && node creatin
                if(!coordinates.contains(x+","+y)) {
                    path.push(DIR.DOWN);
                    coordinates.add(x + "," + y);
                    level++;
                    pathFinder(map);
                    if(node_coordinates.contains(x_holder+","+y_holder)) { //reset the coordinates ||I need to get the latest direction before coordinate
                        x = x_holder;
                        y = y_holder;
                        gate = true;
                        tree.current = tree.getNode(tree.head, x+","+y); //if I am back in the node, then get back to that node
                    }

                }
                else if(node_coordinates.contains(x+","+y) && !connected_nodes.contains(x+","+y) && !(tree.current.element.equals(x+","+y))){ //if its a node
                    connected_nodes.push(x+","+y);
                    System.out.println("Gonex c into else while "+ x + ", " +y);
                    tree.current.prev1 = tree.getNode(tree.head, x+","+y); //prev1 from new node
                    tree.current.prev1.next3 = tree.current;   //from old node
                    tree.current = tree.current.prev1;
                    break;
                }

            } if(v == 1) moveTo(DIR.UP, map); //if its 1, moveTo back

            if(v == 3) { //if last location reached
                coordinates.add(x + "," + y); //add the coordinantes
                path.push(DIR.DOWN);
                tree.addNodeAuto(x + ","+y, "DOWN");
            }

//                if((node=tree.getNode(tree.head, node_coordinates.peek())) != null)
//                    tree.current = node;
        }
        level--;

    }



//        moveS THE POINTER & ADDS TO GLOBAL STACK
//    returns current position value if found, -1 if boundary
//    PARAMETER: the direction values
//    PURPOSE: moves the current position of character then returns the new position value
//    Example: move(RIGHT) moveTos x to right, and returns the value at the right
    public static int moveTo(DIR direction, int[][] map) {
        if(x >= 0 && x <= map[1].length && y >= -39 && y <= 0) //While x & y are within range
            switch(direction) {
                case LEFT: {
                    if(x != 0) {
                        //path.push(DIR.LEFT);
                        return map[abs(y)][--x];
                    } //else System.out.println("left Boundry reached");
                    break;
                }
                case RIGHT: {
                    if(x != map[1].length-1) {
                       // path.push(DIR.RIGHT);
                        return map[abs(y)][++x];
                    } //else System.out.println("right Boundry reached");
                    break;
                }
                case UP: {
                    if(y != 0) {
                       // path.push(DIR.UP);
                        return map[abs(++y)][x];
                    }// else
                       // System.out.println("upper Boundry reached");
                    break;
                }
                case DOWN: {
                    if(y != -39) {
                       // path.push(DIR.DOWN);
                        return map[abs(--y)][x];
                    } //else
                      // System.out.println("down Boundry reached");
                    break;
                }
            }

        // path.push(null);
        return -10;
    }

}
package Suzume_Latest;

/*
 * author @nuraimandanial
 */
public class Node {
    private int row, column;
    private int value, stationVisited;
    private boolean obstacle, isVisited;
    private Node left, right, up, down;
    private String path;

    public Node(int x, int y, int value) {
        this.row = x;
        this.column = y;
        this.value = value; this.stationVisited = 0;
        this.obstacle = (value == 1); this.isVisited = false;
        this.path = "";
    }    

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public void setUp(Node up) {
        this.up = up;
    }

    public void setDown(Node down) {
        this.down = down;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isObstacle() {
        return obstacle;
    }

    public int getValue() {
        return value;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public Node getUp() {
        return up;
    }

    public Node getDown() {
        return down;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    public int getStationVisited() {
        return stationVisited;
    }

    public void setStationVisited(int stationVisited) {
        this.stationVisited = stationVisited;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

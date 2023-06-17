package Suzume;

public class Node {
  private int row, column;
  private int value;
  private String path;
  private boolean isObstacle, isVisited;
  private int stationVisited;

  private Node left, right, up, down;

  public Node(int row, int column, int value) {
    this.row = row;
    this.column = column;
    this.value = value; this.stationVisited = 0;
    this.isObstacle = (value == 1); this.isVisited = false;
    this.path = "";
    left = right = up = down = null;
  }

  public Node[] getNeighbours() {
    return new Node[] { this.left, this.right, this.up, this.down };
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
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

  public Node getLeft() {
    return left;
  }

  public void setLeft(Node left) {
    this.left = left;
  }

  public Node getRight() {
    return right;
  }

  public void setRight(Node right) {
    this.right = right;
  }

  public Node getUp() {
    return up;
  }

  public void setUp(Node up) {
    this.up = up;
  }

  public Node getDown() {
    return down;
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

  public int getValue() {
    return value;
  }

  public boolean isObstacle() {
    return isObstacle;
  }
}

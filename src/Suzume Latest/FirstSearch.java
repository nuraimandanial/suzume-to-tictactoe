package Suzume;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class FirstSearch {
    private int pathCount;
    private Node[][][] nodeMap;
    private int targetStations;

    private boolean[][][] visited;
    private int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public FirstSearch() {
    }

    public FirstSearch(Node[][][] nodeMap) {
        this.nodeMap = nodeMap;
        this.visited = new boolean[nodeMap.length][nodeMap[0].length][nodeMap[0][0].length];
    }

    public int numberOfPath(int i, int stations, boolean search) {
        this.targetStations = stations;
        pathCount = 0;
        if (search) dfs(nodeMap[i], 0, 0, 0);
        else { bfs(nodeMap[i], visited[i]);}
        return pathCount;
    }

    public int numberOfPath(int stations, Node[][] map, int search) {
        this.targetStations = stations;
        pathCount = 0;
        if (search == 1) dfs(map, 0, 0, 0);
        else if (search == 2) { bfs(map, new int[map.length][map[0].length]); }
        else if (search == 3) { bfs(map, new boolean[map.length][map[0].length]); }
        else if (search == 4) { bds(map); }
        return pathCount;
    }

    public List<List<String>> getShortestPath (int stations, Node[][] map) {
        this.targetStations = stations;
        pathCount = 0;
        List<List<String>> shortestPath = new ArrayList<>();
        findShortestPath(shortestPath, map, new int[map.length][map[0].length]);
        return shortestPath;
    }

    // private void dfs(Node[][] map, int row, int column, int stationVisited) {
    //     Node node = map[row][column];

    //     if (node.getValue() == 3 && stationVisited == targetStations) {
    //         pathCount++;
    //     }

    //     if (node.getValue() == 2) {
    //         stationVisited++;
    //     }

    //     node.setVisited(true);

    //     if (row > 0 && !node.getUp().isObstacle() && !node.getUp().isVisited()) {
    //         dfs(map, row - 1, column, stationVisited);
    //     }
    //     if (row < map.length - 1 && !node.getDown().isObstacle() && !node.getDown().isVisited()) {
    //         dfs(map, row + 1, column, stationVisited);
    //     }
    //     if (column > 0 && !node.getLeft().isObstacle() && !node.getLeft().isVisited()) {
    //         dfs(map, row, column - 1, stationVisited);
    //     }
    //     if (column < map[0].length - 1 && !node.getRight().isObstacle() && !node.getRight().isVisited()) {
    //         dfs(map, row, column + 1, stationVisited);
    //     }        

    //     node.setVisited(false);
    // }

    private void dfs(Node[][] map, int row, int column, int stationVisited) {
        Node node = map[row][column];
    
        if (node.getValue() == 3 && stationVisited == targetStations) {
            pathCount++;
            return; // No need to explore further if the target is found
        }
    
        if (node.getValue() == 2) {
            stationVisited++;
        }
    
        node.setVisited(true);
    
        if (row > 0) {
            Node upNode = node.getUp();
            if (!upNode.isObstacle() && !upNode.isVisited()) {
                dfs(map, row - 1, column, stationVisited);
            }
        }
    
        if (row < map.length - 1) {
            Node downNode = node.getDown();
            if (!downNode.isObstacle() && !downNode.isVisited()) {
                dfs(map, row + 1, column, stationVisited);
            }
        }
    
        if (column > 0) {
            Node leftNode = node.getLeft();
            if (!leftNode.isObstacle() && !leftNode.isVisited()) {
                dfs(map, row, column - 1, stationVisited);
            }
        }
    
        if (column < map[0].length - 1) {
            Node rightNode = node.getRight();
            if (!rightNode.isObstacle() && !rightNode.isVisited()) {
                dfs(map, row, column + 1, stationVisited);
            }
        }
    
        node.setVisited(false);
    }    

    private void bfs(Node[][] map, boolean[][] visited) {
        Queue<Node> queue = new LinkedList<>();
        queue.offer(map[0][0]);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            int cX = node.getRow();
            int cY = node.getColumn();
            int stationVisited = node.getStationVisited();

            if (node.getValue() == 3 && stationVisited == targetStations) {
                pathCount++;
                stationVisited = 0;
            }
        
            visited[cX][cY] = true;

            for (int[] direction : directions) {
                int nX = cX + direction[0];
                int nY = cY + direction[1];

                if (isValid(map, nX, nY) && !visited[nX][nY] && !map[nX][nY].isObstacle()) {
                    Node nextNode = map[nX][nY];
                    nextNode.setStationVisited(stationVisited + (nextNode.getValue() == 2 ? 1 : 0));
                    queue.offer(nextNode);
                }
            }
        }
    }

    private void bfs(Node[][] map, int[][] isVisited) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(map[0][0]);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            int row = currentNode.getRow();
            int column = currentNode.getColumn();
            int stationVisited = currentNode.getStationVisited();

            if (currentNode.getValue() == 3 && stationVisited == targetStations) {
                pathCount++;
                stationVisited = 0;
            }

            isVisited[row][column] = 1;

            for (int[] direction : directions) {
                int nX = row + direction[0];
                int nY = column + direction[1];

                if (isValid(map, nX, nY) && isVisited[nX][nY] == 0 && !map[nX][nY].isObstacle()) {
                    Node nextNode = map[nX][nY];
                    
                    if (isVisited[nX][nY] == 0 && !nextNode.isObstacle()) {
                        queue.add(nextNode);
                        nextNode.setStationVisited(stationVisited + (nextNode.getValue() == 2 ? 1 : 0));
                    }
                }
            }
        }
    }

    private boolean isValid(Node[][] map, int x, int y) {
        return x >= 0 && x < map.length && y >= 0 && y < map[0].length;
    }

    private void bds(Node[][] map) {
        Set<Node> visitedForward = new HashSet<>();
        Set<Node> visitedBackward = new HashSet<>();
        Queue<Node> queueForward = new LinkedList<>();
        Queue<Node> queueBackward = new LinkedList<>();

        Node startNode = map[0][0];
        Node endNode = map[map.length - 1][map[0].length - 1];

        queueForward.add(startNode);
        queueBackward.add(endNode);
        visitedForward.add(startNode);
        visitedBackward.add(endNode);

        while (!queueForward.isEmpty() && !queueBackward.isEmpty()) {
            if (searchStep(map, queueForward, visitedForward, visitedBackward)) {
                pathCount += getPathCount(visitedForward, visitedBackward);
            }
            if (searchStep(map, queueBackward, visitedBackward, visitedForward)) {
                pathCount += getPathCount(visitedForward, visitedBackward);
            }
        }
    }

    private boolean searchStep(Node[][] map, Queue<Node> queue, Set<Node> visited, Set<Node> oppositeVisited) {
        Node currentNode = queue.poll();

        if (oppositeVisited.contains(currentNode)) {
            return true;
        }

        int row = currentNode.getRow();
        int column = currentNode.getColumn();

        if (row > 0 && !currentNode.getUp().isObstacle() && visited.add(currentNode.getUp())) {
            queue.add(currentNode.getUp());
        }
        if (row < map.length - 1 && !currentNode.getDown().isObstacle() && visited.add(currentNode.getDown())) {
            queue.add(currentNode.getDown());
        }
        if (column > 0 && !currentNode.getLeft().isObstacle() && visited.add(currentNode.getLeft())) {
            queue.add(currentNode.getLeft());
        }
        if (column < map[0].length - 1 && !currentNode.getRight().isObstacle() && visited.add(currentNode.getRight())) {
            queue.add(currentNode.getRight());
        }

        return false;
    }

    private int getPathCount(Set<Node> visitedForward, Set<Node> visitedBackward) {
        int count = 0;

        for (Node node : visitedForward) {
            if (visitedBackward.contains(node)) {
                count++;
            }
        }

        return count;
    }

    private void findShortestPath (List<List<String>> shortestPath, Node[][] map, int[][] isVisited) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(map[0][0]);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            int row = currentNode.getRow();
            int column = currentNode.getColumn();
            int stationVisited = currentNode.getStationVisited();
            Queue<String> currentPath = new LinkedList<>(currentNode.getPath());

            if (currentNode.getValue() == 3 && stationVisited == targetStations) {
                shortestPath.add(new ArrayList<>(currentPath));
                stationVisited = 0;
            }

            isVisited[row][column] = 1;

            for (int[] direction : directions) {
                int nX = row + direction[0];
                int nY = column + direction[1];

                if (isValid(map, nX, nY) && isVisited[nX][nY] == 0 && !map[nX][nY].isObstacle()) {
                    Node nextNode = map[nX][nY];

                    if (isVisited[nX][nY] == 0 && !nextNode.isObstacle()) {
                        queue.add(nextNode);
                        nextNode.setStationVisited(stationVisited + (nextNode.getValue() == 2 ? 1 : 0));
                        Queue<String> newPath = new LinkedList<>(currentPath);
                        newPath.add(getDirection(row, column, nX, nY));
                        nextNode.setPath(newPath);
                    }
                }
            }
        }
    }

    public String getDirection (int cX, int cY, int nX, int nY) {
        if (cX < nX) {
            return "Down" + getDirection(cX, cY, nX - 1, nY);
        }
        if (cX > nX) {
            return "Up" + getDirection(cX - 1, cY, nX, nY);
        }
        if (cY < nY) {
            return "Right" + getDirection(cX, cY, nX, nY - 1);
        }
        if (cY > nY) {
            return "Left" + getDirection(cX, cY - 1, nX, nY);
        }
        else return "";
    }
}

package Suzume;

/*
 * author @nuraimandanial
 */
public class SuzumeMap {
    static class Map {
        private int index;
        private int[][] map;
        private int possiblePath;

        public Map(int index, int[][] map) {
            this.index = index;
            this.map = map;
        }

        public int[][] getMap(int path, int index) {
            if (path == this.possiblePath) return map;
            else if (index == this.index) return map;
            else return null;
        }        
    }
}
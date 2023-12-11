package year2023.day11;

import general.Advent;

import java.util.ArrayList;
import java.util.List;

public class Universe extends Advent {

    private final int[][] universe;
    private final List<Point> galaxies = new ArrayList<>();

    public Universe() {
        universe = new int[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                if (lines.get(i).charAt(j) == '.') {
                    universe[i][j] = 0;
                } else {
                    universe[i][j] = 1;
                    galaxies.add(new Point(j, i));
                }
                universe[i][j] = lines.get(i).charAt(j) == '.' ? 0 : 1;
            }
        }
    }

    private int expansionBetween(Point a, Point b, int size) {
        int sum = 0;
        for (int i = 0; i < Math.abs(a.x - b.x); i++)
            if (columnEmpty(Math.min(a.x, b.x) + i))
                sum += size;
        for (int i = 0; i < Math.abs(a.y - b.y); i++)
            if (lineEmpty(Math.min(a.y, b.y) + i))
                sum += size;
        return sum;
    }

    private boolean lineEmpty(int x) {
        for (int i = 0; i < universe[x].length; i++)
            if (universe[x][i] == 1) return false;
        return true;
    }

    private boolean columnEmpty(int y) {
        for (int i = 0; i < universe.length; i++)
            if (universe[i][y] == 1) return false;
        return true;
    }

    private int shortestPath(Point a, Point b, int expandMultiplier) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y) + expansionBetween(a, b, expandMultiplier - 1);
    }

    public long findShortestPaths(int expandMultiplier) {
        long sum = 0;
        for (int i = 0; i < galaxies.size(); i++)
            for (int j = i + 1; j < galaxies.size(); j++)
                sum += shortestPath(galaxies.get(i), galaxies.get(j), expandMultiplier);
        return sum;
    }

    record Point(int x, int y){};
}

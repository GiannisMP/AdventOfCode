package year2022.day12;

import general.Advent;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Maze extends Advent {
    private Position start;
    private final List<Position> lowestPoints = new ArrayList<>();
    private Position end;
    private Integer shortestPathToStart = null;
    public Maze() {
        List<List<Position>> positions = lines.stream().map(String::chars).map(IntStream::boxed)
                .map(line -> line
                        .map(c -> new Position(c, c == 'S', c == 'E'))
                        .peek(p -> {
                            if (p.isStartPosition()) {
                                start = p;
                                lowestPoints.add(p);
                            }
                            if (p.getWeight() == 0) lowestPoints.add(p);
                            if (p.isGoal()) end = p;
                        })
                        .collect(Collectors.toList())).toList();
        for (int i = 0; i < positions.size(); i++) {
            for (int j = 0; j < positions.get(i).size(); j++) {
                if (i > 0) {
                    positions.get(i - 1).get(j).adjacent(positions.get(i).get(j), Position.DIRECTION.BOTTOM);
                    positions.get(i).get(j).adjacent(positions.get(i - 1).get(j), Position.DIRECTION.TOP);
                }
                if (i < positions.size() - 1) {
                    positions.get(i + 1).get(j).adjacent(positions.get(i).get(j), Position.DIRECTION.TOP);
                    positions.get(i).get(j).adjacent(positions.get(i + 1).get(j), Position.DIRECTION.BOTTOM);
                }
                if (j > 0) {
                    positions.get(i).get(j - 1).adjacent(positions.get(i).get(j), Position.DIRECTION.RIGHT);
                    positions.get(i).get(j).adjacent(positions.get(i).get(j - 1), Position.DIRECTION.LEFT);
                }
                if (j < positions.get(i).size() - 1) {
                    positions.get(i).get(j + 1).adjacent(positions.get(i).get(j), Position.DIRECTION.LEFT);
                    positions.get(i).get(j).adjacent(positions.get(i).get(j + 1), Position.DIRECTION.RIGHT);
                }
            }
        }
    }
    public void BFS(Position startPosition) {
        List<Position> visited = new ArrayList<>();
        Queue<Position> queue = new LinkedList<>();
        queue.add(startPosition);
        visited.add(startPosition);
        while (!queue.isEmpty()) {
            Position pos = queue.poll();
            pos.getNeighbours()
                    .stream()
                    .filter(p -> !visited.contains(p))
                    .filter(p -> p.getWeight() <= pos.getWeight() + 1)
                    .forEach(p -> {
                        queue.add(p);
                        visited.add(p);
                        p.setPrevious(pos);
                    });
        }
    }
    public Integer shortestPathToStart(Position currentPosition, Position startPosition, Integer current) {
        Integer finalCurrent = current;
        if (currentPosition.getPrevious() == null
                || Optional.ofNullable(shortestPathToStart).filter(i -> finalCurrent > i).isPresent())
            return -1;
        return currentPosition.getPrevious().equals(startPosition)
                ? current
                : shortestPathToStart(currentPosition.getPrevious(), startPosition, ++current);
    }
    public Integer shortestPathToAny() {
        return lowestPoints
                .stream()
                .peek(this::BFS)
                .mapToInt(pos -> shortestPathToStart(end, pos, 1))
                .filter(i -> i >= 0)
                .min()
                .orElse(-1);
    }
    public Integer shortestPathToStart() {
        BFS(start);
        return shortestPathToStart(end, start, 1);
    }
    public static void main(String[] args) {
        Maze maze = new Maze();
        maze.shortestPathToStart = maze.shortestPathToStart();
        System.out.println(maze.shortestPathToStart);
        System.out.println(maze.shortestPathToAny());
    }
}

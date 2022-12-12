package year2022.day12;

import general.Advent;
import org.checkerframework.checker.units.qual.A;
import org.javers.common.collections.Lists;
import year2022.day8.Tree;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Maze extends Advent {

    private final List<List<Position>> positions;
    private Position start;
    private Position end;

    public Maze() {
        positions = lines.stream().map(String::chars).map(IntStream::boxed)
                .map(line -> line
                        .map(c -> new Position(c, c == 'S', c == 'E'))
                        .peek(p -> {
                            if (p.isStartPosition()) start = p;
                            if (p.isGoal()) end = p;
                        })
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
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

    public void dfs() {
        List<Position> visited = new ArrayList<>();
        Queue<Position> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);
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

    public Integer shortestPathToStart(Position position, Integer current) {
        return position.getPrevious().isStartPosition() ? current : shortestPathToStart(position.getPrevious(), ++current);
    }
    public Integer shortestPathToStart() {
        dfs();
        return shortestPathToStart(end, 1);
    }

    public static void main(String[] args) {
        Maze maze = new Maze();
        System.out.println(maze.shortestPathToStart());
    }

}

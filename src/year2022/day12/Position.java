package year2022.day12;

import org.checkerframework.checker.units.qual.A;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Position {
    enum DIRECTION { LEFT, RIGHT, TOP, BOTTOM }

    private final Map<DIRECTION, Position> positionMap = new HashMap<>();
    private final int weight;
    private final boolean startPosition;
    private final boolean goal;
    private final UUID id;

    private int minDistance;

    private List<Position> bestRoute;

    public Position(int weight, boolean startPosition, boolean goal) {
        this.weight = startPosition ? 0 : goal ? 25 : weight - 97;
        this.startPosition = startPosition;
        this.goal = goal;
        this.id = UUID.randomUUID();
    }

    public Optional<Path> minDistanceToGoal(Path current) {
        Path currentPath = Path.copy(current);
//        System.out.println(currentPath.getPositions().size());
        if (foundGoal()) {
            return Optional.of(new Path(this));
        }
        Optional<Path> path = positionMap
                .values()
                .stream()
                .filter(p -> !currentPath.contains(p))
                .filter(p -> !Maze.visited.contains(p))
                .filter(p -> p.weight <= weight + 1)
                .peek(currentPath::add)
                .peek(Maze.visited::add)
                .map(v -> v.minDistanceToGoal(currentPath))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparing((Path p) -> p.getPositions().size()))
                .limit(1)
                .findFirst();
        return path.map(p -> Path.copy(new Path(this), p));
    }

    public UUID getId() {
        return id;
    }

    public void adjacent(Position position, DIRECTION direction) {
        positionMap.put(direction, position);
    }

    public void setBestRoute(List<Position> position) {
        this.bestRoute = position;
    }

    public boolean isStartPosition() {
        return startPosition;
    }

    private boolean foundGoal() {
        return goal;
    }

}

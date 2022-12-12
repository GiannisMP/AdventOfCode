package year2022.day12;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Position {
    enum DIRECTION { LEFT, RIGHT, TOP, BOTTOM }

    private final Map<DIRECTION, Position> positionMap = new HashMap<>();
    private final int weight;
    private final boolean startPosition;
    private final boolean goal;
    private Position previous;
    public Position(int weight, boolean startPosition, boolean goal) {
        this.weight = startPosition ? 0 : goal ? 25 : weight - 97;
        this.startPosition = startPosition;
        this.goal = goal;
    }

    public void adjacent(Position position, DIRECTION direction) {
        positionMap.put(direction, position);
    }
    public List<Position> getNeighbours() {
        return positionMap.values().stream().toList();
    }
    public boolean isStartPosition() {
        return startPosition;
    }
    public boolean isGoal() {
        return goal;
    }

    public int getWeight() {
        return weight;
    }

    public Position getPrevious() {
        return previous;
    }

    public void setPrevious(Position previous) {
        this.previous = previous;
    }
}

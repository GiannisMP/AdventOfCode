package day9;

import com.google.common.collect.ImmutableMap;

import java.util.*;
import java.util.function.Consumer;

public class Position {
    private final Map<Character, Consumer<Position>> headMap = ImmutableMap.of(
            'U', p -> p.y++,
            'D', p -> p.y--,
            'R', p -> p.x++,
            'L', p -> p.x--
    );
    private Integer x;
    private Integer y;
    private Position parent;
    private Position child;
    private final Set<Position> visited = new HashSet<>();

    public Position(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }
    public Position(Integer x, Integer y, Position parent) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        visited.add(new Position(x, y));
        parent.child = this;
    }

    @Override
    public boolean equals(Object obj) {
        Position position = (Position) obj;
        return position.x.equals(x) && position.y.equals(y);
    }

    @Override
    public int hashCode() {
        return this.x.hashCode() + this.y.hashCode();
    }

    public Set<Position> getVisited() {
        return visited;
    }

    public Position getTail() {
        return Optional.ofNullable(child)
                .map(Position::getTail)
                .orElse(this);
    }

    private int horizontalDistance(Position position) {
        return Math.abs(x - position.x);
    }

    private int verticalDistance(Position position) {
        return Math.abs(y - position.y);
    }

    private boolean isHead() {
        return Objects.isNull(parent);
    }

    public void move(char direction) {
        if (isHead()) headMap.get(direction).accept(this);
        Optional.ofNullable(child).ifPresent(Position::moveChild);
    }

    public void moveChild() {
        if (verticalDistance(parent) > 1 || horizontalDistance(parent) > 1) {
            x += Integer.signum(parent.x - x);
            y += Integer.signum(parent.y - y);
            visited.add(new Position(x, y));
        }
        Optional.ofNullable(child).ifPresent(Position::moveChild);
    }
}

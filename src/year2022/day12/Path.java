package year2022.day12;

import java.util.LinkedList;

public class Path {
    private LinkedList<Position> positions = new LinkedList<>();

    public Path() {
    }
    public Path(Position position) {
        this.positions.add(position);
    }
    public Path(LinkedList<Position> positions) {
        this.positions = positions;
    }

    public void add(Position position) {
        positions.add(position);
    }

    public LinkedList<Position> getPositions() {
        return positions;
    }

    public boolean contains(Position position) {
        return positions.stream().anyMatch(p -> p.getId().equals(position.getId()));
    }

    public static Path copy(Path path) {
        return new Path(path.getPositions().size() > 0 ? new LinkedList<>(path.getPositions()) : new LinkedList<>());
    }

    public static Path copy(Path path1, Path path2) {
        Path path = new Path();
        path1.getPositions().forEach(path::add);
        path2.getPositions().forEach(path::add);
        return path;
    }

}

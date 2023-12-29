package year2023.day17;

import general.Utils;

import java.util.*;
import java.util.stream.IntStream;
import general.Utils.Direction;

public class Node {
    private final Utils.Point position;
    private final int weight;
    private int distance = Integer.MAX_VALUE;
    private Set<Node> adjacentNodes = new HashSet<>();
    private List<Node> shortestPath = new ArrayList<>();

    public Node(int x, int y, int weight) {
        this.position = new Utils.Point(x, y);
        this.weight = weight;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getWeight() {
        return weight;
    }

    public Set<Node> getAdjacentNodes() {
        return adjacentNodes;
    }

    public int getDistance() {
        return distance;
    }

    public Utils.Point getPosition() {
        return position;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setAdjacentNodes(Set<Node> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

    public Direction getCurrentDirection() {
        return !shortestPath.isEmpty()
                ? shortestPath.get(shortestPath.size()-1).getDirection(this)
                : null;
    }

    public Direction getDirection(Node next) {
        if (position.x() < next.position.x())
            return Direction.DOWN;
        else if (position.x() > next.position.x())
            return Direction.UP;
        else if (position.y() > next.position.y())
            return Direction.LEFT;
        else
            return Direction.RIGHT;
    }

    public boolean isValidNeighbour(Node next){
        return shortestPath.size() < 3 || !IntStream.range(shortestPath.size() - 2, shortestPath.size())
                .mapToObj(shortestPath::get)
                .map(Node::getCurrentDirection)
                .filter(d -> !Objects.isNull(d))
                .allMatch(d -> d.equals(getDirection(next)) && d.equals(getCurrentDirection()));
    }
}

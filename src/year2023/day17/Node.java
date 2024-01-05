package year2023.day17;

import general.Utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import general.Utils.Direction;

public class Node {
    private final Utils.Point position;
    private final int weight;
    private int distance = 100000;
    private Map<Node, Integer> adjacentNodes = new HashMap<>();
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

    public Map<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public Map<Node, Integer> getInvalidNodes() {
        return adjacentNodes.entrySet().stream().filter(e -> !isValidNeighbour(e.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<Node, Integer> getValidNodes() {
        return adjacentNodes.entrySet().stream().filter(e -> isValidNeighbour(e.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void removeAdjacent(Node node) {
        adjacentNodes.remove(node);
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

    public static Direction getDirection(Node current, Node next) {
        if (current.position.x() < next.position.x())
            return Direction.DOWN;
        else if (current.position.x() > next.position.x())
            return Direction.UP;
        else if (current.position.y() > next.position.y())
            return Direction.LEFT;
        else
            return Direction.RIGHT;
    }

    public boolean isValidNeighbour(Node next){
        return shortestPath.size() < 3 || isValidNeighbour(next, this, shortestPath);
    }

    private static boolean sameDirection(List<Node> nodes) {
        return IntStream.range(1, nodes.size())
                .mapToObj(i -> getDirection(nodes.get(i-1), nodes.get(i)))
                .distinct()
                .count() == 1;
    }

    public static boolean isDistinct(List<?> list) {
        return IntStream.range(2, list.size()).noneMatch(i -> list.get(i-2).equals(list.get(i)));
    }

    public static boolean isValidPath(List<Node> path){
        return isDistinct(path) && (path.size() < 5 || IntStream.range(5, path.size())
                .mapToObj(i -> path.subList(i - 5, i))
                .noneMatch(Node::sameDirection));
    }

    public static boolean isValidNeighbour(Node next, Node current, List<Node> path) {
        return path.size() < 3 || !IntStream.range(path.size() - 2, path.size())
                .mapToObj(path::get)
                .map(Node::getCurrentDirection)
                .filter(d -> !Objects.isNull(d))
                .allMatch(d -> d.equals(getDirection(current, next)) && d.equals(current.getCurrentDirection()));
    }

    @Override
    public boolean equals(Object obj) {
        return position.equals(((Node) obj).getPosition());
    }
}

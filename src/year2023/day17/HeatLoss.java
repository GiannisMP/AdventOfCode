package year2023.day17;

import general.Advent;
import general.Utils;

import java.util.*;

public class HeatLoss extends Advent {
    private final Map<Utils.Point, Node> nodes = new HashMap<>();
    private final Node start;
    private final Node end;

    public HeatLoss() {
        int[][] map = parse(lines);
        createGraph(map);
        start = nodes.get(new Utils.Point(0,0));
        end = nodes.get(new Utils.Point(map.length - 1, map[0].length - 1));
    }

    private static int[][] parse(List<String> input) {
        return input.stream()
                .map(String::chars)
                .map(s -> s.mapToObj(c -> (char) c)
                        .mapToInt(Character::getNumericValue)
                        .toArray())
                .toArray(int[][]::new);
    }

    private void createGraph(int[][] map) {
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[i].length; j++)
                nodes.put(new Utils.Point(i, j), new Node(i, j, map[i][j]));
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                Node currentNode = nodes.get(new Utils.Point(i, j));
                Optional.ofNullable(nodes.get(new Utils.Point(i-1, j)))
                        .ifPresent(n -> currentNode.getAdjacentNodes().put(n, Integer.MAX_VALUE));
                Optional.ofNullable(nodes.get(new Utils.Point(i+1, j)))
                        .ifPresent(n -> currentNode.getAdjacentNodes().put(n, Integer.MAX_VALUE));
                Optional.ofNullable(nodes.get(new Utils.Point(i, j-1)))
                        .ifPresent(n -> currentNode.getAdjacentNodes().put(n, Integer.MAX_VALUE));
                Optional.ofNullable(nodes.get(new Utils.Point(i, j+1)))
                        .ifPresent(n -> currentNode.getAdjacentNodes().put(n, Integer.MAX_VALUE));
            }
        }
    }

    private void shortestPaths(Node from) {
        from.setDistance(0);
        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();
        unsettledNodes.add(from);
        while (!unsettledNodes.isEmpty()) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            currentNode.getValidNodes().keySet()
                    .stream()
                    .filter(n -> !settledNodes.contains(n))
                    .peek(n -> calculateMinimumDistance(n, currentNode))
                    .forEach(unsettledNodes::add);
            currentNode.getInvalidNodes().keySet()
                    .stream()
                    .filter(n -> !settledNodes.contains(n))
                    .peek(n -> calculateMinimumDistance(n, currentNode))
                    .forEach(unsettledNodes::add);
            settledNodes.add(currentNode);
        }
    }

    private void calculateMinimumDistance(Node evaluationNode, Node sourceNode) {
        if (sourceNode.isValidNeighbour(evaluationNode)) {
            int sourceDistance = sourceNode.getDistance();
            if (sourceNode.getAdjacentNodes().get(evaluationNode) > sourceDistance + evaluationNode.getWeight())
                sourceNode.getAdjacentNodes().put(evaluationNode, sourceDistance + evaluationNode.getWeight());
            if (evaluationNode.getAdjacentNodes().get(sourceNode) > sourceDistance + evaluationNode.getWeight())
                evaluationNode.getAdjacentNodes().put(sourceNode, sourceDistance + evaluationNode.getWeight());
            if (sourceDistance + evaluationNode.getWeight() < evaluationNode.getDistance()) {
                evaluationNode.setDistance(sourceDistance + evaluationNode.getWeight());
                LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
                shortestPath.add(sourceNode);
                evaluationNode.setShortestPath(shortestPath);
            }
        } else {
            List<Node> previousNodes = new ArrayList<>(sourceNode.getShortestPath()
                    .subList(sourceNode.getShortestPath().size() - 2, sourceNode.getShortestPath().size()));
            previousNodes.add(sourceNode);
            List<Node> passed = new ArrayList<>();
            while (!previousNodes.isEmpty()) {
                Node current = previousNodes.removeLast();
                current.getAdjacentNodes().entrySet()
                        .stream()
                        .filter(e -> !e.getKey().equals(passed.isEmpty() ? evaluationNode : passed.getFirst()))
                        .filter(e -> !e.getKey().equals(current.getShortestPath().getLast()))
                        .forEach(next -> {
                            LinkedList<Node> shortestPath = new LinkedList<>(next.getKey().getShortestPath());
                            shortestPath.add(next.getKey());
                            shortestPath.add(current);
                            shortestPath.addAll(passed.reversed());
                            int distance = shortestPath.stream().mapToInt(Node::getWeight).sum() + evaluationNode.getWeight() - start.getWeight();
                            if (Node.isValidPath(shortestPath) && distance < evaluationNode.getDistance()) {
                                evaluationNode.setShortestPath(shortestPath);
                                evaluationNode.setDistance(distance);
                            }
                        });
                passed.add(current);
            }
        }
    }

    private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node: unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private void printPath(Node node) {
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                System.out.print(
                        (node.getShortestPath().stream().map(Node::getPosition).anyMatch(new Utils.Point(i, j)::equals)
                                || node.getPosition().equals(new Utils.Point(i, j)))
                                && !Objects.isNull(nodes.get(new Utils.Point(i, j)).getCurrentDirection())
                                ? Utils.Print.red("x")
                                : nodes.get(new Utils.Point(i, j)).getWeight());
            }
            System.out.println();
        }
    }

    public int minimizeHeatLoss() {
        shortestPaths(start);
        printPath(end);
        return end.getDistance();
    }
}
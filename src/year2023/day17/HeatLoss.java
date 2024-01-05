package year2023.day17;

import general.Advent;
import general.Utils;

import java.util.*;

public class HeatLoss extends Advent {
    private final Map<Utils.Point, Node> nodes = new HashMap<>();
    private final Node start;
    private final Node end;

    public static int[][] parse(List<String> input) {
        int[][] map = new int[input.size()][];
        for (int i=0; i< input.size(); i++) {
            map[i] = input.get(i).chars()
                    .mapToObj(c -> (char) c)
                    .mapToInt(Character::getNumericValue)
                    .toArray();
        }
        return map;
    }

    public void createGraph(int[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                Node node = new Node(i, j, map[i][j]);
                nodes.put(node.getPosition(), node);
            }
        }
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

    public void calculateShortestPathFromSource(Node source) {
        source.setDistance(0);
        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();
        unsettledNodes.add(source);
        while (!unsettledNodes.isEmpty()) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Node node: currentNode.getValidNodes().keySet()) {
                if (!settledNodes.contains(node)) {
                    calculateMinimumDistance(node, node.getWeight(), currentNode);
                    unsettledNodes.add(node);
                }
            }
            for (Node node: currentNode.getInvalidNodes().keySet()) {
                if (!settledNodes.contains(node)) {
                    calculateMinimumDistance(node, node.getWeight(), currentNode);
                    unsettledNodes.add(node);
                }
            }
            settledNodes.add(currentNode);
        }
    }

    private void calculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
        if (sourceNode.isValidNeighbour(evaluationNode)) {
            Integer sourceDistance = sourceNode.getDistance();
            if (sourceNode.getAdjacentNodes().get(evaluationNode) > sourceDistance + edgeWeigh)
                sourceNode.getAdjacentNodes().put(evaluationNode, sourceDistance + edgeWeigh);
            if (evaluationNode.getAdjacentNodes().get(sourceNode) > sourceDistance + edgeWeigh)
                evaluationNode.getAdjacentNodes().put(sourceNode, sourceDistance + edgeWeigh);
            if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
                evaluationNode.setDistance(sourceDistance + edgeWeigh);
                LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
                shortestPath.add(sourceNode);
                evaluationNode.setShortestPath(shortestPath);
            }
        } else {
            sourceNode.getAdjacentNodes().entrySet()
                    .stream()
                    .filter(e -> !e.getKey().equals(evaluationNode))
                    .filter(e -> !e.getKey().equals(sourceNode.getShortestPath().get(sourceNode.getShortestPath().size() - 1)))
                    .forEach(next -> {
                        LinkedList<Node> shortestPath = new LinkedList<>(next.getKey().getShortestPath());
                        shortestPath.add(next.getKey());
                        shortestPath.add(sourceNode);
                        int distance = shortestPath.stream().mapToInt(Node::getWeight).sum() + edgeWeigh - start.getWeight();
                        if (Node.isValidPath(shortestPath) && distance < evaluationNode.getDistance()) {
                            evaluationNode.setShortestPath(shortestPath);
                            evaluationNode.setDistance(distance);
                        }
                    });


            Node oneIn = sourceNode.getShortestPath().get(sourceNode.getShortestPath().size() - 1);
            oneIn.getAdjacentNodes()
                    .entrySet()
                    .stream()
                    .filter(e -> !e.getKey().equals(sourceNode))
                    .filter(e -> !e.getKey().equals(oneIn.getShortestPath().get(oneIn.getShortestPath().size() - 1)))
                    .forEach(next -> {
                        LinkedList<Node> shortestPath = new LinkedList<>(next.getKey().getShortestPath());
                        shortestPath.add(next.getKey());
                        shortestPath.add(oneIn);
                        shortestPath.add(sourceNode);
                        int distance = shortestPath.stream().mapToInt(Node::getWeight).sum() + edgeWeigh - start.getWeight();
                        if (Node.isValidPath(shortestPath) && distance < evaluationNode.getDistance()) {
                            evaluationNode.setShortestPath(shortestPath);
                            evaluationNode.setDistance(distance);
                        }
                    });


            Node twoIn = sourceNode.getShortestPath().get(sourceNode.getShortestPath().size() - 2);
            twoIn.getAdjacentNodes()
                    .entrySet()
                    .stream()
                    .filter(e -> !e.getKey().equals(oneIn))
                    .filter(e -> !e.getKey().equals(twoIn.getShortestPath().get(twoIn.getShortestPath().size() - 1)))
                    .forEach(next -> {
                        LinkedList<Node> shortestPath = new LinkedList<>(next.getKey().getShortestPath());
                        shortestPath.add(next.getKey());
                        shortestPath.add(twoIn);
                        shortestPath.add(oneIn);
                        shortestPath.add(sourceNode);
                        int distance = shortestPath.stream().mapToInt(Node::getWeight).sum() + edgeWeigh - start.getWeight();
                        if (Node.isValidPath(shortestPath) && distance < evaluationNode.getDistance()) {
                            evaluationNode.setShortestPath(shortestPath);
                            evaluationNode.setDistance(distance);
                        }
                    });
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

    public HeatLoss() {
        int[][] map = parse(lines);
        createGraph(map);
        start = nodes.get(new Utils.Point(0,0));
        end = nodes.get(new Utils.Point(map.length - 1, map[0].length - 1));
        calculateShortestPathFromSource(start);
    }

    public int minimizeHeatLoss() {
        Node finalNode = end;
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                System.out.print(
                        (finalNode.getShortestPath().stream().map(Node::getPosition).anyMatch(new Utils.Point(i, j)::equals)
                                || finalNode.getPosition().equals(new Utils.Point(i, j)))
                                && !Objects.isNull(nodes.get(new Utils.Point(i, j)).getCurrentDirection())
                                ? Utils.Print.red("x")
                                : nodes.get(new Utils.Point(i, j)).getWeight());
            }
            System.out.println();
        }
        return finalNode.getDistance();
    }
}
//47
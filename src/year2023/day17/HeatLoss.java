package year2023.day17;

import general.Advent;
import general.Utils;

import java.util.*;

public class HeatLoss extends Advent {
    private final Map<Utils.Point, Node> nodes = new HashMap<>();

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
                        .ifPresent(currentNode.getAdjacentNodes()::add);
                Optional.ofNullable(nodes.get(new Utils.Point(i+1, j)))
                        .ifPresent(currentNode.getAdjacentNodes()::add);
                Optional.ofNullable(nodes.get(new Utils.Point(i, j-1)))
                        .ifPresent(currentNode.getAdjacentNodes()::add);
                Optional.ofNullable(nodes.get(new Utils.Point(i, j+1)))
                        .ifPresent(currentNode.getAdjacentNodes()::add);
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
            for (Node node: currentNode.getAdjacentNodes()) {
                if (!settledNodes.contains(node) && currentNode.isValidNeighbour(node)) {
                    calculateMinimumDistance(node, node.getWeight(), currentNode);
                    unsettledNodes.add(node);
                }
            }
            settledNodes.add(currentNode);
        }
    }

    private void calculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
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
        calculateShortestPathFromSource(nodes.get(new Utils.Point(0, 0)));
    }

    public int minimizeHeatLoss() {
        Node finalNode = nodes.get(new Utils.Point(0, 4));
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                System.out.print(
                        (finalNode.getShortestPath().stream().map(Node::getPosition).anyMatch(new Utils.Point(i, j)::equals)
                                || finalNode.getPosition().equals(new Utils.Point(i, j)))
                                && !Objects.isNull(nodes.get(new Utils.Point(i, j)).getCurrentDirection())
                                ? Utils.Print.red(nodes.get(new Utils.Point(i, j)).getCurrentDirection().toString())
                                : nodes.get(new Utils.Point(i, j)).getWeight());
            }
            System.out.println();
        }
        return finalNode.getDistance();
    }
}
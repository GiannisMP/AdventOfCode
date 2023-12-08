package year2023.day8;

import general.Advent;
import general.Utils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Desert extends Advent {

    private final Map<String, Pair> data = new HashMap<>();
    private final Map<String, Node> nodes = new HashMap<>();
    private final List<Character> directions;

    public Desert() {
        directions = Utils.chars(lines.get(0));
        for (int i = 2; i < lines.size(); i++) {
            String[] tokens = lines.get(i).split("[ =(),]+");
            data.put(tokens[0], new Pair(tokens[1], tokens[2]));
        }
        data.forEach(this::createNode);
    }

    private void createNode(String location, Pair lr) {
        nodes.put(location, new Node(location));
        if (!nodes.containsKey(lr.left)) createNode(lr.left, data.get(lr.left));
        if (!nodes.containsKey(lr.right)) createNode(lr.right, data.get(lr.right));
        nodes.get(location).addLR(nodes.get(lr.left), nodes.get(lr.right));
    }

    public int steps(String from, String to) {
        int i = 0;
        int steps = 0;
        Node current = nodes.get(from);
        while (!current.location.endsWith(to)) {
            current = current.get(directions.get(i));
            i++;
            steps++;
            if (i == directions.size()) i = 0;
        }
        return steps;
    }

    public BigInteger traverse(String from, String to) {
        return Utils.lcm(nodes.values()
                .stream()
                .filter(node -> node.endsWith(from))
                .map(node -> steps(node.location, to))
                .map(BigInteger::valueOf)
                .collect(Collectors.toList()));
    }

    static final class Node {
        private final String location;
        private Node left;
        private Node right;

        Node(String location) {
            this.location = location;
        }

        public void addLR(Node left, Node right) {
            this.left = left;
            this.right = right;
        }

        public Node get(Character direction) {
            return direction == 'L' ? left : right;
        }

        public boolean endsWith(String s) {
            return location.endsWith(s);
        }

    }

    record Pair(String left, String right) {}
}


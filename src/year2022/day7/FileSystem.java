package year2022.day7;

import general.Advent;

import java.util.ArrayList;
import java.util.List;

public class FileSystem extends Advent {
    private int sum = 0;

    Node base = new Node("/", 0, 0, null, new ArrayList<>());


    private Node found = base;

    private Node generateNode(Node parent, String current) {
        String[] tokens = current.split(" ");
        Node node = tokens[0].equals("dir")
                ? new Node(tokens[1], 0, 0, parent, new ArrayList<>())
                : new Node(tokens[1], 1, Integer.parseInt(tokens[0]), parent, new ArrayList<>());
        parent.addChild(node);
        return node;
    }

    private void handleLS(Node parent, List<String> output) {
        output.forEach(ls -> generateNode(parent, ls));
    }

    private Command getCommand(String command, List<String> input) {
        String[] tokens = command.split(" ");
        return tokens[1].equals(Command.CD)
                ? new Command(Command.CD, List.of(tokens[2]))
                : new Command(Command.LS, input);
    }

    private void calcSize(Node node, int bound) {
        if (node.getSize() <= bound) {
            sum += node.getSize();
        }
        node.getChildren().stream().filter(n -> n.getType() == 0).forEach(d -> calcSize(d, bound));
    }

    private void calcWhichToDelete(Node node, int requiredSpace) {
        if (node.getSize() > requiredSpace && node.getSize() < found.getSize()) {
            found = node;
        }
        node.getChildren().stream().filter(n -> n.getType() == 0).forEach(d -> calcWhichToDelete(d, requiredSpace));
    }

    public int populate() {
        Node currentNode = base;
        for (int i = 0; i< lines.size(); i++) {
            String current = lines.get(i);
            if (current.startsWith("$")) {
                List<String> input = new ArrayList<>();
                while (i < lines.size() - 1 && !lines.get(i + 1).startsWith("$")) {
                    input.add(lines.get(i+1));
                    i++;
                }
                Command command = getCommand(current, input);
                if (command.getType().equals(Command.LS)) {
                    handleLS(currentNode, command.getInput());
                } else {
                    if (command.getInput().get(0).equals("/")) {
                        currentNode = base;
                    } else if (command.getInput().get(0).equals("..")) {
                        currentNode = currentNode.getParent();
                    } else {
                        currentNode = currentNode.cd(command.getInput().get(0)).get();
                    }
                }
            }
        }

        calcSize(base, 100000);
        int freeSpace = 70000000 - base.getSize();
        int requiredSpace = 30000000 - freeSpace;
        calcWhichToDelete(base, requiredSpace);
        return found.getSize();
    }
}

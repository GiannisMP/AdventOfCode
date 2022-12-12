package year2022.day7;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Node {

    private String name;
    private int type;
    private int size;
    private Node parent;
    private ArrayList<Node> children;

    public Node(String name, int type, int size, Node parent, ArrayList<Node> children) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.parent = parent;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Optional<Node> cd(String name) {
        return children.stream().filter(c -> c.getName().equals(name)).findFirst();
    }

    public int getSize() {
        return type == 0 ? children.stream().mapToInt(Node::getSize).sum() : size;
    }

    public void addChild(Node node) {
        children.add(node);
    }
}

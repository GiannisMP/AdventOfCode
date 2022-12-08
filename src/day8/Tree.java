package day8;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Tree {
    enum DIRECTION { LEFT, RIGHT, TOP, BOTTOM }

    private final Map<DIRECTION, Tree> treeMap = new HashMap<>();
    private final int height;

    public Tree(int height) {
        this.height = height;
    }

    public void adjacent(Tree tree, DIRECTION direction) {
        treeMap.put(direction, tree);
    }

    private boolean isVisibleFrom(Tree tree, DIRECTION direction) {
        return Objects.isNull(tree) || height > tree.height && isVisibleFrom(tree.treeMap.get(direction), direction);
    }

    public boolean isVisible() {
        return Arrays.stream(DIRECTION.values())
                .map(direction -> isVisibleFrom(treeMap.get(direction), direction))
                .reduce(Boolean::logicalOr).orElse(true);
    }

    private int scenicFrom(Tree tree, DIRECTION direction, int current) {
        return Objects.isNull(tree)
                ? current
                : height > tree.height ? scenicFrom(tree.treeMap.get(direction), direction, ++current) : ++current;
    }
    public int scenic() {
        return treeMap.keySet().stream()
                .map(direction -> scenicFrom(treeMap.get(direction), direction, 0))
                .reduce((a,b) -> a * b).orElse(0);
    }

}

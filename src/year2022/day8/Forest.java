package year2022.day8;

import general.Advent;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Forest extends Advent {
    private final List<List<Tree>> trees;

    public Forest() {
        trees = lines.stream().map(String::chars).map(IntStream::boxed)
                .map(line -> line.map(Character::getNumericValue).map(Tree::new).collect(Collectors.toList()))
                .collect(Collectors.toList());
        for (int i = 0; i < trees.size(); i++) {
            for (int j = 0; j < trees.get(i).size(); j++) {
                if (i > 0) {
                    trees.get(i - 1).get(j).adjacent(trees.get(i).get(j), Tree.DIRECTION.BOTTOM);
                    trees.get(i).get(j).adjacent(trees.get(i - 1).get(j), Tree.DIRECTION.TOP);
                }
                if (i < trees.size() - 1) {
                    trees.get(i + 1).get(j).adjacent(trees.get(i).get(j), Tree.DIRECTION.TOP);
                    trees.get(i).get(j).adjacent(trees.get(i + 1).get(j), Tree.DIRECTION.BOTTOM);
                }
                if (j > 0) {
                    trees.get(i).get(j - 1).adjacent(trees.get(i).get(j), Tree.DIRECTION.RIGHT);
                    trees.get(i).get(j).adjacent(trees.get(i).get(j - 1), Tree.DIRECTION.LEFT);
                }
                if (j < trees.get(i).size() - 1) {
                    trees.get(i).get(j + 1).adjacent(trees.get(i).get(j), Tree.DIRECTION.LEFT);
                    trees.get(i).get(j).adjacent(trees.get(i).get(j + 1), Tree.DIRECTION.RIGHT);
                }
            }
        }
    }
    public long findVisible() {
        return trees.stream().flatMap(Collection::stream).filter(Tree::isVisible).count();
    }
    public int calculateMaxScenicValue() {
        return trees.stream().flatMap(Collection::stream).mapToInt(Tree::scenic).max().orElse(0);
    }
}

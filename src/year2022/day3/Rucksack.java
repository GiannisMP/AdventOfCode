package year2022.day3;

import com.google.common.collect.Lists;
import general.Advent;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Rucksack extends Advent {

    private final Function<Integer, Integer> priority = letter -> letter >= 97 ? letter - 96 : letter - 38;

    private final Function<String, List<Integer>> getChars = s -> s.chars().boxed().toList();

    private OptionalInt priorityForLine(String line) {
        int split = line.length() / 2;
        IntStream firstHalf = line.substring(0, split).chars();
        List<Integer> secondHalf = getChars.apply(line.substring(split));
        return firstHalf.filter(secondHalf::contains).map(priority::apply).findFirst();
    }

    private OptionalInt priorityForLines(List<String> lines) {
        return lines.get(0).chars()
                .boxed()
                .filter(i -> lines.stream().map(getChars).allMatch(chars -> chars.stream().anyMatch(i::equals)))
                .mapToInt(priority::apply)
                .findFirst();
    }

    public Integer calculatePriorityForLine() {
        return lines.stream()
                .map(this::priorityForLine)
                .filter(OptionalInt::isPresent)
                .mapToInt(OptionalInt::getAsInt)
                .sum();
    }
    public Integer calculatePriorityForLines() {
        return Lists.partition(lines, 3)
                .stream()
                .map(this::priorityForLines)
                .filter(OptionalInt::isPresent)
                .mapToInt(OptionalInt::getAsInt)
                .sum();
    }
}

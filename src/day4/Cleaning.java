package day4;

import com.google.common.collect.Streams;
import general.Advent;
import org.javers.common.collections.Pair;
import org.javers.common.collections.Sets;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Cleaning extends Advent {

    private Set<Integer> calculateArea(int from, int to) {
        return IntStream.rangeClosed(from, to)
                .boxed()
                .collect(Collectors.toSet());
    }

    private Set<Integer> parseArea(String area) {
        String[] fromTo = area.split("-");
        return calculateArea(Integer.parseInt(fromTo[0]), Integer.parseInt(fromTo[1]));
    }

    private Pair<Set<Integer>, Set<Integer>> parseLine(String line) {
        String[] elfs = line.split(",");
        return new Pair<>(parseArea(elfs[0]), parseArea(elfs[1]));
    }

    private boolean containsAny(Set<Integer> one, Set<Integer> other) {
        return one.stream().anyMatch(other::contains);
    }

    public long findCompleteOverlaps() {
        return lines.stream()
                .map(this::parseLine)
                .filter(areas -> areas.left().containsAll(areas.right()) || areas.right().containsAll(areas.left()))
                .count();
    }

    public long findPartialOverlaps() {
        return lines.stream()
                .map(this::parseLine)
                .filter(areas -> containsAny(areas.left(), areas.right()) || containsAny(areas.right(), areas.left()))
                .count();
    }
}

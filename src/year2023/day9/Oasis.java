package year2023.day9;

import general.Advent;
import general.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Oasis extends Advent {

    private List<Long> steps(List<Long> numbers) {
        return IntStream.range(1, numbers.size())
                .mapToObj(i -> numbers.get(i) - numbers.get(i-1))
                .collect(Collectors.toList());
    }

    private Long findNext(List<Long> numbers) {
        return numbers.stream().distinct().count() == 1
                ? numbers.get(0)
                : numbers.get(numbers.size() - 1) + findNext(steps(numbers));
    }

    public long calculate(boolean reverse) {
        return lines.stream()
                .map(Utils::parseSpacedLongNumbers)
                .map(ArrayList::new)
                .peek(reverse ? Collections::reverse : __ -> {})
                .mapToLong(this::findNext)
                .sum();
    }
}

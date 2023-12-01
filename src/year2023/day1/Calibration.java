package year2023.day1;

import general.Advent;
import general.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Calibration extends Advent {

    Map<String, Integer> numbers = Map.of(
            "one", 1,
            "two", 2,
            "three", 3,
            "four", 4,
            "five", 5,
            "six", 6,
            "seven", 7,
            "eight", 8,
            "nine", 9);

    private Optional<Integer> parse(String s) {
        return Optional.of(s)
                .filter(value -> Character.isDigit(value.charAt(0)))
                .map(value -> Character.getNumericValue(value.charAt(0))).
                or(() -> numbers.entrySet()
                        .stream()
                        .filter(e -> s.startsWith(e.getKey()))
                        .map(Map.Entry::getValue)
                        .findFirst());
    }

    private int parseLine(String line) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < line.length(); i++)
            parse(line.substring(i)).ifPresent(numbers::add);
        return numbers.get(0) * 10 + numbers.get(numbers.size() - 1);
    }
    public int p1() {
        return lines.stream().mapToInt(this::parseLine).sum();
    }
}
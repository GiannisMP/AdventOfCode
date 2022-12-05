package day2;

import general.Advent;

import java.util.Map;

public class RPS extends Advent {

    private final Map<String, Integer> value = Map.of("A", 1, "B", 2, "C", 3, "X", 1, "Y", 2, "Z", 3);
    private final Map<String, String> win = Map.of("X", "C", "Z", "B", "Y", "A");
    private final Map<String, String> lose = Map.of("X", "B", "Z", "A", "Y", "C");
    private final Map<String, String> draw = Map.of("X", "A", "Y", "B", "Z", "C");
    private final Map<String, Map<String, String>> find = Map.of("X", lose,"Y", draw, "Z", win);

    private String getReverse(Map<String, String> map, String value) {
        return map.entrySet().stream().filter(e -> e.getValue().equals(value)).findFirst().map(Map.Entry::getKey).orElse(null);
    }

    private int battle(String mine, String other) {
        return draw.get(mine).equals(other) ? 3 : (win.get(mine).equals(other) ? 6 : 0);
    }

    private String replace(String mine, String other) {
        return getReverse(find.get(mine), other);
    }

    private int calc(String line) {
        String[] values = line.split(" ");
        return value.get(values[1]) + battle(values[1], values[0]);
    }

    private int calcWithReplace(String line) {
        String[] values = line.split(" ");
        String myValue = replace(values[1], values[0]);
        return value.get(myValue) + battle(myValue, values[0]);
    }

    public int p1() {
        return lines.stream().mapToInt(this::calc).sum();
    }

    public int p2() {
        return lines.stream().mapToInt(this::calcWithReplace).sum();
    }
}

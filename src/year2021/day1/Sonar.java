package year2021.day1;

import general.Advent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Sonar extends Advent {
    private List<Integer> measurements = new ArrayList<>();
    public void load() {
        measurements = lines.stream().map(Integer::parseInt).collect(Collectors.toList());
    }
    public Integer findNumbersIncreased() {
        load();
        int counter = 0;
        for (int i = 1; i < measurements.size(); i++)
            if (measurements.get(i) > measurements.get(i-1)) counter++;
        return counter;
    }
    public Integer findNumbersIncreasedThreefold() {
        Function<Integer, Integer> threeSum = i -> measurements.get(i-2) + measurements.get(i-1) + measurements.get(i);
        load();
        int counter = 0;
        for (int i = 3; i < measurements.size(); i++)
            if (threeSum.apply(i) > threeSum.apply(i-1)) counter++;
        return counter;
    }
    public static void main(String[] args) {
        System.out.println(new Sonar().findNumbersIncreasedThreefold());
    }
}

package year2023.day12;

import general.Advent;
import general.Utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Springs extends Advent {

    private List<Spring> springs;

    public Springs() {
        springs = lines.stream()
                .map(line -> {
                    String[] tokens = line.split(" ");
                    List<Integer> ecc = Utils.parseSpacedNumbers(tokens[1].replace(",", " "));
                    return new Spring(tokens[0], ecc);
                }).collect(Collectors.toList());

    }

    public int process() {
        return springs.stream()
                .mapToInt(s -> {
                    int res = process(s,0,0);
                    System.out.println(res + " " + s.state + s.ecc);
                    return res;
                })
                .sum();
    }

    private int findPossibleArrangements(Spring spring, int start, int end, int eccIndex) {
        int sum = 0;
        for (int i = start; i < end - spring.ecc.get(eccIndex) + 1; i += 1) {
            if (spring.isValid(eccIndex, i, start, end)) {
                if (eccIndex < spring.ecc.size() - 1 ){
                    int res = process(spring, i + spring.ecc.get(eccIndex) + 1, eccIndex + 1);
                    sum += res;
                } else sum++;
            }
        }
        return sum;
    }

    private int process(Spring spring, int s, int eccIndex) {
        return findPossibleArrangements(spring,
                s,
                spring.state.length() - (eccIndex < spring.ecc.size() - 1
                        ? IntStream.range(eccIndex + 1, spring.ecc.size())
                            .map(spring.ecc::get)
                            .sum() + spring.ecc.size() - eccIndex - 1
                        : 0),
                eccIndex);
    }

    record Spring(String state, List<Integer> ecc) {
        private boolean isValid(int eccIndex, int location, int start, int end) {
            Integer ecc = ecc().get(eccIndex);
            return !state.substring(location, location + ecc).contains(".")
                    && (location == 0 || state.charAt(location - 1) != '#')
                    && (location+ecc >= state.length() || state.charAt(location+ecc) != '#')
                    && !state.substring(start, location).contains("#")
                    && (!(eccIndex == ecc().size() - 1) || !state.substring(location + ecc).contains("#"));
        }
    }
}

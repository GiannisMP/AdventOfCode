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
        process(springs.get(2), 0, 0);
        return springs.stream().mapToInt(s -> process(s,0,0)).peek(System.out::println).sum();
    }

    private int findPossibleArrangements(Spring spring, int start, int end, int eccIndex) {
        int sum = 0;
        for (int i = start; i < end - spring.ecc.get(eccIndex) + 1; i += 1) {
            if (spring.isValid(spring.ecc.get(eccIndex), i)) {
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
                spring.state.length() - (eccIndex < spring.ecc.size() - 1 ? IntStream.range(eccIndex + 1, spring.ecc.size()).map(spring.ecc::get).sum() + spring.ecc.size() - eccIndex - 1 : 0),
                eccIndex);
    }

    static final class Spring {
        private final String state;
        private final List<Integer> ecc;

        private Set<String> validStates = new HashSet<>();

        Spring(String state, List<Integer> ecc) {
            this.state = state;
            this.ecc = ecc;
        }

        public String state() {
            return state;
        }


        public List<Integer> ecc() {
            return ecc;
        }

        private boolean isValid(int size, int location) {
            return !state.substring(location, location + size).contains(".")
                    && (location == 0 || state.charAt(location - 1) != '&')
                    && (location+size >= state.length() - 1 || state.charAt(location+size+1) != '&');
        }

    }
}

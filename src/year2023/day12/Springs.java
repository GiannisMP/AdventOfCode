package year2023.day12;

import general.Advent;
import general.Utils;

import java.math.BigInteger;
import java.util.*;

public class Springs extends Advent {

    private final Map<Integer, BigInteger> cache = new HashMap<>();

    public BigInteger process(int multiply) {
        return lines.stream()
                .map(line -> {
                    String[] tokens = line.split(" ");
                    String state = tokens[0];
                    List<Integer> numbers = Utils.parseSpacedNumbers(tokens[1].replace(",", " "));
                    ArrayList<Integer> ecc = new ArrayList<>(numbers);
                    for (int i = 0; i < multiply; i++) {
                        ecc.addAll(numbers);
                        state = state + "?" + tokens[0];
                    }
                    int current = ecc.remove(0);
                    return findPossibleArrangements(new Spring(state, current, 0, ecc));
                }).reduce(BigInteger::add).orElse(BigInteger.ZERO);
    }

    private BigInteger findPossibleArrangements(Spring spring) {
        if (cache.containsKey(spring.hashCode()))
            return cache.get(spring.hashCode());
        BigInteger sum = BigInteger.ZERO;
        int end = spring.remainingEcc.isEmpty()
                ? spring.state.length()
                : spring.state.length() - spring.remainingEcc.stream().mapToInt(i -> i).sum() + spring.remainingEcc.size();
        for (int i = spring.start; i < end - spring.ecc + 1; i += 1) {
            if (isValid(spring.state, spring.ecc, i, spring.start, spring.remainingEcc.size())) {
                if (!spring.remainingEcc.isEmpty()){
                    ArrayList<Integer> newEcc = new ArrayList<>(spring.remainingEcc);
                    int next = newEcc.remove(0);
                    BigInteger res = findPossibleArrangements(new Spring(spring.state, next,i + spring.ecc + 1, newEcc));
                    sum = sum.add(res);
                } else sum = sum.add(BigInteger.ONE);
            }
        }
        cache.put(spring.hashCode(), sum);
        return sum;
    }

    public boolean isValid(String state, int ecc, int location, int start, int remaining) {
        return !state.substring(location, location + ecc).contains(".")
                && (location == 0 || state.charAt(location - 1) != '#')
                && (location+ecc >= state.length() || state.charAt(location+ecc) != '#')
                && !state.substring(start, location).contains("#")
                && (!(remaining == 0) || !state.substring(location + ecc).contains("#"));
    }

    record Spring(String state, int ecc, int start, List<Integer> remainingEcc) {
        @Override
        public int hashCode() {
            return (state + ecc + start + remainingEcc).hashCode();
        }
    }
}

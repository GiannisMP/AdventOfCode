package year2023.day12;

import general.Advent;
import general.Utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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

    public BigInteger process() {
        return springs.stream()
                .map(s -> {
//                    BigInteger resss = process(s.multi(), 0, 0);
//                    if (resss.compareTo(BigInteger.valueOf(7)) == 0) {
////                        System.out.println(s.state);
////                        System.out.println(process(s, 0, 0));
////                        System.out.println(process(s.multi(), 0, 0));
////                        System.out.println(process(s.triple(), 0, 0));
//                    }
                    BigInteger res1 = process(s,0,0);
                    BigInteger res2 = process(s.multi(),0,0);
                    BigInteger base = res2.divide(res1);
                    return base.pow(4).multiply(res1);
                }).reduce(BigInteger::add).orElse(BigInteger.ZERO);
    }

    private BigInteger findPossibleArrangements(Spring spring, int start, int end, int eccIndex) {
        BigInteger sum = BigInteger.ZERO;
        for (int i = start; i < end - spring.ecc.get(eccIndex) + 1; i += 1) {
            if (spring.isValid(eccIndex, i, start, end)) {
                if (eccIndex < spring.ecc.size() - 1 ){
                    BigInteger res = process(spring, i + spring.ecc.get(eccIndex) + 1, eccIndex + 1);
                    sum = sum.add(res);
                } else sum = sum.add(BigInteger.ONE);
            }
        }
        return sum;
    }

    private BigInteger process(Spring spring, int s, int eccIndex) {
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
        public boolean isValid(int eccIndex, int location, int start, int end) {
            Integer ecc = ecc().get(eccIndex);
            return !state.substring(location, location + ecc).contains(".")
                    && (location == 0 || state.charAt(location - 1) != '#')
                    && (location+ecc >= state.length() || state.charAt(location+ecc) != '#')
                    && !state.substring(start, location).contains("#")
                    && (!(eccIndex == ecc().size() - 1) || !state.substring(location + ecc).contains("#"));
        }

        public Spring multi() {
            ArrayList<Integer> list = new ArrayList<>();
            list.addAll(ecc);
            list.addAll(ecc);
            return new Spring(state + "?" + state, list);
        }
        public Spring triple() {
            ArrayList<Integer> list = new ArrayList<>();
            list.addAll(ecc);
            list.addAll(ecc);
            list.addAll(ecc);
            return new Spring(state + "?" + state + "?" + state, list);
        }
    }
}

package year2023.day14;

import general.Advent;
import general.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Dish extends Advent {

    private Integer[][] rocks = new Integer[lines.size()][];

    public Dish() {
        for (int i = 0; i < lines.size(); i++)
            rocks[i] = Utils.chars(lines.get(i))
                    .stream()
                    .map(c -> c == '.' ? 0 : c == 'O' ? 1 : 2)
                    .toArray(Integer[]::new);;
    }
    private long getWeight() {
        return IntStream.range(0, rocks.length)
                .mapToLong(i -> Arrays.stream(rocks[i]).filter(n -> n == 1).count() * (rocks.length - i))
                .sum();
    }

    private Integer[] moveAndRotate(int index) {
        Integer[] newColumn = new Integer[rocks.length];
        Arrays.fill(newColumn, 0);
        for (int i = 0; i < rocks.length; i++) {
            int top = i;
            int numberOfRocks = 0;
            while (i < rocks.length && rocks[i][index] != 2) {
                if(rocks[i][index] == 1) numberOfRocks++;
                i++;
            }
            if (i < rocks.length && rocks[i][index] == 2) newColumn[rocks.length - i -1] = 2;
            for (int j = 0; j < numberOfRocks; j++)
                newColumn[rocks.length-1 - j - top] = 1;
        }
        return newColumn;
    }


    private void execute(int times) {
        Map<Integer, Integer[][]> cache = new HashMap<>();
        int count = 0;
        int hash = Arrays.deepHashCode(rocks);
        while (count < times && !cache.containsKey(hash)) {
            runCycle();
            cache.put(hash, rocks);
            hash = Arrays.deepHashCode(rocks);
            count++;
        }
        ArrayList<RockField> smartCache = new ArrayList<>();
        int finalHash = hash;
        while (count < times && smartCache.stream().noneMatch(r -> r.hash == finalHash)) {
            rocks = cache.get(hash);
            smartCache.add(new RockField(Arrays.deepHashCode(rocks), rocks));
            hash = Arrays.deepHashCode(rocks);
            count++;
        }
        rocks = smartCache.get((times - count -1) % smartCache.size()).rocks;
    }


    private void runCycle() {
        for (long count = 0; count < 4; count++) {
            rocks = IntStream.range(0, rocks[0].length)
                    .mapToObj(this::moveAndRotate)
                    .toArray(Integer[][]::new);
        }
    }

    public long calculateWeight(int times) {
        execute(times);
        return getWeight();
    }

    record RockField(int hash, Integer[][] rocks) {
        @Override
        public boolean equals(Object obj) {
            return hash == ((RockField) obj).hash;
        }
    }
}

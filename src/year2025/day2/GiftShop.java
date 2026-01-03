package year2025.day2;

import general.Advent;

import java.util.Arrays;
import java.util.stream.LongStream;

public class GiftShop extends Advent {

    private boolean isDInvalid(long i) {
        String s = String.valueOf(i);
        return s.substring(0, s.length() / 2).equals(s.substring(s.length() / 2));
    }

    private boolean isDeepInvalid(long i) {
        String s = String.valueOf(i);
        for (int j = 1; j < s.length(); j++)
            if (s.split(s.substring(0, j)).length == 0)
                return true;
        return false;
    }

    private long sumInvalid(long start, long end, boolean deep) {
        return LongStream.rangeClosed(start, end)
                .filter(deep ? this::isDeepInvalid : this::isDInvalid)
                .sum();
    }

    public long p1(boolean deep) {
        return Arrays.stream(lines.getFirst().split(","))
                .map(s -> s.split("-"))
                .mapToLong(t -> sumInvalid(Long.parseLong(t[0]), Long.parseLong(t[1]), deep))
                .sum();
    }
}

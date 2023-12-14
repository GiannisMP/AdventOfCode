package year2023.day14;

import general.Advent;
import general.Utils;

import java.util.stream.IntStream;

public class SimpleDish extends Advent {

    private final Integer[][] rocks = new Integer[lines.size()][];

    public SimpleDish() {
        for (int i = 0; i < lines.size(); i++)
            rocks[i] = Utils.chars(lines.get(i))
                    .stream()
                    .map(c -> c == '.' ? 0 : c == 'O' ? 1 : 2)
                    .toArray(Integer[]::new);;
    }

    private long weightForColumn(int column) {
        int sum = 0;
        for (int i = 0; i < rocks.length; i++) {
            int top = rocks.length - i;
            int numberOfRocks = 0;
            while (i < rocks.length && rocks[i][column] != 2) {
                if(rocks[i][column] == 1) numberOfRocks++;
                i++;
            }
            for (int j = 0; j < numberOfRocks; j++) sum += top - j;
        }
        return sum;
    }

    public long calculateWeight() {
        return IntStream.range(0, rocks[0].length).mapToLong(this::weightForColumn).sum();
    }

}

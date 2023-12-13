package year2023.day13;

import general.Advent;
import general.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Volcano extends Advent {

    private final List<Field> fields = new ArrayList<>();

    public Volcano() {
        for (int i = 0; i < lines.size(); i++) {
            List<String> field = new ArrayList<>();
            while (i < lines.size() && !lines.get(i).isBlank()) {
                field.add(lines.get(i));
                i++;
            }
            Integer[][] mirrors = field.stream()
                    .map(line -> Utils.chars(line).stream().map(c -> c == '.' ? 0 : 1)
                            .toArray(Integer[]::new))
                    .toArray(Integer[][]::new);
            fields.add(new Field(mirrors));
        }
    }

    public int calculate(Result required) {
        return fields.stream()
                .mapToInt(field -> field.reflections(required, true) * 100 + field.reflections(required, false))
                .peek(System.out::println)
                .sum();
    }

    record Field(Integer[][] mirrors){
        private int reflection(int left, int right, Function<Integer, Integer> value, int size) {
            return IntStream.range(0, Math.min(left + 1, size - right))
                    .mapToDouble(i -> value.apply(right + i) * Math.pow(2, i) - value.apply(left - i) * Math.pow(2, i))
                    .mapToInt(v -> ((Double) Math.abs(v)).intValue())
                    .sum();
        }

        private Result reflection(int left, int right, Function<Integer, Function<Integer, Integer>> element, int hSize, int vSize) {
            int i = 0;
            int smudge = 0;
            while (i < hSize) {
                int finalI = i;
                int reflection = reflection(left, right, element.apply(finalI), vSize);
                if (reflection == 0)
                    i++;
                else if ((reflection & -reflection) == reflection) {
                    smudge++;
                    i++;
                }
                else break;
            }
            if (i == hSize) {
                if (smudge == 0) return Result.TRUE;
                if (smudge == 1) return Result.SMUDGED;
            }
            return Result.FALSE;
        }

        private int reflections(Result required, boolean horizontal) {
            for (int i = 1; i < (horizontal ? mirrors.length : mirrors[0].length); i++)
                if (reflection(i - 1, i,
                        index -> j -> horizontal ? mirrors[j][index] : mirrors[index][j],
                        horizontal ? mirrors[0].length : mirrors.length,
                        horizontal ? mirrors.length : mirrors[0].length) == required)
                    return i;
            return 0;
        }
    }
    public enum Result { TRUE, FALSE, SMUDGED }
}

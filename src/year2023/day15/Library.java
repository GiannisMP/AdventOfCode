package year2023.day15;

import general.Advent;
import general.Utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

public class Library extends Advent {

    private final HashSet<Lens> lenses = new HashSet<>();

    public Library() {
        Arrays.stream(lines.get(0).split(","))
                .forEach(value -> {
                    if (value.contains("=")) {
                        String[] tokens = value.split("=");
                        Lens lens = new Lens(tokens[0], Integer.parseInt(tokens[1]));
                        lenses.stream()
                                .filter(l -> l.label.equals(lens.label))
                                .findFirst()
                                .ifPresentOrElse(l -> l.focalLength = lens.focalLength, () -> lenses.add(lens));
                    } else lenses.removeIf(l -> l.label.equals(value.split("-")[0]));
                });
    }

    public int sumHashes() {
        return Arrays.stream(lines.get(0).split(","))
                .map(v -> new Lens(v, 0))
                .mapToInt(Lens::hashCode)
                .sum();
    }

    private int handleBucket(int index) {
        List<Lens> bucket = lenses.stream().filter(l -> l.hashCode() == index - 1).toList();
        return IntStream.range(0, bucket.size())
                .map(j -> index * (j + 1) * bucket.get(j).focalLength)
                .sum();
    }

    public int execute() {
        return IntStream.rangeClosed(1, 256)
                .map(this::handleBucket)
                .sum();
    }

    static final class Lens {
        public final String label;
        public int focalLength;

        public Lens(String label, int focalLength) {
            this.label = label;
            this.focalLength = focalLength;
        }

        @Override
        public int hashCode() {
            int sum = 0;
            List<Integer> ascii = Utils.chars(label).stream().map(c -> (int) c).toList();
            for (Integer i : ascii) {
                sum += i;
                sum *= 17;
                sum = sum % 256;
            }
            return sum;
        }
    }
}
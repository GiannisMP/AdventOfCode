package year2023.day21;

import general.Advent;
import general.Utils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Steps extends Advent {

    private final int[][] garden;

    private Utils.Point start;

    public Steps() {
        garden = IntStream.range(0, lines.size())
                .mapToObj(i -> lines.get(i).chars()
                        .mapToObj(c -> (char)c)
                        .peek(c -> {
                            if (c == 'S') start = new Utils.Point(i, lines.get(i).indexOf(c));
                        })
                        .mapToInt(c -> c == '#' ? 1 : 0)
                        .toArray())
                .toArray(int[][]::new);
    }

    private boolean canMoveTo(int x, int y) {
        return x >= 0 && x < garden.length && y >= 0 && y < garden[x].length && garden[x][y] == 0;
    }

    private int transform(int x) {
        return x >= 0 ? x % garden.length : garden.length - (Math.abs(x) - 1) % garden.length - 1 ;
    }

    private boolean canMoveFreelyTo(int x, int y) {
        return garden[transform(x)][transform(y)] == 0;
    }

    private Set<Utils.Point> neighbours(Utils.Point point, BiPredicate<Integer, Integer> check) {
        Set<Utils.Point> points = new HashSet<>();
        if (check.test(point.intX() + 1, point.intY()))
            points.add(new Utils.Point(point.x() + 1, point.y()));
        if (check.test(point.intX() - 1, point.intY()))
            points.add(new Utils.Point(point.x() - 1, point.y()));
        if (check.test(point.intX(), point.intY() + 1))
            points.add(new Utils.Point(point.x(), point.y() + 1));
        if (check.test(point.intX(), point.intY() - 1))
            points.add(new Utils.Point(point.x(), point.y() - 1));
        return points;
    }

    private final Set<String> skip = new HashSet<>();

    private Set<Utils.Point> move(Utils.Point point, int steps, Set<Utils.Point> destinations) {
        String hash = transform(point.intX()) + "^" + transform(point.intY()) + "^" + steps;
        if (skip.contains(hash))
            return Set.of();
        skip.add(hash);
        if (steps == 0)
            destinations.add(point);
        else
            for (Utils.Point p : neighbours(point, this::canMoveTo))
                destinations.addAll(move(p, steps - 1, destinations));
        return destinations;
    }

    private Set<Utils.Point> move(Utils.Point point, int steps) {
        Set<Utils.Point> points = neighbours(point, this::canMoveFreelyTo);
        for (int i = 1; i < steps; i++)
            points = points.stream()
                    .flatMap(p -> neighbours(p, this::canMoveFreelyTo).stream())
                    .collect(Collectors.toSet());
        return new HashSet<>(points);
    }

    public long move(int target) {
        return move(start, target, new HashSet<>()).size();
    }

    public long numberOfPossibleDestinations(int target) {
        long change = 0;
        long rate = 0;

        int i = target % garden.length;
        long a = move(start, i).size();
        long b = move(start, i + garden.length).size();

        while (b - a - change != rate) {
            rate = b - a - change;
            change = b - a;
            i += garden.length;
            a = move(start, i).size();
            b = move(start, i + garden.length).size();
        }

        for (int j = 1; j < (target - i) / garden.length; j++) {
            long temp = b;
            b = b + b - a + rate;
            a = temp;
        }
        return b;
    }
}

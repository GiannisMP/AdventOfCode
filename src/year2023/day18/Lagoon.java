package year2023.day18;

import general.Advent;
import general.Utils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;


public class Lagoon extends Advent {
    enum Direction { UP, DOWN, LEFT, RIGHT }

    private static final long xMultiplier = 1000000;
    private static final int initialPosition = 100;
    record Cursor(int x, int y) {
        public Cursor move(Direction direction) {
            Cursor next;
            switch (direction) {
                case UP -> next = new Cursor(x, y - 1);
                case DOWN -> next = new Cursor(x, y + 1);
                case LEFT -> next = new Cursor(x - 1, y);
                case RIGHT -> next = new Cursor(x + 1, y);
                default -> next = new Cursor(x, y);
            }
            return next;
        }

        public long hash() {
            return Objects.hash(x * xMultiplier + y);
        }
    }
    private Map<Character, Function<Cursor, Cursor>> tiles = Map.of(
            'U', c -> c.move(Direction.UP),
            'D', c -> c.move(Direction.DOWN),
            'L', c -> c.move(Direction.LEFT),
            'R', c -> c.move(Direction.RIGHT));

    private Cursor cursor = new Cursor(initialPosition, initialPosition);
    private final SortedSet<Long> passed = new TreeSet<>();
    private final SortedSet<Long> filled = new TreeSet<>();
    int x1 = 0;
    int x2 = 1000;
    int y1 = 0;
    int y2 = 1000;

    private void print(SortedSet<Long> values, SortedSet<Long> moreValues) {
        for (int i = x2; i < x1 + 1 + initialPosition; i++) {
            for (int j = y2; j < y1 + 1 + initialPosition; j++) {
                System.out.print(values.contains(i * xMultiplier + j)
                        ? Utils.Print.red("X")
                        : moreValues.contains(i * xMultiplier + j)
                        ? Utils.Print.yellow("X")
                        : 0);
            }
            System.out.println();
        }
    }

    private void move(Cursor c, Character direction) {
        if (c.x > x1) x1 = c.x;
        if (c.x < x2) x2 = c.x;
        if (c.y > y1) y1 = c.y;
        if (c.y < y2) y2 = c.y;
        passed.add(c.hash());
        cursor = tiles.get(direction).apply(c);
    }

    private boolean shouldFill(long i) {
        return passed.contains(i - xMultiplier) || filled.contains(i - xMultiplier);
    }

    private void fill(List<Long> passedSorted) {
        for (int i = 1; i < passedSorted.size(); i++)
            if (passedSorted.get(i-1) / xMultiplier == passedSorted.get(i) / xMultiplier) {
                if (shouldFill(passedSorted.get(i - 1) + 1) && passedSorted.get(i) - passedSorted.get(i - 1) > 1) {
                    for (long j = passedSorted.get(i - 1) + 1; j < passedSorted.get(i); j++)
                        filled.add(j);
                    i++;
                }
            }
    }

    public int dug() {
        lines.stream()
                .map(line -> line.split(" "))
                .forEach(t -> IntStream.range(0, Integer.parseInt(t[1]))
                        .forEach(__ -> move(cursor, t[0].charAt(0))));
        fill(new ArrayList<>(passed));
        print(passed, filled);
        passed.addAll(filled);
        return passed.size();
    }
}

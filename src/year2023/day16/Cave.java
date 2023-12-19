package year2023.day16;

import general.Advent;
import general.Utils;

import java.util.*;
import java.util.function.Function;

public class Cave extends Advent {
    enum Direction { UP, DOWN, LEFT, RIGHT }
    record Cursor(int x, int y, Direction direction) {
        public Cursor move(Direction direction) {
            Cursor next;
            switch (direction) {
                case UP -> next = new Cursor(x, y - 1, Direction.UP);
                case DOWN -> next = new Cursor(x, y + 1, Direction.DOWN);
                case LEFT -> next = new Cursor(x - 1, y, Direction.LEFT);
                case RIGHT -> next = new Cursor(x + 1, y, Direction.RIGHT);
                default -> next = new Cursor(x, y, Direction.RIGHT);
            }
            return next;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction.toString() + x*1000 + y);
        }
    }
    private Map<Character, Function<Cursor, List<Cursor>>> tiles = Map.of(
            '.', c -> List.of(c.move(c.direction)),
            '|', c -> c.direction.equals(Direction.UP) || c.direction.equals(Direction.DOWN)
                    ? List.of(c.move(c.direction))
                    : List.of(c.move(Direction.UP), c.move(Direction.DOWN)),
            '-', c -> c.direction.equals(Direction.LEFT) || c.direction.equals(Direction.RIGHT)
                    ? List.of(c.move(c.direction))
                    : List.of(c.move(Direction.LEFT), c.move(Direction.RIGHT)),
            '/', c -> List.of(c.move(c.direction == Direction.UP
                    ? Direction.RIGHT
                    : c.direction == Direction.DOWN
                    ? Direction.LEFT
                    : c.direction == Direction.LEFT
                    ? Direction.DOWN
                    : Direction.UP)),
            '\\', c -> List.of(c.move(c.direction == Direction.UP
                    ? Direction.LEFT
                    : c.direction == Direction.DOWN
                    ? Direction.RIGHT
                    : c.direction == Direction.LEFT
                    ? Direction.UP
                    : Direction.DOWN)));
    Character[][] map;

    public Cave() {
        map = new Character[lines.size()][];
        for (int i = 0; i < lines.size(); i++)
            map[i] = Utils.chars(lines.get(i)).toArray(Character[]::new);
    }

    private boolean isValid(Cursor cursor) {
        return cursor.y >= 0 && cursor.y < map.length && cursor.x >= 0 && cursor.x < map[0].length;
    }
    private Set<Integer> lava = new HashSet<>();
    private ArrayList<Integer> passed = new ArrayList<>();
    private void move(Cursor cursor) {
        if (isValid(cursor) && !passed.contains(cursor.hashCode())) {
            lava.add(cursor.x * 1000 + cursor.y);
            passed.add(cursor.hashCode());
            tiles.get(map[cursor.y][cursor.x]).apply(cursor).forEach(this::move);
        }
    }

    public int findMax() {
        int max = 0;
        for (int i = 0; i < map.length; i++) {
            int result = energized(0, i, Direction.RIGHT);
            if (result > max) max = result;
            result = energized(map[i].length - 1, i, Direction.LEFT);
            if (result > max) max = result;
        }
        for (int j = 0; j < map[0].length; j++) {
            int result = energized(j, 0, Direction.DOWN);
            if (result > max) max = result;
            result = energized(j, map.length - 1, Direction.UP);
            if (result > max) max = result;
        }
        return max;
    }

    public int energized() {
        return energized(0, 0, Direction.RIGHT);
    }

    public int energized(int x, int y, Direction direction) {
        lava = new HashSet<>();
        passed = new ArrayList<>();
        Cursor cursor = new Cursor(x, y, direction);
        move(cursor);
        return lava.size();
    }
}

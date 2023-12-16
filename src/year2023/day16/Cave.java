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
    private Set<Integer> passed = new HashSet<>();
    private ArrayList<Integer> cache = new ArrayList<>();
    private void move(Cursor cursor) {
//        System.out.println(cursor.x + " " + cursor.y + " " + cursor.direction);
        if (isValid(cursor) && !cache.contains(cursor.hashCode())) {
            passed.add(cursor.x * 1000 + cursor.y);
            cache.add(cursor.hashCode());
            tiles.get(map[cursor.y][cursor.x]).apply(cursor).forEach(this::move);
        }
    }

    public int energized() {
        Cursor cursor = new Cursor(0, 0, Direction.RIGHT);
        move(cursor);
        return passed.size();
    }

}

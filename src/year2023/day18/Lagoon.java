package year2023.day18;

import general.Advent;
import general.Utils;
import general.Utils.Cursor;
import general.Utils.Direction;
import general.Utils.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;


public class Lagoon extends Advent {
    private final Map<Character, BiFunction<Cursor, Long, Cursor>> tiles = Map.of(
            'U', (c, s) -> c.move(Direction.UP, s),
            'D', (c, s) -> c.move(Direction.DOWN, s),
            'L', (c, s) -> c.move(Direction.LEFT, s),
            'R', (c, s) -> c.move(Direction.RIGHT, s),
            '3', (c, s) -> c.move(Direction.UP, s),
            '1', (c, s) -> c.move(Direction.DOWN, s),
            '2', (c, s) -> c.move(Direction.LEFT, s),
            '0', (c, s) -> c.move(Direction.RIGHT, s));

    private Cursor cursor;
    private long perimeter;
    private List<Point> points;

    private void move(Cursor c, Character direction, long steps) {
        perimeter += steps;
        cursor = tiles.get(direction).apply(c, steps);
        points.add(new Point(cursor.x(), cursor.y()));
    }

    record Move(Character direction, int steps) {}
    public long dug(Function<String, Move> move) {
        perimeter = 1;
        cursor = new Cursor(0, 0);
        points = new ArrayList<>(List.of(new Point(cursor.x(), cursor.y())));
        lines.stream()
                .map(move)
                .forEach(mv -> move(cursor, mv.direction, mv.steps));
        return perimeter + ((Double)Math.ceil((Utils.area(points)-perimeter)/2.0)).longValue();
    }

    public long dugSimple() {
        return dug(s ->
                Optional.of(s.split(" "))
                        .map(t -> new Move(t[0].charAt(0), Integer.parseInt(t[1])))
                        .orElse(null));
    }

    public long dugHex() {
        return dug(s ->
                Optional.of(s.split("#"))
                        .map(t -> t[1].substring(0, t[1].length() - 1))
                        .map(t -> new Move(t.charAt(t.length() - 1), Integer.parseInt(t.substring(0, t.length() - 1) , 16)))
                        .orElse(null));
    }


}
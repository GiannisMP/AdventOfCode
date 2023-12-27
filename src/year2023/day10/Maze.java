package year2023.day10;

import general.Advent;
import general.Utils;
import general.Utils.Point;

import java.util.*;
import java.util.function.Function;

public class Maze extends Advent {
    private final Map<Point, Pipe> pipes = new HashMap<>();
    private Point start;

    record Pipe(Point Point, Function<Point, Point> move){
        public Optional<Point> move(Point p) {
            return Optional.of(move.apply(p))
                    .filter(n -> !n.equals(Point))
                    .map(__ -> Point);
        }
    };

    private List<Point> passed;

    private final Map<Character, Function<Point, Function<Point, Point>>> move = Map.of(
            '-', curr -> prev -> prev.y() < curr.y()
                    ? new Point(curr.x(), curr.y() +1)
                    : prev.y() > curr.y()
                    ? new Point(curr.x(), curr.y() -1)
                    : curr,
            '|', curr -> prev -> prev.x() < curr.x()
                    ? new Point(curr.x()+1, curr.y())
                    : (prev.x() > curr.x()
                    ? new Point(curr.x()-1, curr.y())
                    : curr),
            'L', curr -> prev -> prev.x() < curr.x()
                    ? new Point(curr.x(), curr.y()+1)
                    : prev.y() > curr.y()
                    ? new Point(curr.x()-1, curr.y())
                    : curr,
            'J', curr -> prev -> prev.x() < curr.x()
                    ? new Point(curr.x(), curr.y()-1)
                    : prev.y() < curr.y()
                    ? new Point(curr.x()-1, curr.y())
                    : curr,
            '7', curr -> prev -> prev.x() > curr.x()
                    ? new Point(curr.x(), curr.y()-1)
                    : prev.y() < curr.y()
                    ? new Point(curr.x()+1, curr.y())
                    : curr,
            'F', curr -> prev -> prev.x() > curr.x()
                    ? new Point(curr.x(), curr.y()+1)
                    : (prev.y() > curr.y()
                    ? new Point(curr.x()+1, curr.y())
                    : curr),
            '.', curr -> prev -> curr
    );

    private void parse(List<String> input) {
        for (int i=0; i< input.size(); i++) {
            for (int j=0; j< input.get(i).length(); j++){
                Point Point = new Point(i, j);
                char c = input.get(i).charAt(j);
                if (c == 'S')
                    start = Point;
                else
                    pipes.put(Point, new Pipe(Point, move.get(c).apply(Point)));
            }
        }
    }

    public int getSteps() {
        passed = new ArrayList<>();
        passed.add(start);
        Point current = next(start);
        Point previous = start;
        int steps = 1;
        while(!current.equals(start)){
            passed.add(current);
            Point newPoint = pipes.get(current).move.apply(previous);
            previous = current;
            current = newPoint;
            steps++;
        }
        return steps/2;
    }

    private Point next(Point p) {
        return Optional.ofNullable(pipes.get(new Point(p.x(), p.y() + 1)))
                .flatMap(pipe -> pipe.move(p))
                .orElse(Optional.ofNullable(pipes.get(new Point(p.x(), p.y() - 1)))
                        .flatMap(pipe -> pipe.move(p))
                        .orElse(Optional.ofNullable(pipes.get(new Point(p.x() + 1, p.y())))
                                .flatMap(pipe -> pipe.move(p))
                                .orElse(Optional.ofNullable(pipes.get(new Point(p.x() - 1, p.y())))
                                        .flatMap(pipe -> pipe.move(p))
                                        .orElse(p))));
    }

    public long getArea() {
        passed.add(start);
        return (Utils.area(passed) - (passed.size() - 4)) / 2;
    }

    public Maze() {
        parse(lines);
    }
}
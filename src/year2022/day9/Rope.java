package year2022.day9;

import general.Advent;
import java.util.stream.IntStream;

public class Rope extends Advent {
    private final Position head = new Position(0, 0);

    public Rope(int length) {
        Position current = head;
        for (int i = 0; i < length; i++) {
            current = new Position(0, 0, current);
        }
    }
    public int execute() {
        lines.stream()
                .map(line -> line.split(" "))
                .forEach(tokens -> IntStream.range(0, Integer.parseInt(tokens[1]))
                        .forEach(i -> head.move(tokens[0].charAt(0))));
        return head.getTail().getVisited().size();
    }
}

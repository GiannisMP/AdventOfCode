package year2022.day10;

import com.google.common.collect.Lists;
import general.Advent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CPU extends Advent {

    private final Integer horizontalSize;
    private final Queue<Command> commands = new LinkedList<>();
    private final ArrayList<Integer> timedState = new ArrayList<>();
    private final ArrayList<Boolean> CRT = new ArrayList<>();

    public CPU(Integer horizontalSize) {
        this.horizontalSize = horizontalSize;
        lines.stream()
                .map(line -> line.split(" "))
                .map(tokens -> Command.Create(tokens[0], tokens.length > 1 ? Integer.parseInt(tokens[1]) : 0))
                .forEach(commands::add);
        timedState.add(1);
    }
    public void draw() {
        IntStream.range(0, timedState.size())
                .forEach(i -> CRT.add(Math.abs(timedState.get(i) - i + (horizontalSize * (i / horizontalSize))) < 2));
        Lists.partition(CRT, horizontalSize)
                .stream()
                .map(pixels -> pixels.stream().map(p -> p ? "#" : ".").collect(Collectors.joining()))
                .filter(pixels -> pixels.length() == horizontalSize)
                .forEach(System.out::println);
    }
    private Integer getState() {
        return timedState.get(timedState.size() - 1);
    }
    public Integer execute() {
        while (!commands.isEmpty())
            timedState.add(commands.peek().ready() ? commands.remove().execute(getState()) : getState());

        return IntStream.range(0, timedState.size() / horizontalSize)
                .map(i -> i * horizontalSize + horizontalSize / 2)
                .map(i -> timedState.get(i - 1) * i)
                .sum();
    }
}

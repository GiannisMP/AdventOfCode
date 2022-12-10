package day10;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.function.Function;

public class Command {
    private static final Map<String, Function<Integer, Command>> commands = ImmutableMap.of(
            "addx", value -> new Command(value, 2),
            "noop", __ -> new Command(0, 1));
    private final Integer value;
    private Integer waiting;
    private Command(Integer value, Integer waiting) {
        this.value = value;
        this.waiting = waiting;
    }
    public static Command Create(String name, Integer value) {
        return commands.get(name).apply(value);
    }
    public Integer execute(Integer input) {
        return input + value;
    }
    public boolean ready() {
        return --waiting <= 0;
    }
}

package year2021.day2;

import com.google.common.collect.ImmutableMap;
import general.Advent;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class Submarine extends Advent {

    private Integer x = 0;
    private Integer y = 0;
    private Integer aim = 0;
    private final Map<String, Consumer<Integer>> move = ImmutableMap.of(
            "forward", i -> {
                x+=i;
                y+=aim*i;
            }, "up", i -> aim-=i, "down", i -> aim+=i);

    public Integer execute() {
        lines.stream()
                .map(line -> line.split(" "))
                .forEach(tokens -> move.get(tokens[0]).accept(Integer.parseInt(tokens[1])));
        return x * y;
    }

    public static void main(String[] args) {
        System.out.println(new Submarine().execute());
    }
}

package year2022.day5;

import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Stacks {
    private static Stack<Character> stack(List<Character> items) {
        Stack<Character> stack = new Stack<>();
        items.forEach(stack::push);
        return stack;
    }

    private final Map<Integer, Stack<Character>> stacks = Map.of(
            1, stack(List.of('L', 'N', 'W', 'T', 'D')),
            2, stack(List.of('C', 'P', 'H')),
            3, stack(List.of('W', 'P', 'H', 'N', 'D', 'G', 'M', 'J')),
            4, stack(List.of('C', 'W', 'S', 'N', 'T', 'Q', 'L')),
            5, stack(List.of('P', 'H', 'C', 'N')),
            6, stack(List.of('T', 'H', 'N', 'D', 'M', 'W', 'Q', 'B')),
            7, stack(List.of('M', 'B', 'R', 'J', 'G', 'S', 'L')),
            8, stack(List.of('Z', 'N', 'W', 'G', 'V', 'B', 'R', 'T')),
            9, stack(List.of('W', 'G', 'D', 'N', 'P', 'L'))
    );
}

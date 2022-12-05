package day5;

import com.google.common.collect.Lists;
import general.Advent;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Crates extends Advent {
    private final static String TIMES = "move";
    private final static String FROM = "from";
    private final static String TO = "to";
    private final Map<Integer, Stack<Character>> stacks = new HashMap<>();

    private Optional<Character> getValue(String s, int location) {
        return Optional.of(location)
                .filter(l -> s.length() > l)
                .map(l -> s.charAt(location))
                .filter(c -> c >= 65 && c <= 90);
    }

    private void initializeStacks(String stackNumbers) {
        Arrays.stream(stackNumbers.split(" "))
                .filter(l -> !l.isEmpty())
                .mapToInt(Integer::parseInt)
                .forEach(i -> stacks.put(i, new Stack<>()));
    }

    private void populateStacks(String stackNumbers, List<String> stackEntries) {
        IntStream
                .range(0, stackEntries.size())
                .boxed()
                .sorted(Collections.reverseOrder())
                .forEach(i -> stacks.forEach((key, stack) ->
                        getValue(stackEntries.get(i), stackNumbers.indexOf(key.toString()))
                                .ifPresent(stack::push)));
    }

    private void move(int from, int to) {
        stacks.get(to).push(stacks.get(from).pop());
    }

    private void move(Map<String, Integer> action) {
        IntStream.range(0, action.get(TIMES)).forEach(__ -> move(action.get(FROM), action.get(TO)));
    }

    private void moveOrdered(Map<String, Integer> action) {
        Stack<Character> middle = new Stack<>();
        IntStream.range(0, action.get(TIMES)).forEach(__ -> middle.push(stacks.get(action.get(FROM)).pop()));
        IntStream.range(0, action.get(TIMES)).forEach(__ -> stacks.get(action.get(TO)).push(middle.pop()));
    }

    private Map<String, Integer> getAction(String line) {
        return Lists.partition(Arrays.stream(line.split(" ")).toList(), 2)
                .stream()
                .collect(Collectors.toMap(s -> s.get(0), s -> Integer.parseInt(s.get(1))));
    }

    private String calculateResult() {
        return stacks.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .map(Stack::pop)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    public String execute(boolean ordered) {
        int splitIndex = lines.indexOf("");
        String stackNumbers = lines.get(splitIndex - 1);

        initializeStacks(stackNumbers);

        populateStacks(stackNumbers, lines.stream().limit(splitIndex - 1).collect(Collectors.toList()));

        IntStream.range(splitIndex + 1, lines.size())
                .boxed()
                .map(lines::get)
                .map(this::getAction)
                .forEach(ordered ? this::moveOrdered : this::move);

        return calculateResult();
    }
}

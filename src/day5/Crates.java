package day5;

import base.Advent;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Crates extends Advent {

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

    private void move(int from, int to, int times) {
        IntStream.range(0, times).forEach(__ -> move(from, to));
    }

    private void moveOrdered(int from, int to, int howMany) {
        Stack<Character> middle = new Stack<>();
        IntStream.range(0, howMany).forEach(__ -> middle.push(stacks.get(from).pop()));
        IntStream.range(0, howMany).forEach(__ -> stacks.get(to).push(middle.pop()));
    }

    private void handleAction(String line) {
        String[] tokens = line.split(" ");
        move(Integer.parseInt(tokens[3]), Integer.parseInt(tokens[5]), Integer.parseInt(tokens[1]));
    }

    private void handleActionInOrder(String line) {
        String[] tokens = line.split(" ");
        moveOrdered(Integer.parseInt(tokens[3]), Integer.parseInt(tokens[5]), Integer.parseInt(tokens[1]));
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
                .forEach(ordered ? this::handleActionInOrder : this::handleAction);

        return calculateResult();
    }

}

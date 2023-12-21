package year2023.day19;

import general.Advent;
import general.Utils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Aplenty extends Advent {
    private final Map<String, Workflow> workflows = new HashMap<>();
    private final List<Part> parts = new ArrayList<>();
    private final List<Path> paths = new ArrayList<>();

    public Aplenty() {
        int i = 0;
        while (!lines.get(i).isEmpty()) {
            String[] tokens = lines.get(i++).split("\\{");
            workflows.put(tokens[0], new Workflow(tokens[0], Arrays.stream(tokens[1].split("[,}]+"))
                    .map(r -> r.split(":"))
                    .map(t -> t.length == 1
                            ? new Rule("", __ -> true, t[0])
                            : new Rule(t[0], p -> p.check(t[0]), t[1]))
                    .collect(Collectors.toList())));
        }
        while (++i < lines.size()) parts.add(Part.get(lines.get(i)));
        parse("in", "", List.of(), List.of());
    }

    private void parse(String workflow, String predicate, List<String> positive, List<String> negative) {
        ArrayList<String> pos = new ArrayList<>(positive);
        if (!predicate.equals("")) pos.add(predicate);
        if (workflow.equals("A"))
            paths.add(new Path(pos, negative));
        else
            workflows.get(workflow).rules
                    .forEach(r -> {
                        if (!r.workflow.equals("R")) {
                            ArrayList<String> neg = new ArrayList<>(negative);
                            List<Rule> rules = workflows.get(workflow).rules;
                            for (Rule rule : rules) {
                                if (rule.predicate.equals(r.predicate)) break;
                                if (!rule.predicate.isEmpty())
                                    neg.add(rule.predicate);
                            }
                            parse(r.workflow, r.predicate, pos, neg);
                        }
                    });
    }

    private boolean accept(Part part) {
        String result = workflows.get("in").apply(part);
        while (!result.equals("A") && !result.equals("R"))
            result = workflows.get(result).apply(part);
        return result.equals("A");
    }

    private long possible(Path path, String var) {
        return IntStream.rangeClosed(1, 4000)
                .filter(i -> path.positive.stream()
                        .filter(r -> r.startsWith(var))
                        .allMatch(r -> check(r, i)) && path.negative.stream()
                        .filter(r -> r.startsWith(var))
                        .noneMatch(r -> check(r, i)))
                .count();
    }

    public long calc() {
        return paths.stream()
                .mapToLong(p -> possible(p, "x")
                        * possible(p, "m")
                        * possible(p, "a")
                        * possible(p, "s"))
                .sum();
    }

    public int accepted() {
        return parts.stream().filter(this::accept).mapToInt(Part::value).sum();
    }

    static boolean check(String expr, int number) {
        return Utils.moreOrLess(expr.charAt(1)).test(number, Integer.parseInt(expr.substring(2)));
    }

    record Path(List<String> positive, List<String> negative) {}

    record Workflow(String name, List<Rule> rules) {
        String apply(Part part) {
            for (Rule rule: rules)
                if (rule.check.test(part))
                    return rule.workflow;
            return "";
        }
    }

    record Rule(String predicate, Predicate<Part> check, String workflow) {}

    record Part(int x, int m, int a, int s) {
        boolean check(String expr) {
            return Utils.moreOrLess(expr.charAt(1)).test(get(expr.charAt(0)), Integer.parseInt(expr.substring(2)));
        }

        static Part get(String vars) {
            Map<Character, Integer> values = Arrays.stream(vars.split("[,{}]+"))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toMap(s -> s.charAt(0), s -> Integer.parseInt(s.substring(2))));
            return new Part(values.get('x'), values.get('m'), values.get('a'), values.get('s'));
        }

        int get(char c) {
            return switch (c) {
                case 'x' -> x;
                case 'm' -> m;
                case 'a' -> a;
                case 's' -> s;
                default -> 0;
            };
        }

        int value() {
            return x + m + a + s;
        }
    }
}

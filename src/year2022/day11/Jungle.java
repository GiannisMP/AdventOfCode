package year2022.day11;

import com.google.common.collect.Lists;
import general.Advent;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Jungle extends Advent {

    private final Map<Integer, Monkey> monkeys = new HashMap<>();
    private Function<BigInteger, BigInteger> lowerWorry;

    private void handleMonkey(List<String> mLines) {
        Integer mId = Character.getNumericValue(mLines.get(0).charAt(mLines.get(0).length() - 2));
        String[] oTokens = mLines.get(2).split(" ");
        String operator = oTokens[oTokens.length - 2];
        Optional<BigInteger> operand = Optional.of(oTokens[oTokens.length - 1])
                .filter(op -> op.chars().allMatch(Character::isDigit))
                .map(Long::parseLong)
                .map(BigInteger::valueOf);
        String[] dTokens = mLines.get(3).split(" ");
        BigInteger divisible = BigInteger.valueOf(Long.parseLong(dTokens[dTokens.length - 1]));
        Integer ifTrue = Character.getNumericValue(mLines.get(4).charAt(mLines.get(4).length() - 1));
        Integer ifFalse = Character.getNumericValue(mLines.get(5).charAt(mLines.get(5).length() - 1));
        LinkedList<BigInteger> items = new LinkedList<>();
        Arrays.stream(mLines.get(1).split("[,\\s]\\s*"))
                .filter(item -> item.length() > 0 && item.chars().allMatch(Character::isDigit))
                .map(Long::parseLong)
                .map(BigInteger::valueOf)
                .forEach(items::add);

        monkeys.put(mId, new Monkey(items,
                i -> operator.equals("+") ? operand.orElse(i).add(i) : operand.orElse(i).multiply(i),
                divisible,
                ifTrue,
                ifFalse));
    }

    private void round() {
        monkeys.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .forEach(monkey -> monkey.play(lowerWorry).forEach(p -> monkeys.get(p.left()).addItem(p.right())));
    }
    private Function<BigInteger, BigInteger> moduloLowerWorry() {
        return i ->
                i.mod(monkeys.values()
                        .stream()
                        .map(Monkey::getDivisible)
                        .reduce(BigInteger::multiply)
                        .orElse(BigInteger.ONE));
    }
    public BigInteger play() {
        Lists.partition(lines, 7).forEach(this::handleMonkey);
//        lowerWorry = i -> i.divide(BigInteger.valueOf(3));
        lowerWorry = moduloLowerWorry();
        IntStream.range(0, 10000).forEach(i -> round());

        return monkeys.values()
                .stream()
                .map(Monkey::getCounter)
                .sorted(Comparator.reverseOrder())
                .limit(2)
                .map(BigInteger::valueOf)
                .reduce(BigInteger::multiply)
                .orElse(BigInteger.ZERO);
    }
}

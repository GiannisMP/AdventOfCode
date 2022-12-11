package year2022.day11;

import org.javers.common.collections.Pair;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Monkey {

    private final LinkedList<BigInteger> items;
    private final Function<BigInteger, BigInteger> operation;
    private final BigInteger divisible;
    private final Integer ifTrue;
    private final Integer ifFalse;
    private Integer counter = 0;

    public Monkey(LinkedList<BigInteger> items, Function<BigInteger, BigInteger> operation, BigInteger divisible, Integer ifTrue, Integer ifFalse) {
        this.items = items;
        this.operation = operation;
        this.divisible = divisible;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }
    public void addItem(BigInteger item) {
        items.add(item);
    }
    public List<Pair<Integer, BigInteger>> play(Function<BigInteger, BigInteger> lowerWorry) {
        List<Pair<Integer, BigInteger>> result = new ArrayList<>();
        while (!items.isEmpty()) {
            Optional.ofNullable(items.poll())
                    .map(operation)
                    .map(lowerWorry)
                    .map(item -> {
                        counter++;
                        return new Pair<>(item.mod(divisible).equals(BigInteger.ZERO) ? ifTrue : ifFalse, item);
                    }).ifPresent(result::add);
        }
        return result;
    }
    public BigInteger getDivisible() {
        return divisible;
    }
    public Integer getCounter() {
        return counter;
    }
}

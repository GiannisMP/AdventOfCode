package year2023.day7;

import general.Advent;
import general.Utils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Poker extends Advent {
    public long run(String order, boolean enableJoker) {
        List<Hand> hands = lines.stream()
                .map(line -> line.split(" "))
                .map(tokens -> new Hand(tokens[0], Long.parseLong(tokens[1]), order, enableJoker))
                .sorted(Comparator.comparing(Hand::value))
                .toList();
        return IntStream.range(0, hands.size()).mapToLong(i -> hands.get(i).bid * (i+1)).sum();
    }

    static class Hand {
        record Card(Character value, int power) {}
        public final List<Card> cards;
        public final long bid;
        private final Map<Character, Long> frequency;

        public Hand(String cards, long bid, String order, boolean enableJoker) {
            this.bid = bid;
            this.cards = Utils.chars(cards).stream().map(c -> new Card(c, order.length() - order.indexOf(c))).toList();
            this.frequency = this.cards
                    .stream()
                    .map(Card::value)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            if (enableJoker && frequency.containsKey('J') && frequency.keySet().size() > 1) {
                Long j = frequency.remove('J');
                frequency.entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .ifPresent(e -> frequency.put(e.getKey(), frequency.get(e.getKey()) + j));
            }
        }

        private double high() {
            return IntStream.range(0, cards.size())
                    .mapToDouble(i -> cards.get(i).power * Math.pow(15, cards.size() - i - 1))
                    .sum();
        }

        private double value() {
            return frequency
                    .values()
                    .stream()
                    .mapToDouble(count -> Math.pow(count, 2))
                    .sum() * 1000000 + high();
        }
    }
}

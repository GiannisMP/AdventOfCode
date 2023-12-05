package year2023.day4;

import general.Advent;
import general.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Lottery extends Advent {

    private final Map<Integer, ArrayList<Card>> cards;

    private Card parse(Utils.Record record) {
        String[] numbers = record.data().split("\\|");
        return new Card(record.id(), Utils.parseSpacedNumbers(numbers[0]), Utils.parseSpacedNumbers(numbers[1]));
    }

    public Lottery() {
        cards = lines.stream()
                .map(line -> Utils.parseRecord("Card", ":", line))
                .map(this::parse)
                .collect(Collectors.toMap(card -> card.id, card -> new ArrayList<>(List.of(card))));
    }

    public Double calculateScore() {
        return cards
                .values()
                .stream()
                .flatMap(Collection::stream)
                .mapToDouble(Card::getScore)
                .sum();
    }

    private void win(int id) {
        cards.get(id).add(cards.get(id).get(0));
    }

    private void win(int id, int number) {
        for (int i = id + 1; i <= id + number; i++) win(i);
    }

    public int calculateCards() {
        for (int i = 1; i <= cards.keySet().size(); i++)
            for (int j = 0; j < cards.get(i).size(); j++)
                win(i, cards.get(i).get(j).getWinCount());
        return cards.values().stream().mapToInt(List::size).sum();
    }

    static final class Card {
        private final int id;
        private final List<Integer> winningNumbers;
        private final List<Integer> selectedNumbers;
        private Integer winCount = null;

        Card(int id, List<Integer> winningNumbers, List<Integer> selectedNumbers) {
            this.id = id;
            this.winningNumbers = winningNumbers;
            this.selectedNumbers = selectedNumbers;
        }

        public List<Integer> getSelectedWinningNumbers() {
            return winningNumbers.stream().filter(selectedNumbers::contains).collect(Collectors.toList());
        }

        public Integer getWinCount() {
            if (winCount == null)
                winCount = getSelectedWinningNumbers().size();
            return winCount;
        }

        public double getScore() {
            return getSelectedWinningNumbers().size() > 0 ? Math.pow(2, getSelectedWinningNumbers().size() - 1) : 0;
        }

        public int id() {
            return id;
        }

        public List<Integer> winningNumbers() {
            return winningNumbers;
        }

        public List<Integer> selectedNumbers() {
            return selectedNumbers;
        }
    }

}

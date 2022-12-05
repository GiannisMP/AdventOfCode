package day1;

import general.Advent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Calories extends Advent {
    public int p2() {
        List<List<Integer>> caloriesPerElf = new ArrayList<>();
        int currentIndex = 0;
        for (String item : lines) {
            if (item.isEmpty())
                currentIndex++;
            else {
                if (caloriesPerElf.size() <= currentIndex) caloriesPerElf.add(new ArrayList<>());
                caloriesPerElf.get(currentIndex).add(Integer.parseInt(item));
            }
        }
        return caloriesPerElf
                .stream()
                .map(calories -> calories.stream().mapToInt(i -> i).sum())
                .sorted(Collections.reverseOrder())
                .limit(3)
                .mapToInt(i -> i)
                .sum();
    }
}
package year2021.day3;

import general.Advent;
import general.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Submarine extends Advent {
    private List<Bits> getBitList() {
        return lines.stream()
                .map(Utils::chars)
                .map(Bits::GetFromChars)
                .collect(Collectors.toList());
    }
    private Integer rate(Predicate<Bits> filter) {
        List<Bits> bitList = getBitList();
        List<Bits> temp = new ArrayList<>();
        for (Bits bits : bitList) {
            for (int j = 0; j < bits.get().size(); j++) {
                if (temp.size() <= j) temp.add(new Bits());
                temp.get(j).add(bits.get().get(j));
            }
        }
        return Bits.Get(temp.stream().map(filter::test).collect(Collectors.toList())).toInteger();
    }
    private Integer gamma() {
        return rate(Bits::mostCommon);
    }
    private Integer epsilon() {
        return rate(Bits::leastCommon);
    }
    private Integer rating(Predicate<Bits> filter) {
        List<Bits> bitList = getBitList();
        int position = 0;
        while (bitList.size() > 1) {
            int finalPosition = position;
            boolean flag = filter.test(
                    Bits.Get(bitList
                            .stream()
                            .map(Bits::get)
                            .map(b -> b.get(finalPosition))
                            .collect(Collectors.toList())));
            bitList = bitList.stream().filter(bits -> bits.get().get(finalPosition) == flag).collect(Collectors.toList());
            position++;
        }
        return bitList.get(0).toInteger();
    }
    private Integer oxygen() {
        return rating(Bits::mostCommon);
    }
    private Integer co2() {
        return rating(Bits::leastCommon);
    }
    public Integer rates() {
        return gamma() * epsilon();
    }
    public Integer ratings() {
        return oxygen() * co2();
    }
    public static void main(String[] args) {
        System.out.println(new Submarine().rates());
        System.out.println(new Submarine().ratings());
    }
}

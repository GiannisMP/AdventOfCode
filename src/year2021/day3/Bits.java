package year2021.day3;

import java.util.ArrayList;
import java.util.List;

public class Bits {
    private final ArrayList<Boolean> bits = new ArrayList<>();
    public void add(Boolean bit) {
        bits.add(bit);
    }
    public boolean mostCommon() {
        return bits.stream().filter(b -> b).count() >= bits.stream().filter(b -> !b).count();
    }
    public boolean leastCommon() {
        return !mostCommon();
    }
    public static Bits GetFromChars(List<Character> charBits) {
        Bits bits = new Bits();
        charBits.stream().map(ch -> ch.equals('1')).forEach(bits::add);
        return bits;
    }
    public static Bits Get(List<Boolean> charBits) {
        Bits bits = new Bits();
        charBits.forEach(bits::add);
        return bits;
    }
    public List<Boolean> get() {
        return bits;
    }
    public Integer toInteger() {
        int sum = 0;
        for (int i = 0; i < bits.size(); i++)
            sum += bits.get(i) ? Math.pow(2, bits.size() - i - 1) : 0;
        return sum;
    }
}

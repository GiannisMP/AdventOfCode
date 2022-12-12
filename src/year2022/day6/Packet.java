package year2022.day6;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Packet {

    private String data;
    private final int headerSize;
    private int foundIndex = 0;
    private final Predicate<String> allDifferent = s -> s.chars().distinct().count() == s.length();
    private final BiPredicate<String, Integer> packetMarkerDetected =
            (s, i) -> s.length() >= i && allDifferent.test(s.substring(s.length() - i));

    public Packet(int headerSize) {
        this.data = "";
        this.headerSize = headerSize;
    }

    public boolean append(int character) {
        data += (char) character;
        if (packetMarkerDetected.test(data, headerSize) && foundIndex == 0) foundIndex = data.length();
        return true;
    }

    public int getFoundIndex() {
        return foundIndex;
    }
}

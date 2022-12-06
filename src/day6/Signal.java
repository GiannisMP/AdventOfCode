package day6;

import general.Advent;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Signal extends Advent {

    private final Predicate<String> allDifferent = s -> s.chars().distinct().count() == s.length();
    private final BiPredicate<String, Integer> packetMarkerDetected =
            (s, i) -> s.length() >= i && allDifferent.test(s.substring(s.length() - i));

    public Integer findUniqueCharactersIndex(int size) {
        String line = lines.get(0);
        for (int i = size; i < line.length(); i++)
            if (packetMarkerDetected.test(line.substring(0, i), size))
                return i;

        return 0;
    }

    public Integer findUniqueCharactersIndexWithPacket(int size) {
        String line = lines.get(0);
        Packet packet = new Packet(size);
        line.chars().forEach(packet::append);
        return packet.getFoundIndex();
    }

}

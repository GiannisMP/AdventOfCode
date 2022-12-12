package general;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static List<Character> chars(String string) {
        return string.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
    }
}

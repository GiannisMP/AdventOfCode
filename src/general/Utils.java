package general;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {
    public static class Print {
        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_BLACK = "\u001B[30m";
        public static final String ANSI_RED = "\u001B[31m";
        public static final String ANSI_GREEN = "\u001B[32m";
        public static final String ANSI_YELLOW = "\u001B[33m";
        public static final String ANSI_BLUE = "\u001B[34m";
        public static final String ANSI_PURPLE = "\u001B[35m";
        public static final String ANSI_CYAN = "\u001B[36m";
        public static final String ANSI_WHITE = "\u001B[37m";

        public static String red(String s) {
            return ANSI_RED + s + ANSI_RESET;
        }

        public static String yellow(String s) {
            return ANSI_YELLOW + s + ANSI_RESET;
        }
    }

    public enum Direction { UP("^"), DOWN("v"), LEFT("<"), RIGHT(">");
        private final String value;

        Direction(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
    public record Point(long x, long y){

        public int intX() {
            return ((Long) x).intValue();
        }
        public int intY() {
            return ((Long) y).intValue();
        }
        @Override
        public int hashCode() {
            return ((Long) (x*10000 + y)).intValue();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Point && ((Point) obj).x() == x && ((Point) obj).y() == y;
        }
    }

    public record Cursor(long x, long y) {
        public Cursor move(Direction direction, long steps) {
            Cursor next;
            switch (direction) {
                case UP -> next = new Cursor(x, y - steps);
                case DOWN -> next = new Cursor(x, y + steps);
                case LEFT -> next = new Cursor(x - steps, y);
                case RIGHT -> next = new Cursor(x + steps, y);
                default -> next = new Cursor(x, y);
            }
            return next;
        }
    }

    public static BiPredicate<Integer, Integer> moreOrLess(Character comparator) {
        return comparator == '>' ? (a, b) -> a > b : (a, b) -> a < b;
    };

    public static long area(List<Point> points) {
        return Math.abs(IntStream.range(1, points.size())
                .mapToLong(i -> points.get(i-1).x() * points.get(i).y() - points.get(i).x() * points.get(i-1).y())
                .sum());
    }

    public static List<Character> chars(String string) {
        return string.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
    }

    public static Record parseRecord(String prefix, String delimiter, String data) {
        String withoutPrefix = data.substring(prefix.length());
        String[] tokens = withoutPrefix.split(delimiter);
        return new Record(Integer.parseInt(tokens[0].trim()), tokens[1]);
    }

    public static List<Integer> parseSpacedNumbers(String s) {
        return Arrays.stream(s.trim().split("\\s+"))
                .map(Integer::parseInt)
                .toList();
    }
    public static List<Long> parseSpacedLongNumbers(String s) {
        return Arrays.stream(s.trim().split("\\s+"))
                .map(Long::parseLong)
                .toList();
    }

    public static Long concat(List<Long> numbers) {
        return Long.parseLong(numbers.stream().map(Object::toString).collect(Collectors.joining()));
    }

    public static BigInteger lcm(List<BigInteger> numbers) {
        BigInteger gcd = numbers.stream().reduce(BigInteger::gcd).orElse(BigInteger.ONE);
        return numbers.stream().map(n -> n.divide(gcd)).reduce(BigInteger::multiply).map(n -> n.multiply(gcd)).orElse(BigInteger.ZERO);
    }

    public record Record(int id, String data){}
}

package year2023.day3;

import general.Advent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Engine extends Advent {
    private final int lineLength;
    private List<Gear> gears;
    private int adjacentSum = 0;
    private int gearRatio = 0;
    public Engine() {
        this.lineLength = lines.get(0).length();
    }

    public record Point(int x, int y){}

    static class Gear {
        private final List<Point> adjacentPoints;
        private final Point position;
        private List<Integer> parts = new ArrayList<>();

        public Gear(Point position, List<Point> adjacentPoints) {
            this.position = position;
            this.adjacentPoints = adjacentPoints;
        }

        public void addPart(int part) {
            parts.add(part);
        }

        public List<Point> getAdjacentPoints() {
            return adjacentPoints;
        }

        public boolean isValid() {
            return parts.size() == 2;
        }

        public int getRatio() {
            return parts.stream().reduce((a,b) -> a*b).orElse(0);
        }
    }

    public void findAdjacentPoints(Predicate<Character> check) {
        gears = new ArrayList<>();

        for(int i=0; i < lines.size(); i++){
            for(int j=0; j < lineLength; j++){
                if(check.test(lines.get(i).charAt(j))){
                    List<Point> adjacentPoints = new ArrayList<>();
                    adjacentPoints.add(new Point(i, j-1));
                    adjacentPoints.add(new Point(i, j+1));
                    adjacentPoints.add(new Point(i-1, j));
                    adjacentPoints.add(new Point(i-1, j-1));
                    adjacentPoints.add(new Point(i-1, j+1));
                    adjacentPoints.add(new Point(i+1, j));
                    adjacentPoints.add(new Point(i+1, j-1));
                    adjacentPoints.add(new Point(i+1, j+1));
                    gears.add(new Gear(new Point(i,j), adjacentPoints));
                }
            }
        }
    }

    public void execute() {
        adjacentSum = 0;
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lineLength; j++) {
                if(Character.isDigit(lines.get(i).charAt(j))){
                    int startIndex = j;
                    int number = Character.getNumericValue(lines.get(i).charAt(j));
                    while(j < lineLength - 1 && Character.isDigit(lines.get(i).charAt(++j))){
                        number = number * 10 + Character.getNumericValue(lines.get(i).charAt(j));
                    }

                    int endIndex = j-1;
                    int line = i;

                    for (Gear gear: gears) {
                        if (gear.getAdjacentPoints().stream().anyMatch(p -> p.x == line && p.y >= startIndex && p.y <= endIndex)) {
                            gear.addPart(number);
                            adjacentSum += number;
                        }
                    }
                }
            }
        }
        gearRatio = gears
                .stream()
                .filter(Gear::isValid)
                .mapToInt(Gear::getRatio)
                .sum();
    }

    public int getAdjacentSum() {
        return adjacentSum;
    }

    public int getGearRatio() {
        return gearRatio;
    }

    public static boolean isSymbol(Character c) {
        String specialChars = "/*!@#$%^&*()\"{}+-_[=]|\\?/<>,";
        return specialChars.indexOf(c) != -1;
    }
    public static boolean isGear(Character c) {
        return c == '*';
    }

}

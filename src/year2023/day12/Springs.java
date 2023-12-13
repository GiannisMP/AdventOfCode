package year2023.day12;

import general.Advent;
import general.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Springs extends Advent {

    private List<Spring> springs;

    public Springs() {
        springs = lines.stream()
                .map(line -> {
                    String[] tokens = line.split(" ");
                    List<Integer> ecc = Utils.parseSpacedNumbers(tokens[1].replace(",", " "));
                    return new Spring(tokens[0], ecc);
                }).collect(Collectors.toList());

    }

    public int processSpring(Spring spring) {
        List<Cursor> cursors = new ArrayList<>();
        for (Integer number : spring.ecc)
            cursors.add(new Cursor(cursors.isEmpty() ? 0 : cursors.get(cursors.size() - 1).location + cursors.get(cursors.size() - 1).size + 1, number, spring.state, spring));
        for (int i = 0; i < cursors.size() - 1; i++)
            cursors.get(i).setNext(cursors.get(i+1));
        spring.setCursors(cursors);
        while (cursors.get(0).move()) {
            System.out.println("moved");
        }
        return 0;
    }

    public void uniqueArrangements() {
        springs.forEach(this::processSpring);
    }

    static final class Spring {
        private final String state;
        private final List<Integer> ecc;
        private List<Cursor> cursors;

        private Set<String> validStates = new HashSet<>();

        Spring(String state, List<Integer> ecc) {
            this.state = state;
            this.ecc = ecc;
            this.cursors = new ArrayList<>();
        }

        public String state() {
            return state;
        }

        public void addCursor(Cursor cursor) {
            cursors.add(cursor);
        }

        public void setCursors(List<Cursor> cursors) {
            this.cursors = cursors;
        }

        public List<Integer> ecc() {
            return ecc;
        }

        public void checkIfValid() {
            if (stateIsValid()) validStates.add(cursors.stream().map(Cursor::getLocation).map(Objects::toString).collect(Collectors.joining()));
        }

        public boolean stateIsValid() {
            return cursors.stream().map(Cursor::stateIsValid).reduce(Boolean::logicalAnd).orElse(false);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Spring) obj;
            return Objects.equals(this.state, that.state) &&
                    Objects.equals(this.ecc, that.ecc);
        }

        @Override
        public int hashCode() {
            return Objects.hash(state, ecc);
        }

        @Override
        public String toString() {
            return "Spring[" +
                    "state=" + state + ", " +
                    "ecc=" + ecc + ']';
        }


        }

    static class Cursor {

        private int location;
        private int size;
        private Cursor next;

        private Spring parent;
        private String map;
        private int validStates = 0;

        public Cursor(int location, int size, String map, Spring parent) {
            this.location = location;
            this.map = map;
            this.size = size;
            this.parent = parent;
        }

        public int getLocation() {
            return location;
        }

        public void setLocation(int location) {
            this.location = location;
        }

        public Cursor getNext() {
            return next;
        }

        public void setNext(Cursor next) {
            this.next = next;
        }

        public String getMap() {
            return map;
        }

        public void setMap(String map) {
            this.map = map;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
        public boolean stateIsValid() {
            return !map.substring(location, location + size).contains(".");
        }

        public boolean move() {
            if (next != null) {
                if (next.move()) {
                    int nextLocation = next.location;
                    while (next.move()) {}
                    next.location = nextLocation;
                }
                if (next.location > location + size + 1) {
                    location++;
                    parent.checkIfValid();
                    return true;
                };
            } else {
                if (location + size < map.length()) {
                    location++;
                    parent.checkIfValid();
                    return true;
                }
            }
            return false;
        }
    }

}

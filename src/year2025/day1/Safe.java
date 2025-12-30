package year2025.day1;

import general.Advent;

public class Safe extends Advent {

    private SafeImpl safe;

    private void parseLine(String line) {
        char direction = line.charAt(0);
        int times = Integer.parseInt(line.substring(1));
        if (direction == 'R') safe.right(times);
        else safe.left(times);
    }

    public int p1() {
        safe = new SafeImpl();
        lines.forEach(this::parseLine);
        return safe.resetCount;
    }

    static class SafeImpl {
        public int resetCount = 0;
        public int low = 0;
        public int high = 99;
        public int current = 50;

        public void right(int times) {
            for (int i = 0; i < times; i++) {
                current++;
                if (current > high) {
                    current = low;
                    resetCount++;
                }
            }
        }

        public void left(int times) {
            for (int i = 0; i < times; i++) {
                current--;
                if (current < low) {
                    current = high;
                    resetCount++;
                }
            }
        }
    }

}

package year2023.day6;

import general.Advent;
import general.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Race extends Advent {

    private final List<Run> runs = new ArrayList<>();
    private final Run finalRun;

    public Race() {
        Function<Integer, List<Long>> parseNumbersFromLine =
                lineNumber -> Utils.parseSpacedLongNumbers(lines.get(lineNumber).split(":")[1]);
        List<Long> time = parseNumbersFromLine.apply(0);
        List<Long> distance = parseNumbersFromLine.apply(1);
        for (int i = 0; i < time.size() && i < distance.size(); i++) {
            runs.add(new Run(time.get(i), distance.get(i)));
        }
        finalRun = new Run(Utils.concat(time), Utils.concat(distance));
    }

    public long run() {
        return runs.stream().map(Run::calculateNumberOfBetterAlternatives).reduce((a,b) -> a*b).orElse(0L);
    }

    public long finalRun() {
        return finalRun.calculateNumberOfBetterAlternatives();
    }

    record Run(Long time, Long distance){
        public long getDistance(long charge, long time) {
            return charge >= time ? 0 : charge * (time-charge);
        }
        public long calculateNumberOfBetterAlternatives() {
            long count = 0;
            for (int i = 0; i < time; i++)
                if (getDistance(i, time) > distance) count++;
            return count;
        }
    };
}

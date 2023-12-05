package year2023.day5;

import general.Advent;
import general.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class Garden extends Advent {
    private final List<State> states = new ArrayList<>();

    public Garden() {
        states.add(new State("seed"));
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i).isBlank()) continue;
            String[] parts = lines.get(i).split("-to-");
            State state = new State(parts[1].split(" ")[0], get(parts[0]));
            i++;
            while (i < lines.size() && !lines.get(i).isBlank()) {
                List<Long> parsed = Utils.parseSpacedLongNumbers(lines.get(i));
                State.Transform transform = new State.Transform(parsed.get(1), parsed.get(2), parsed.get(0));
                state.addTransform(transform);
                i++;
            }
            states.add(state);
        }
    }

    private State get(String name) {
        return states.stream().filter(state -> state.name.equals(name)).findFirst().get();
    }

    public long calculate() {
        List<Long> seeds = Utils.parseSpacedLongNumbers(lines.get(0).split(":")[1]);
        List<CompletableFuture<Long>>futures = new ArrayList<>();
        for (int j=0; j<seeds.size(); j++)
            futures.add(createWorker(j, seeds.get(j),1, get("seed"), get("location")));
        return futures.stream().map(CompletableFuture::join).mapToLong(Long.class::cast).min().orElse(0);
    }
    public long calculateParallel() {
        //TODO: merge ranges so that only one map necessary and skip unnecessary checks
        List<Long> seeds = Utils.parseSpacedLongNumbers(lines.get(0).split(":")[1]);
        List<CompletableFuture<Long>>futures = new ArrayList<>();
        for (int j=0; j<seeds.size(); j+=2)
            futures.add(createWorker(j, seeds.get(j), seeds.get(j+1), get("seed"), get("location")));
        return futures.stream().map(CompletableFuture::join).mapToLong(Long.class::cast).min().orElse(0);
    }

    private CompletableFuture<Long> createWorker(int id, long start, long times, State begin, State end) {
        return CompletableFuture.supplyAsync(() -> {
            long time = System.currentTimeMillis();
            long min = Long.MAX_VALUE;
            for (long k = 0; k < times; k++) {
                long res = end.transform(start + k, begin);
                if (res < min) min = res;
                if (k % 10000000 == 0) System.out.println("Progress: " + k*100/times + "% (Worker #" + id + ")");
            }
            System.out.println("Worker #" + id + " finished after " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - time) + " seconds");
            return min;
        });
    }

    static class State {
        private final String name;
        private State from;
        private final List<Transform> transforms = new ArrayList<>();

        public State(String name, State from) {
            this.name = name;
            this.from = from;
        }
        public State(String name) {
            this.name = name;
        }

        public void addTransform(Transform transform) {
            transforms.add(transform);
        }

        public long transform(long value, State state) {
            if (!state.name.equals(name)) {
                value = from.transform(value, state);
            }
            return transform(value);
        }

        public long transform(long value) {
            return transforms
                    .stream()
                    .filter(transform -> value >= transform.from && value < transform.from + transform.range)
                    .findFirst()
                    .map(transform -> transform.execute(value))
                    .orElse(value);
        }

        record Transform(long from, long range, long to) {
            public long execute(long number) {
                return to + number - from;
            }
        }
    }

}

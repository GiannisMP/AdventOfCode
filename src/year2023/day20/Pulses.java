package year2023.day20;

import general.Advent;

import java.util.*;

public class Pulses extends Advent {
    public static final Map<String, Module.Base> modules = new HashMap<>();
    public static final ArrayList<Module.Event> eventBus = new ArrayList<>();
    private final Map<Module.Pulse, Long> history = new HashMap<>(Map.of(Module.Pulse.Low, 0L, Module.Pulse.High, 0L));
    private final Map<String, Set<Long>> high = new HashMap<>();
    private long rxLow = 0;

    private void button(long i) {
        eventBus.add(new Module.Event(Module.Pulse.Low, "broadcaster", "button"));
        while (!eventBus.isEmpty()) {
            Module.Event event = eventBus.remove(0);
            if (event.receiver().equals("rx")) {
                Module.Conjunction sender = (Module.Conjunction) modules.get(event.sender());
                sender.previous.entrySet()
                        .stream()
                        .filter(e -> e.getValue().equals(Module.Pulse.High))
                        .peek(e -> high.put(e.getKey(), high.getOrDefault(e.getKey(), new HashSet<>())))
                        .forEach(e -> high.get(e.getKey()).add(i));
                if (high.size() == sender.previous.size() && high.entrySet().stream().allMatch(e -> e.getValue().size() >= 2))
                    rxLow = high.values().stream()
                            .mapToLong(h -> h.stream().sorted().limit(2).reduce((a, b) -> a - b).orElse(0L))
                            .reduce((a,b) -> a * b)
                            .orElse(0L);
            }
            if (i <= 1000) history.put(event.pulse(), history.get(event.pulse()) + 1);
            Optional.ofNullable(modules.get(event.receiver())).ifPresent(m -> m.accept(event.pulse(), event.sender()));
        }
    }

    public void run() {
        long i = 0;
        while (rxLow == 0) button(++i);
    }

    public long getTotal() {
        return history.get(Module.Pulse.Low) * history.get(Module.Pulse.High);
    }

    public long getMinimumForRX() {
        return rxLow;
    }

    public Pulses() {
        lines.stream()
                .map(line -> line.split(" -> "))
                .map(t -> t[0].startsWith("%")
                            ? new Module.FlipFlop(t[0].substring(1), Arrays.stream(t[1].split(", ")).toList())
                            : t[0].startsWith("&")
                            ? new Module.Conjunction(t[0].substring(1), Arrays.stream(t[1].split(", ")).toList())
                            : new Module.Broadcaster(t[0],  Arrays.stream(t[1].split(", ")).toList()))
                .forEach(m -> modules.put(m.name, m));
        modules.values()
                .forEach(e ->
                        e.destinations.stream()
                                .map(modules::get)
                                .filter(Module.Conjunction.class::isInstance)
                                .map(Module.Conjunction.class::cast)
                                .forEach(d -> d.addInput(e.name)));
    }
}

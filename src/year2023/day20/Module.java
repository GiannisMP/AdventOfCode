package year2023.day20;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static year2023.day20.Pulses.eventBus;

public class Module {
    record Event(Pulse pulse, String receiver, String sender){}
    enum Pulse { High, Low }
    static class Conjunction extends Base {
        Map<String, Pulse> previous = new HashMap<>();
        Conjunction(String name, List<String> destinations) {
            super(name, destinations);
        }

        void addInput(String name) {
            previous.put(name, Pulse.Low);
        }

        @Override
        void accept(Pulse pulse, String sender) {
            previous.put(sender, pulse);
            if (previous.values().stream().anyMatch(Pulse.Low::equals)) send(Pulse.High);
            else send(Pulse.Low);
        }
    }

    static class Broadcaster extends Base {
        Broadcaster(String name, List<String> destinations) {
            super(name, destinations);
        }

        @Override
        void accept(Pulse pulse, String sender) {
            send(pulse);
        }
    }

    static class FlipFlop extends Base {
        boolean active = false;

        protected FlipFlop(String name, List<String> destinations) {
            super(name, destinations);
        }

        @Override
        void accept(Pulse pulse, String sender) {
            if (pulse.equals(Pulse.Low)) {
                if (active) {
                    active = false;
                    send(Pulse.Low);
                } else {
                    active = true;
                    send(Pulse.High);
                }
            }
        }
    }

    static abstract class Base {
        String name;
        List<String> destinations;

        protected Base(String name, List<String> destinations) {
            this.name = name;
            this.destinations = destinations;
        }
        abstract void accept(Pulse pulse, String sender);
        void send(Pulse pulse) {
            destinations.forEach(d -> eventBus.add(new Event(pulse, d, name)));
        }
    }
}

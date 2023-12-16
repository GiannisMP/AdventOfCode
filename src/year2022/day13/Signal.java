//package year2022.day13;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ArrayNode;
//import com.google.common.collect.Lists;
//import general.Advent;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class Signal extends Advent {
//
//    ObjectMapper objectMapper = new ObjectMapper();
//    int counter = 0;
//    List<Integer> results = new ArrayList<>();
//    public Optional<Boolean> compare(JsonNode left, JsonNode right) {
//        if (left == null) return Optional.of(true);
//        if (right == null) return Optional.of(false);
//        if (left.isInt() && right.isInt()) {
//            if (left.asInt() < right.asInt()) {
//                return Optional.of(true);
//            } else if (left.asInt() > right.asInt()) {
//                return Optional.of(false);
//            } else {
//                return Optional.empty();
//            }
//        } else if (left.isArray() && right.isArray()) {
//            for (int i = 0; i < left.size(); i++) {
//                if (right.size() == i) return Optional.of(false);
//                Optional<Boolean> flag = compare(left.get(i), right.get(i));
//                if (flag.isPresent()) return flag;
//            }
//            if (left.size() > right.size()) return Optional.of(false);
//            else if (left.size() < right.size()) return Optional.of(true);
//            else return Optional.empty();
//        } else if (left.isInt()) {
//            ArrayNode arrayNode = objectMapper.createArrayNode();
//            return compare(arrayNode.add(left.asInt()), right);
//        } else {
//            ArrayNode arrayNode = objectMapper.createArrayNode();
//            return compare(left, arrayNode.add(right.asInt()));
//        }
//    }
//
//    public JsonNode parsePacket(String line) {
//        JsonNode jsonNode = null;
//        try {
//            jsonNode = objectMapper.readTree(line);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        return jsonNode;
//    }
//
//    public void readPackets() {
//        Lists.partition(lines, 3)
//                .stream()
//                .map(lin -> compare(parsePacket(lin.get(0)), parsePacket(lin.get(1))))
//                .peek(__ -> counter++)
//                .forEach(lin -> lin.ifPresentOrElse(b -> {
//                    if (b) {
//                        results.add(counter);
//                    }
//                }, () -> results.add(counter)));
//    }
//    public Integer sortPackets() {
//        List<String> dividers = List.of("[[2]]", "[[6]]");
//        lines.addAll(dividers);
//        List<String> withDividers = lines.stream()
//                .filter(line -> !line.isEmpty())
//                .map(this::parsePacket)
//                .sorted((a, b) -> compare(a, b).filter(i -> i).map(__ -> -1).orElse(1))
//                .map(JsonNode::toString).toList();
//        return (withDividers.indexOf(dividers.get(0)) + 1) * (withDividers.indexOf(dividers.get(1)) + 1);
//    }
//
//    public static void main(String[] args) {
//        Signal signal = new Signal();
//        System.out.println(signal.sortPackets());
//    }
//}

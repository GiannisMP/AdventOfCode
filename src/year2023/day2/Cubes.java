package year2023.day2;

import general.Advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Cubes extends Advent {

    private List<Game> games;

    private boolean isValid(Game game, RGB limit) {
        return game.rounds.stream().allMatch(round ->
                round.red <= limit.red && round.green <= limit.green && round.blue <= limit.blue);
    }

    private RGB parseRound(String round) {
        Map<String, Integer> map =  Arrays.stream(round.split(", ")).map(v -> v.split(" ")).collect(Collectors.toMap(
                v -> v[1], v -> Integer.parseInt(v[0])));
        return new RGB(
                map.getOrDefault("red", 0),
                map.getOrDefault("green", 0),
                map.getOrDefault("blue", 0));
    }

    private RGB max(RGB current, RGB minimum) {
        return new RGB(
                Integer.max(current.red, minimum.red),
                Integer.max(current.green, minimum.green),
                Integer.max(current.blue, minimum. blue));
    }

    private RGB max(RGB currentMax, List<RGB> rest) {
        return rest.isEmpty()
                ? currentMax
                : max(max(rest.get(0), currentMax), rest.subList(1, rest.size()));
    }

    private Game parse(String line) {
        String withoutGame = line.substring(5);
        String[] tokens = withoutGame.split(": ");
        List<RGB> rounds = Arrays.stream(tokens[1].split("; "))
                .map(this::parseRound)
                .toList();
        return new Game(Integer.parseInt(tokens[0]), rounds, max(new RGB(0, 0, 0), rounds));
    }

    public Cubes() {
        games = lines
                .stream()
                .map(this::parse)
                .collect(Collectors.toList());
    }

    public int valid() {
        return games.stream()
                .filter(game -> isValid(game, new RGB(12, 13, 14)))
                .mapToInt(Game::id)
                .sum();
    }

    public int minimum() {
        return games.stream()
                .mapToInt(game -> game.minimum.red * game.minimum.green * game.minimum.blue)
                .sum();
    }

    record RGB(int red, int green, int blue){};
    record Game(int id, List<RGB> rounds, RGB minimum){};
}

package year2022.day7;

import java.util.List;

public class Command {
    public static final String LS = "ls";
    public static final String CD = "cd";

    private final String type;
    private final List<String> input;

    public Command(String type, List<String> input) {
        this.type = type;
        this.input = input;
    }

    public List<String> getInput() {
        return input;
    }

    public String getType() {
        return type;
    }
}

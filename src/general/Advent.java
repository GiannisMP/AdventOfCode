package general;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public abstract class Advent {

    protected List<String> lines;

    public Advent() {
        try {
            lines = Files.readAllLines(Paths.get("src/" + getClass().getPackage().getName().replace(".", "/") + "/input"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

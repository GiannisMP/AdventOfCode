import year2023.day1.Calibration;
import year2023.day2.Cubes;
import year2023.day3.Engine;

public class Main {

    public static void main(String[] args) {
//        System.out.println(new Calories().p2());
//        System.out.println(new RPS().p1());
//        System.out.println(new RPS().p2());
//        System.out.println(new Cleaning().findCompleteOverlaps());
//        System.out.println(new Crates().execute(false));
//        System.out.println(new Signal().findUniqueCharactersIndex(14));
//        System.out.println(new FileSystem().populate());
//        Forest forest = new Forest();
//        System.out.println(forest.findVisible());
//        System.out.println(forest.calculateMaxScenicValue());
//        System.out.println(new Rope(1).execute());
//        CPU cpu = new CPU(40);
//        System.out.println(cpu.execute());
//        cpu.draw();
//        Jungle jungle = new Jungle();
//        Calibration calibration = new Calibration();
//        System.out.println(calibration.p1());
//        Cubes cubes = new Cubes();
//        System.out.println(cubes.valid());
//        System.out.println(cubes.minimum());
        Engine engine = new Engine();
        engine.findAdjacentPoints(Engine::isGear);
        engine.execute();
        System.out.println(engine.getAdjacentSum());
        System.out.println(engine.getGearRatio());
    }
}

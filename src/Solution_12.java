import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Solution_12 {

    enum Direction {
        N, S, E, W, L, R, F;


        static Direction getNext(Direction d, List<Direction> order) {
            int i = order.indexOf(d);
            if(i < order.size()-1) return order.get(i+1);
            return order.get(0);
        }
        static Direction getRight(Direction d) {
            return getNext(d, List.of(N, E, S, W));
        }
        static Direction getLeft(Direction d) {
            return getNext(d, List.of(N, W, S, E));
        }
    }

    static class Instruction {
        Direction direction;
        int value;

        Instruction(Direction direction, int value) {
            this.direction = direction;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("Instruction: %s %d", direction, value);
        }
    }

    static class Waypoint {

        int x; // east-west
        int y; // south-north

        Waypoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public static Waypoint turnRight(int x, int y) {
            int temp_x; int temp_y;
            temp_x = Math.abs(y); temp_y = Math.abs(x);
            if(y >= 0) temp_x = -temp_x;
            if(x < 0) temp_y = -temp_y;
            return new Waypoint(temp_x, temp_y);
        }

        public static Waypoint turnLeft(int x, int y) {
            int temp_x; int temp_y;
            temp_x = Math.abs(y); temp_y = Math.abs(x);
            if(y < 0) temp_x = -temp_x;
            if(x >= 0) temp_y = -temp_y;
            return new Waypoint(temp_x, temp_y);
        }
    }

    private static List<Instruction> parseInput() {
        String path = InputPath.input_12.getPath();
        try {
            return Files.lines(Path.of(path)).map(s ->
                            new Instruction(
                                    Direction.valueOf(s.substring(0, 1)),
                                    Integer.parseInt(s.substring(1))))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    // answer: 1424
    public static int solve1() {
        int x, y;
        x = 0; y = 0;
        Direction shipDirection = Direction.E;

        for(Instruction ins : parseInput()) {
            Direction d = ins.direction == Direction.F ? shipDirection : ins.direction;
            switch (d) {
                case N -> y = y - ins.value;
                case S -> y += ins.value;
                case W -> x = x - ins.value;
                case E -> x += ins.value;
                case R -> {
                    int turnBy = Math.floorDiv(ins.value, 90);
                    for(int i = 0; i < turnBy; i++)
                        shipDirection = Direction.getRight(shipDirection);
                }
                case L -> {
                    int turnBy = Math.floorDiv(ins.value, 90);
                    for(int i = 0; i < turnBy; i++)
                        shipDirection = Direction.getLeft(shipDirection);
                }
            } //System.out.printf("%s [%d, %d]%n", shipDirection, x, y);
        }
        return Math.abs(x) + Math.abs(y);
    }

    // answer: 63447
    public static int solve2() {
        int x, y;
        x = 0; y = 0;

        Waypoint w = new Waypoint(10, -1);

        for(Instruction ins : parseInput()) {
            switch (ins.direction) {
                case F -> {
                    x += (w.x * ins.value);
                    y += + (w.y * ins.value);
                }
                case N -> w = new Waypoint(w.x, w.y - ins.value);
                case S -> w = new Waypoint(w.x, w.y + ins.value);
                case W -> w = new Waypoint(w.x - ins.value, w.y);
                case E -> w = new Waypoint(w.x + ins.value, w.y);
                case R -> {
                    int turnBy = Math.floorDiv(ins.value, 90);
                    for(int i = 0; i < turnBy; i++) {
                        w = Waypoint.turnRight(w.x, w.y);
                    }
                }
                case L -> {
                    int turnBy = Math.floorDiv(ins.value, 90);
                    for(int i = 0; i < turnBy; i++) {
                        w = Waypoint.turnLeft(w.x, w.y);
                    }
                }
            }
        }
        return Math.abs(x) + Math.abs(y);
    }

    public static void solveAll() {
        System.out.println(solve1());
        System.out.println(solve2());
    }
}

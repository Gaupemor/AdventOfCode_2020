import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Solution_10 {
    private static List<Integer> parseInput() {
        try {
            List<Integer> input = Files.lines(Path.of(InputPath.input_mock.getPath()))
                    .mapToInt(Integer::parseInt)
                    .sorted().boxed().collect(Collectors.toList());
            input.add(input.stream()
                    .max(Integer::compareTo).orElseThrow() + 3);
            return input;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    // answer: 1700
    public static int solve1() {
        List<Integer> adapters = parseInput();

        int[] joltsDiff = new int[4];
        int joltsBase = 0;
        for(int j : adapters) {
            joltsDiff[j - joltsBase]++;
            joltsBase = j;
        }

        return joltsDiff[1] * joltsDiff[3];
    }

    // answer:
    // num of valid arrangements between
    // for mock: 8
    public static int solve2() {
        //List<Integer> adapters = parseInput();

        return -1;
    }

    public static void solveAll() {
        System.out.println(solve1());
        System.out.println(solve2());
    }
}

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Solution_06 {

    private static String[] parseInput() {
        String path = InputPath.input_06.getPath();
        try {
            return Files.readAllLines(Path.of(path)).toArray(String[]::new);
        } catch (IOException e) {
            return new String[0];
        }
    }

    // answer: 6947
    public static int solve1() {
        int sum = 0;
        Set<Character> set = new HashSet<>();
        for(String line : parseInput()) {
            if(line.isBlank()) {
                sum += set.size();
                set.clear();
            } else for(char c : line.toCharArray()) set.add(c);
        } sum += set.size();

        return sum;
    }

    // answer: 3398
    public static int solve2() {
        int sum = 0;

        int i = 0;
        Map<Character, Integer> map = new HashMap<>();
        for(String line : parseInput()) {
            if(line.isBlank()) {
                int i_final = i;
                sum += map.entrySet().stream()
                        .filter(e -> e.getValue() == i_final)
                        .count();
                map.clear();
                i = 0;
            } else  {
                i++;
                for(Character c : line.toCharArray())
                    map.put(c, map.containsKey(c) ? map.get(c) + 1 : 1);
            }
        }
        int i_final = i;
        sum += map.entrySet().stream()
                .filter(e -> e.getValue() == i_final)
                .count();

        return sum;
    }

    public static void solveAll() {
        System.out.println(solve1());
        System.out.println(solve2());
    }
}

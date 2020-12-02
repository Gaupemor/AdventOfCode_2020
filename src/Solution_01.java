import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Solution_01 {

    private static int[] parseInput() {
        String path = "C:\\Users\\selma\\IdeaProjects\\AOC2020\\input\\input_01.txt";
        try {
            return Files.lines(Path.of(path))
                    .mapToInt(s -> Integer.parseInt(String.valueOf(s)))
                    .toArray();
        } catch (IOException e) {
            return new int[] {};
        }
    }

    // answer: 1013211
    public static int solve1() {
        int[] numbers = parseInput();
        for(int i = 0; i < numbers.length; i++) {
            for(int j = 0; j <= i; j++) {
                if(numbers[i] + numbers[j] == 2020) {
                    return numbers[i]*numbers[j];
                }
            }
        } return -1;
    }

    // answer: 13891280
    public static int solve2() {
        int[] numbers = parseInput();
        for(int i = 0; i < numbers.length; i++) {
            for(int j = 0; j <= i; j++) {
                for(int k = 0; k <= j; k++) {
                    if(numbers[i] + numbers[j] + numbers[k] == 2020) {
                        return numbers[i]*numbers[j]*numbers[k];
                    }
                }
            }
        } return -1;
    }

    public static void solveAll() {
        System.out.println(solve1());
        System.out.println(solve2());
    }
}

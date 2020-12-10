import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;

public class Solution_09 {

    private static long[] parseInput() {
        try {
            return Files.lines(Path.of(InputPath.input_09.getPath()))
                    .mapToLong(Long::parseLong).toArray();
        } catch (IOException e) {
            return new long[0];
        }
    }

    public static long weakness() {
        int preambleLen = 25;

        long[] numbers = parseInput();
        Queue<Long> preamble = new LinkedList<>();

        for(int i = 0; i < preambleLen; i++) preamble.add(numbers[i]);

        for(int i = preambleLen; i < numbers.length; i++) {
            long n = numbers[i];
            if(n >= preamble.stream().max(Long::compareTo).orElseThrow() * 2)
                return n;

            boolean sumExists = false;
            sumIter:
            for(long j : preamble) {
                for(long k : preamble) {
                    if((k != j) && (k + j == n)) {
                        sumExists = true;
                        break sumIter;
                    }
                }
            } if(!sumExists) return n;
            preamble.remove(); preamble.add(numbers[i]);
        } return 0;
    }

    // answer: 1504371145
    public static long solve1() {
        return weakness();
    }

    // answer: 183278487
    public static long solve2() {
        long[] numbers = parseInput();
        long weakness = weakness();
        long weaknessIndex = IntStream.range(0, numbers.length)
                .filter(i -> numbers[i] == weakness)
                .findFirst().orElseThrow();

        for(int i = 0; i < weaknessIndex; i++) {
            for(int j = 0; j < i; j++) {
                if(Arrays.stream(numbers, j, i + 1).sum() == weakness)
                    return Arrays.stream(numbers, j, i + 1).min().orElseThrow()
                            + Arrays.stream(numbers, j, i + 1).max().orElseThrow();
            }
        } return -1;
    }

    public static void solveAll() {
        System.out.println(solve1());
        System.out.println(solve2());
    }
}

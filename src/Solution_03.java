import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Solution_03 {
    private static boolean[][] parseInput() {
        String path = "C:\\Users\\selma\\IdeaProjects\\AOC2020\\input\\input_03.txt";

        try {
            String[] s = Files.readAllLines(Path.of(path)).toArray(new String[0]);
            boolean[][] b = new boolean[s.length][s[0].length()];
            for(int i = 0; i < b.length; i++) {
                char[] line = s[i].toCharArray();
                for(int j = 0; j < line.length; j++) {
                    b[i][j] = line[j] == '#';
                }
            } return b;

        } catch (IOException e) {
            return new boolean[0][0];
        }
    }

    private static int toboggan(int right, int down) {
        boolean[][] field = parseInput();

        int treeCount = 0;

        int pos = 0;
        int width = field[0].length;
        for(int i = down; i < field.length; i += down) {
            pos = pos + right;
            if(width <= pos) pos = pos - width;
            if(field[i][pos]) treeCount++;
        }

        return treeCount;
    }

    // answer: 191
    public static int solve1() {
        return toboggan(3, 1);
    }

    // answer: 1478615040
    public static int solve2() {
        return  toboggan(1, 1) *
                toboggan(3, 1) *
                toboggan(5, 1) *
                toboggan(7, 1) *
                toboggan(1, 2);
    }

    public static void solveAll() {
        System.out.println(solve1());
        System.out.println(solve2());
    }
}

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Solution_05 {

    private static List<BoardingPass> parseInput() {
        try {
            return Files.readAllLines(Path.of(InputPath.input_05.getPath()))
                    .stream().map(BoardingPass::new)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private static class BoardingPass {
        int row;
        int col;
        int id;

        BoardingPass(int row, int col) {
            this.row = row;
            this.col = col;
            id = calculateId(row, col);
        }
        
        BoardingPass(String boardingString) {
            char f = 'F';
            int maxRow = 127; int minRow = 0;
            for(char c : boardingString.substring(0, 6).toCharArray()) {
                int i = (maxRow + minRow + 1) / 2;
                if(c == f) maxRow = i - 1;
                else minRow = i;
            }
            row = boardingString.charAt(6) == f ? minRow : maxRow;

            char l = 'L';
            int maxCol = 7; int minCol = 0;
            for(char c: boardingString.substring(7, 9).toCharArray()) {
                int i = (maxCol + minCol + 1) / 2;
                if(c == l) maxCol = i - 1;
                else minCol = i;
            }
            col = boardingString.charAt(9) == l ? minCol : maxCol;

            id = calculateId(row, col);
        }

        private int calculateId(int row, int col) { return row * 8 + col; }
    }

    // answer: 906
    public static int solve1() {
        return parseInput().stream()
                .max(Comparator.comparing(bp -> bp.id))
                .get().id;
    }

    // answer: 519
    public static int solve2() {
        boolean[][] seats = new boolean[128][8];

        for(BoardingPass bp : parseInput()) {
            seats[bp.row][bp.col] = true;
        }

        int[][] unoccupied;

        List<int[]> u = new ArrayList<>();
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[0].length; j++) {
                if (!seats[i][j]) {
                    u.add(new int[]{i, j});
                }
            }
        }
        unoccupied = u.toArray(int[][]::new);

        for(int i = 0; i < unoccupied.length; i++) {
            int thisRow = unoccupied[i][0]; int thisCol = unoccupied[i][1];
            int prevRow = i != 0 ? unoccupied[i-1][0] : unoccupied[i][0];
            int nextRow = i != unoccupied.length-1 ? unoccupied[i+1][0] : unoccupied[i][0];

            if(thisRow != prevRow && thisRow != nextRow)
                return new BoardingPass(thisRow, thisCol).id;
        }

        return -1;
    }

    public static void solveAll() {
        System.out.println(solve1());
        System.out.println(solve2());
    }
}

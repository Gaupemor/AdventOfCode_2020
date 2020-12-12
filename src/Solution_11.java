import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

public class Solution_11 {

    static class Seat {
        boolean floor;
        boolean occupied = false;

        Seat(boolean floor) {
            this.floor = floor;
        }
    }

    private static Seat[][] parseInput() {
        try {
            return Files.lines(Path.of(InputPath.input_11.getPath()))
                    .map(s -> IntStream.range(0, s.length())
                            .mapToObj(i -> {
                                if(s.charAt(i) == 'L') return new Seat(false);
                                return new Seat(true);
                            }).toArray(Seat[]::new))
                    .toArray(Seat[][]::new);
        } catch (IOException e) {
            return new Seat[][] {};
        }
    }

    private static int neighbours(Seat[][] layout, int i, int j) {
        int neighbours = 0;
        if (i > 0 && layout[i-1][j].occupied) neighbours++; // above
        if (i < layout.length-1 && layout[i+1][j].occupied) neighbours++; // below
        if (j > 0 && layout[i][j-1].occupied) neighbours++; // left
        if (j < layout[i].length-1 && layout[i][j+1].occupied) neighbours++; // right
        if (i > 0 && j > 0 && layout[i-1][j-1].occupied) neighbours++; // top left
        if (i > 0 && j < layout[i].length-1 && layout[i-1][j+1].occupied) neighbours++; // top right
        if (i < layout.length-1 && j > 0 && layout[i+1][j-1].occupied) neighbours++; // bottom left
        if (i < layout.length-1 && j < layout[i].length-1 && layout[i+1][j+1].occupied) neighbours++; // bottom right
        return neighbours;
    }

    private static int visible(Seat[][] layout, int i, int j)
    {
        long visible = 0;

        int index;
        index = IntStream.range(0, i) // above
                .filter(index_row -> !layout[index_row][j].floor)
                .boxed().max(Comparator.naturalOrder()).orElse(-1);
        visible += index != -1 && layout[index][j].occupied ? 1 : 0;
        index = IntStream.range(i+1, layout.length) // below
                .filter(index_row -> !layout[index_row][j].floor)
                .boxed().min(Comparator.naturalOrder()).orElse(-1);
        visible += index != -1 && layout[index][j].occupied ? 1 : 0;
        index = IntStream.range(0, j) // left
                .filter(index_col -> !layout[i][index_col].floor)
                .boxed().max(Comparator.naturalOrder()).orElse(-1);
        visible += index != -1 && layout[i][index].occupied ? 1 : 0;
        index = IntStream.range(j+1, layout[i].length) // right
                .filter(index_col -> !layout[i][index_col].floor)
                .boxed().min(Comparator.naturalOrder()).orElse(-1);
        visible += index != -1 && layout[i][index].occupied ? 1 : 0;

        int i1, j1;
        i1 = i; j1 = j;
        while(i1 > 0 && j1 > 0) { //>= ? // top left
            i1--; j1--;
            if(!layout[i1][j1].floor) {
                if(layout[i1][j1].occupied) visible++;
                break;
            }
        }
        i1 = i; j1 = j;
        while(i1 > 0 && j1 < layout[i].length-1) { // top right
            i1--; j1++;
            if(!layout[i1][j1].floor) {
                if(layout[i1][j1].occupied) visible++;
                break;
            }
        }
        i1 = i; j1 = j;
        while(i1 < layout.length-1 && j1 > 0) { // bottom left
            i1++; j1--;
            if(!layout[i1][j1].floor) {
                if(layout[i1][j1].occupied) visible++;
                break;
            }
        }
        i1 = i; j1 = j;
        while(i1 < layout.length-1 && j1 < layout[i].length-1) { // bottom right
            i1++; j1++;
            if(!layout[i1][j1].floor) {
                if(layout[i1][j1].occupied) visible++;
                break;
            }
        }

        return Math.toIntExact(visible);
    }

    private static Seat[][] simulateWithNeighbours(Seat[][] layout, int tolerable) {
        boolean changed;
        do {
            changed = false;
            Seat[][] next = new Seat[layout.length][layout[0].length];
            for(int i = 0; i < layout.length; i++) {
                for(int j = 0; j < layout[i].length; j++) {
                    Seat currentSeat = layout[i][j];

                    if(currentSeat.floor)  {
                        next[i][j] = new Seat(true);
                        continue;
                    }

                    Seat newSeat = new Seat(false);
                    newSeat.occupied = currentSeat.occupied;

                    int neighbours = neighbours(layout, i, j);

                    if((!currentSeat.occupied && neighbours == 0) || (currentSeat.occupied && neighbours >= tolerable)) {
                        newSeat.occupied = !currentSeat.occupied;
                        changed = true;
                    }

                    next[i][j] = newSeat;

                }
            } layout = next;
        } while(changed);

        return layout;
    }

    private static Seat[][] simulateWithVisible(Seat[][] layout, int tolerable) {
        boolean changed;
        do {
            //printSeats(layout);
            changed = false;
            Seat[][] next = new Seat[layout.length][layout[0].length];
            for(int i = 0; i < layout.length; i++) {
                for(int j = 0; j < layout[i].length; j++) {
                    Seat currentSeat = layout[i][j];

                    if(currentSeat.floor)  {
                        next[i][j] = new Seat(true);
                        continue;
                    }

                    Seat newSeat = new Seat(false);
                    newSeat.occupied = currentSeat.occupied;

                    int neighbours = visible(layout, i, j);

                    if((!currentSeat.occupied && neighbours == 0) || (currentSeat.occupied && neighbours >= tolerable)) {
                        newSeat.occupied = !currentSeat.occupied;
                        changed = true;
                    }

                    next[i][j] = newSeat;

                }
            } layout = next;
        } while(changed);

        return layout;
    }

    private static void printSeats(Seat[][] layout) {
        for (Seat[] seats : layout) {
            for (Seat s : seats) {
                if (s.floor) System.out.print(".");
                else if (s.occupied) System.out.print("#");
                else System.out.print("L");
            }
            System.out.println();
        }
        System.out.println(); System.out.println();
    }

    // answer: 2476
    public static int solve1() {
        return Math.toIntExact(
                Arrays.stream(simulateWithNeighbours(parseInput(), 4))
                        .flatMap(Arrays::stream)
                        .filter(s -> s.occupied)
                        .count());
    }

    // answer: 2257
    public static int solve2() {
        return Math.toIntExact(
                Arrays.stream(simulateWithVisible(parseInput(), 5))
                        .flatMap(Arrays::stream)
                        .filter(s -> s.occupied)
                        .count());
    }

    public static void solveAll() {
        System.out.println(solve1());
        System.out.println(solve2());
    }
}

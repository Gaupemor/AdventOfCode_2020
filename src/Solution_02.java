import org.apache.commons.lang3.Range;
import org.javatuples.Triplet;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution_02 {
    private static List<Triplet<Character, Range<Integer>, String>> parseInput() {
        String path = "C:\\Users\\selma\\IdeaProjects\\AOC2020\\input\\input_02.txt";
        List<Triplet<Character, Range<Integer>, String>> l = new ArrayList<>();
        try (Scanner in = new Scanner(Path.of(path))) {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                String[] split = line.split(":");
                String s = split[1].strip();
                split = split[0].split(" ");
                char c = split[1].charAt(0);
                split = split[0].split("-");
                Range<Integer> r = Range.between(
                        Integer.parseInt(split[0]),
                        Integer.parseInt(split[1]));
                l.add(new Triplet<>(c, r, s));
            }
        } catch (IOException ignored) { }

        return l;
    }

    // answer: 564
    public static int solve1() {
        List<Triplet<Character, Range<Integer>, String>> passwords = parseInput();

        int i = 0;
        for(Triplet<Character, Range<Integer>, String> p : passwords) {
            if (p.getValue1().contains(
                    Math.toIntExact(
                            p.getValue2()
                            .chars()
                            .filter(ch -> ch == p.getValue0())
                            .count())))
                i++;
        } return i;
    }

    // answer: 325
    public static int solve2() {
        List<Triplet<Character, Range<Integer>, String>> passwords = parseInput();

        int i = 0;
        for(Triplet<Character, Range<Integer>, String> p : passwords) {
            char c = p.getValue0();
            Range<Integer> r = p.getValue1();
            String s = p.getValue2();
            if(s.charAt(r.getMinimum() - 1) == c
                    ^ s.charAt(r.getMaximum() - 1) == c)
                i++;
        } return i;
    }

    public static void solveAll() {
        System.out.println(solve1());
        System.out.println(solve2());
    }
}

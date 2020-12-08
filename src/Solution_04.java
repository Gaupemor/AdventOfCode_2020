import org.javatuples.KeyValue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution_04 {

    private static List<List<KeyValue<String, String>>> parseInput() {
        String path = InputPath.input_04.getPath();

        List<List<KeyValue<String, String>>> passports = new ArrayList<>();

        try (Scanner in = new Scanner(Path.of(path))) {

            List<KeyValue<String, String>> passport = new ArrayList<>();
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.isBlank()) {
                    passports.add(passport);
                    passport = new ArrayList<>();
                }
                else {
                    String[][] fields = Stream.of(line
                            .split(" "))
                            .map(str -> str.split(":"))
                            .toArray(String[][]::new);
                    for (String[] f : fields) {
                        passport.add(new KeyValue<>(f[0], f[1]));
                    }
                }
            } passports.add(passport);
        } catch (IOException ignored) { }
        return passports;
    }

    private static boolean realPassport(List<KeyValue<String, String>> passport) {
        return  (passport.size() == 8) ||
                (passport.size() == 7 &&
                (passport.stream().noneMatch(kv -> kv.getKey().equals("cid"))));
    }

    private static List<List<KeyValue<String, String>>> realPassports() {
        return parseInput().stream()
                .filter(Solution_04::realPassport)
                .collect(Collectors.toList());
    }

    private static boolean validPassport(List<KeyValue<String, String>> passport) {
       for(KeyValue<String, String> f : passport) {
            boolean valid;

            String k = f.getKey();
            String v = f.getValue();

            int v_int;
            try {
                v_int = Integer.parseInt(v);
            } catch (Exception e) {
                v_int = -1;
            }

            if ("byr".equals(k)) {
                valid = (v_int >= 1920) && (v_int <= 2002);
            } else if ("iyr".equals(k)) {
                valid = (v_int >= 2010) && (v_int <= 2020);
            } else if ("eyr".equals(k)) {
                valid = (v_int >= 2020) && (v_int <= 2030);
            } else if ("hgt".equals(k)) {
                String v_unit = "";
                try {
                    v_int = Integer.parseInt(v.substring(0, v.length() - 2));
                    v_unit = v.substring(v.length() - 2);
                } catch (Exception e) {
                    v_int = -1;
                }

                valid = (v_unit.equals("cm") && (v_int >= 150) && (v_int <= 193)) ||
                        (v_unit.equals("in") && (v_int >= 59) && (v_int <= 76));
            } else if ("hcl".equals(k)) {
                valid = v.matches("#[0-9Aa-f]{6}");
            } else if ("ecl".equals(k)) {
                valid = List.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
                        .stream().anyMatch(s -> s.equals(v));
            } else if ("pid".equals(k)) {
                valid = v.matches("\\d{9}");
            } else valid = "cid".equals(k);

            if (!valid) return false;
        } return realPassport(passport);
    }

    private static List<List<KeyValue<String, String>>> validPassports() {
        return parseInput().stream()
                .filter(Solution_04::validPassport)
                .collect(Collectors.toList());
    }

    // answer: 254
    public static int solve1() {
        return realPassports().size();
    }

    // answer: 184
    public static int solve2() {
        return validPassports().size();
    }

    public static void solveAll() {
        System.out.println(solve1());
        System.out.println(solve2());
    }
}
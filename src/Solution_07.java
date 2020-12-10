import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Solution_07 {

    static class Bag {
        String id;
        String hue;
        String colour;
        Map<String, Integer> contains;

        Bag(String hue, String colour, Map<String, Integer> contains) {
            this.hue = hue;
            this.colour = colour;
            this.contains = contains;
            id = hue+colour;
        }

        Bag(String hue, String colour) {
            this(hue, colour, new HashMap<>());
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append(String.format("%s %s bag", hue, colour));
            if(contains.size() > 0) s.append(" contains");
            for(Map.Entry<String, Integer> b : contains.entrySet())
                s.append(String.format("\n> %d %s", b.getValue(), b.getKey()));
            return s.toString();
        }
    }

    private static Bag[] parseInput() {
        String path = InputPath.input_07.getPath();
        try {
            return Files.lines(Path.of(path))
                    .map(l -> {
                        String[] divide = l.split("contain");
                        String[] bag = divide[0].trim().split(" ");
                        if(divide[1].contains("no other bags")) return new Bag(bag[0], bag[1]);
                        divide = divide[1].split(",");
                        Map<String, Integer> contains = new HashMap<>();
                        for(String s : divide) {
                            String[] conBagStr = s.trim().split(" ");
                            Bag conBag = new Bag(conBagStr[1], conBagStr[2]);
                            contains.put(conBag.id, Integer.parseInt(conBagStr[0]));
                        } return new Bag(bag[0], bag[1], contains);
                    }).toArray(Bag[]::new);
        } catch (IOException e) {
            return new Bag[0];
        }
    }

    private static Bag[] canHoldBag(String bagId, Bag[] rules) {
        List<Bag> canHoldDirectly = Arrays.stream(rules)
                .filter(b -> b.contains.containsKey(bagId))
                .collect(Collectors.toList());

        List<Bag> canHold = new ArrayList<>(canHoldDirectly);
        canHoldDirectly.stream()
                .map(b -> canHoldBag(b.id, rules))
                .forEach(b -> canHold.addAll(List.of(b)));

        return canHold.stream()
                .filter(distinctByKey(Bag::getId))
                .toArray(Bag[]::new);
    }

    private static int holdsNumberOfBagsAndSelf(String bagId, Bag[] rules) {
        Map<String, Integer> contains = Arrays.stream(rules)
                .filter(b -> b.id.equals(bagId))
                .findFirst().orElseThrow()
                .contains;

        return contains.entrySet().stream()
                .mapToInt(e -> holdsNumberOfBagsAndSelf(e.getKey(), rules) * e.getValue())
                .sum() + 1;
    }

    // Stateful predicate
    // Implementation from: baeldung.com
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    // answer: 348
    public static int solve1() {
        return canHoldBag("shinygold", parseInput()).length;
    }

    // answer: 18885
    public static int solve2() {
        return holdsNumberOfBagsAndSelf("shinygold", parseInput()) - 1;
    }

    public static void solveAll() {
        System.out.println(solve1());
        System.out.println(solve2());
    }
}

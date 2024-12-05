package in.ravir.day05;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
public class Day05 {

    @Setter
    private List<String> inputLines;

    private final Map<Integer, List<Integer>> rules = new HashMap<>();
    private final List<List<Integer>> updates = new ArrayList<>();
    private final List<List<Integer>> inCorrectOrder = new ArrayList<>();

    public void part1() {
        log.info("Day05, Part 01");
        parseInput();
        log.info("Answer Part 1: {}", updates.stream().mapToInt(this::isInOrder).sum());
    }

    public void part2() {
        log.info("Day05, Part 02");
        log.info("Answer Part 2: {}",  inCorrectOrder.stream().mapToInt(this::correctOrder).sum());
    }

    private Integer isInOrder(List<Integer> givenUpdate) {
        for (var rule:rules.entrySet()) {
            var x = rule.getKey();
            for (var y : rule.getValue()) {
                if (isWrong(givenUpdate, x, y)) {
                    inCorrectOrder.add(new ArrayList<>(givenUpdate));
                    return 0;
                }
            }
        }
        return givenUpdate.get((int) Math.ceil(givenUpdate.size()/2));
    }

    private int correctOrder(List<Integer> inCorrect) {
        return doTheCorrection(inCorrect).get((int) Math.ceil(inCorrect.size() / 2));
    }

    private List<Integer> doTheCorrection(List<Integer> inCorrect) {
        for (var rule:rules.entrySet()) {
            var x = rule.getKey();
            for(var y : rule.getValue()) {
                if (isWrong(inCorrect, x, y)) {
                    int indexX = inCorrect.indexOf(x);
                    int indexY = inCorrect.indexOf(y);

                    // Swap elements
                    inCorrect.set(indexX, y);
                    inCorrect.set(indexY, x);

                    // Recursive call to ensure all rules are satisfied
                    return doTheCorrection(inCorrect);
                }
            }
        }
        return inCorrect;
    }

    private static boolean isWrong(List<Integer> order, int x, int y) {
        return order.contains(x) && order.contains(y) && order.indexOf(x) > order.indexOf(y);
    }

    private void parseInput() {
        // Parse input here
        inputLines.forEach(line -> {
            if(line.isBlank()) return;

            if(line.contains("|")) {
                var parts = line.split("\\|");
                if(!rules.containsKey(Integer.parseInt(parts[0]))) {
                    rules.put(Integer.parseInt(parts[0]), new ArrayList<>());
                }

                rules.get(Integer.parseInt(parts[0])).add(Integer.parseInt(parts[1]));
            } else {
                // Do something else
                updates.add(Stream.of(line.split(",")).map(Integer::parseInt).toList());
            }
        });
    }

    @Test
    void test() {
        setInputLines(List.of(
                """
                        47|53
                        97|13
                        97|61
                        97|47
                        75|29
                        61|13
                        75|53
                        29|13
                        97|29
                        53|29
                        61|53
                        97|53
                        61|29
                        47|13
                        75|47
                        97|75
                        47|61
                        75|61
                        47|29
                        75|13
                        53|13
                        
                        75,47,61,53,29
                        97,61,53,29,13
                        75,29,13
                        75,97,47,61,53
                        61,13,29
                        97,13,75,29,47
                        """.split("\n")));

        part1();
        part2();
    }

}

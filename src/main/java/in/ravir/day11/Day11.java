package in.ravir.day11;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Day11 {

    @Setter
    private List<String> inputLines;

    private Map<Long, Long> stones;

    public void part1() {
        log.info("Day11, Part 01");

        parseInput();

        blink(25);

        log.info("Answer Part 1: {}", stones.values().stream().mapToLong(Long::longValue).sum());
    }

    public void part2() {
        log.info("Day11, Part 02");

        blink(50);

        log.info("Answer Part 2: {}", stones.values().stream().mapToLong(Long::longValue).sum());
    }

    private void blink(int times) {
        for (int i = 0; i < times; i++) {
            stones = run(stones);
        }
    }

    private static Map<Long, Long> run(Map<Long, Long> stones) {
        Map<Long, Long> newStones = new HashMap<>();

        for (Map.Entry<Long, Long> entry : stones.entrySet()) {
            long stone = entry.getKey();
            long count = entry.getValue();

            String stoneNumber = String.valueOf(stone);
            int length = stoneNumber.length();

            if (stone == 0) {
                newStones.put(1L, newStones.getOrDefault(1L, 0L) + count);
            } else if (length % 2 == 0) {
                long firstStone = Long.parseLong(stoneNumber.substring(0, length / 2));
                long secondStone =  Long.parseLong(stoneNumber.substring(length / 2));

                newStones.put(firstStone, newStones.getOrDefault(firstStone, 0L) + count);
                newStones.put(secondStone, newStones.getOrDefault(secondStone, 0L) + count);
            } else {
                long newStone = stone * 2024L;
                newStones.put(newStone, newStones.getOrDefault(newStone, 0L) + count);
            }
        }

        return newStones;
    }

    private void parseInput() {
        stones = new HashMap<>();
        for (String stoneStr : inputLines.get(0).split(" ")) {
            long stone = Long.parseLong(stoneStr);
            stones.put(stone, stones.getOrDefault(stone, 0L) + 1);
        }
    }

    @Test
    void test() {
        setInputLines(List.of("125 17"));

        part1();
        part2();
    }
}

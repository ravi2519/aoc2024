package in.ravir.day01;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class Day01 {

    @Setter
    private List<String> inputLines;

    private final List<Integer> leftList = new ArrayList<>();
    private final List<Integer> rightList = new ArrayList<>();

    public void part1() {
        log.info("Day01, Part 01");

        parseInput();

        long distance = 0L;
        for (int i = 0; i < leftList.size(); i++) {
            distance += Math.abs(leftList.get(i) - rightList.get(i));
        }

        log.info("Answer: {}", distance);

    }

    public void part2() {
        log.info("Day01, Part 02");

        var numberOfTimesMap = new HashMap<Integer, Long>();
        AtomicLong similarityScore = new AtomicLong();
        leftList.forEach(left -> {
            if(!numberOfTimesMap.containsKey(left)) {
                numberOfTimesMap.put(left, rightList.stream().filter(right -> Objects.equals(right, left)).count());
            }
            similarityScore.addAndGet(left * numberOfTimesMap.get(left));
        });

        log.info("Answer: {}", similarityScore.get());
    }

    private void parseInput() {
        inputLines.forEach(line -> {
            var content  = line.split(" {2}");
            leftList.add(Integer.parseInt(content[0].trim()));
            rightList.add(Integer.parseInt(content[1].trim()));
        });

        leftList.sort(Integer::compareTo);
        rightList.sort(Integer::compareTo);
    }

    @Test
    void testPart1() {
        inputLines = List.of("3  4", "4  3", "2  5", "1  3", "3  9", "3  3");
        part1();
        part2();
    }
}

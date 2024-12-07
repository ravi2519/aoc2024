package in.ravir.day07;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class Day07 {

    @Setter
    private List<String> inputLines;
    private boolean useConcatenation = false;

    public void part1() {
        log.info("Day07, Part 01");

        log.info("Answer Part  1 : {}", inputLines
                .stream()
                .mapToLong(this::hasValidOperators)
                .sum());

    }

    public void part2() {
        log.info("Day07, Part 02");
        useConcatenation = true;
        log.info("Answer Part  2 : {}", inputLines
                .stream()
                .mapToLong(this::hasValidOperators)
                .sum());
    }

    private long hasValidOperators(String s) {

        var testValue = Long.parseLong(s.split(": ")[0]);
        var testRule = Arrays.stream(s.split(": ")[1].split(" ")).mapToLong(Long::parseLong).toArray();

        if(testThisRule(testValue, 0, testRule, 0) == testValue) {
            return testValue;
        }

        return 0;
    }

    private long testThisRule(long testValue, long currValue, long[] testRule, int i) {

        if (i == testRule.length) {
            return currValue;
        }

        if(currValue > testValue) {
            return 0;
        }

        var left = testThisRule(testValue, currValue + testRule[i], testRule, i + 1);
        var right = testThisRule(testValue, currValue * testRule[i], testRule, i + 1);

        if(useConcatenation) {
            var concat = testThisRule(testValue, Long.parseLong(String.valueOf(currValue) + String.valueOf(testRule[i])), testRule, i + 1);
            if (left == testValue || right == testValue || concat == testValue) {
                return testValue;
            }
        } else {
            if (left == testValue || right == testValue) {
                return testValue;
            }
        }

        return 0;
    }

    @Test
    void test() {
        setInputLines(List.of(
                """
                        190: 10 19
                        3267: 81 40 27
                        83: 17 5
                        156: 15 6
                        7290: 6 8 6 15
                        161011: 16 10 13
                        192: 17 8 14
                        21037: 9 7 18 13
                        292: 11 6 16 20
                        """.split("\n")));

        part1();
        part2();
    }
}

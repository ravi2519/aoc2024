package in.ravir.day03;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class Day03 {

    @Setter
    private List<String> inputLines;

    public void part1() {
        log.info("Day03, Part 01");
        var input = String.join("", inputLines);
        log.info("Answer Part 1: {}", solve(input));
    }

    public void part2() {
        var input = String.join("", inputLines).replaceAll("don't\\(\\).*?do\\(\\)|don't\\(\\).*$", "");
        log.info("Answer Part 1: {}", solve(input));
    }

    private static long solve(String input) {
        Pattern pattern = Pattern.compile("mul\\(\\d+,\\d+\\)");
        Pattern numberPattern  = Pattern.compile("mul\\((\\d+),(\\d+)\\)");

        var matcher = pattern.matcher(input);
        var productSum = 0L;
        while (matcher.find()) {
            var numberMatcher = numberPattern.matcher(matcher.group());
            if (numberMatcher.find()) {
                productSum += (long) Integer.parseInt(numberMatcher.group(1)) * Integer.parseInt(numberMatcher.group(2));
            }
        }
        return productSum;
    }

    @Test
    void test () {
        log.info("Day03, Test");
        setInputLines(List.of("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"));
        part1();
        part2();
    }
}

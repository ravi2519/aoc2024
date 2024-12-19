package in.ravir.day19;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Day19 {

    @Setter
    private List<String> inputLines;

    private List<String> tDesigns = new ArrayList<>();
    private List<String> tPatterns = new ArrayList<>();

    public void part1() {
        log.info("Day19, Part 01");

        parseInput();

        long res = 0;
        for (String tPattern : tPatterns) {
            var dp = new boolean[tPattern.length() + 1];
            dp[0] = true;

            for (int i = 0; i < tPattern.length(); i++) {
                if (dp[i]) {
                    for (String tDesign : tDesigns) {
                        if (tPattern.startsWith(tDesign, i)) {
                            dp[i + tDesign.length()] = true;
                        }
                    }
                }
            }
            res += dp[tPattern.length()] ? 1 : 0;
        }

        log.info("Answer Part 1: {}", res);

    }

    public void part2() {
        log.info("Day19, Part 02");

        long res = 0;
        for (String tPattern : tPatterns) {
            long[] dp = new long[tPattern.length() + 1];
            dp[0] = 1;

            for (int i = 0; i < tPattern.length(); i++) {
                if (dp[i] > 0) {
                    for (String tDesign : tDesigns) {
                        if (tPattern.startsWith(tDesign, i)) {
                            dp[i + tDesign.length()] += dp[i];
                        }
                    }
                }
            }

            res += dp[tPattern.length()];
        }

        log.info("Answer Part 2: {}", res);
    }

    private void parseInput() {
        tDesigns = Arrays.stream(inputLines.get(0).split(", ")).toList();
        tPatterns = inputLines.subList(2, inputLines.size());
    }

    @Test
    void test() {
        setInputLines(List.of(
                """
                r, wr, b, g, bwu, rb, gb, br
                
                brwrr
                bggr
                gbbr
                rrbgbr
                ubwu
                bwurrg
                brgr
                bbrgwb
                """.split("\n")
        ));

        part1();
        part2();
    }
}

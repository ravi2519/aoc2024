package in.ravir.day22;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class Day22 {

    @Setter
    private List<String> inputLines;
    private final Map<BigInteger, BigInteger> secretNumberMap = new java.util.HashMap<>();

    public void part1() {
        log.info("Day22, Part 01");

        var result = BigInteger.ZERO;

        for (var line : inputLines) {
            var number = getSecretNumber(line);
            result = result.add(number);
        }

        log.info("Part 01: {}", result);

    }

    public void part2() {
        log.info("Day22, Part 02");

        Map<String, Map<List<Integer>, Integer>> sequenceMap = new java.util.HashMap<>();
        for (String line : inputLines) {
            sequenceMap.put(line, getSequence(line));
        }

        var distinctSequences = sequenceMap.values().stream().flatMap(v -> v.keySet().stream()).collect(Collectors.toSet());

        long max = Long.MIN_VALUE;
        for(var sequence : distinctSequences) {
            max = Math.max(max, sequenceMap
                    .values()
                    .stream()
                    .map(listIntegerMap -> listIntegerMap.getOrDefault(sequence, 0))
                    .mapToLong(Integer::longValue)
                    .sum());
        }

        log.info("Part 02: {}", max);
    }

    private BigInteger getSecretNumber(String line) {
        var number = new BigInteger(line);
        for (int i = 0; i < 2000; i++) {
            number = secretNumberMap.getOrDefault(number, getSecretNumber(number));
        }
        return number;
    }

    private Map<List<Integer>, Integer> getSequence(String line) {
        var prevPrice = new BigInteger(line);
        var differenceList = new java.util.ArrayList<Integer>();
        Map<List<Integer>, Integer> differenceMap = new java.util.HashMap<>();
        for (int i = 0; i < 2000; i++) {
            var currPrice = secretNumberMap.getOrDefault(prevPrice, getSecretNumber(prevPrice));
            differenceList.add(getOnesPlace(prevPrice) - getOnesPlace(currPrice));
            if(differenceList.size() == 4) {
                differenceMap.putIfAbsent(differenceList, getOnesPlace(currPrice));
                differenceList = new ArrayList<>(differenceList.subList(1, 4));
                assert differenceList.size() == 3;
            }
            prevPrice = currPrice;
        }
        return differenceMap;
    }

    private int getOnesPlace(BigInteger price) {
        return price.mod(BigInteger.TEN).intValue();
    }

    private BigInteger getSecretNumber(BigInteger a) {
        var number = a;
        number = number.shiftLeft(6).xor(number).mod(BigInteger.valueOf(16777216));
        number = number.shiftRight(5).xor(number).mod(BigInteger.valueOf(16777216));
        number = number.shiftLeft(11).xor(number).mod(BigInteger.valueOf(16777216));
        return number;
    }

    @Test
    void testPart1() {
        setInputLines(List.of("""
                1
                10
                100
                2024
                """.split("\n")));

        part1();
    }

    @Test
    void testPart2() {
        setInputLines(List.of("""
                1
                2
                3
                2024
                """.split("\n")));

        part2();
    }

    @Test
    void testSecretNumberGenerator() {
        assertThat(getSecretNumber(BigInteger.valueOf(123))).isEqualTo(BigInteger.valueOf(15887950));
    }
}

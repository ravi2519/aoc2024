package in.ravir.day02;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Day02 {

    @Setter
    private List<String> inputLines;

    private final List<List<Integer>> records = new ArrayList<>();

    public void part1() {
        log.info("Day02, Part 01");
        parseInput();

        long safe = records
                .stream()
                .filter(this::isSafe)
                .count();

        log.info("Answer Part 1: {}", safe);

    }

    public void part2() {
        log.info("Day02, Part 02");
        long safe = records
                .stream()
                .filter(this::isSafeWithDampener)
                .count();

        log.info("Answer: {}", safe);
    }

    private boolean isSafe(List<Integer> rec) {
        return isIncreasing(rec) ||  isDecreasing(rec);
    }

    private boolean isSafeWithDampener(List<Integer> rec) {
        return isIncreasingWithOneDampner(rec) || isDecreasingWithOneDampener(rec);
    }

    private boolean isIncreasing(List<Integer> rec) {
        for (int i = 0; i < rec.size() - 1; i++) {
            if (rec.get(i) >= rec.get(i + 1) || isNumberBetween(Math.abs(rec.get(i + 1) - rec.get(i)))) {
                return false;
            }
        }
        return true;
    }

    private boolean isDecreasing(List<Integer> rec) {
        for (int i = 0; i < rec.size() - 1; i++) {
            if (rec.get(i+1) >= rec.get(i) || isNumberBetween(Math.abs(rec.get(i + 1) - rec.get(i)))) {
                return false;
            }
        }
        return true;
    }

    private boolean isDecreasingWithOneDampener(List<Integer> rec) {

        var flag = -1;
        for (int i = 0; i < rec.size() - 1; i++) {
            if (rec.get(i+1) >= rec.get(i) || isNumberBetween(Math.abs(rec.get(i + 1) - rec.get(i)))) {
                flag = i;
                break;
            }
        }

        if(flag == -1 ) return true;

        return isDecreasing(removingCurrent(rec, flag))
                || isDecreasing(removingNext(rec, flag))
                || (flag != 0 && isDecreasing(removingPrevious(rec, flag)));
    }

    private static ArrayList<Integer> removingPrevious(List<Integer> rec, int flag) {
        var list3 = new ArrayList<>(rec);
        list3.set(flag, list3.get(flag - 1));
        list3.remove(flag - 1);
        return list3;
    }

    private static ArrayList<Integer> removingNext(List<Integer> rec, int flag) {
        var list2 = new ArrayList<>(rec);
        list2.remove(flag + 1);
        return list2;
    }

    private static ArrayList<Integer> removingCurrent(List<Integer> rec, int flag) {
        var list1 = new ArrayList<>(rec);
        list1.remove(flag);
        return list1;
    }

    private boolean isIncreasingWithOneDampner(List<Integer> rec) {
        var flag = -1;
        for (int i = 0; i < rec.size() - 1; i++) {
            if (rec.get(i+1) <= rec.get(i) || isNumberBetween(Math.abs(rec.get(i + 1) - rec.get(i)))) {
                flag = i;
                break;
            }
        }

        if (flag == -1) return true;

        return isIncreasing(removingCurrent(rec, flag))
                || isIncreasing(removingNext(rec, flag))
                || (flag != 0 && isIncreasing(removingPrevious(rec, flag)));
    }

    private static boolean isNumberBetween(int number) {
        return number <= 0 || number >= 4;
    }

    private void parseInput() {
        inputLines.forEach(line ->
            records.add(Arrays.stream(line.split(" ")).map(Integer::parseInt).toList())
        );
    }

    @Test
    void test() {
        inputLines = List.of("7 6 4 2 1", "1 2 7 8 9", "9 7 6 2 1", "1 3 2 4 5", "8 6 4 4 1", "1 3 6 7 9");
        part1();
        part2();
    }

}

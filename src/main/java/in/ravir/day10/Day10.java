package in.ravir.day10;

import javafx.util.Pair;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
public class Day10 {

    @Setter
    private List<String> inputLines;

    private int[][] input;
    private int rows;
    private int cols;
    private final List<Pair<Integer, Integer>> trailHeads = new java.util.ArrayList<>();
    private static final int[][] DIRS = {
            {1, 0},   // Right
            {0, 1},   // Down
            {-1, 0},  // Left
            {0, -1}   // Up
    };

    public void part1() {
        log.info("Day10, Part 01");
        parseInput();
        log.info("Answer Part 1: {}", getScore(true));
    }

    public void part2() {
        log.info("Day10, Part 02");
        log.info("Answer Part 2: {}", getScore(false));
    }

    private int getScore(boolean checkVisited) {
        var score = 0;
        for (var trailHead : trailHeads) {
            List<Pair<Integer, Integer>> visited = new java.util.ArrayList<>();
            int traverse = traverse(0, trailHead.getKey(), trailHead.getValue(), visited, checkVisited);
            score += traverse;
        }
        return score;
    }

    private int traverse(Integer currNumber, Integer key, Integer value, List<Pair<Integer, Integer>> visited, boolean checkVisited) {

        if(checkVisited) {
            visited.add(new Pair<>(key, value));
        }

        if(input[key][value] == 9) {
            return 1;
        }

        var score = 0;
        for (int[] dir : DIRS) {
            var newKey = key + dir[0];
            var newValue = value + dir[1];
            if(isNotOutOfBound(newKey, newValue) && input[newKey][newValue] == currNumber + 1 && !visited.contains(new Pair<>(newKey, newValue))) {
                score += traverse(currNumber + 1, newKey, newValue, visited, checkVisited);
            }
        }
        return score;
    }

    private boolean isNotOutOfBound(Integer key, Integer value) {
        if(key < 0 || key >= rows || value < 0 || value >= cols) {
            return false;
        }
        return true;
    }

    private void parseInput() {
        rows = inputLines.size();
        cols = inputLines.get(0).length();
        input = new int[rows][cols];
        for (int i = 0; i < inputLines.size(); i++) {
            var line = inputLines.get(i).split("");
            for (int j = 0; j < line.length; j++) {
                input[i][j] = Integer.parseInt(line[j]);
                if(input[i][j] == 0) {
                    trailHeads.add(new Pair<>(i, j));
                }
            }
        }
    }

    @Test
    void test() {
        setInputLines(List.of("""
                89010123
                78121874
                87430965
                96549874
                45678903
                32019012
                01329801
                10456732
                """.split("\n")));

        part1();
        part2();
    }
}

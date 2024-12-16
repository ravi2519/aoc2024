package in.ravir.day16;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

@Slf4j
public class Day16 {

    @Setter
    private List<String> inputLines;

    private String[][] map;
    private int[] start = new int[2];
    private int[] end = new int[2];
    private int rows;
    private int cols;

    private List<String> visited = new ArrayList<>();

    private static final int[][] DIRS = {
        {1, 0, 1000},   // Right
        {0, 1, 1},   // Down
        {-1, 0, 1},  // Left
        {0, -1, 1000}   // Up
    };

    public void part1() {
        log.info("Day16, Part 01");

        parseInput();

        var routes = new ArrayList<Integer>();
        for(var dir:DIRS) {
            var newX = start[0] + dir[0];
            var newY = start[1] + dir[1];

            if(map[newX][newY].equals(".")) {
                routes.add(visit(newX, newY, dir[2]));

                var ret = visit(newX, newY, dir[2]);
                if(ret != 0) {
                    routes.add(ret);
                }
            }
        }

        log.info("Answer Part 1 {}", routes.stream().min(Integer::compareTo));
    }

    private int visit(int startX, int startY, int score) {
        if(visited.contains(startX + "-" + startY)) {
            return 0;
        }

        visited.add(startX + "-" + startY);

        if(startX == end[0] && startY == end[1]) {
            return score;
        }

        if(map[startX][startY].equals("#")) {
            return 0;
        }

        var minScore = Integer.MAX_VALUE;
        for(var dir:DIRS) {
            var newX = startX + dir[0];
            var newY = startY + dir[1];

            if(map[newX][newY].equals(".")) {
                var ret = visit(newX, newY, score + dir[2]);
                if(ret != 0 && ret < minScore) {
                    minScore = ret;
                }
            }
        }

        if(minScore == Integer.MAX_VALUE) {
            return 0;
        } else {
            return minScore;
        }

    }



    public void part2() {
        log.info("Day16, Part 02");
    }

    private void parseInput() {
        rows = inputLines.size();
        cols = inputLines.get(0).length();

        map = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            var line = inputLines.get(i);
            if(line.contains("S")) {
                start[0] = i;
                start[1] = line.indexOf("S");
            } 

            if (line.contains("E")) {
                end[0] = i;
                end[1] = line.indexOf("E");
            }

            map[i] = line.split("");
        }
    }

    @Test
    void test_1() {

        setInputLines(List.of(
            """
            ###############
            #.......#....E#
            #.#.###.#.###.#
            #.....#.#...#.#
            #.###.#####.#.#
            #.#.#.......#.#
            #.#.#####.###.#
            #...........#.#
            ###.#.#####.#.#
            #...#.....#.#.#
            #.#.#.###.#.#.#
            #.....#...#.#.#
            #.###.#.#.#.#.#
            #S..#.....#...#
            ###############        
            """.split("\n")));

        part1();
        part2();

    }
}

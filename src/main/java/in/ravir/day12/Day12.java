package in.ravir.day12;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class Day12 {

    @Setter
    private List<String> inputLines;

    private static final int[][] DIRS = {
            {1, 0},   // Right
            {0, 1},   // Down
            {-1, 0},  // Left
            {0, -1}   // Up
    };

    private String[][] gardenPlot;
    private int rows;
    private int cols;

    public void part1() {
        log.info("Day12, Part 01");
        parseInput();
        log.info("Answer Part 1: {}", calculatePrice(false));

    }

    public void part2() {
        log.info("Day12, Part 02");
        log.info("Answer Part 2: {}", calculatePrice(true));
    }

    private int calculatePrice(boolean checkSides) {
        var total = 0;
        var visited = new HashSet<String>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                if(visited.contains(i + "-" + j)) {
                    continue;
                }

                var nodes = new HashSet<String>();
                nodes.add(i + "-" + j);

                getNodes(i, j, gardenPlot[i][j], visited, nodes);

                if(!nodes.isEmpty()) {
                    if(checkSides) {
                        var sides = getSides(nodes,  gardenPlot[i][j]);
                        total += nodes.size() * sides;
                    } else {
                        var perimeter = getPerimeter(nodes, gardenPlot[i][j]);
                        total += nodes.size() * perimeter;
                    }
                }
            }
        }
        return total;
    }

    private void getNodes(int x, int y, String currPlot, Set<String> visited, Set<String> nodes) {
        if(visited.contains(x + "-" + y)) {
            return;
        }
        visited.add(x + "-" + y);

        for (int[] dir : DIRS) {
            var newX = x + dir[0];
            var newY = y + dir[1];

            if(!isEdge(currPlot, newX, newY)) {
                nodes.add(newX + "-" + newY);
                getNodes(newX, newY, gardenPlot[newX][newY], visited, nodes);
            }
        }
    }

    private int getPerimeter(HashSet<String> nodes, String s) {
        var perimeter = 0;
        for(String node : nodes) {
            var x = Integer.parseInt(node.split("-")[0]);
            var y = Integer.parseInt(node.split("-")[1]);
            for (int[] dir : DIRS) {
                var newX = x + dir[0];
                var newY = y + dir[1];
                if(isEdge(s, newX, newY)) {
                    perimeter++;
                }
            }
        }
        return perimeter;
    }

    private int getSides(HashSet<String> nodes, String curr) {
        var corner = 0;
        for(String node : nodes) {
            var x = Integer.parseInt(node.split("-")[0]);
            var y = Integer.parseInt(node.split("-")[1]);

            for (var ns : new int[][]{{1, 0}, {-1, 0}}) {
                for (var ew : new int[][]{{0, -1}, {0, 1}}) {

                    if (isEdge(curr, x + ns[0], y + ns[1]) && isEdge(curr, x + ew[0], y + ew[1])) {
                        // is outward corner
                        corner++;
                    } else if (!isEdge(curr, x + ns[0], y + ns[1]) && !isEdge(curr, x + ew[0], y + ew[1]) && isEdge(curr, x + ns[0], y + ew[1])) {
                        // is inward corner
                        corner++;
                    }
                }
            }
        }
        return corner;
    }

    private boolean isEdge(String currPlant, int x, int y) {
        // out of bound
        if(x < 0 || y < 0 || x >= gardenPlot.length || y >= gardenPlot[0].length) {
            return true;
        }

        // different plant
        return !gardenPlot[x][y].equals(currPlant);
    }

    private void parseInput() {
        rows = inputLines.size();
        cols = inputLines.get(0).length();
        gardenPlot = new String[rows][cols];
        for (int i = 0; i < inputLines.size(); i++) {
            gardenPlot[i] = inputLines.get(i).split("");
        }
    }

    @Test
    public void test_1() {
        setInputLines(List.of("""
                AAAA
                BBCD
                BBCC
                EEEC
                """.split("\n")));

        part1();
        part2();
    }

    @Test
    public void test_2() {
        setInputLines(List.of("""
                OOOOO
                OXOXO
                OOOOO
                OXOXO
                OOOOO
                """.split("\n")));

        part1();
        part2();
    }

    @Test
    public void test_3() {
        setInputLines(List.of("""
             RRRRIICCFF
             RRRRIICCCF
             VVRRRCCFFF
             VVRCCCJFFF
             VVVVCJJCFE
             VVIVCCJJEE
             VVIIICJJEE
             MIIIIIJJEE
             MIIISIJEEE
             MMMISSJEEE
             """.split("\n")));

        part1();
        part2();
    }
}

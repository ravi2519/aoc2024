package in.ravir.day20;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

@Slf4j
public class Day20 {

    @Setter
    private List<String> inputLines;

    private final Set<List<Integer>> walls = new HashSet<>();
    private final int[] source = new int[2];
    private final int[] target = new int[2];
    private final int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    private int rows;
    private int cols;
    private final Map<String, Integer> steps = new HashMap<>();

    public void part1() {
        log.info("Day20, Part 01");
        parseInput();

        traverse(steps);

        var cheats = 0;
        for (String block : steps.keySet()) {
            var i = Integer.parseInt(block.split("-")[0]);
            var j = Integer.parseInt(block.split("-")[1]);
            for(var dir: dirs) {
                String block1 = ( i + dir[0]) + "-" + ( j + dir[1]);
                String block2 = ( i + 2 * dir[0]) + "-" + (j + 2 * dir[1]);

                if (!steps.containsKey(block1)
                        && steps.containsKey(block2)
                        && steps.get(block2) - steps.get(block) >= 102) {
                    cheats++;
                }
            }
        }

        log.info("Answer Part 1 - {}", cheats);

    }

    public void part2() {
        log.info("Day20, Part 02");

        int count = 0;
        for (String key : steps.keySet()) {
            String[] parts = key.split("-");
            int[] coords = {Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
            Set<String> potentials = allBlockCoords(coords);
            for (String otherKey : potentials) {
                String[] otherParts = otherKey.split("-");
                int[] otherCoords = {Integer.parseInt(otherParts[0]), Integer.parseInt(otherParts[1])};
                if (steps.get(otherKey) - steps.get(key) - manhattanDistance(coords, otherCoords) >= 100) {
                    count++;
                }
            }
        }

        log.info("Answer Part 2 - {}", count);
    }

    private int traverse(Map<String, Integer> steps) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Set<String> seen = new HashSet<>();
        queue.offer(new Node(0, source[0], source[1], 0));

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            String state = current.y + "," + current.x + "," + current.direction;

            if (seen.contains(state)) continue;
            if (current.y == target[0] && current.x == target[1]) return current.cost;

            seen.add(state);

            for(var dir: dirs) {
                int[] forward = {current.y + dir[0], current.x + dir[1]};

                if (notOutOfBound(forward) && !walls.contains(Arrays.asList(forward[0], forward[1]))) {
                    queue.offer(new Node(current.cost + 1, forward[0], forward[1], current.direction));
                    steps.put(forward[0] + "-" + forward[1], current.cost);
                }
            }
        }

        return -1;
    }

    private boolean notOutOfBound(int[] forward) {
        return forward[0] >= 0 && forward[0] < rows && forward[1] >= 0 && forward[1] < cols;
    }

    private Set<String> allBlockCoords(int[] blockCoords) {
        int i = blockCoords[0], j = blockCoords[1];
        Set<String> output = new HashSet<>();
        for (int distX = -20; distX <= 20; distX++) {
            int distXMax = 20 - Math.abs(distX);
            for (int distY = -distXMax; distY <= distXMax; distY++) {
                String block = (i + distX) + "-" + (j + distY);
                if (steps.containsKey(block)) {
                    output.add(block);
                }
            }
        }
        return output;
    }

    private int manhattanDistance(int[] coord1, int[] coord2) {
        return Math.abs(coord1[0] - coord2[0]) + Math.abs(coord1[1] - coord2[1]);
    }

    static class Node implements Comparable<Node> {
        int cost, y, x, direction;
        String prev;

        Node(int cost, int y, int x, int direction) {
            this(cost, y, x, direction, null);
        }

        Node(int cost, int y, int x, int direction, String prev) {
            this.cost = cost;
            this.y = y;
            this.x = x;
            this.direction = direction;
            this.prev = prev;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.cost, other.cost);
        }
    }

    private void parseInput() {
        rows = inputLines.size();
        cols = inputLines.get(0).length();
        for (int y = 0; y < rows; y++) {
            String line = inputLines.get(y);
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                switch (c) {
                    case '#':
                        walls.add(Arrays.asList(y, x));
                        break;
                    case 'S':
                        source[0] = y;
                        source[1] = x;
                        break;
                    case 'E':
                        target[0] = y;
                        target[1] = x;
                        break;
                }
            }
        }
    }

    @Test
    void test() {
        setInputLines(List.of(
                """
                ###############
                #...#...#.....#
                #.#.#.#.#.###.#
                #S#...#.#.#...#
                #######.#.#.###
                #######.#.#...#
                #######.#.###.#
                ###..E#...#...#
                ###.#######.###
                #...###...#...#
                #.#####.#.###.#
                #.#...#.#.#...#
                #.#.#.#.#.#.###
                #...#...#...###
                ###############
                """.split("\n")));

        part1();
        part2();
    }

}

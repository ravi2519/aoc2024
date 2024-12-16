package in.ravir.day16;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.junit.jupiter.api.Test;

@Slf4j
public class Day16 {

    @Setter
    private List<String> inputLines;
    private final Set<List<Integer>> walls = new HashSet<>();
    private final int[] source = new int[2];
    private final int[] target = new int[2];
    private final int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    private int bestCost = 0;

    public void part1() {
        log.info("Day16, Part 01");
        parseInput();
        bestCost = traverse(source, target, walls);
        log.info("Answer Part 1 - {}", bestCost);
    }

    public void part2() {
        log.info("Day16, Part 02");
        log.info("Answer Part 2 - {}", findAllPaths(source, target, bestCost, walls));
    }

    private int traverse(int[] source, int[] target, Set<List<Integer>> walls) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Set<String> seen = new HashSet<>();
        queue.offer(new Node(0, source[0], source[1], 0));

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            String state = current.y + "," + current.x + "," + current.direction;

            if (seen.contains(state)) continue;
            if (current.y == target[0] && current.x == target[1]) return current.cost;

            seen.add(state);

            int[] forward = {current.y + dirs[current.direction][0], current.x + dirs[current.direction][1]};
            if (!walls.contains(Arrays.asList(forward[0], forward[1]))) {
                queue.offer(new Node(current.cost + 1, forward[0], forward[1], current.direction));
            }
            queue.offer(new Node(current.cost + 1000, current.y, current.x, (current.direction + 1) % 4));
            queue.offer(new Node(current.cost + 1000, current.y, current.x, (current.direction + 3) % 4));
        }

        return -1;
    }

    private int findAllPaths(int[] source, int[] target, int targetCost, Set<List<Integer>> walls) {
        Map<String, Integer> bestCosts = new HashMap<>();
        Map<String, Set<String>> links = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>();

        queue.offer(new Node(0, source[0], source[1], 0, null));

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            String state = current.y + "," + current.x + "," + current.direction;

            if (current.cost > targetCost) break;
            if (bestCosts.containsKey(state)) {
                if (current.cost == bestCosts.get(state)) {
                    links.get(state).add(current.prev);
                }
                continue;
            }

            bestCosts.put(state, current.cost);
            links.computeIfAbsent(state, k -> new HashSet<>()).add(current.prev);

            String prev = state;
            int[] forward = {current.y + dirs[current.direction][0], current.x + dirs[current.direction][1]};
            if (!walls.contains(Arrays.asList(forward[0], forward[1]))) {
                queue.offer(new Node(current.cost + 1, forward[0], forward[1], current.direction, prev));
            }
            queue.offer(new Node(current.cost + 1000, current.y, current.x, (current.direction + 1) % 4, prev));
            queue.offer(new Node(current.cost + 1000, current.y, current.x, (current.direction + 3) % 4, prev));
        }

        // Walk backward from the target to record all tiles
        Set<String> routes = new HashSet<>();
        Set<List<Integer>> tiles = new HashSet<>();

        for (int d = 0; d < 4; d++) {
            walk(target[0] + "," + target[1] + "," + d, routes, tiles, links);
        }

        return tiles.size();
    }

    private void walk(String current, Set<String> routes, Set<List<Integer>> tiles, Map<String, Set<String>> links) {
        if (current != null && !routes.contains(current)) {
            routes.add(current);
            String[] parts = current.split(",");
            tiles.add(Arrays.asList(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));

            if (links.containsKey(current)) {
                for (String prev : links.get(current)) {
                    walk(prev, routes, tiles, links);
                }
            }
        }
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
        for (int y = 0; y < inputLines.size(); y++) {
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

    @Test
    void test_2() {

        setInputLines(List.of(
            """
#################
#...#...#...#..E#
#.#.#.#.#.#.#.#.#
#.#.#.#...#...#.#
#.#.#.#.###.#.#.#
#...#.#.#.....#.#
#.#.#.#.#.#####.#
#.#...#.#.#.....#
#.#.#####.#.###.#
#.#.#.......#...#
#.#.###.#####.###
#.#.#...#.....#.#
#.#.#.#####.###.#
#.#.#.........#.#
#.#.#.#########.#
#S#.............#
#################
            """.split("\n")));

        part1();
        part2();

    }
}

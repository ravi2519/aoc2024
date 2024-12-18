package in.ravir.day18;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

@Slf4j
public class Day18 {


    private final Set<List<Integer>> bLocation = new HashSet<>();
    private int[] source = new int[]{0, 0};
    private int[] target = new int[]{70, 70};
    private final int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    private int[][] fallingBytes;
    private int rows = 71;
    private int cols = 71;
    private int toSimulate = 1024;

    @Setter
    private List<String> inputLines;

    public void part1() {
        log.info("Day18, Part 01");

        parseInput();

        for (int i = 0; i < toSimulate; i++) {
            bLocation.add(Arrays.asList(fallingBytes[i][0], fallingBytes[i][1]));
        }

        var bestSteps = traverse();

        log.info("Answer Part 1 - {}", bestSteps);

    }

    public void part2() {
        log.info("Day18, Part 02");
        for (int i = toSimulate; i < fallingBytes.length; i++) {
            bLocation.add(Arrays.asList(fallingBytes[i][0], fallingBytes[i][1]));
            if(traverse() == -1 ) {
                log.info("Answer Part 2 - {},{}", fallingBytes[i][0], fallingBytes[i][1]);
                break;
            }
        }
    }

    private int traverse() {
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
                if (notOutOfBout(forward) && !bLocation.contains(Arrays.asList(forward[0], forward[1]))) {
                    queue.offer(new Node(current.cost + 1, forward[0], forward[1], current.direction));
                }
            }
        }

        return -1;
    }

    private boolean notOutOfBout(int[] forward) {
        return forward[0] >= 0 && forward[0] < rows && forward[1] >= 0 && forward[1] < cols;
    }


    private void parseInput() {
        fallingBytes = new int[inputLines.size()][2];

        for (int i = 0; i < inputLines.size(); i++) {
            String line = inputLines.get(i);
            String[] parts = line.split(",");
            fallingBytes[i] = new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
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

    @Test
    void test() {
        setInputLines(List.of(
                """
                        5,4
                        4,2
                        4,5
                        3,0
                        2,1
                        6,3
                        2,4
                        1,5
                        0,6
                        3,3
                        2,6
                        5,1
                        1,2
                        5,5
                        2,5
                        6,5
                        1,4
                        0,4
                        6,4
                        1,1
                        6,1
                        1,0
                        0,5
                        1,6
                        2,0
                        """.split("\n")));
        rows = 7;
        cols = 7;
        toSimulate = 12;
        target = new int[]{6, 6};
        part1();
        part2();
    }
}

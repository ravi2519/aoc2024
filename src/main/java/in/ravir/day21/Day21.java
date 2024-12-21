package in.ravir.day21;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

@Slf4j
public class Day21 {

    @Setter
    private List<String> inputLines;

    private final Integer[] wall =  new Integer[]{3, 0};
    private Integer[] source = new Integer[2];
    private Integer[] target = new Integer[2];
    private final Integer[][] dirs = {{0, 1, 0}, {1, 0, 1}, {0, -1, 2}, {-1, 0, 3}};
    private final int rows = 4;
    private final int cols = 3;
    private final Map<String, Integer[]> sourceMap = new HashMap<>();
    private final Map<String, String> directionMap = Map.of(
            "0" , ">",
            "1", "V",
            "2", "<",
            "3", "^"
    );
    private Map<String, List<String>> movesRequired =new HashMap<>();
    private List<String> robot1Steps = new ArrayList<>();

    public void part1() {
        log.info("Day21, Part 01");

        parseInput();

        log.info("Answer Part 1 - {}", saveHistorians(2));
    }

    public void part2() {
        log.info("Day21, Part 02");

        log.info("Answer Part 2 - {}", saveHistorians(25));
    }

    private long saveHistorians(int nrRobots) {
        long ans = 0L;
        for (String line : inputLines) {
            List<String> steps = new ArrayList<>();
            var parts = line.split("");
            source = sourceMap.get("A");
            for (String part : parts) {
                target = sourceMap.get(part);
                traverse(steps);
                source = target;
            }

            for (int i = 0; i < nrRobots; i++) {
                robot1Steps = getRobot1Steps(steps);
                steps = robot1Steps;
            }
            ans += (long) robot1Steps.size() * Integer.parseInt(line.substring(0, line.length() - 1));
            robot1Steps.clear();
        }
        return ans;
    }

    private ArrayList<String> getRobot1Steps(List<String> steps) {
        var robot1Steps = new ArrayList<String>();
        var start = "A";
        for(String step: steps) {
            if(start.equals(step)) {
                robot1Steps.add("A");
            } else if(movesRequired.containsKey(start + "-" + step)) {
                robot1Steps.addAll(movesRequired.get(start + "-" + step));
            } else if(movesRequired.containsKey(step + "-" + start)) {
                robot1Steps.addAll(movesRequired.get(step + "-" + start));
            } else {
                log.error("No moves found for {} - {}", start, step);
            }

            start = step;
        }
        return robot1Steps;
    }

    private int traverse(List<String> steps) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Set<String> seen = new HashSet<>();
        Node startNode = new Node(0, source[0], source[1], 0);
        queue.offer(startNode);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.x == target[0] && current.y == target[1]) {
                break;
            }

            for (var dir : dirs) {
                Integer[] forward = {current.x + dir[0], current.y + dir[1]};

                if (forward[0] == target[0] && forward[1] == target[1]) {
                    current.addNext(new Node(current.cost + 1, forward[0], forward[1], dir[2]));
                    break;
                }

                if (notOutOfBound(forward) && !seen.contains( forward[0] + "," +forward[1] + "," + dir[2])) {
                    current.addNext(new Node(current.cost + 1, forward[0], forward[1], dir[2]));
                    seen.add(forward[0] + "," +forward[1] + "," + dir[2]);
                }
            }

            for (Node next : current.nextNodes) {
                queue.offer(next);
            }
        }

        var localSteps = new ArrayList<String>();
        visit(startNode, target, localSteps);
        Collections.reverse(localSteps);
        steps.addAll(localSteps);
        return -1;
    }

    private boolean visit(Node startNode, Integer[] target, List<String> steps) {
        if (startNode.x == target[0] && startNode.y == target[1]) {
            steps.add("A");
            return true;
        }

        for (Node next : startNode.nextNodes) {
            if (visit(next, target, steps)) {
                steps.add(directionMap.get(String.valueOf(next.direction)));
                return true;
            }
        }

        return false;
    }

    private boolean notOutOfBound(Integer[] forward) {
        return (forward[0] >= 0 && forward[0] < rows && forward[1] >= 0 && forward[1] < cols) && (!Arrays.equals(forward, wall));
    }

    static class Node implements Comparable<Node> {
        int cost, x, y, direction;
        List<Node> nextNodes = new ArrayList<>();

        Node(int cost, int x, int y, int direction) {
            this.cost = cost;
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        public void addNext(Node next) {
            this.nextNodes.add(next);
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.cost, other.cost);
        }
    }

    private void parseInput() {
        sourceMap.put("7", new Integer[]{0, 0});
        sourceMap.put("8", new Integer[]{0, 1});
        sourceMap.put("9", new Integer[]{0, 2});
        sourceMap.put("4", new Integer[]{1, 0});
        sourceMap.put("5", new Integer[]{1, 1});
        sourceMap.put("6", new Integer[]{1, 2});
        sourceMap.put( "1", new Integer[]{2, 0});
        sourceMap.put("2", new Integer[]{2, 1});
        sourceMap.put("3", new Integer[]{2, 2});
        sourceMap.put("0", new Integer[]{3, 1});
        sourceMap.put("A", new Integer[]{3, 2});

        movesRequired.put("A-^", List.of("<", "A"));
        movesRequired.put("^-A", List.of(">", "A"));
        movesRequired.put("A-<", List.of("V", "<", "<", "A"));
        movesRequired.put("<-A", List.of(">", ">", "^", "A"));
        movesRequired.put("A-V", List.of("<", "V", "A"));
        movesRequired.put("V-A", List.of(">", "^", "A"));
        movesRequired.put("A->", List.of("V", "A"));
        movesRequired.put(">-A", List.of("^", "A"));
        movesRequired.put("^-<", List.of("V", "<", "A"));
        movesRequired.put("<-^", List.of(">", "^", "A"));
        movesRequired.put("^-V", List.of("V", "A"));
        movesRequired.put("V-^", List.of("^", "A"));
        movesRequired.put("^->", List.of("V", ">", "A"));
        movesRequired.put(">-^", List.of("<", "^", "A"));
        movesRequired.put("<-V", List.of(">", "A"));
        movesRequired.put("V-<", List.of("<", "A"));
        movesRequired.put("<->", List.of(">", ">", "A"));
        movesRequired.put(">-<", List.of("<", "<", "A"));
        movesRequired.put("V->", List.of(">", "A"));
        movesRequired.put(">-V", List.of("<", "A"));

    }

    @Test
    public void testPart1() {
        setInputLines(List.of(
                """
                029A
                980A
                179A
                456A
                379A
                """.split("\n")
        ));
        part1();
    }


}

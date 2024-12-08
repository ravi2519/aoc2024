package in.ravir.day08;

import javafx.util.Pair;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class Day08 {

    @Setter
    private List<String> inputLines;

    private Map<String, List<Pair<Integer, Integer>>> antennas = new HashMap<>();
    private Set<String> antiNodes = new HashSet<>();

    public void part1() {
        log.info("Day08, Part 01");
        parseInput();

        for (var entry : antennas.values()) {
           var i = -1;
           while(i + 1 < entry.size()) {
               i++;
               countAntiNodes(entry.get(i), entry.subList(i + 1, entry.size()), false);
           }
        }

        log.info("Answer Part 1 : {}", antiNodes.size());

    }

    public void part2() {
        log.info("Day08, Part 02");
        antiNodes.clear();

        for (int i = 0; i < inputLines.size(); i++) {
            String line = inputLines.get(i);
            String[] parts = line.split("");
            for (int j = 0; j < parts.length; j++) {
                if (!parts[j].equals(".")) {
                    antiNodes.add(i + "=" + j);
                }
            }
        }

        for (var entry : antennas.values()) {
            var i = -1;
            while(i + 1 < entry.size()) {
                i++;
                countAntiNodes(entry.get(i), entry.subList(i + 1, entry.size()), true);
            }
        }

        log.info("Day08, Part 02 : {}", antiNodes.size());
    }

    private void countAntiNodes(Pair<Integer, Integer> integerIntegerPair, List<Pair<Integer, Integer>> pairs, boolean part2) {
        for (var pair : pairs) {
            if(part2) {
                countAntiNodesForTwoNodes2(integerIntegerPair, pair);
            } else {
                countAntiNodesForTwoNodes(integerIntegerPair, pair);
            }
        }
    }

    private void countAntiNodesForTwoNodes(Pair<Integer, Integer> node, Pair<Integer, Integer> nodeToCheck) {
        Pair<Integer, Integer> nodeOne;
        Pair<Integer, Integer> nodeTwo;
        if(node.getKey() <= nodeToCheck.getKey()) {
            nodeOne = node;
            nodeTwo = nodeToCheck;
        } else  {
            nodeOne = nodeToCheck;
            nodeTwo = node;
        }

        var dx = Math.abs(nodeTwo.getKey() - nodeOne.getKey());
        var dy = Math.abs(nodeTwo.getValue() - nodeOne.getValue());

         if(!Objects.equals(nodeOne.getKey(), nodeTwo.getKey())) {
           if(nodeOne.getValue() < nodeTwo.getValue()) {
              // node 1 is top left and node 2 is bottom right
               checkBound(nodeOne.getKey() - dx, nodeOne.getValue() - dy);
               checkBound(nodeTwo.getKey() + dx, nodeTwo.getValue() + dy);
           } else if (nodeOne.getValue() > nodeTwo.getValue()) {
               // node 1 is top right and node 2 is bottom left
               checkBound(nodeOne.getKey() - dx, nodeOne.getValue() + dy);
               checkBound(nodeTwo.getKey() + dx, nodeTwo.getValue() - dy);
           } else {
               // are horizontally aligned
               checkBound(nodeOne.getKey() - dx, nodeOne.getValue());
               checkBound(nodeTwo.getKey() + dx, nodeTwo.getValue());
           }
       } else {
           checkBound(nodeOne.getKey(), nodeOne.getValue() - dy);
           checkBound(nodeTwo.getKey(), nodeTwo.getValue() + dy);
       }
    }

    private boolean checkBound(int dx, int dy) {
        boolean b = dx >= 0 && dx < inputLines.size() && dy >= 0 && dy < inputLines.get(0).length();
        if(b) {
            antiNodes.add(dx + "=" + dy);
        }
        return b;
    }

    private void countAntiNodesForTwoNodes2(Pair<Integer, Integer> node, Pair<Integer, Integer> nodeToCheck) {
        Pair<Integer, Integer> nodeOne;
        Pair<Integer, Integer> nodeTwo;
        if(node.getKey() <= nodeToCheck.getKey()) {
            nodeOne = node;
            nodeTwo = nodeToCheck;
        } else  {
            nodeOne = nodeToCheck;
            nodeTwo = node;
        }

        var dx = Math.abs(nodeTwo.getKey() - nodeOne.getKey());
        var dy = Math.abs(nodeTwo.getValue() - nodeOne.getValue());

        if(!Objects.equals(nodeOne.getKey(), nodeTwo.getKey())) {
            if(nodeOne.getValue() < nodeTwo.getValue()) {
                // node 1 is top left and node 2 is bottom right
                var x = nodeOne.getKey() - dx;
                var y = nodeOne.getValue() - dy;
                while(checkBound(x, y)) {
                    x -= dx;
                    y -= dy;
                }

                x = nodeOne.getKey() + dx;
                y = nodeOne.getValue() + dy;
                while(checkBound(x, y)) {
                    x += dx;
                    y += dy;
                }
            } else if (nodeOne.getValue() > nodeTwo.getValue()) {
                // node 1 is top right and node 2 is bottom left
                var x = nodeOne.getKey() - dx;
                var y = nodeOne.getValue() + dy;
                while(checkBound(x, y)) {
                    x -= dx;
                    y += dy;
                }

                x = nodeOne.getKey() + dx;
                y = nodeOne.getValue() - dy;
                while(checkBound(x, y)) {
                    x += dx;
                    y -= dy;
                }
            }
        }
    }

    private void parseInput() {
        for (int i = 0; i < inputLines.size(); i++) {
            String line = inputLines.get(i);
            String[] parts = line.split("");
            for (int j = 0; j < parts.length; j++) {
                if (!parts[j].equals(".")) {
                    if (!antennas.containsKey(parts[j])) {
                        antennas.put(parts[j], new ArrayList<>());
                    }
                    antennas.get(parts[j]).add(new Pair<>(i, j));
                }
            }
        }
    }

    @Test
    void test() {
        setInputLines(List.of(
                """
                        ............
                        ........0...
                        .....0......
                        .......0....
                        ....0.......
                        ......A.....
                        ............
                        ............
                        ........A...
                        .........A..
                        ............
                        ............
                        """.split("\n")));
        part1();
        part2();
    }

}

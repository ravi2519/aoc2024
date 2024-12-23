package in.ravir.day23;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class Day23 {

    @Setter
    private List<String> inputLines;
    private Map<String, Integer> nodes = new HashMap();
    private Map<String, List<String>> distinctNodes = new HashMap<>();

    public void part1() {
        log.info("Day23, Part 01");
        parseInput();

        var count = 0;

        List<List<String>> sets = new ArrayList<>();

        for (var key : distinctNodes.keySet().stream().filter(k -> k.startsWith("t")).toList()) {
            var neighbors = distinctNodes.get(key);
            for (int i = 0; i < neighbors.size(); i++) {
                var toCheck  = neighbors.get(i);
                for (int j = i + 1; j < neighbors.size(); j++) {
                    var neighbor = neighbors.get(j);
                    if((nodes.containsKey(neighbor + "-" + toCheck) || nodes.containsKey(toCheck + "-" + neighbor))
                    && sets.stream().noneMatch(s -> new HashSet<>(s).containsAll(List.of(neighbor, toCheck, key)))) {
                        count++;
                        sets.add(List.of(key, toCheck, neighbor));
                    }
                }
            }

        }

        log.info("Part 01: {}", count);

    }

    public void part2() {
        log.info("Day23, Part 02");

        Set<String> sets = new HashSet<>();
        Set<String> maxSets = new HashSet<>();
        List<String> allSets = new ArrayList<>();

        var max = Integer.MIN_VALUE;
        for (var node : distinctNodes.entrySet()) {
            var neighbors =node.getValue();
            for (int i = 0; i < neighbors.size(); i++) {
                var toCheck = neighbors.get(i);
                for (int j = i + 1; j < neighbors.size(); j++) {
                    var neighbor = neighbors.get(j);
                    if (distinctNodes.get(toCheck).contains(neighbor)) {
                        sets.add(node.getKey());
                        sets.add(toCheck);
                        sets.add(neighbor);
                    }
                }

                // check if the sets existed to start with
                var arr = sets.toArray();
                for (int j = 0; j < arr.length; j++) {
                    for (int k = 0; k < arr.length; k++) {
                        if(j != k && !(nodes.containsKey(arr[j] + "-" + arr[k]) || nodes.containsKey(arr[k] + "-" + arr[j]))) {
                                sets.clear();
                            }

                    }
                }

                if(sets.isEmpty()) {
                    continue;
                }

                if(sets.size() > max) {
                    maxSets = new HashSet<>(sets);
                    max = sets.size();
                }
                sets = new HashSet<>();
            }
        }

        log.info("Part 02: {}", maxSets.stream().sorted().collect(Collectors.joining(",")));
    }

    private void parseInput() {
        for (var line : inputLines) {
            nodes.put(line, 1);

            String left = line.split("-")[0];
            String right = line.split("-")[1];

            if(distinctNodes.containsKey(left)) {
                distinctNodes.get(left).add(right);
            } else {
                distinctNodes.put(left, new ArrayList<>(List.of(right)));
            }

            if(distinctNodes.containsKey(right)) {
                distinctNodes.get(right).add(left);
            } else {
                distinctNodes.put(right, new ArrayList<>(List.of(left)));
            }
        }
    }

    @Test
    void test() {
        setInputLines(List.of("""
                kh-tc
                qp-kh
                de-cg
                ka-co
                yn-aq
                qp-ub
                cg-tb
                vc-aq
                tb-ka
                wh-tc
                yn-cg
                kh-ub
                ta-co
                de-co
                tc-td
                tb-wq
                wh-td
                ta-ka
                td-qp
                aq-cg
                wq-ub
                ub-vc
                de-ta
                wq-aq
                wq-vc
                wh-yn
                ka-de
                kh-ta
                co-tc
                wh-qp
                tb-vc
                td-yn
                """.split("\n")));
        part1();
        part2();
    }
}

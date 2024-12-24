package in.ravir.day24;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Slf4j
public class Day24 {

    @Setter
    private List<String> inputLines;

    private final Map<String, Integer> inputs = new HashMap<>();
    private final List<List<String>> operations = new java.util.ArrayList<>();
    private ArrayList<String> zOpr;

    public void part1() {
        log.info("Day24, Part 01");
        parseInput();

        zOpr = new ArrayList<>(operations
                .stream()
                .filter(k -> k.stream().anyMatch(p -> p.startsWith("z")))
                .map(k -> k.stream().filter(p -> p.startsWith("z")))
                .flatMap(p -> p)
                .toList());
        var finishedOpr = 0;
         List<List<String>> fOperations = new java.util.ArrayList<>();

        while (finishedOpr != zOpr.size()) {
            for(var opr : operations) {
                if(fOperations.contains(opr)) {
                    continue;
                }
                if(inputs.containsKey(opr.get(0)) && inputs.containsKey(opr.get(2))) {
                    inputs.put(opr.get(3), switch (opr.get(1)) {
                        case "AND" -> inputs.get(opr.get(0)) & inputs.get(opr.get(2));
                        case "OR" -> inputs.get(opr.get(0)) | inputs.get(opr.get(2));
                        case "XOR" -> inputs.get(opr.get(0)) ^ inputs.get(opr.get(2));
                        default -> throw new IllegalArgumentException("Invalid operation: " + opr.get(1));
                    });
                    if(opr.get(3).startsWith("z")) finishedOpr++;
                    fOperations.add(opr);
                }
            }
        }

        zOpr.sort(new AlphanumericComparator());

        String z = new StringBuilder(
                zOpr.stream()
                        .map(k -> inputs.get(k) == 1 ? "1" : "0")
                        .collect(Collectors.joining(""))).reverse().toString();

        log.info("Part 01: {}", new BigInteger(z, 2));
    }

    public void part2() {
        log.info("Day24, Part 02");

        String highestZ = "z00";
        Set<String> wrong = new HashSet<>();
        for (var operation : operations) {
            String op1 = operation.get(0);
            String op =  operation.get(1);
            String op2 =  operation.get(2);
            String res =  operation.get(3);

            if (res.startsWith("z") && !op.equals("XOR") && !res.equals(highestZ)) {
                wrong.add(res);
            }
            if (op.equals("XOR") && !Set.of("x", "y", "z").contains(String.valueOf(res.charAt(0))) &&
                    !Set.of("x", "y", "z").contains(String.valueOf(op1.charAt(0))) &&
                    !Set.of("x", "y", "z").contains(String.valueOf(op2.charAt(0)))) {
                wrong.add(res);
            }
            if (op.equals("AND") && !"x00".equals(op1) && !"x00".equals(op2)) {
                for (var subOperation : operations) {
                    String subOp1 = subOperation.get(0);
                    String subOp = subOperation.get(1);
                    String subOp2 = subOperation.get(2);
                    if ((res.equals(subOp1) || res.equals(subOp2)) && !subOp.equals("OR")) {
                        wrong.add(res);
                    }
                }
            }
            if (op.equals("XOR")) {
                for (var subOperation : operations) {
                    String subOp1 = subOperation.get(0);
                    String subOp = subOperation.get(1);
                    String subOp2 = subOperation.get(2);
                    if ((res.equals(subOp1) || res.equals(subOp2)) && subOp.equals("OR")) {
                        wrong.add(res);
                    }
                }
            }
        }

        log.info("Part 02: {}", String.join(",", new TreeSet<>(wrong)));
    }

    private void parseInput() {
        var isInput = true;
        for (var line : inputLines) {
            if(line.isBlank()) {
                isInput = false;
                continue;
            }

            if(isInput) {
                var parts = line.split(": ");
                inputs.put(parts[0], Integer.parseInt(parts[1]));
            }else {
                operations.add(Arrays.stream(line.split(" ")).filter(p -> !p.isBlank() && !p.equals("->")).toList());
            }
        }
    }

    static class AlphanumericComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            int i = 0, j = 0;
            while (i < s1.length() && j < s2.length()) {
                char c1 = s1.charAt(i);
                char c2 = s2.charAt(j);

                if (Character.isDigit(c1) && Character.isDigit(c2)) {
                    int start1 = i, start2 = j;
                    while (i < s1.length() && Character.isDigit(s1.charAt(i))) i++;
                    while (j < s2.length() && Character.isDigit(s2.charAt(j))) j++;
                    int num1 = Integer.parseInt(s1.substring(start1, i));
                    int num2 = Integer.parseInt(s2.substring(start2, j));
                    if (num1 != num2) {
                        return num1 - num2;
                    }
                } else {
                    if (c1 != c2) {
                        return c1 - c2;
                    }
                    i++;
                    j++;
                }
            }
            return s1.length() - s2.length();
        }
    }

    @Test
    public void test1() {
        setInputLines(List.of("""
                x00: 1
                x01: 1
                x02: 1
                y00: 0
                y01: 1
                y02: 0
                
                x00 AND y00 -> z00
                x01 XOR y01 -> z01
                x02 OR y02 -> z02
                """.split("\n")));
        part1();
    }

    @Test
    public void test2() {
        setInputLines(List.of("""
             x00: 1
             x01: 0
             x02: 1
             x03: 1
             x04: 0
             y00: 1
             y01: 1
             y02: 1
             y03: 1
             y04: 1

             ntg XOR fgs -> mjb
             y02 OR x01 -> tnw
             kwq OR kpj -> z05
             x00 OR x03 -> fst
             tgd XOR rvg -> z01
             vdt OR tnw -> bfw
             bfw AND frj -> z10
             ffh OR nrd -> bqk
             y00 AND y03 -> djm
             y03 OR y00 -> psh
             bqk OR frj -> z08
             tnw OR fst -> frj
             gnj AND tgd -> z11
             bfw XOR mjb -> z00
             x03 OR x00 -> vdt
             gnj AND wpb -> z02
             x04 AND y00 -> kjc
             djm OR pbm -> qhw
             nrd AND vdt -> hwm
             kjc AND fst -> rvg
             y04 OR y02 -> fgs
             y01 AND x02 -> pbm
             ntg OR kjc -> kwq
             psh XOR fgs -> tgd
             qhw XOR tgd -> z09
             pbm OR djm -> kpj
             x03 XOR y03 -> ffh
             x00 XOR y04 -> ntg
             bfw OR bqk -> z06
             nrd XOR fgs -> wpb
             frj XOR qhw -> z04
             bqk OR frj -> z07
             y03 OR x01 -> nrd
             hwm AND bqk -> z03
             tgd XOR rvg -> z12
             tnw OR pbm -> gnj
                """.split("\n")));
        part1();
        part2();
    }

    @Test
    public void test3() {
        setInputLines(List.of("""
                x00: 0
                x01: 1
                x02: 0
                x03: 1
                x04: 0
                x05: 1
                y00: 0
                y01: 0
                y02: 1
                y03: 1
                y04: 0
                y05: 1
                
                x00 AND y00 -> z05
                x01 AND y01 -> z02
                x02 AND y02 -> z01
                x03 AND y03 -> z03
                x04 AND y04 -> z04
                x05 AND y05 -> z00
                """.split("\n")));
        part1();
        part2();
    }
}

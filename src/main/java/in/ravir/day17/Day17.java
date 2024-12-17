package in.ravir.day17;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Day17 {

    @Setter
    private List<String> inputLines;

    private BigInteger registerA = new BigInteger("0");
    private BigInteger registerB = new BigInteger("0");
    private BigInteger registerC  = new BigInteger("0");
    private int[] program;
    private int instr = 0;
    private String ansP1 = "";
    boolean debug = false;

    public void part1() {
        log.info("Day17, Part 01");
        parseInput();

        while (instr < program.length) {
            execute();
        }

        log.info("Answer Part 1 - {}", String.join(",", ansP1.split("")));
    }

    public void part2() {
        log.info("Day17, Part 02");

        /**
         * Register A: 35200350
         * Register B: 0
         * Register C: 0
         *
         * Program: 2,4,1,2,7,5,4,7,1,3,5,5,0,3,3,0
         *2412754713550330
         * 635277571101776
         * b = a % 8
         *52571111115135007570

         *  - 10000000877990 ----- 235217377760076
         *   - 35100006354844 ----- 734251177760076
         *7257111111130330
         *  11 - 4096 -  2 ^ 12
         *  12 - 512 - 2 ^ 9
         *  13 - 16 - 2 ^ 4
         *43771111117541175276
         *  2 ^ 4
         *  28081698772087747
         */
//        long a = 0;
//
//        var orig = new BigInteger("37177236917308");
//        orig = orig.subtract(new BigInteger("2010044694528"));
//
//        while(true) {
//            orig = orig.add(new BigInteger("1")); // 17179869184
//            if(orig.compareTo(BigInteger.ZERO) < 0) {
//                break;
//            }
//            registerA = orig;
//           registerB = BigInteger.valueOf(0L);
//            registerC = BigInteger.valueOf(0L);
//            instr = 0;
//            ansP1 = "";
//            while (instr < program.length) {
//                execute();
//            }
//
//            if(ansP1.endsWith("3550330")) {
//                log.info("{} ----- {}", orig.add(BigInteger.ONE).toString(), ansP1);
//            }
//            if(ansP1.equals("2412754713550330")) {
//                break;
//            }
//
////            if(!ansP1.endsWith("50330")) {
////                log.info("diff {}", orig.subtract(new BigInteger("37177236917308")));
////                break;
////            }
//
////            if(ansP1.length() > 16) {
////                log.info("size more than 16 diff {}", orig.subtract(new BigInteger("37177236917308")));
////                break;
////            }
//
//            if(ansP1.length() == 16) {
//                log.info("size less than 16 diff {}", orig.subtract(new BigInteger("37177236917308")));
//                break;
//            }
//        }


        Deque<BigInteger> candidates = new LinkedList<>();
        candidates.add(BigInteger.ONE);
        int minCandidate = (int) Math.pow(2, 3 * (program.length - 1));

        while (!candidates.isEmpty() && candidates.peekLast().intValue() < minCandidate) {
            var seed = candidates.pollFirst();
            for (int a = 0; a < (1 << 6); a++) {
                registerA = BigInteger.valueOf(a).add(seed.shiftLeft(6));
                registerB = BigInteger.ZERO;
                registerC = BigInteger.ZERO;
                instr = 0;
                ansP1 = "";
                execute();

                if (registerA.compareTo(BigInteger.valueOf(8)) < 0) {
                    ansP1 = "0 "+ ansP1;
                }

                if (ansP1.equals(Arrays.stream(Arrays.copyOfRange(program, program.length - 1 - ansP1.length(), program.length - 1)).mapToObj(String::valueOf).collect(Collectors.joining("")))) {
                    log.info("candidate found {} for ans {}", registerA, ansP1);
                    candidates.add(registerA);
                }
                if (ansP1.equals(String.join("", Arrays.stream(program).mapToObj(String::valueOf).collect(Collectors.joining(""))))) {
                    break;
                }
            }
        }

        log.info("Answer Part 2 - {}", candidates.pollLast());

    }

    private void execute() {
        int instruction = program[instr];
        int operand = program[instr + 1];
        instr += 2;
        if(instruction == 0) {
            registerA = registerA.divide(BigInteger.valueOf(2L).pow(getCombo(operand).intValue()));
//           if(debug) log.info("registerA = [registerA]{} / {}", registerA, Math.pow(2L, getCombo(operand)));
        } else if(instruction == 1) {
            registerB = registerB.xor(getCombo(operand));
            if(debug) log.info("registerB = {} ^ {}", registerB, getCombo(operand));
        } else if(instruction == 2) {
            registerB = getCombo(operand).mod(BigInteger.valueOf(8));
            if(debug) log.info("registerB = {} % {}", getCombo(operand), 8);
        } else if(instruction == 3) {
            if(!registerA.equals(BigInteger.valueOf(0))) {
                instr = getCombo(operand).intValue();
                if(debug) log.info("instr set to  = {}", getCombo(operand));
            }
        } else if (instruction == 4) {
            registerB = registerB.xor(registerC);
            if(debug) log.info("registerB  = {} ^ {}", registerB, registerC);
        } else if(instruction == 5) {
            ansP1 += getCombo(operand).mod(BigInteger.valueOf(8)).toString();
//            if(debug)  log.info("output  =  {}", String.valueOf(getCombo(operand) % 8));
        } else if(instruction == 6) {
            registerB  = registerA.divide(BigInteger.valueOf(2L).pow(getCombo(operand).intValue()));
//            if(debug)  log.info("registerB  =  {}} / {}", registerA, (Math.pow(2L, getCombo(operand))));
        } else if (instruction == 7) {
            registerC = registerA.divide(BigInteger.valueOf(2L).pow(getCombo(operand).intValue()));
//            if(debug) log.info("registerC  =  {}} / {}", registerA, (Math.pow(2L, getCombo(operand))));
        }
    }

    private void parseInput() {
        for(var line: inputLines) {
            if(StringUtils.isBlank(line)) continue;
            if(line.startsWith("Register A")) {
                registerA = new BigInteger(line.substring(12));
            } else if (line.startsWith("Register B")) {
                registerB = new BigInteger(line.substring(12));
            } else if (line.startsWith("Register C")) {
                registerC = new BigInteger(line.substring(12));
            } else if(line.startsWith("Program: ")){
                program = Arrays.stream(line.substring(9).split(",")).mapToInt(Integer::parseInt).toArray();
            }
        }
    }

    private BigInteger getCombo(int op) {
        if(op == 0) return BigInteger.ZERO;
        if(op == 1) return  BigInteger.ONE;
        if(op == 2) return BigInteger.ONE.add(BigInteger.ONE);
        if(op == 3) return new BigInteger("3");
        if(op == 4) return registerA;
        if(op == 5) return registerB;
        if(op == 6) return registerC;
        return null;
    }

    @Test
    void test() {
        setInputLines(List.of(
                """
                        Register A: 729
                        Register B: 0
                        Register C: 0
                        
                        Program: 0,1,5,4,3,0
                        """.split("\n")
        ));

        part1();
        part2();
    }


    @Test
    void test_2() {
        setInputLines(List.of(
                """
                Register A: 729
                Register B: 0
                Register C: 0
                
                Program: 0,1,5,4,3,0
                """.split("\n")
        ));

        part1();
        part2();
    }

}

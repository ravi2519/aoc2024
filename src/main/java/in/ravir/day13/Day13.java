package in.ravir.day13;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class Day13 {

    @Setter
    private List<String> inputLines;

    public void part1() {
        log.info("Day13, Part 01");
        log.info("Answer Part 1: {}", getMinTokens(false));
    }

    public void part2() {
        log.info("Day13, Part 02");
        log.info("Answer Part 2: {}", getMinTokens(true));
    }

    private long getMinTokens(boolean isError) {
        var minTokens = 0L;

        for (int i = 0; i < inputLines.size(); i++) {
            String line = inputLines.get(i);
            if (line.startsWith("Button A")) {
                var aX = Long.parseLong(line.substring(line.indexOf("X+") + 2, line.indexOf(", Y")));
                var aY = Long.parseLong(line.substring(line.indexOf("Y+") + 2));

                line = inputLines.get(++i);
                var bX = Long.parseLong(line.substring(line.indexOf("X+") + 2, line.indexOf(", Y")));
                var bY = Long.parseLong(line.substring(line.indexOf("Y+") + 2));

                line = inputLines.get(++i);
                var prizeX = Long.parseLong(line.substring(line.indexOf("X=") + 2, line.indexOf(", Y")));
                var prizeY = Long.parseLong(line.substring(line.indexOf("Y=") + 2));

                if(isError) {
                    prizeX += 10000000000000L;
                    prizeY += 10000000000000L;
                }

                i++;

                if(isError) {

                    /**
                     *
                     * j * aX + k * bX == prizeX
                     * STEP 1
                     * =====
                     * a.
                     * j = (prizeX - k * bX) / aX
                     * k = (prizeX - j * aX) / bX
                     * b.
                     * j * aY + k * bY == prizeY
                     * j = (prizeY - k * bY) / aY
                     * k = (prizeY - j * aY) / bY
                     *
                     * STEP 2
                     *  =====
                     *  Replace b in a:
                     *
                     *  j = (prizeX - ( (prizeY - j * aY) *bx/ bY)  / aX
                     * k = (prizeX - (prizeY - k * bY)* aX)/ bX
                     *
                     * j * aX = prizeX - (prizeY * bx - j * aY * bx) / bY
                     * j * aX - prizeX = - (prizeY * bx - j * aY * bx) / bY
                     * j * aX * bY - prizeX * bY =  j * aY * bx  + prizeY * bx
                     * j * aX * bY - j * aY * bx = prizeX * bY - prizeY * bx
                     * j * (aX * bY - aY * bx) = prizeX * bY - prizeY * bx
                     * j = (prizeX * bY - prizeY * bx) / (aX * bY - aY * bx)
                     *
                     * k * bX = prizeX - (prizeY - k * bY) * aX / aY
                     * k * bX - prizeX = - (prizeY - k * bY) * aX / aY
                     * k * bX * aY - prizeX  * aY =  prizeY * aX -  k * bY * aX
                     * k * bX * aY - k * bY * aX = - prizeX * aY + prizeY * aX
                     * k * (bX * aY - bY * aX) = - prizeY * aX + prizeX * aY
                     * k = (prizeX * aY - prizeY * aX) / (bX * aY - bY * aX)
                     *
                     * STEP 3
                     * =====
                     * Some rearrangement
                     * j = (prizeX * bY - prizeY * bX) / (aY * bX - bY * aX)
                     * k = (prizeX * aY - prizeY * aX) / (bY * aX - bX * aY)
                     *
                     */

                    var j = (prizeX * bY - prizeY * bX) / (aX * bY - aY * bX);
                    var k =  (prizeX * aY - prizeY * aX ) / (bX * aY - bY * aX);

                    if (j * aX + k * bX == prizeX && j * aY + k * bY == prizeY) {
                        minTokens += j * 3 + k;
                    }
                } else {

                    var tokens = new ArrayList<Integer>();

                    for (int j = 1; j <= 100; j++) {
                        for (int k = 1; k <= 100; k++) {
                            if (j * aX + k * bX == prizeX && j * aY + k * bY == prizeY) {
                                tokens.add(j * 3 + k);
                            }
                        }
                    }

                    if (!tokens.isEmpty()) {
                        minTokens += Collections.min(tokens);
                    }
                }
            }
        }
        return minTokens;
    }

    @Test
    void test() {

        setInputLines(List.of(
                """
                Button A: X+94, Y+34
                Button B: X+22, Y+67
                Prize: X=8400, Y=5400
                
                Button A: X+26, Y+66
                Button B: X+67, Y+21
                Prize: X=12748, Y=12176
                
                Button A: X+17, Y+86
                Button B: X+84, Y+37
                Prize: X=7870, Y=6450
                
                Button A: X+69, Y+23
                Button B: X+27, Y+71
                Prize: X=18641, Y=10279
                """.split("\n")
        ));

//        part1();
        part2();

    }
}

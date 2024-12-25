package in.ravir.day25;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class Day25 {

    @Setter
    private List<String> inputLines;

    private final List<List<Integer>> locks = new java.util.ArrayList<>();
    private final List<List<Integer>> keys = new java.util.ArrayList<>();

    public void part1() {
        log.info("Day25, Part 01");
        parseInput();
        log.info("Locks: {}", locks);
        log.info("Keys: {}", keys);

        var pairs = 0;
        for(var lock : locks) {
            for(var key : keys) {
                if(fit(lock, key)) {
                    pairs++;
                }
            }
        }

        log.info("Part 01: {}", pairs);
    }

    public void part2() {
        log.info("Day25, Part 02");
    }

    private boolean fit(List<Integer> lock, List<Integer> key) {
        for (int i = 0; i < 5; i++) {
            if(lock.get(i) + key.get(i) > 7) {
                return false;
            }
        }
        return true;
    }

    private void parseInput() {
        for (int i = 0; i < inputLines.size(); i = i+8) {
            var lst = new ArrayList<>(List.of(7, 7, 7, 7, 7));
            for (int j = i; j < i + 7; j++) {
                for (int k = 0; k < 5; k++) {
                    if(inputLines.get(j).charAt(k) == '.') {
                        lst.set(k, lst.get(k)-1);
                    }
                }
            }
            if(inputLines.get(i).equals("#####")) {
                locks.add(lst);
            } else {
                keys.add(lst);
            }
        }
    }

    @Test
    public void test() {
        setInputLines(List.of("""
                #####
                .####
                .####
                .####
                .#.#.
                .#...
                .....
                
                #####
                ##.##
                .#.##
                ...##
                ...#.
                ...#.
                .....
                
                .....
                #....
                #....
                #...#
                #.#.#
                #.###
                #####
                
                .....
                .....
                #.#..
                ###..
                ###.#
                ###.#
                #####
                
                .....
                .....
                .....
                #....
                #.#..
                #.#.#
                #####
                """.split("\n")));
        part1();
        part2();
    }
}

package in.ravir.day09;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Day09 {

    @Setter
    private List<String> inputLines;
    private final List<Integer> toBeCompacted = new ArrayList<>();


    private String[] input;

    public void part1() {
        log.info("Day09, Part 01");
        parseInput();

        List<Integer> compacted = new ArrayList<>(toBeCompacted);
        var lastIndex = compacted.size() - 1;
        for (int i = 0; i < compacted.size(); ++i) {
            if (compacted.get(i) == -1) {
                int j = lastIndex;
                for (; j > i; --j) {
                    if (compacted.get(j) != -1) {
                        compacted.set(i, compacted.get(j));
                        compacted.set(j, -1);
                        break;
                    }
                }
                lastIndex = j;
            }
        }

        log.info("Answer Part 1 {}", getChecksum(compacted));
    }

    public void part2() {
        log.info("Day09, Part 02");

        List<Integer> compacted = new ArrayList<>(toBeCompacted);

        for (int i = compacted.size() -1; i >= 0 ; i--) {

            if (compacted.get(i) != -1 && (i == compacted.size() - 1 || !compacted.get(i + 1).equals(compacted.get(i)))) {

                // find to move
                var toMove = new ArrayList<Integer>();
                for (int j = i; j >=0 ; --j) {
                    if(!compacted.get(j).equals(compacted.get(i))) {
                        break;
                    }
                    toMove.add(j);
                }

                // find available spaces
                var availableSpaces = new ArrayList<Integer>();
                for (int j = 0; j < i; ++j) {

                    var currAvailableSpaces = new ArrayList<Integer>();
                    if(compacted.get(j).equals(-1) && (j == 0 || !compacted.get(j - 1).equals(-1))) {
                        for (int k = j; k < i ; ++k) {
                            if(!compacted.get(k).equals(-1)) {
                                break;
                            }
                            currAvailableSpaces.add(k);
                        }

                        if(currAvailableSpaces.size() >= toMove.size()) {
                            availableSpaces = currAvailableSpaces;
                            break;
                        }
                    }
                }

                if(!toMove.isEmpty() && !availableSpaces.isEmpty() && toMove.size() <= availableSpaces.size()) {
                    for (int j = 0; j < toMove.size(); ++j) {
                        compacted.set(availableSpaces.get(j), compacted.get(i));
                    }
                    for (int j = 0; j < toMove.size(); ++j) {
                        compacted.set(toMove.get(j), -1);
                    }
                }

            }
        }

        log.info("Answer Part 2 {}", getChecksum(compacted));
    }

    private void getToBeCompactedList() {
        var n = 0;
        var fileIndex = 0;
        for (String s : input) {
            if(n%2 == 0) {
                var fileBlock = Integer.parseInt(s);
                while(fileBlock > 0) {
                    toBeCompacted.add(fileIndex);
                    fileBlock--;
                }
                fileIndex++;
            } else {
                var spaceBlock = Integer.parseInt(s);
                while(spaceBlock > 0) {
                    toBeCompacted.add(-1);
                    spaceBlock--;
                }
            }
            n++;
        }
    }



    private static long getChecksum(List<Integer> compacted) {
        long checksum = 0;
        for (int i = 0; i < compacted.size(); ++i) {
            if (compacted.get(i) == -1) {
                continue;
            }
            checksum += (long) i * compacted.get(i);
        }
        return checksum;
    }

    private void parseInput() {
        input = inputLines.get(0).split("");
        getToBeCompactedList();
    }

    @Test
    void test() {
        setInputLines(List.of(
                "2333133121414131402"
        ));

        part1();
        part2();
    }
}

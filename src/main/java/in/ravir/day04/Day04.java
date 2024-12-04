package in.ravir.day04;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class Day04 {

    @Setter
    private List<String> inputLines;
    private String[][] input ;
    private int rows;
    private int cols;

    public void part1() {
        log.info("Day04, Part 01");
        parseInput();

        var allLines = new ArrayList<>(inputLines);
        allLines.addAll(getAllVerticalLines());
        allLines.addAll(getAllDiagonalLines());

        var count = 0L;

        var xmasPattern = Pattern.compile("XMAS");
        var samxPattern = Pattern.compile("SAMX");

        for (var line : allLines) {
            count += xmasPattern.matcher(line).results().count();
            count += samxPattern.matcher(line).results().count();

        }

        log.info("Answer Part 1: {}", count);
    }

    public void part2() {
        log.info("Day04, Part 02");

        var count = 0L;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(input[i][j].equals("A") && i - 1 >= 0 && i + 1 < rows && j - 1 >= 0 && j + 1 < cols) {
                    if(input[i-1][j-1].equals("M")
                            && input[i-1][j+1].equals("S")
                            && input[i+1][j-1].equals("M")
                            && input[i+1][j+1].equals("S")) {
                        count++;
                    }else if(input[i-1][j-1].equals("S")
                            && input[i-1][j+1].equals("S")
                            && input[i+1][j-1].equals("M")
                            && input[i+1][j+1].equals("M")) {
                        count++;
                    }else if(input[i-1][j-1].equals("S")
                            && input[i-1][j+1].equals("M")
                            && input[i+1][j-1].equals("S")
                            && input[i+1][j+1].equals("M")) {
                        count++;
                    }else if(input[i-1][j-1].equals("M")
                            && input[i-1][j+1].equals("M")
                            && input[i+1][j-1].equals("S")
                            && input[i+1][j+1].equals("S")) {
                        count++;
                    }
                }
            }
        }

        log.info("Answer Part 2: {}", count);
    }

    private List<String> getAllDiagonalLines() {
        var diagonalLines = new ArrayList<String>();
        var rowExtent = rows;

        for (int i = 0; i < rowExtent; i++) {
            var currentRow = i;
            var currentCol = 0;
            var line = new StringBuilder();

            while(currentCol <= rowExtent && currentRow >= 0) {
                line.append(input[currentRow][currentCol]);
                currentRow--;
                currentCol++;
            }

            diagonalLines.add(line.toString());
        }

        var colExtent = 1;
        for (int i = colExtent; i < cols; i++) {
            var currentRow = rowExtent - 1;
            var currentCol = colExtent;
            var line = new StringBuilder();

            while(currentCol <= rowExtent && currentRow >= colExtent) {
                line.append(input[currentRow][currentCol]);
                currentRow--;
                currentCol++;
            }

            diagonalLines.add(line.toString());
            colExtent ++;
        }

        rowExtent = rows - 1;
        for (int i = rowExtent; i >= 0; i--) {
            var currentRow = i;
            var currentCol = 0;
            var line = new StringBuilder();

            while(currentRow <= rowExtent ) {
                line.append(input[currentRow][currentCol]);
                currentRow++;
                currentCol++;
            }

            diagonalLines.add(line.toString());
        }

        colExtent = 1;
        for (int i = colExtent; i < cols; i++) {
            var currentRow = 0;
            var currentCol = colExtent;
            var line = new StringBuilder();

            while(currentCol < cols) {
                line.append(input[currentRow][currentCol]);
                currentRow++;
                currentCol++;
            }

            diagonalLines.add(line.toString());
            colExtent ++;
        }

        return diagonalLines;
    }

    private List<String> getAllVerticalLines() {
        var verticalLines = new ArrayList<String>();
        for (int i = 0; i < cols; i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < rows; j++) {
                line.append(input[j][i]);
            }
            verticalLines.add(line.toString());
        }

        return verticalLines;

    }


    @Test
    void test() {
        inputLines = List.of("""
                    MMMSXXMASM
                    MSAMXMSMSA
                    AMXSXMAAMM
                    MSAMASMSMX
                    XMASAMXAMM
                    XXAMMXXAMA
                    SMSMSASXSS
                    SAXAMASAAA
                    MAMMMXMMMM
                    MXMXAXMASX
                        """.split("\n"));
        part1();
        part2();

    }

    private void parseInput() {
        rows = inputLines.size();
        cols = inputLines.get(0).length();
        input = new String[rows][cols];
       for (int i = 0; i < inputLines.size(); i++) {
            input[i] = inputLines.get(i).split("");
        }
    }
}

package in.ravir.day14;

import javafx.util.Pair;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Day14 {

    @Setter
    private List<String> inputLines;

    private int cols;
    private int rows;
    private int[][] tiles;
    private int[][] robots;

    public void part1() {
        log.info("Day14, Part 01");
        parseInput();

        rows = 103;
        cols = 101;

        tiles = new int[rows][cols];

        for (var robot : robots) {
            int lx = robot[0];
            int ly = robot[1];

            int vx = robot[2];
            int vy = robot[3];

            for (int i = 0; i < 100; i++) {
                lx += vx;
                ly += vy;

                if (lx < 0) {
                    lx = rows + lx;
                }
                if (lx >= rows) {
                    lx = lx - rows;
                }
                if (ly < 0) {
                    ly = cols + ly;
                }
                if (ly >= cols) {
                    ly = ly - cols;
                }
            }
            tiles[lx][ly] += 1;
        }

        var prod = 1;
        int qSum[] = new int[4];
        var q1y = 0;
        for (int i = 0; i < rows; i++) {
            if(i == rows/2) continue;
            for (int j = q1y; j < cols; j++) {
                if(j == cols/2) continue;

                if(i < rows/2 && j < cols/2) {
                    qSum[0] += tiles[i][j];
                } else if(i < rows/2 && j >= cols/2) {
                    qSum[1] += tiles[i][j];
                } else if(i >= rows/2 && j < cols/2) {
                    qSum[2] += tiles[i][j];
                } else {
                    qSum[3] += tiles[i][j];
                }
            }
        }

        for (int i = 0; i < 4; i++) {
           if(qSum[i] > 0) prod *= qSum[i];
        }

        log.info("Answer Part 1: {}", prod);

    }

    public void part2() {
        log.info("Day14, Part 02");

        tiles = new int[rows][cols];

        for (int sec = 1; sec <= rows * cols; sec++) {
            for (var robot : robots) {
                int lx = robot[0];
                int ly = robot[1];

                if(tiles[lx][ly] > 0 ) tiles[lx][ly] -= 1;

                int vx = robot[2];
                int vy = robot[3];

                lx += vx;
                ly += vy;

                if (lx < 0) {
                    lx = rows + lx;
                }
                if (lx >= rows) {
                    lx = lx - rows;
                }
                if (ly < 0) {
                    ly = cols + ly;
                }
                if (ly >= cols) {
                    ly = ly - cols;
                }

                tiles[lx][ly] += 1;
                robot[0] = lx;
                robot[1] = ly;
            }

            if(frameDetected(tiles)) {
                log.info("Answer Part 2: {}", sec);
                print();
                break;
            }
        }
    }

    private void print() {
        for (int j = 0; j < rows; j++) {
            for (int k = 0; k < cols; k++) {
                System.out.print(tiles[j][k] > 0 ? " X" : " . ");
            }
            System.out.println();
        }
    }

    private static final int[][] DIRS = {
            {1, 0},   // Right
            {0, 1},   // Down
            {-1, 0},  // Left
            {0, -1}   // Up
    };

    private boolean frameDetected(int[][] tiles) {
        // find connected components
        var visited = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(tiles[i][j] > 0 && !visited[i][j]) {
                    var ele = 0;
                    if(dfs(visited, i, j, ele, null)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean dfs(boolean[][] visited, int x, int y, int count, String parent) {
        visited[x][y] = true;

        for(var dir :DIRS) {
            var nx = x + dir[0];
            var ny = y + dir[1];

            if(isValid(nx, ny)) {
                if(!visited[nx][ny] ) {
                    if(dfs(visited, nx, ny, count++, x + "-" + y)) {
                        tiles[x][y] = 100;
                        return true;
                    }
                } else if((nx + "-" + ny).equals(parent) && count > 4) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isValid(int nx, int ny) {
        return nx >= 0 && nx < rows && ny >= 0 && ny < cols && tiles[nx][ny] > 0;
    }

    private void parseInput() {
        robots = new int[inputLines.size()][4];
        for (int i = 0; i < inputLines.size(); i++) {
            String line = inputLines.get(i);
            String[] parts = line.split(" ");

            var robot = parts[0].replace("p=", "").split(",");
            robots[i][0] = Integer.parseInt(robot[1]);
            robots[i][1] = Integer.parseInt(robot[0]);

            var velocity = parts[1].replace("v=", "").split(",");
            robots[i][2] = Integer.parseInt(velocity[1]);
            robots[i][3] = Integer.parseInt(velocity[0]);
        }
    }

    @Test
    void test() {
        setInputLines(List.of(
                """
                p=0,4 v=3,-3
                p=6,3 v=-1,-3
                p=10,3 v=-1,2
                p=2,0 v=2,-1
                p=0,0 v=1,3
                p=3,0 v=-2,-2
                p=7,6 v=-1,-3
                p=3,0 v=-1,-2
                p=9,3 v=2,3
                p=7,3 v=-1,2
                p=2,4 v=2,-3
                p=9,5 v=-3,-3
                """.split("\n")));


        rows = 7;
        cols = 11;

        part1();
        part2();

    }
}

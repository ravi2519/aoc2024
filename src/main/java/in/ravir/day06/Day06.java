package in.ravir.day06;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class Day06 {

    @Setter
    private List<String> inputLines;

    private String[][] patrolMap;
    private int[] start = new int[2];
    private final Set<String> visited = new HashSet<>();
    private Direction currentDirection;

    @Getter
    enum Direction {
        NORTH, EAST, SOUTH, WEST
    }

    public void part1() {
        log.info("Day06, Part 01");
        parseInput();

        while(!isOutOfBounds(start)) {
            var x = start[0];
            var y = start[1];
            visited.add(x + "," + y);
            start = newPosition(start, false);

            if(isOutOfBounds(start)) {
                break;
            }

            if(patrolMap[start[0]][start[1]].equals("#")) {
                start[0] = x;
                start[1] = y;
                start = newPosition(start, true);
            }
        }

        log.info("Answer Part 1: {}", visited.size());
    }

    public void part2() {
        log.info("Day06, Part 02");

        int ans = 0;
        for (int ox = 0; ox < patrolMap.length; ox++) {
            for (int oy = 0; oy < patrolMap[0].length; oy++) {
                if (patrolMap[ox][oy].equals("#") || patrolMap[ox][oy].equals("^")) {
                    continue;
                }

                var copyPatrolMap = getMap();
                copyPatrolMap[ox][oy] = "#";
                visited.clear();

                while(!isOutOfBounds(start) || !visited.contains(start[0] + "," + start[1] + "," + currentDirection)) {
                    visited.add(start[0] + "," + start[1] + "," + currentDirection);
                    var x = start[0];
                    var y = start[1];

                    start = newPosition(start, false);

                    if (isOutOfBounds(start)) {
                        break;
                    }

                    while (copyPatrolMap[start[0]][start[1]].equals("#")) {
                        start[0] = x;
                        start[1] = y;
                        start = newPosition(start, true);
                    }

                    if(visited.contains(start[0] + "," + start[1]  + "," + currentDirection)) {
                        break;
                    }
                }

                if (visited.contains(start[0] + "," + start[1] + "," + currentDirection)) {
                    ans++;
                }
            }
        }

        log.info("Answer Part 2: {}", ans);
    }

    private void parseInput() {
        patrolMap = getMap();
    }

    private String[][] getMap() {
        var ret = new String[inputLines.size()][inputLines.get(0).length()];
        for (int i = 0; i < inputLines.size(); i++) {
            if (inputLines.get(i).contains("^")) {
                start[0] = i;
                start[1] = inputLines.get(i).indexOf("^");
                currentDirection = Direction.NORTH;
            }
            ret[i] = inputLines.get(i).split("");
        }
        return ret;
    }

    private int[] newPosition(int[] currentPosition, boolean isObstacle) {
        int[] newPosition = new int[2];
        switch (currentDirection) {
            case NORTH:
                if (isObstacle) {
                    newPosition[0] = currentPosition[0];
                    newPosition[1] = currentPosition[1] + 1;
                    currentDirection = Direction.EAST;
                } else {
                    newPosition[0] = currentPosition[0] - 1;
                    newPosition[1] = currentPosition[1];
                }
                break;
            case EAST:
                if (isObstacle) {
                    newPosition[0] = currentPosition[0] + 1;
                    newPosition[1] = currentPosition[1];
                    currentDirection = Direction.SOUTH;
                } else {
                    newPosition[0] = currentPosition[0];
                    newPosition[1] = currentPosition[1] + 1;
                }
                break;
            case SOUTH:
                if(isObstacle) {
                    newPosition[0] = currentPosition[0];
                    newPosition[1] = currentPosition[1] - 1;
                    currentDirection = Direction.WEST;
                } else {
                    newPosition[0] = currentPosition[0] + 1;
                    newPosition[1] = currentPosition[1];
                }
                break;
            case WEST:
                if (isObstacle) {
                    newPosition[0] = currentPosition[0] - 1;
                    newPosition[1] = currentPosition[1];
                    currentDirection = Direction.NORTH;
                } else {
                    newPosition[0] = currentPosition[0];
                    newPosition[1] = currentPosition[1] - 1;
                }
                break;
        }
        return newPosition;
    }

    private boolean isOutOfBounds(int[] position) {
        return position[0] < 0 || position[0] >= patrolMap.length || position[1] < 0 || position[1] >= patrolMap[0].length;
    }

    @Test
    void test() {
        setInputLines(List.of(
                """
                        ....#.....
                        .........#
                        ..........
                        ..#.......
                        .......#..
                        ..........
                        .#..^.....
                        ........#.
                        #.........
                        ......#...
                        
                        """.split("\n")));

        part1();
        part2();
    }



}

package in.ravir.day15;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
// TODO: Refactor please
public class Day15 {

    @Setter
    private List<String> inputLines;

    private String[][] map;
    private String[][] doubleMap;
    private String[] moves;
    private int[] pos = new int[2];
    private int[] doublePos = new int[2];
    private int rows;
    private int cols;

    private Map<Direction, Integer[]> coords = Map.of(
        Direction.WEST, new Integer[]{0, -1},
        Direction.EAST, new Integer[]{0, 1},
        Direction.NORTH, new Integer[]{-1, 0},
        Direction.SOUTH, new Integer[]{1, 0}
    );

    enum Direction {
        WEST("<"),
        NORTH("^"),
        SOUTH("v"),
        EAST(">");

        private String symbol;

        private Direction(String symbol) {
            this.symbol = symbol;
        }
    }

    public void part1() {
        log.info("Day15, Part 01");
        parseInput();

        for(var symbol: moves) {
            toMove(getDirection(symbol), pos[0], pos[1]);
        }

        var total = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if(map[i][j].equals("O")) {
                    total += i*100 + j;
                }
            }
        }

        log.info("Answer Part 1 {}", total);
    }

    public void part2() {
        log.info("Day15, Part 02");

        for(var symbol: moves) {
            toMoveTwo(getDirection(symbol), doublePos[0], doublePos[1]);

            System.out.println();
            System.out.println("==============================================");
            System.out.println();
            for (int i = 0; i < doubleMap.length; i++) {
                for (int j = 0; j < doubleMap[0].length; j++) {
                    System.out.print(doubleMap[i][j] + " ");
                }
                System.out.println();
            }
    
            
        }

        var total = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if(map[i][j].equals("O")) {
                    total += i*100 + j;
                }
            }
        }

        log.info("Answer Part 2 {}", total);
    }

    private void toMove(Direction direction, int startX, int startY) {

        var newStartX = startX;
        var newStartY = startY;
        while (!outOfBound(newStartX, newStartY, false)) {
            
            newStartX = newStartX + coords.get(direction)[0];
            newStartY = newStartY + coords.get(direction)[1];

            if(map[newStartX][newStartY].equals(".")) {
                // move
                if(map[newStartX - coords.get(direction)[0]][newStartY - coords.get(direction)[1]].equals("O")) {
                    map[newStartX][newStartY] = "O";
                }
                map[startX + coords.get(direction)[0]][startY + coords.get(direction)[1]] = map[startX][startY];
                map[startX][startY] = ".";
                pos[0] = startX + coords.get(direction)[0];
                pos[1] = startY + coords.get(direction)[1];
                break;
            } else if(map[newStartX][newStartY].equals("#")) {
                break;
            }

        }
    }

    private void toMoveTwo(Direction direction, int startX, int startY) {

        var newStartX = startX;
        var newStartY = startY;
        while (!outOfBound(newStartX, newStartY, true)) {
            
            newStartX = newStartX + coords.get(direction)[0];
            newStartY = newStartY + coords.get(direction)[1];

            if(doubleMap[newStartX][newStartY].equals(".")) {
                // move
                move(direction, startX, startY, newStartX, newStartY);
                doublePos[0] = startX + coords.get(direction)[0];
                doublePos[1] = startY + coords.get(direction)[1];
                break;
            } else if(doubleMap[newStartX][newStartY].equals("#")) {
                break;
            }

        }
    }

    private void move(Direction direction, int startX, int startY, int endX, int endY) {

        if(direction.equals(Direction.EAST)){
            while(endY >= startY) {
                var ele = doubleMap[endX][endY - 1];
                doubleMap[endX][endY] = ele;
                endY--; 
            }
        }else if(direction.equals(Direction.WEST)){
            while(endY <= startY) {
                var ele = doubleMap[endX][endY + 1];
                doubleMap[endX][endY] = ele;
                endY++; 
            }
        } else if(direction.equals(Direction.NORTH)){
            var listToMove = new ArrayList<String>();
            if(doubleMap[startX-1][startY].equals("[") || doubleMap[startX-1][startY].equals("]")) {
                var start = startX;
                startX--;

                if(doubleMap[startX][startY].equals("[")) {
                    listToMove.add(startY + "-" + (startY + 1) + "[]");
                    doubleMap[startX][startY] = ".";
                    doubleMap[startX][startY + 1] = ".";
                } else {
                    listToMove.add((startY - 1) + "-" + (startY) + "[]");
                    doubleMap[startX][startY - 1] = ".";
                    doubleMap[startX][startY] = ".";
                }

                startX--;

                while(endX < startX) {
                    var str = listToMove.get(listToMove.size() - 1);
                    var coord = str.substring(0, 3).split("-");
                    
                    var toCopy = String.join("", Arrays.copyOfRange(doubleMap[startX], Integer.parseInt(coord[0]), 
                        Integer.parseInt(coord[1]) + 1));
                    
                    if(toCopy.contains("#")) {
                        break;
                    }

                    if(doubleMap[startX][Integer.parseInt(coord[0])].equals("]")) {
                        toCopy = "[" + toCopy;
                        coord[0] = "" + (Integer.parseInt(coord[0]) -1);
                    }

                    if(doubleMap[startX][Integer.parseInt(coord[1])].equals("[")) {
                        toCopy = toCopy + "]";
                        coord[1] = "" + (Integer.parseInt(coord[1]) + 1);
                    }

                    listToMove.add(coord[0] + "-" + coord[1] + toCopy);


                    startX--;
                    // if(right) {

                    //     var str = listToMove.get(listToMove.size() - 1);
                    //     var coord = str.substring(0, 3).split("-");
                    //     if(List.of(Arrays.copyOfRange(doubleMap[startX], Integer.parseInt(coord[0]), Integer.parseInt(coord[1]))
                    //     ).contains("#")) {
                    //         break;
                    //     }

                    //     var toCopy = String.join("", Arrays.copyOfRange(doubleMap[startX], Integer.parseInt(coord[0]), Integer.parseInt(coord[1])));
                    //     if(doubleMap[startX][Integer.parseInt(coord[0])].equals("]")) {
                    //         toCopy = "[" + toCopy;
                    //         coord[0] = "" + (Integer.parseInt(coord[0]) -1);
                    //     }

                    //     if(doubleMap[startX][Integer.parseInt(coord[1])].equals("[")) {
                    //         toCopy = toCopy + "]";
                    //         coord[1] = "" + (Integer.parseInt(coord[1]) + 1);
                    //     }

                    //     listToMove.add(coord[0] + "-" + coord[1] + toCopy);

                    //     for(int y = Integer.parseInt(coord[0]); y <= Integer.parseInt(coord[1]); y++) {
                    //         doubleMap[endX][y] = ".";
                    //     }

                    //     // if(doubleMap[startX][startY].equals(".") && 
                    //     //     doubleMap[startX][startY + 1].equals("[")) {
                    //     //         listToMove.add(startY + "-" + (startY + 2) + ".[]");
                    //     //         doubleMap[startX][startY + 1] = ".";
                    //     // } else if(doubleMap[startX][startY].equals("]") && 
                    //     //     doubleMap[startX][startY + 1].equals(".")) {
                    //     //         listToMove.add((startY - 1) + "-" + (startY + 1) + "[].");
                    //     //         doubleMap[startX][startY - 1] = ".";
                    //     //         doubleMap[startX][startY] = ".";
                    //     //         doubleMap[startX][startY + 1] = ".";
                    //     // } else if(doubleMap[startX][startY].equals("]") && 
                    //     // doubleMap[startX][startY + 1].equals("[")) {
                    //     //     listToMove.add((startY - 1) + "-" + (startY + 2) + "[][]");
                    //     //     doubleMap[startX][startY - 1] = ".";
                    //     //     doubleMap[startX][startY] = ".";
                    //     //     doubleMap[startX][startY + 1] = ".";
                    //     //     doubleMap[startX][startY + 2] = ".";
                    //     // } else if(doubleMap[startX][startY].equals("[")) {
                    //     //     listToMove.add((startY) + "-" + (startY + 1) + "[]");
                    //     //     doubleMap[startX][startY] = ".";
                    //     //     doubleMap[startX][startY + 1] = ".";
                    //     // }
                    // } else {
                    //     if(doubleMap[startX][startY].equals(".") && 
                    //         doubleMap[startX][startY - 1].equals("]")) {
                    //             listToMove.add((startY - 2) + "-" + startY + "[].");
                    //             doubleMap[startX][startY - 2] = ".";
                    //             doubleMap[startX][startY - 1] = ".";
                    //             doubleMap[startX][startY] = ".";
                    //     } else if(doubleMap[startX][startY].equals("[") && 
                    //         doubleMap[startX][startY - 1].equals(".")) {
                    //             listToMove.add((startY - 1) + "-" + (startY + 1) + ".[]");
                    //             doubleMap[startX][startY - 1] = ".";
                    //             doubleMap[startX][startY] = ".";
                    //             doubleMap[startX][startY + 1] = ".";
                    //     } else if(doubleMap[startX][startY].equals("[") && 
                    //     doubleMap[startX][startY - 1].equals("]")) {
                    //         listToMove.add((startY - 2) + "-" + (startY + 1) +"[][]");
                    //         doubleMap[startX][startY - 2] = ".";
                    //         doubleMap[startX][startY - 1] = ".";
                    //         doubleMap[startX][startY] = ".";
                    //         doubleMap[startX][startY + 1] = ".";
                    //     } else if(doubleMap[startX][startY].equals("]")) {
                    //         listToMove.add((startY - 1) + "-" + startY +"[]");
                    //         doubleMap[startX][startY - 1] = ".";
                    //         doubleMap[startX][startY] = ".";
                    //     }
                    // }
                }

                var str = listToMove.get(listToMove.size() - 1);
                var coord = str.substring(0, 3).split("-");
                
                if(String.join("", Arrays.copyOfRange(doubleMap[endX], Integer.parseInt(coord[0]), 
                Integer.parseInt(coord[1]) + 1)).contains("#")) {
                    return;
                }

                var end = endX;
                var k = listToMove.size() - 1;
                while(k >= 0) {
                    end++;
                    for(int y = Integer.parseInt(coord[0]); y <= Integer.parseInt(coord[1]); y++) {
                        doubleMap[end][y] = ".";
                    }
                    k--;
                }

                k = listToMove.size() - 1;
                while(k >= 0) {
                    str = listToMove.get(k);
                    coord = str.substring(0, 3).split("-");
                    var strArr = str.substring(3).split("");

                    var i = 0;
                    for(int y = Integer.parseInt(coord[0]); y <= Integer.parseInt(coord[1]); y++) {
                        doubleMap[endX][y] = strArr[i];
                        i++;
                    }
                    endX++;
                    k--;
                }
                doubleMap[endX][endY] = "@";
                doubleMap[start][endY] = ".";
            } else {
                doubleMap[endX][endY] = "@";
                doubleMap[startX][startY] = ".";
            }


        }else {
            var listToMove = new ArrayList<String>();
            if(doubleMap[startX + 1][startY].equals("[") || doubleMap[startX + 1][startY].equals("]")) {
                var start = startX;
                startX++;

                if(doubleMap[startX][startY].equals("[")) {
                    listToMove.add(startY + "-" + (startY + 1) + "[]");
                    doubleMap[startX][startY] = ".";
                    doubleMap[startX][startY + 1] = ".";
                } else {
                    listToMove.add((startY - 1) + "-" + (startY) + "[]");
                    doubleMap[startX][startY - 1] = ".";
                    doubleMap[startX][startY] = ".";
                }
                startX++;
                while(endX > startX) {
                    var str = listToMove.get(listToMove.size() - 1);
                    var coord = str.substring(0, 3).split("-");

                    var toCopy = String.join("", Arrays.copyOfRange(doubleMap[startX], Integer.parseInt(coord[0]), 
                        Integer.parseInt(coord[1]) + 1));
                    
                    if(toCopy.contains("#")) {
                        break;
                    }

                    if(doubleMap[startX][Integer.parseInt(coord[0])].equals("]")) {
                        toCopy = "[" + toCopy;
                        coord[0] = "" + (Integer.parseInt(coord[0]) -1);
                    }

                    if(doubleMap[startX][Integer.parseInt(coord[1])].equals("[")) {
                        toCopy = toCopy + "]";
                        coord[1] = "" + (Integer.parseInt(coord[1]) + 1);
                    }

                    listToMove.add(coord[0] + "-" + coord[1] + toCopy);

                    for(int y = Integer.parseInt(coord[0]); y <= Integer.parseInt(coord[1]); y++) {
                        doubleMap[startX][y] = ".";
                    }
                    startX++;
                    // if(right) {
                    //     if(doubleMap[startX][startY].equals(".") && 
                    //         doubleMap[startX][startY + 1].equals("[")) {
                    //             listToMove.add(startY + "-" + (startY + 2) + ".[]");
                    //             doubleMap[startX][startY + 1] = ".";
                    //     } else if(doubleMap[startX][startY].equals("]") && 
                    //         doubleMap[startX][startY + 1].equals(".")) {
                    //             listToMove.add((startY - 1) + "-" + (startY + 1) + "[].");
                    //             doubleMap[startX][startY - 1] = ".";
                    //             doubleMap[startX][startY] = ".";
                    //             doubleMap[startX][startY + 1] = ".";
                    //     } else if(doubleMap[startX][startY].equals("]") && 
                    //     doubleMap[startX][startY + 1].equals("[")) {
                    //         listToMove.add((startY - 1) + "-" + (startY + 2) + "[][]");
                    //         doubleMap[startX][startY - 1] = ".";
                    //         doubleMap[startX][startY] = ".";
                    //         doubleMap[startX][startY + 1] = ".";
                    //         doubleMap[startX][startY + 2] = ".";
                    //     } else if(doubleMap[startX][startY].equals("[")) {
                    //         listToMove.add((startY) + "-" + (startY + 1) + "[]");
                    //         doubleMap[startX][startY] = ".";
                    //         doubleMap[startX][startY + 1] = ".";
                    //     }
                    // } else {
                    //     if(doubleMap[startX][startY].equals(".") && 
                    //         doubleMap[startX][startY - 1].equals("]")) {
                    //             listToMove.add((startY - 2) + "-" + startY + "[].");
                    //             doubleMap[startX][startY - 2] = ".";
                    //             doubleMap[startX][startY - 1] = ".";
                    //             doubleMap[startX][startY] = ".";
                    //     } else if(doubleMap[startX][startY].equals("[") && 
                    //         doubleMap[startX][startY - 1].equals(".")) {
                    //             listToMove.add((startY - 1) + "-" + (startY + 1) + ".[]");
                    //             doubleMap[startX][startY - 1] = ".";
                    //             doubleMap[startX][startY] = ".";
                    //             doubleMap[startX][startY + 1] = ".";
                    //     } else if(doubleMap[startX][startY].equals("[") && 
                    //     doubleMap[startX][startY - 1].equals("]")) {
                    //         listToMove.add((startY - 2) + "-" + (startY + 1) +"[][]");
                    //         doubleMap[startX][startY - 2] = ".";
                    //         doubleMap[startX][startY - 1] = ".";
                    //         doubleMap[startX][startY] = ".";
                    //         doubleMap[startX][startY + 1] = ".";
                    //     } else if(doubleMap[startX][startY].equals("]")) {
                    //         listToMove.add((startY - 1) + "-" + startY +"[]");
                    //         doubleMap[startX][startY - 1] = ".";
                    //         doubleMap[startX][startY] = ".";
                    //     }
                    // }
                }

                var str = listToMove.get(listToMove.size() - 1);
                var coord = str.substring(0, 3).split("-");
                
                if(String.join("", Arrays.copyOfRange(doubleMap[endX], Integer.parseInt(coord[0]), 
                Integer.parseInt(coord[1]) + 1)).contains("#")) {
                    return;
                }

                var end = endX;
                var k = listToMove.size() - 1;
                while(k >= 0) {
                    end--;
                    for(int y = Integer.parseInt(coord[0]); y <= Integer.parseInt(coord[1]); y++) {
                        doubleMap[end][y] = ".";
                    }
                    k--;
                }

                k = listToMove.size() - 1;
                while(k >= 0) {
                    str = listToMove.get(k);
                    coord = str.substring(0, 3).split("-");
                    var strArr = str.substring(3).split("");

                    var i = 0;
                    for(int y = Integer.parseInt(coord[0]); y <= Integer.parseInt(coord[1]); y++) {
                        doubleMap[endX][y] = strArr[i];
                        i++;
                    }
                    endX--;
                    k--;
                }
                doubleMap[endX][endY] = "@";
                doubleMap[start][endY] = ".";
            } else {
                doubleMap[endX][endY] = "@";
                doubleMap[startX][startY] = ".";
            }


        }


    }


    private boolean outOfBound(int x, int y, boolean isDouble) {
        if(isDouble) {
            if(x < 0 || x >= rows || y < 0 || y >= doubleMap[0].length) {
                return true;
            }
        } else {
            if(x < 0 || x >= rows || y < 0 || y >= cols) {
                return true;
            }
        }
        return false;
    }

    void parseInput() {
        AtomicBoolean isMap = new AtomicBoolean(true);
        List<String> mapLines = new ArrayList<>();
        List<String> moveLines = new ArrayList<>();
        inputLines.forEach(line -> {
            if(line.trim().equals("")) {
                isMap.set(false);
            } else {
                if (isMap.get()) {
                    mapLines.add(line);
                } else {
                    moveLines.add(line);
                }
            }

        });

        rows = mapLines.size();
        cols = mapLines.get(0).length();
        map = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            map[i] = mapLines.get(i).split("");
            if(mapLines.get(i).contains("@")) {
                pos[0] = i;
                pos[1] = mapLines.get(i).indexOf("@");
            }
        }

        doubleMap = new String[rows][cols*2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(map[i][j].equals("#")) {
                    doubleMap[i][j*2] = "#";
                    doubleMap[i][j*2 + 1] = "#";
                } else if(map[i][j].equals(".")) {
                    doubleMap[i][j*2] = ".";
                    doubleMap[i][j*2 + 1] = ".";
                } else if(map[i][j].equals("O")) {
                    doubleMap[i][j*2] = "[";
                    doubleMap[i][j*2 + 1] = "]";
                } else if(map[i][j].equals("@")) {
                    doubleMap[i][j*2] = "@";
                    doubleMap[i][j*2 + 1] = ".";
                    doublePos[0] = i;
                    doublePos[1] = j*2;
                } 
            }
        }

        moves = moveLines.stream().collect(Collectors.joining("")).split("");
    }

    Direction getDirection(String symbol) {
        switch (symbol) {
            case ">":
                return Direction.EAST;
            case "<":
                return Direction.WEST;
            case "v":
                return Direction.SOUTH;
            default:
                return Direction.NORTH;
        }
    }

    @Test
    void test() {

        setInputLines(List.of(
            """
            ##########
            #..O..O.O#
            #......O.#
            #.OO..O.O#
            #..O@..O.#
            #O#..O...#
            #O..O..O.#
            #.OO.O.OO#
            #....O...#
            ##########

            <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
            vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
            ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
            <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
            ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
            ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
            >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
            <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
            ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
            v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
            """.split("\n")
        ));

        part1();
        part2();


    }
}

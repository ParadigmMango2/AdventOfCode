package com.paradigmmango.day4;

import com.paradigmmango.util.Pair;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    private static final String SOURCE_PATH = "src/main/java/";

    private static String getInputsPath() {
        String[] packageArr = Main.class.getName().split("\\.");
        packageArr = Arrays.copyOfRange(packageArr, 0, packageArr.length - 1);
        String packagePath = String.join("/", packageArr);

        return SOURCE_PATH + packagePath + "/inputs/";
    }

    @SneakyThrows
    private static List<List<Character>> parse1(String path) {
        List<List<Character>> grid = new ArrayList<>();

        @Cleanup BufferedReader br = new BufferedReader(new FileReader(path));

        String line;
        while ((line = br.readLine()) != null) {
            List<Character> row = new ArrayList<>();

            for (char ch : line.toCharArray()) {
                row.add(ch);
            }

            grid.add(row);
        }

        return grid;
    }

//    @SneakyThrows
//    private static void parse2(String path) {
//        @Cleanup BufferedReader br = new BufferedReader(new FileReader(path));
//
//        String line;
//        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//        }
//    }

    public static void part1() {
        int[][] NEIGHBORS = {
            {-1,-1},
            {-1, 0},
            {-1, 1},
            { 0,-1},
            { 0, 1},
            { 1,-1},
            { 1, 0},
            { 1, 1},
        };

        int accessibleRolls = 0;

        var grid = parse1(getInputsPath() + "real.txt");
        int width = grid.getFirst().size();
        int length = grid.size();

        List<List<Character>> gridWrapper = new ArrayList<>();
        gridWrapper.add(Collections.nCopies(width+2, '.'));
        for (var row : grid) {
            List<Character> newRow = new ArrayList<>();

            newRow.add('.');
            newRow.addAll(row);
            newRow.add('.');

            gridWrapper.add(newRow);
        }
        gridWrapper.add(Collections.nCopies(width+2, '.'));

        for (int row = 0; row < length; row++) {
            for (int col = 0; col < width; col++) {
                int actualRow = row + 1;
                int actualCol = col + 1;

                char tile = grid.get(row).get(col);

                if (tile == '@') {
                    int adjacentRolls = 0;

                    for (int[] neighbor : NEIGHBORS) {
                        char neighborVal = gridWrapper.get(actualRow + neighbor[0]).get(actualCol + neighbor[1]);

                        if (neighborVal == '@') adjacentRolls++;
                    }

                    if (adjacentRolls < 4) {
                        accessibleRolls++;
//                        System.out.printf("%d %d: x\n", row, col);
                    }
                }
            }
        }

        System.out.println(accessibleRolls);
    }

    public static void part2() {
        int[][] NEIGHBORS = {
                {-1,-1},
                {-1, 0},
                {-1, 1},
                { 0,-1},
                { 0, 1},
                { 1,-1},
                { 1, 0},
                { 1, 1},
        };

        long accessibleRolls = 0;

        var grid = parse1(getInputsPath() + "real.txt");
        int width = grid.getFirst().size();
        int length = grid.size();

        List<List<Character>> gridWrapper = new ArrayList<>();
        gridWrapper.add(Collections.nCopies(width+2, '.'));
        for (var row : grid) {
            List<Character> newRow = new ArrayList<>();

            newRow.add('.');
            newRow.addAll(row);
            newRow.add('.');

            gridWrapper.add(newRow);
        }
        gridWrapper.add(Collections.nCopies(width+2, '.'));

        long totalRemovedThisCycle;

        do {
            List<Pair<Integer, Integer>> removedThisCycle = new ArrayList<>();

            for (int row = 0; row < length; row++) {
                for (int col = 0; col < width; col++) {
                    int actualRow = row + 1;
                    int actualCol = col + 1;

                    char tile = gridWrapper.get(actualRow).get(actualCol);

                    if (tile == '@') {
                        int adjacentRolls = 0;

                        for (int[] neighbor : NEIGHBORS) {
                            char neighborVal = gridWrapper.get(actualRow + neighbor[0]).get(actualCol + neighbor[1]);

                            if (neighborVal == '@') adjacentRolls++;
                        }

                        if (adjacentRolls < 4) {
                            accessibleRolls++;

                            removedThisCycle.add(new Pair<>(actualRow, actualCol));

//                            System.out.printf("%d %d: x\n", row, col);
                        }
                    }
                }
            }

            for (var toRemove : removedThisCycle) {
                gridWrapper.get(toRemove.getL()).set(toRemove.getR(), '.');
            }

            totalRemovedThisCycle = removedThisCycle.size();
        } while (totalRemovedThisCycle > 0);

        System.out.println(accessibleRolls);
    }
+
    public static void main(String[] args) {
        part1();
        part2();
    }
}

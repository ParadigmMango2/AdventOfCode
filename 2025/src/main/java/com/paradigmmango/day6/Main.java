package com.paradigmmango.day6;

import com.paradigmmango.util.Pair;
import com.paradigmmango.util.parse.Parser;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Main {
    private static final String SOURCE_PATH = "src/main/java/";

    private static String getInputsPath() {
        String[] packageArr = Main.class.getName().split("\\.");
        packageArr = Arrays.copyOfRange(packageArr, 0, packageArr.length - 1);
        String packagePath = String.join("/", packageArr);

        return SOURCE_PATH + packagePath + "/inputs/";
    }

    @SneakyThrows
    private static Pair<List<String>, List<List<Integer>>> parse1(String path) {
        var lines = Parser.parseLines(path);

        String ops = lines.getLines().getLast();
        lines.getLines().removeLast();

        var parsedOps = Parser.getMatches(Pattern.compile("[\\*\\+]"), ops).toStrs();
        var parsedVals = lines.getMatches(Pattern.compile("\\b\\d+\\b")).parseStrs(Integer::parseInt);

        return new Pair<>(parsedOps, parsedVals);
    }

    private static boolean isEmptyColumn(int col, List<List<Character>> grid) {
        if (col >= grid.getFirst().size()) return true;

        for (int row = 0; row < grid.size() - 1; row++) {
            if (grid.get(row).get(col) != ' ') return false;
        }

        return true;
    }

    @SneakyThrows
    private static ArrayList<Pair<Character, List<Integer>>> parse2(String path) {
        ArrayList<Pair<Character, List<Integer>>> problems = new ArrayList<>();

        var grid = Parser.parseChars(path).getChars();

        int width = grid.stream()
                .map(List::size)
                .mapToInt(Integer::intValue)
                .max()
                .getAsInt();

        for (var row : grid) {
            int diff = width - row.size();

            while (diff > 0) {
                row.add(' ');

                diff--;
            }
        }

        int length = grid.size() - 1;

        int col = 0;

        while (col <= width) {
            char problemOp = grid.get(length).get(col);

            var problemVals = new ArrayList<Integer>();

            while (!isEmptyColumn(col, grid)) {
                String rawVal = "";

                for (int row = 0; row < length; row++) {
                    char digit = grid.get(row).get(col);

                    if (digit != ' ') {
                        rawVal += digit;
                    }
                }

                int val = Integer.parseInt(rawVal);
                problemVals.add(val);

                col++;
            }

            problems.add(new Pair<>(problemOp, problemVals));

            col++;
        }

        return problems;
    }

    public static void part1() {
        var input = parse1(getInputsPath() + "real.txt");

        var ops = input.getL();
        var vals = input.getR();

        int width = ops.size();
        int length = vals.size();

        long resultSum = 0;

        for (int i = 0; i < width; i++) {
            String op = ops.get(i);

            long result;

            if (op.equals("*")) {
                result = 1;

                for (int j = 0; j < length; j++) {
                    long val = vals.get(j).get(i);

                    result *= val;
                }
            } else {
                result = 0;

                for (int j = 0; j < length; j++) {
                    long val = vals.get(j).get(i);

                    result += val;
                }
            }

            resultSum += result;
        }


        System.out.println(resultSum);
    }

    public static void part2() {
        var input = parse2(getInputsPath() + "real.txt");

        long resultSum = 0;

        for (var problem : input) {
            char op = problem.getL();
            var vals = problem.getR();

            long result;

            if (op == '*') {
                result = 1;

                for (var value : vals) {
                    result *= value;
                }
            } else {
                result = 0;

                for (var value : vals) {
                    result += value;
                }
            }

            resultSum += result;

//            System.out.println();
        }

        System.out.println(resultSum);
    }

    public static void main(String[] args) {
        part1();
        part2();
    }
}

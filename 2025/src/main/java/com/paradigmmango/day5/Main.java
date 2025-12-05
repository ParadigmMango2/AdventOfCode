package com.paradigmmango.day5;

import com.paradigmmango.util.Pair;
import com.paradigmmango.util.parse.ParseLines;
import com.paradigmmango.util.parse.Parser;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toCollection;

public class Main {
    private static final String SOURCE_PATH = "src/main/java/";

    private static String getInputsPath() {
        String[] packageArr = Main.class.getName().split("\\.");
        packageArr = Arrays.copyOfRange(packageArr, 0, packageArr.length - 1);
        String packagePath = String.join("/", packageArr);

        return SOURCE_PATH + packagePath + "/inputs/";
    }

    @SneakyThrows
    private static Pair<List<List<Long>>, List<Long>> parse1(String path) {
        var lines = Parser.parseLines(path).getLines();

        List<String> rangeLines = new ArrayList<>();
        List<String> idLines = new ArrayList<>();

        int i = 0;
        while (!lines.get(i).isEmpty()) {
            rangeLines.add(lines.get(i));
            i++;
        }
        i++;
        while (i < lines.size()) {
            idLines.add(lines.get(i));
            i++;
        }

        Pattern rangesPattern = Pattern.compile("(\\d+)-(\\d+)");
        var ranges = new ParseLines(rangeLines)
                .getMatches(rangesPattern)
                .getFirstMatches()
                .toGroups()
                .parse(Long::parseLong);

        var ids = idLines.stream().map(Long::parseLong).toList();

        return new Pair<>(ranges, ids);
    }

    @SneakyThrows
    private static void parse2(String path) {
        @Cleanup BufferedReader br = new BufferedReader(new FileReader(path));

        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static void part1() {
        var input = parse1(getInputsPath() + "real.txt");

        long totalFresh = 0;

        idLoop:
        for (long id : input.getR()) {
            for (var range : input.getL()) {
                if (id >= range.get(0) && id <= range.get(1)) {
                    totalFresh++;
                    continue idLoop;
                }
            }
        }

        System.out.println(totalFresh);
    }

    public static void part2() {
        var input = parse1(getInputsPath() + "real.txt");

        var ranges = input.getL();

        ranges = ranges.stream().sorted(Comparator.comparing(List::getFirst)).toList();
        ranges = new ArrayList<>(ranges);

        for (int firstI = 0; firstI < ranges.size() - 1; ) {
            int secondI = firstI + 1;

            var first = ranges.get(firstI);
            var second = ranges.get(secondI);

            if (first.getLast() >= second.getLast() && first.getFirst() <= second.getFirst()) {
                ranges.remove(secondI);
            } else if (first.getLast() >= second.getFirst()) {
                List<Long> newRange = new ArrayList<>();
                newRange.add(first.getFirst());
                newRange.add(second.getLast());

                ranges.set(firstI, newRange);
                ranges.remove(secondI);
            } else {
                firstI++;
            }
        }

        long totalFresh = 0;
        for (var range : ranges) {
            long rangeFresh = range.getLast() - range.getFirst() + 1;
            totalFresh += rangeFresh;
        }

//        HashSet noDupSet = new HashSet();
//
//        for (var range : ranges) {
//            for (long i = range.get(0); i <= range.get(1); i++) {
//                noDupSet.add(i);
//            }
//        }

//        for (int i = 0; i < effectiveRanges.size() - 1; i++) {
//            var rangeI = effectiveRanges.
//
//            for (int j = i + 1; j < effectiveRanges.size(); j++) {
//
//            }
//        }

//        long totalFresh = 0;
//
//        ArrayList<Long> validFresh = new ArrayList<>();
//
//        for (var range : input.getL()) {
//            for (long i = range.get(0); i <= range.get(1); i++) {
//                if (!validFresh.contains(i)) {
//                    validFresh.add(i);
//                }
//            }
//        }

        System.out.println(totalFresh);
    }

    public static void main(String[] args) {
        part1();
        part2();
    }
}

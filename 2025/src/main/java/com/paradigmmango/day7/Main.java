package com.paradigmmango.day7;

import com.paradigmmango.util.parse.Parser;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

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
        return Parser.parseChars(path).getChars();
    }

    @SneakyThrows
    private static void parse2(String path) {
        @Cleanup BufferedReader br = new BufferedReader(new FileReader(path));

        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static void addIfUnique(List<Integer> list, int val) {
        if (!list.contains(val)) {
            list.add(val);
        }
    }

    public static void part1() {
        var input = parse1(getInputsPath() + "real.txt");

        int countSplits = 0;

        var firstLine = input.getFirst();
        int firstBeamIdx = firstLine.indexOf('S');

        ArrayList<Integer> beams = new ArrayList<>();
        beams.add(firstBeamIdx);

        for (int rowIdx = 2; rowIdx < input.size(); rowIdx += 2) {
            var row = input.get(rowIdx);

            var allSplits = IntStream.range(0, row.size())
                    .filter(i -> row.get(i) == '^')
                    .boxed()
                    .toList();

            ArrayList<Integer> newBeams = new ArrayList<>();
            for (int beam : beams) {
                if (allSplits.contains(beam)) {
                    if (beam > 0)
                        addIfUnique(newBeams, beam - 1);
                    if (beam < row.size() - 1)
                        addIfUnique(newBeams, beam + 1);

                    countSplits++;
                } else {
                    addIfUnique(newBeams, beam);
                }
            }

            beams = newBeams;
        }

        System.out.println(countSplits);
    }

    public static long numPaths(List<List<Character>> input, int beam, int depth) {
        if (depth >= input.size()) return 1;

        if (input.get(depth).get(beam) == '^') {
            long total = 0;

            if (beam > 0)
                total += numPaths(input, beam - 1, depth + 2);
            if (beam < input.getFirst().size() - 1)
                total += numPaths(input, beam + 1, depth + 2);

            return total;
        } else {
            return numPaths(input, beam, depth + 2);
        }
    }

    public static void part2() {
        var input = parse1(getInputsPath() + "real.txt");

        var firstLine = input.getFirst();
        int firstBeamIdx = firstLine.indexOf('S');

        ArrayList<Long> beams = new ArrayList<Long>(Collections.nCopies(input.getFirst().size(), 0l));
        beams.set(firstBeamIdx, beams.get(firstBeamIdx) + 1);


        for (int rowIdx = 2; rowIdx < input.size(); rowIdx += 2) {
            var beamsClone = beams;

            var row = input.get(rowIdx);

            var allSplits = IntStream.range(0, row.size())
                    .filter(i -> row.get(i) == '^')
                    .boxed()
                    .toList();
            var allNonZeroColIdxs = IntStream.range(0, beams.size())
                    .filter(i -> beamsClone.get(i) != 0)
                    .boxed()
                    .toList();

            ArrayList<Long> newBeams = new ArrayList<Long>(Collections.nCopies(input.getFirst().size(), 0l));
            for (int beamIdx : allNonZeroColIdxs) {
                long beamCount = beams.get(beamIdx);

                if (allSplits.contains(beamIdx)) {
                    if (beamIdx > 0)
                        newBeams.set(beamIdx - 1, newBeams.get(beamIdx - 1) + beamCount);
                    if (beamIdx < row.size() - 1)
                        newBeams.set(beamIdx + 1, newBeams.get(beamIdx + 1) + beamCount);
                } else {
                    newBeams.set(beamIdx, newBeams.get(beamIdx) + beamCount);
                }
            }

            beams = newBeams;

//            System.out.println();
        }


        System.out.println(beams.stream()
                .mapToLong(Long::longValue)
                .sum());
    }

    public static void main(String[] args) {
        part1();
        part2();
    }
}

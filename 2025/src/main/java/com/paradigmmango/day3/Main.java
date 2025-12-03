package com.paradigmmango.day3;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final String SOURCE_PATH = "src/main/java/";

    private static String getInputsPath() {
        String[] packageArr = Main.class.getName().split("\\.");
        packageArr = Arrays.copyOfRange(packageArr, 0, packageArr.length - 1);
        String packagePath = String.join("/", packageArr);

        return SOURCE_PATH + packagePath + "/inputs/";
    }

    @SneakyThrows
    private static List<List<Integer>> parse1(String path) {
        List<List<Integer>> banks = new ArrayList<>();

        @Cleanup BufferedReader br = new BufferedReader(new FileReader(path));

        String line;
        while ((line = br.readLine()) != null) {
            List<Integer> bank = new ArrayList<>();

            for (char ch : line.toCharArray()) {
                bank.add(Integer.parseInt(ch + ""));
            }

            banks.add(bank);
        }

        return banks;
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
        long totalJolts = 0;

        var banks = parse1(Main.getInputsPath() + "real.txt");

        for (var bank : banks) {
            var firstSlice = bank.subList(0, bank.size() - 1);

            int max = firstSlice.getFirst();
            int maxIdx = 0;

            for (int i = 1; i < firstSlice.size(); i++) {
                if (max < firstSlice.get(i)) {
                    max = firstSlice.get(i);
                    maxIdx = i;
                }
            }

            var secondSlice = bank.subList(maxIdx + 1, bank.size());
            int secondMax = secondSlice.stream().max(Integer::compareTo).get();

            int maxTotal = 10 * max + secondMax;

            totalJolts += maxTotal;
        }

        System.out.println(totalJolts);
    }

    public static void part2() {
        long totalJolts = 0;

        int batteries = 12;

        var banks = parse1(Main.getInputsPath() + "real.txt");

        for (var bank : banks) {
            ArrayList<Integer> largestBatts = new ArrayList<>();
//            System.out.println(bank);

            int startingIdx = 0;
            for (int i = batteries; i > 0; i--) {
                var slice = bank.subList(startingIdx, bank.size() - i + 1);

                int max = slice.getFirst();
                int maxJ = 0;

                for (int j = 1; j < slice.size(); j++) {
                    if (max < slice.get(j)) {
                        max = slice.get(j);
                        maxJ = j;
                    }
                }

                startingIdx += maxJ + 1;

                largestBatts.add(max);
            }

            long jolts = Long.parseLong(largestBatts.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining()));

//            System.out.println(jolts);
//            System.out.println();

            totalJolts += jolts;
        }

        System.out.println(totalJolts);
    }

    public static void main(String[] args) {
        part2();
    }
}

package com.paradigmmango.old;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class Main {
    private static final String SOURCE_PATH = "src/main/java/";

    private static String getInputsPath() {
        CharSequence[] packageArr = Main.class.getName().split("\\.");
        packageArr = Arrays.copyOfRange(packageArr, 0, packageArr.length - 1);
        String packagePath = String.join((CharSequence)"/", packageArr);
        return SOURCE_PATH + packagePath + "/inputs/";
    }

    private static int toInt(String str) {
        int num = switch (str) {
            case "1", "one" -> 1;
            case "2", "two" -> 2;
            case "3", "three" -> 3;
            case "4", "four" -> 4;
            case "5", "five" -> 5;
            case "6", "six" -> 6;
            case "7", "seven" -> 7;
            case "8", "eight" -> 8;
            case "9", "nine" -> 9;
            default -> -1;
        };
        return num;
    }

    @SneakyThrows
    private static List<Integer> parse1(String path) {
        ArrayList<Integer> arrayList;
        block7: {
            ArrayList<Integer> calibrationVals = new ArrayList<Integer>();
            BufferedReader br = new BufferedReader(new FileReader(path));
            try {
                String line;
                while ((line = br.readLine()) != null) {
                    List<String> matches = Pattern.compile("\\d").matcher(line).results().map(matchResult -> matchResult.group(0)).toList();
                    int calibrationVal = matches.size() >= 2 ? Integer.parseInt(matches.getFirst() + matches.getLast()) : Integer.parseInt(matches.getFirst() + matches.getFirst());
                    calibrationVals.add(calibrationVal);
                }
                arrayList = calibrationVals;
                if (Collections.singletonList(br).get(0) == null) break block7;
            }
            catch (Throwable throwable) {
                if (Collections.singletonList(br).get(0) != null) {
                    br.close();
                }
                throw throwable;
            }
            br.close();
        }
        return arrayList;
    }

    @SneakyThrows
    private static List<Integer> parse2(String path) {
        ArrayList<Integer> arrayList;
        block7: {
            ArrayList<Integer> calibrationVals = new ArrayList<Integer>();
            BufferedReader br = new BufferedReader(new FileReader(path));
            try {
                String line;
                while ((line = br.readLine()) != null) {
                    List<String> matches = Pattern.compile("(?=(\\d|one|two|three|four|five|six|seven|eight|nine))").matcher(line).results().map(matchResult -> matchResult.group(1)).toList();
                    int calibrationVal = matches.size() >= 2 ? 10 * Main.toInt(matches.getFirst()) + Main.toInt(matches.getLast()) : 11 * Main.toInt(matches.getFirst());
                    calibrationVals.add(calibrationVal);
                }
                arrayList = calibrationVals;
                if (Collections.singletonList(br).get(0) == null) break block7;
            }
            catch (Throwable throwable) {
                if (Collections.singletonList(br).get(0) != null) {
                    br.close();
                }
                throw throwable;
            }
            br.close();
        }
        return arrayList;
    }

    public static void part1() {
        List<Integer> input = Main.parse1(Main.getInputsPath() + "real.txt");
        long sum = 0L;
        for (Integer val : input) {
            sum += (long)val.intValue();
        }
        System.out.println(sum);
    }

    public static void part2() {
        List<Integer> input = Main.parse2(Main.getInputsPath() + "real.txt");
        long sum = 0L;
        for (Integer val : input) {
            sum += (long)val.intValue();
        }
        System.out.println(sum);
    }

    public static void main(String[] args) {
        Main.part1();
        Main.part2();
    }
}

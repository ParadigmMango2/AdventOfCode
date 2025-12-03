package com.paradigmmango.day1;

import com.paradigmmango.util.Pair;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String SOURCE_PATH = "src/main/java/";

    private static String getInputsPath() {
        CharSequence[] packageArr = Main.class.getName().split("\\.");
        packageArr = Arrays.copyOfRange(packageArr, 0, packageArr.length - 1);
        String packagePath = String.join((CharSequence)"/", packageArr);
        return SOURCE_PATH + packagePath + "/inputs/";
    }

    @SneakyThrows
    private static List<Pair<Character, Integer>> parse1(String path) {
        ArrayList<Pair<Character, Integer>> arrayList;
        block7: {
            ArrayList<Pair<Character, Integer>> input = new ArrayList<Pair<Character, Integer>>();
            BufferedReader br = new BufferedReader(new FileReader(path));
            try {
                String line;
                Pattern pattern = Pattern.compile("[L|R](\\d+)");
                while ((line = br.readLine()) != null) {
                    char dir = line.charAt(0);
                    Matcher matcher = pattern.matcher(line);
                    matcher.matches();
                    int offset = Integer.parseInt(matcher.group(1));
                    input.add(new Pair<Character, Integer>(Character.valueOf(dir), offset));
                }
                arrayList = input;
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
        List<Pair<Character, Integer>> input = Main.parse1(Main.getInputsPath() + "real.txt");
        int zeroCount = 0;
        int pos = 50;
        for (Pair<Character, Integer> instruction : input) {
            pos = instruction.getL().charValue() == 'L' ? (pos -= instruction.getR().intValue()) : (pos += instruction.getR().intValue());
            if ((pos %= 100) != 0) continue;
            ++zeroCount;
        }
        System.out.println(zeroCount);
    }

    public static void part2() {
        List<Pair<Character, Integer>> input = Main.parse1(Main.getInputsPath() + "real.txt");
        int zeroCount = 0;
        int pos = 50;
        for (Pair<Character, Integer> instruction : input) {
            char dir = instruction.getL().charValue();
            int offset = instruction.getR();
            int origPos = pos;
            zeroCount += offset / 100;
            pos = dir == 'R' ? (pos += offset) : (pos -= (offset %= 100));
            if (pos > 99 || pos <= 0 && origPos > 0) {
                ++zeroCount;
            }
            pos = Math.floorMod(pos, 100);
        }
        System.out.println(zeroCount);
    }

    public static void main(String[] args) {
        Main.part1();
        Main.part2();
    }
}

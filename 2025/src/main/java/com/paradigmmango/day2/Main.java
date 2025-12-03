/*
 * Decompiled with CFR 0.152.
 */
package com.paradigmmango.day2;

import com.paradigmmango.util.Pair;
import com.paradigmmango.util.RegexHelper;
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
    private static List<Pair<Long, Long>> parse1(String path) {
        ArrayList<Pair<Long, Long>> arrayList;
        block8: {
            ArrayList<Pair<Long, Long>> ranges = new ArrayList<Pair<Long, Long>>();
            BufferedReader br = new BufferedReader(new FileReader(path));
            try {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] rawRanges = line.split(",");
                    Pattern pattern = Pattern.compile("(\\d+)-(\\d+)");
                    for (String rawRange : rawRanges) {
                        Matcher matcher = RegexHelper.match(pattern, rawRange);
                        Pair<Long, Long> range = new Pair<Long, Long>(Long.parseLong(matcher.group(1)), Long.parseLong(matcher.group(2)));
                        ranges.add(range);
                    }
                }
                arrayList = ranges;
                if (Collections.singletonList(br).get(0) == null) break block8;
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
        int invlaidCount = 0;
        long invalidTotal = 0L;
        List<Pair<Long, Long>> ranges = Main.parse1(Main.getInputsPath() + "real.txt");
        for (Pair<Long, Long> range : ranges) {
            for (long i = range.getL().longValue(); i <= range.getR(); ++i) {
                String secondHalf;
                String firstHalf;
                String iStr = String.valueOf(i);
                if (iStr.length() % 2 != 0 || !(firstHalf = iStr.substring(0, iStr.length() / 2)).equals(secondHalf = iStr.substring(iStr.length() / 2))) continue;
                ++invlaidCount;
                invalidTotal += i;
            }
        }
        System.out.println(invalidTotal);
    }

    public static void part2() {
        long invalidTotal = 0L;
        List<Pair<Long, Long>> ranges = Main.parse1(Main.getInputsPath() + "real.txt");
        Pattern invalidPattern = Pattern.compile("^\\b(\\d+)\\1+\\b$");
        for (Pair<Long, Long> range : ranges) {
            for (long i = range.getL().longValue(); i <= range.getR(); ++i) {
                String iStr = String.valueOf(i);
                Matcher matcher = invalidPattern.matcher(iStr);
                if (!matcher.matches()) continue;
                invalidTotal += i;
            }
        }
        System.out.println(invalidTotal);
    }

    public static void main(String[] args) {
        Main.part1();
        Main.part2();
    }
}

package com.paradigmmango.sandbox;

import com.paradigmmango.util.Pair;
import com.paradigmmango.util.parse.Parser;

import java.util.Arrays;
import java.util.regex.Pattern;

import static com.paradigmmango.util.parse.ParserOld.*;

public class Main {
    private static final String SOURCE_PATH = "src/main/java/";

    private static String getInputsPath() {
        String[] packageArr = Main.class.getName().split("\\.");
        packageArr = Arrays.copyOfRange(packageArr, 0, packageArr.length - 1);
        String packagePath = String.join("/", packageArr);

        return SOURCE_PATH + packagePath + "/inputs/";
    }

    public static void main(String[] args) {
//        var in = parseLines(getInputsPath() + "test1.txt");
//
//        var line = in.get(0);

        var day1 = Parser.parseLines(getInputsPath() + "day1.txt")
                .getMatches(Pattern.compile("([LR])(\\d+)"))
                .getFirstMatches()
                .toGroups()
                .parseByGroupSet(groupSet -> new Pair<>(groupSet.get(0).charAt(0), Integer.parseInt(groupSet.get(1))));

        var p = Pattern.compile("(\\d+)-(\\d+)");
        var day2 = Parser.parseLines(getInputsPath() + "test1.txt")
                .getMatches(p)
                .toGroups()
                .parseByGroupSet(groupSet -> new Pair<>(Long.parseLong(groupSet.get(0)), Long.parseLong(groupSet.get(1))));

        var day3 = Parser.parseChars(getInputsPath() + "chars.txt").parse(Character::getNumericValue);

        System.out.println();
    }
}

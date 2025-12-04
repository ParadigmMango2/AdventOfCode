package com.paradigmmango.sandbox;

import com.paradigmmango.util.Pair;
import com.paradigmmango.util.parse.ParseLines;
import com.paradigmmango.util.parse.Parser2;

import java.util.Arrays;
import java.util.regex.Pattern;

import static com.paradigmmango.util.parse.Parser.*;

public class Main {
    private static final String SOURCE_PATH = "src/main/java/";

    private static String getInputsPath() {
        String[] packageArr = Main.class.getName().split("\\.");
        packageArr = Arrays.copyOfRange(packageArr, 0, packageArr.length - 1);
        String packagePath = String.join("/", packageArr);

        return SOURCE_PATH + packagePath + "/inputs/";
    }

    public static void main(String[] args) {
        var in = parseLines(getInputsPath() + "test1.txt");

        var line = in.get(0);

        var p = Pattern.compile("(\\d+)-(\\d+)");
        var foo = Parser2.parseLines(getInputsPath() + "test1.txt")
                .getMatches(p)
                .toGroups()
                .parseByGroupSet(groupSet -> new Pair<>(Integer.parseInt(groupSet.get(0)), Long.parseLong(groupSet.get(1))));

        System.out.println();
    }
}

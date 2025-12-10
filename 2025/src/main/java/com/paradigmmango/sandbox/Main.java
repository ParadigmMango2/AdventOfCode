package com.paradigmmango.sandbox;

import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Optimize;
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

    public static void parseTest() {
        var day1 = Parser.parseLines(getInputsPath() + "day1.txt")
                .getMatches(Pattern.compile("([LR])(\\d+)"))
                .getFirstMatches()
                .toGroups()
                .parseByGroupSet(groupSet -> new Pair<>(groupSet.get(0).charAt(0), Integer.parseInt(groupSet.get(1))));

        var p = Pattern.compile("(\\d+)-(\\d+)");
        var in = parseLines(getInputsPath() + "test1.txt");
        var line = in.get(0);
        var day2 = Parser.getMatches(p, line)
                .toGroups()
                .parse(Integer::parseInt);

        var day3 = Parser.parseChars(getInputsPath() + "chars.txt").parse(Character::getNumericValue);
    }

    public static void main(String[] args) {
        Context ctx = new Context();

        Optimize opt = ctx.mkOptimize();

        // Set constraints.
        IntExpr xExp = ctx.mkIntConst("x");
        IntExpr yExp = ctx.mkIntConst("y");

        opt.Add(ctx.mkEq(ctx.mkAdd(xExp, yExp), ctx.mkInt(10)),
                ctx.mkGe(xExp, ctx.mkInt(0)),
                ctx.mkGe(yExp, ctx.mkInt(0)));

        // Set objectives.
        Optimize.Handle mx = opt.MkMinimize(xExp);
        Optimize.Handle my = opt.MkMaximize(yExp);

        System.out.println(opt.Check());
        System.out.println(mx);
        System.out.println(my);

        System.out.println();
    }
}

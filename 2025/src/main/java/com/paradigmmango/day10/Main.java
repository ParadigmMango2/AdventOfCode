package com.paradigmmango.day10;

import com.microsoft.z3.*;
import com.paradigmmango.util.ComboFinder;
import com.paradigmmango.util.parse.Parser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.ejml.dense.row.misc.RrefGaussJordanRowPivot_DDRM;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

@Data
@AllArgsConstructor
class Procedure {
    private char[] indicators;
    private int[][] buttonWirings;
    private int[] joltReqs;
}

public class Main {
    private static final String SOURCE_PATH = "src/main/java/";

    private static String getInputsPath() {
        String[] packageArr = Main.class.getName().split("\\.");
        packageArr = Arrays.copyOfRange(packageArr, 0, packageArr.length - 1);
        String packagePath = String.join("/", packageArr);

        return SOURCE_PATH + packagePath + "/inputs/";
    }

    @SneakyThrows
    private static List<Procedure> parse1(String path) {
        List<Procedure> procedures = new ArrayList<>();

        List<String> lines = Parser.parseLines(path).getLines();

        for (String line : lines) {
            String[] blocks = line.split(" ");
            for (int i = 0; i < blocks.length; i++) {
                blocks[i] = blocks[i].substring(1, blocks[i].length() - 1);
            }

            char[] indicators = blocks[0].toCharArray();

            int[][] buttonWirings = new int[blocks.length - 2][];
            for (int i = 1; i < blocks.length - 1; i++) {
                int[] wiring = Arrays.stream(blocks[i].split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray();

                buttonWirings[i-1] = wiring;
            }

            int[] joltReqs = Arrays.stream(blocks[blocks.length - 1]
                    .split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            procedures.add(new Procedure(indicators, buttonWirings, joltReqs));
        }

        return procedures;
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

    public static void toggle(char[] canvas, int i) {
        if (canvas[i] == '.') {
            canvas[i] = '#';
        } else {
            canvas[i] = '.';
        }
    }


    public static boolean equals(char[] lights1, char[] lights2) {
        for (int i = 0; i < lights1.length; i++)
            if (lights1[i] != lights2[i])
                return false;

        return true;
    }


    public static void part1() {
        long totalFewest = 0;

        var input = parse1(getInputsPath() + "real.txt");

        for (Procedure proc : input) {

            var indicators = proc.getIndicators();
            var wirings = proc.getButtonWirings();

            boolean solved = false;
            int n = 0;
            while (!solved) {
                n++;

                var combosN = ComboFinder.findCombosN(wirings, n, 0, new Stack<>());

                for (var combo : combosN) {
                    char[] canvas = new char[indicators.length];
                    Arrays.fill(canvas, '.');

                    for (var button : combo) {
                        for (int light : button) {
                            toggle(canvas, light);
                        }
                    }

                    if (equals(canvas, indicators)) {
                        solved = true;
                        break;
                    }
                }

//                System.out.println();
            }

            totalFewest += n;
        }

        System.out.println(totalFewest);
    }

    public static long bfs(int[][] wirings, int[] target, int[] curJolts, long depth) {
        if (Arrays.equals(target, curJolts)) {
            return depth;
        }

        for (int i = 0; i < curJolts.length; i++) {
            if (curJolts[i] > target[i]) {
                return -1;
            }
        }

        for (int[] wiring : wirings) {
            for (int lightIdx : wiring) curJolts[lightIdx]++;

            long retval = bfs(wirings, target, curJolts, depth + 1);
            if (retval != -1)
                return retval;

            for (int lightIdx : wiring) curJolts[lightIdx]--;
        }

        return -1;
    }

    public static void part2() {
        long totalFewest = 0;

        var input = parse1(getInputsPath() + "test1.txt");

        for (Procedure proc : input) {
//        var proc = input.get(1);
            var wirings = proc.getButtonWirings();
            var joltReqs = proc.getJoltReqs();

        //            SimpleMatrix W = new SimpleMatrix(wirings.length, joltReqs.length);
        //            for (int row = 0; row < wirings.length; row++) {
        //                for (int col : wirings[row]) {
        //                    W.set(row, col, 1);
        //                }
        //            }

            SimpleMatrix W = new SimpleMatrix(joltReqs.length, wirings.length+1);
            for (int col = 0; col < wirings.length; col++) {
                for (int row : wirings[col]) {
                    W.set(row, col, 1);
                }
            }

            for (int i = 0; i < joltReqs.length; i++) {
                W.set(i, wirings.length, joltReqs[i]);
            }

            RrefGaussJordanRowPivot_DDRM solver = new RrefGaussJordanRowPivot_DDRM();
            solver.reduce(W.getDDRM(), wirings.length);

            System.out.println(W.toString());
        }

        System.out.println(totalFewest);
    }

    public static void part2Z3() {
        long totalFewest = 0;

        var input = parse1(getInputsPath() + "real.txt");

        for (Procedure proc : input) {
            var wirings = proc.getButtonWirings();
            var joltReqs = proc.getJoltReqs();

            // build solver
            Context ctx = new Context();
            Optimize opt = ctx.mkOptimize();

            // lhs vars of each equation
            List<List<IntExpr>> lhsVars = new ArrayList<>();
            for (int i = 0; i < joltReqs.length; i++) lhsVars.add(new ArrayList<>());

            // instantiate each button and it's conditions
            List<IntExpr> bVars = new ArrayList<>();
            for (int i = 0; i < wirings.length; i++) {
                IntExpr bExp = ctx.mkIntConst("B_" + i);
                bVars.add(bExp);

                // ensure button is pressed positive number of times
                opt.Add(ctx.mkGe(bExp, ctx.mkInt(0)));

                // add button to the count of each joltage
                for (int button : wirings[i]) {
                    lhsVars.get(button).add(bExp);
                }
            }

            // formally add each equation to the sovler
            for (int i = 0; i < joltReqs.length; i++) {
                List<IntExpr> lhs = lhsVars.get(i);

                opt.Add(ctx.mkEq(ctx.mkAdd(lhs.toArray(new IntExpr[0])), ctx.mkInt(joltReqs[i])));
            }

            // define the target variable and solve
            IntExpr bSum = ctx.mkIntConst("B_SUM");
            opt.Add(ctx.mkEq(bSum, ctx.mkAdd(bVars.toArray(new IntExpr[0]))));

            Optimize.Handle<IntSort> minCountRaw = opt.MkMinimize(bSum);

            long minCount = Long.MIN_VALUE; // default to an absurdly negative number to ruin output if an error occurs
            if (opt.Check() == Status.SATISFIABLE) {
                minCount = Long.parseLong(minCountRaw.toString());
            }

            totalFewest += minCount;

            ctx.close();
        }

        System.out.println(totalFewest);
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        part2Z3();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1000000;

        System.out.println(duration);
    }
}

package com.paradigmmango.day12;

import com.paradigmmango.util.Pair;
import com.paradigmmango.util.parse.ParseLines;
import com.paradigmmango.util.parse.Parser;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
class Region {
    private int width;
    private int length;
    private int[] counts;
}

public class Main {
    private static final String SOURCE_PATH = "src/main/java/";

    private static String getInputsPath() {
        String[] packageArr = Main.class.getName().split("\\.");
        packageArr = Arrays.copyOfRange(packageArr, 0, packageArr.length - 1);
        String packagePath = String.join("/", packageArr);

        return SOURCE_PATH + packagePath + "/inputs/";
    }

    public static List<List<String>> splitByEmpty(List<String> list) {
        List<List<String>> splits = new ArrayList<>();

        int i = 0;
        while(i < list.size()) {
            List<String> split = new ArrayList<>();
            while (i < list.size() && !list.get(i).isEmpty()) {
                split.add(list.get(i));
                i++;
            }
            splits.add(split);

            i++;
        }

        return splits;
    }

    @SneakyThrows
    private static Pair<boolean[][][], Region[]> parse1(String path) {
        // raw processing
        var lines = Parser.parseLines(path).getLines();
        var paragraphs = splitByEmpty(lines);
        var rawShapes = paragraphs.subList(0,6);
        var rawRegions = paragraphs.getLast();

        // convert raw shapes to shapes
        boolean[][][] shapes = new boolean[rawShapes.size()][3][3];
        for (var rawShape : rawShapes) {
            int index = Integer.parseInt(rawShape.getFirst().substring(0,1));

            boolean[][] shape = new boolean[3][3];
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    shape[row][col] = '#' == rawShape.get(row + 1).charAt(col);
                }
            }

            shapes[index] = shape;
        }

        // convert raw regions into regions
        var regionNums = new ParseLines(rawRegions)
                .getMatches(Pattern.compile("\\d+"))
                .parseStrs(Integer::parseInt);

        Region[] regions = new Region[regionNums.size()];
        for (int i = 0; i < rawRegions.size(); i++) {
            List<Integer> regionNum = regionNums.get(i);
            regions[i] = new Region(regionNum.get(0),regionNum.get(1), regionNum.subList(2,8)
                    .stream()
                    .mapToInt(Integer::intValue)
                    .toArray());
        }

        return new Pair<>(shapes, regions);
    }

    @SneakyThrows
    private static void parse2(String path) {
        @Cleanup BufferedReader br = new BufferedReader(new FileReader(path));

        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        System.out.println();
    }

    public static boolean[][] rotateCW(boolean[][] shape) {
//        boolean tempCorner = shape[0][0];
//        shape[0][0] = shape[2][0];
//        shape[2][0] = shape[2][2];
//        shape[2][2] = shape[0][2];
//        shape[0][2] = tempCorner;
//
//        boolean tempEdge = shape[0][1];
//        shape[0][1] = shape[1][0];
//        shape[1][0] = shape[2][1];
//        shape[2][1] = shape[1][2];
//        shape[1][2] = tempEdge;
        return new boolean[][]{
            {shape[2][0], shape[1][0], shape[0][0]},
            {shape[2][1], shape[1][1], shape[0][1]},
            {shape[2][2], shape[1][2], shape[0][2]}
        };
    }

    public static boolean[][] flipH(boolean[][] shape) {
        return new boolean[][]{
                {shape[0][2], shape[0][1], shape[0][0]},
                {shape[1][2], shape[1][1], shape[1][0]},
                {shape[2][2], shape[2][1], shape[2][0]}
        };
    }

    public static boolean contains(List<boolean[][]> list, boolean[][] shape) {
        for (boolean[][] shape2 : list) {
            if (Arrays.deepEquals(shape, shape2)) {
                return true;
            }
        }

        return false;
    }

    public static List<boolean[][]> getUnique(List<boolean[][]> nonUnique) {
        List<boolean[][]> unique = new ArrayList<>();

        for (boolean[][] shape : nonUnique) {
            if (!contains(unique, shape)) {
                unique.add(shape);
            }
        }

        return unique;
    }

    public static List<List<boolean[][]>> uniqueOrientations(boolean[][][] shapes) {
        List<List<boolean[][]>> allOrientations = new ArrayList<>();

        for (int i = 0; i < shapes.length; i++) {
            List<boolean[][]> shapeOrientations = new ArrayList<>();

            boolean[][] shape = shapes[i];
            boolean[][] flip = flipH(shape);

            shapeOrientations.add(shape);
            shapeOrientations.add(flip);
            for (int j = 0; j < 3; j++) {
                shape = rotateCW(shape);
                flip = rotateCW(flip);

                shapeOrientations.add(shape);
                shapeOrientations.add(flip);
            }

//            Set<boolean[][]> distinctOrientations= new HashSet<>(shapeOrientations);
            List<boolean[][]> distinctOrientations = getUnique(shapeOrientations);

            allOrientations.add(distinctOrientations);
        }

        return allOrientations;
    }

    public static Comparator<BitSet> bitsetComparator = Comparator.comparing(set -> set.stream().toArray(), (s1, s2) -> {
        int i1;
        int i2;

        int idx = 0;
        do {
            i1 = s1[idx];
            i2 = s2[idx];
            idx++;
        } while (i1 == i2 && idx < s1.length && idx < s2.length);

        if (i1 < i2) return -1;
        else if (i2 < i1) return 1;
        else {
            if (s1.length == s2.length) {
                return 0;
            } else if (idx == s1.length && idx < s2.length) {
                return -1;
            } else {
                return 1;
            }
        }
    });

    public static Comparator<BitSet> optimizedComparator = (a, b) -> {
        if (a == b) return 0;
        int i = a.nextSetBit(0);
        int j = b.nextSetBit(0);

        while (i != -1 && j != -1) {
            if (i != j) return Integer.compare(i, j);
            i = a.nextSetBit(i + 1);
            j = b.nextSetBit(j + 1);
        }

        if (i == -1 && j == -1) return 0;
        return (i == -1) ? -1 : 1;
    };

    public static ArrayList<Map.Entry<BitSet, Integer>> generatePlacements(List<List<boolean[][]>> orientations, int width, int length) {
//        List<List<BitSet>> allPlacements2 = new ArrayList<>();
        SortedMap<BitSet, Integer> allPlacements = new TreeMap<>(optimizedComparator);
//        HashMap<BitSet, Integer> allPlacements = new HashMap<>();

        for (int i = 0; i < orientations.size(); i++) {
//            List<BitSet> placements = new ArrayList<>();

            for (int j = 0; j < orientations.get(i).size(); j++) {
                boolean[][] orientation = orientations.get(i).get(j);

                for (int r = 0; r < length-2; r++) {
                    for (int c = 0; c < width-2; c++) {
                        BitSet placement = new BitSet(width * length);

                        for (int shapeR = 0; shapeR < 3; shapeR++) {
                            for (int shapeC = 0; shapeC < 3; shapeC++) {
                                if (orientation[shapeR][shapeC]) {
                                    int tileIdx = (r + shapeR) * width + c + shapeC;
                                    placement.set(tileIdx);
                                }
                            }
                        }

//                        var lol = placement.stream().toArray();

//                        placements.add(placement);
                        allPlacements.put(placement, i);
                    }
                }
            }

//            placements.sort(Comparator.comparing(set -> set.stream().findFirst().getAsInt()));
//            placements.sort(Comparator.comparing(set -> set.stream().toArray(), (s1, s2) -> {
//                int i1;
//                int i2;
//
//                int idx = 0;
//                do {
//                    i1 = s1[idx];
//                    i2 = s2[idx];
//                    idx++;
//                } while (i1 == i2 && idx < s1.length && idx < s2.length);
//
//                if (s1.length == s2.length) {
//                    if (i1 < i2) return 1;
//                    else if (i2 < i1) return -1;
//                    else return 0;
//                } else if (idx == s1.length && idx < s2.length) {
//                    return 1;
//                } else {
//                    return -1;
//                }
//            }));

//            allPlacements2.add(placements);
        }

        var result = new ArrayList<>(allPlacements.entrySet());

//        result.sort(Map.Entry.comparingByKey(optimizedComparator));

        return result;
    }

    public static boolean dfs(ArrayList<Map.Entry<BitSet, Integer>> placements, int start, BitSet board, int[] counts, int[] countsTgt) {
        for (int i = start; i < placements.size(); i++) {
            Map.Entry<BitSet, Integer> placement = placements.get(i);
            BitSet mask = placement.getKey();

            if (board.intersects(mask)) continue;

            int presentId = placement.getValue();
            if (counts[presentId] == countsTgt[presentId]) continue;

            board.xor(mask);
            counts[presentId]++;
            if (Arrays.equals(counts, countsTgt))
                return true;
            boolean retval = dfs(placements, i+1, board, counts, countsTgt);
            if (retval) return true;
            board.xor(mask);
            counts[presentId]--;
        }

        return false;
    }

    public static void printShape(boolean[][] shape) {
        for (boolean[] row : shape) {
            for (boolean cell : row) {
                System.out.print(cell ? '#' : '.');
            }
            System.out.println();
        }
    }

    public static void printOrientations(List<List<boolean[][]>> orientations) {
        for (int i = 0; i < orientations.size(); i++) {
            System.out.println("Shape " + i + ":");

            for (var shape : orientations.get(i)) {
                printShape(shape);
                System.out.println();
            }
        }
    }

    public static void printPlacements(ArrayList<Map.Entry<BitSet, Integer>> placements, Region region) {
        for (var placement : placements) {
            var bitset = placement.getKey();

            for (int r = 0; r < region.getLength(); r++) {
                for (int c = 0; c < region.getWidth(); c++) {
                    if (bitset.get(r * region.getWidth() + c)) {
                        System.out.print('#');
                    } else {
                        System.out.print('.');
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static int countTrue(boolean[][] shape) {
        int count = 0;

        for (boolean[] row : shape)
            for (boolean cell : row)
                if (cell)
                    count++;

        return count;
    }

    // areas.length == counts.length
    public static long countProductSum(int[] areas, int[] counts) {
        long sum = 0;

        for (int i = 0; i < areas.length; i++) {
            sum += (long) areas[i] * counts[i];
        }

        return sum;
    }

    public static void part1() {
        var input = parse1(getInputsPath() + "real.txt");
        var shapes = input.getL();
        Region[] regions = input.getR();

        List<List<boolean[][]>> orientations = uniqueOrientations(shapes);

        long startTime = System.nanoTime();
//        var region = regions[2];

        long totalPossible = 0;

        var shapeAreas = Arrays.stream(shapes)
                .map(shape -> countTrue(shape))
                .mapToInt(Integer::intValue)
                .toArray();

        for (int i = 0; i < regions.length; i++) {
            Region region = regions[i];

            int area = region.getWidth() * region.getLength();

            if (countProductSum(shapeAreas, region.getCounts()) <= area) {
                ArrayList<Map.Entry<BitSet, Integer>> placements = generatePlacements(orientations, region.getWidth(), region.getLength());

                boolean isPossible = dfs(placements, 0, new BitSet(area), new int[]{0, 0, 0, 0, 0, 0}, region.getCounts());

                if (isPossible) totalPossible++;
            }

            System.out.println("Solved region " + i + "!");
        }
//        printOrientations(orientations);

        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1000000;

        System.out.println(duration);

        System.out.println(totalPossible);
    }

    public static void part2() {
        parse2(getInputsPath() + "test1.txt");

        System.out.println();
    }

    public static void main(String[] args) {
        part1();
    }
}

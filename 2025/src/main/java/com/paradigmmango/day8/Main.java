package com.paradigmmango.day8;

import com.paradigmmango.util.Pair;
import com.paradigmmango.util.Vector3;
import com.paradigmmango.util.parse.Parser;
import lombok.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@NoArgsConstructor
class Circuit {
    private List<Vector3> boxes;

    public void add(Vector3 vec) {
        boxes.add(vec);
    }
}

@Data
@AllArgsConstructor
class Node {
    private Vector3 box;
    private int id;
//    private List<Node> connections;
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
    private static List<Vector3> parse1(String path) {
        return Parser.parseLines(path).getMatches(Pattern.compile("\\d+")).parseStrs(Integer::parseInt).stream()
                .map(row -> new Vector3(row.get(0), row.get(1), row.get(2)))
                .toList();
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
        var input = parse1(getInputsPath() + "test1.txt");

//        var dist = Vector3.distance(new Vector3(1,1,1), new Vector3(2,2,2));

        int keep = 10;
        var shortestPairs = new HashMap<>();
        double maxDistInShortest = 0;
        double secondMaxDistInShortest = 0;
        Pair<Vector3, Vector3> maxPairInShortest = null;
        Pair<Vector3, Vector3> secondMaxPairInShortest = null;

        for (int i = 0; i < input.size() - 1; i++) {
            Vector3 v1 = input.get(i);

            for (int j = i + 1; j < input.size(); j++) {
                Vector3 v2 = input.get(j);

                double dist = Vector3.distance(v1, v2);

                if (shortestPairs.size() < keep) {
                    Pair<Vector3, Vector3> thisPair = new Pair<>(v1, v2);

                    shortestPairs.put(thisPair, Double.valueOf(dist));

                    if (dist > maxDistInShortest) {
//                        secondMaxDistInShortest = maxDistInShortest;
//                        secondMaxPairInShortest = maxPairInShortest;
                        maxDistInShortest = dist;
                        maxPairInShortest = thisPair;
                    }
                } else if (dist < maxDistInShortest) {
                    Pair<Vector3, Vector3> thisPair = new Pair<>(v1, v2);

                    shortestPairs.put(thisPair, dist);
                    shortestPairs.remove(maxPairInShortest);

                    var entries = new ArrayList<>(shortestPairs.entrySet());
//                    var max = entries.stream().max(Comparator.comparing(entry -> entry.getValue()));

//                    var max = Collections.max(shortestPairs.entrySet(), Map.Entry.comparingByValue());

//                    var lol = shortestPairs.entrySet();

//                    var entries = new ArrayList<>(shortestPairs.entrySet());
//                    Collections.sort(entries, Comparator.comparing(
//                            Map.Entry::getValue, (val1, val2) -> {
//                                return ((Double) val2).compareTo((Double) val1);
//                            }
//                    ));

                    System.out.println();
                }
            }
        }

        System.out.println();
    }

    public static void replace(int[] arr, int from, int to) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == from)
                arr[i] = to;
    }

    public static void part1Retry() {
        var input = parse1(getInputsPath() + "real.txt");

//        var dist = Vector3.distance(new Vector3(1,1,1), new Vector3(2,2,2));

        var nodes = new ArrayList<Node>();
        for (int i = 0; i < input.size(); i++) {
            nodes.add(new Node(input.get(i), i));
        }

        int keep = 1000;
        var pairEntries = new ArrayList<Pair<Pair<Node, Node>, Double>>();

        for (int i = 0; i < input.size() - 1; i++) {
            Node n1 = nodes.get(i);

            for (int j = i + 1; j < input.size(); j++) {
                Node n2 = nodes.get(j);

                double dist = Vector3.distance(n1.getBox(), n2.getBox());

                pairEntries.add(new Pair<>(new Pair<>(n1, n2), dist));
            }
        }

        pairEntries.sort(Comparator.comparing(Pair::getR));

        var shortestEntries = pairEntries.subList(0, keep);

        // effectively a hashmap, where the int at index points to the lowest index in its group
        // the indices align with the IDs on the nodes
        int[] groupIdentities = IntStream
                .range(0, input.size())
                .toArray();

        for (var entry : shortestEntries) {
            Node n1 = entry.getL().getL();
            Node n2 = entry.getL().getR();

            int n1Group = groupIdentities[n1.getId()];
            int n2Group = groupIdentities[n2.getId()];

            if (n1Group != n2Group) {
                if (n1Group < n2Group) {
                    replace(groupIdentities, n2Group, n1Group);
                } else {
                    replace(groupIdentities, n1Group, n2Group);
                }

//                System.out.println();
            }

//            System.out.println();
        }

        Map<Integer, Long> countsMap = Arrays.stream(groupIdentities)
                .boxed()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        List<Long> counts = new ArrayList<>(countsMap.values().stream().toList());

        counts.sort(Comparator.reverseOrder());

        System.out.println(counts.get(0) * counts.get(1) * counts.get(2));
    }

    public static boolean isZeroes(int[] arr) {
        for (int val : arr)
            if (val != 0)
                return false;

        return true;
    }

    public static void part2() {
        var input = parse1(getInputsPath() + "real.txt");

//        var dist = Vector3.distance(new Vector3(1,1,1), new Vector3(2,2,2));

        var nodes = new ArrayList<Node>();
        for (int i = 0; i < input.size(); i++) {
            nodes.add(new Node(input.get(i), i));
        }

        var pairEntries = new ArrayList<Pair<Pair<Node, Node>, Double>>();

        for (int i = 0; i < input.size() - 1; i++) {
            Node n1 = nodes.get(i);

            for (int j = i + 1; j < input.size(); j++) {
                Node n2 = nodes.get(j);

                double dist = Vector3.distance(n1.getBox(), n2.getBox());

                pairEntries.add(new Pair<>(new Pair<>(n1, n2), dist));
            }
        }

        pairEntries.sort(Comparator.comparing(Pair::getR));

        // effectively a hashmap, where the int at index points to the lowest index in its group
        // the indices align with the IDs on the nodes
        int[] groupIdentities = IntStream
                .range(0, input.size())
                .toArray();

        int i = 0;
        while (true) {
            var entry = pairEntries.get(i);

            Node n1 = entry.getL().getL();
            Node n2 = entry.getL().getR();

            int n1Group = groupIdentities[n1.getId()];
            int n2Group = groupIdentities[n2.getId()];

            if (n1Group != n2Group) {
                if (n1Group < n2Group) {
                    replace(groupIdentities, n2Group, n1Group);
                } else {
                    replace(groupIdentities, n1Group, n2Group);
                }

                if (isZeroes(groupIdentities)) {
                    System.out.println((long) n1.getBox().getX() * (long) n2.getBox().getX());

                    break;
                }
            }

            i++;

//            System.out.println();
        }

//        System.out.println();
    }

    public static void main(String[] args) {
        part1Retry();
        part2();
    }
}

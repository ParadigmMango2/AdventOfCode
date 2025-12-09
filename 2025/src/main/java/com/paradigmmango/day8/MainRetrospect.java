package com.paradigmmango.day8;

import com.paradigmmango.util.Pair;
import com.paradigmmango.util.Vector3;
import com.paradigmmango.util.parse.Parser;
import lombok.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
class Node2 {
    private Vector3 box;
    private int id;
    private ArrayList<Node2> connections;

    public Node2(Vector3 box, int id) {
        this.box = box;
        this.id = id;
        connections = new ArrayList<>();
    }

    public void addConnection(Node2 node) {
        connections.add(node);
    }

    @Override
    public String toString() {
        List<String> connectionStrs = connections.stream()
                .map(Node2::getId)
                .map(String::valueOf)
                .toList();

        return box.toString() + " : " + String.valueOf(id) + " <-> " + String.join(",", connectionStrs);
    }
}

public class MainRetrospect {
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

    public static void replace(int[] arr, int from, int to) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == from)
                arr[i] = to;
    }

    public static void part1() {
        int keep = 10;
        var input = parse1(getInputsPath() + "test1.txt");

        var nodes = new ArrayList<Node2>();
        for (int i = 0; i < input.size(); i++) {
            nodes.add(new Node2(input.get(i), i));
        }

        var pairEntries = new ArrayList<Pair<Pair<Node2, Node2>, Double>>();

        for (int i = 0; i < input.size() - 1; i++) {
            Node2 n1 = nodes.get(i);

            for (int j = i + 1; j < input.size(); j++) {
                Node2 n2 = nodes.get(j);

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
            Node2 n1 = entry.getL().getL();
            Node2 n2 = entry.getL().getR();

            int n1Group = groupIdentities[n1.getId()];
            int n2Group = groupIdentities[n2.getId()];

            if (n1Group != n2Group) {
                if (n1Group < n2Group) {
                    replace(groupIdentities, n2Group, n1Group);
                } else {
                    replace(groupIdentities, n1Group, n2Group);
                }

                n1.addConnection(n2);
                n2.addConnection(n1);
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
        var input = parse1(getInputsPath() + "test1.txt");

//        var dist = Vector3.distance(new Vector3(1,1,1), new Vector3(2,2,2));

        var nodes = new ArrayList<Node2>();
        for (int i = 0; i < input.size(); i++) {
            nodes.add(new Node2(input.get(i), i));
        }

        var pairEntries = new ArrayList<Pair<Pair<Node2, Node2>, Double>>();

        for (int i = 0; i < input.size() - 1; i++) {
            Node2 n1 = nodes.get(i);

            for (int j = i + 1; j < input.size(); j++) {
                Node2 n2 = nodes.get(j);

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

            Node2 n1 = entry.getL().getL();
            Node2 n2 = entry.getL().getR();

            int n1Group = groupIdentities[n1.getId()];
            int n2Group = groupIdentities[n2.getId()];

            if (n1Group != n2Group) {
                if (n1Group < n2Group) {
                    replace(groupIdentities, n2Group, n1Group);
                } else {
                    replace(groupIdentities, n1Group, n2Group);
                }

                n1.addConnection(n2);
                n2.addConnection(n1);

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

    public static double calcLength(Node2 parent, Node2 curNode) {
        if (curNode.getConnections().size() == 1 && curNode.getConnections().getFirst().getId() == parent.getId()) {
            return 0.0;
        }

        double totalDistOfChildren = 0.0;
        for (Node2 connection : curNode.getConnections()) {
            if (parent == null || connection.getId() != parent.getId()) {
                totalDistOfChildren += Vector3.distance(connection.getBox(), curNode.getBox());
                totalDistOfChildren += calcLength(curNode, connection);
            }
        }
        return totalDistOfChildren;
    }

    public static void cableLength() {
        var input = parse1(getInputsPath() + "test1.txt");

//        var dist = Vector3.distance(new Vector3(1,1,1), new Vector3(2,2,2));

        var nodes = new ArrayList<Node2>();
        for (int i = 0; i < input.size(); i++) {
            nodes.add(new Node2(input.get(i), i));
        }

        var pairEntries = new ArrayList<Pair<Pair<Node2, Node2>, Double>>();

        for (int i = 0; i < input.size() - 1; i++) {
            Node2 n1 = nodes.get(i);

            for (int j = i + 1; j < input.size(); j++) {
                Node2 n2 = nodes.get(j);

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

            Node2 n1 = entry.getL().getL();
            Node2 n2 = entry.getL().getR();

            int n1Group = groupIdentities[n1.getId()];
            int n2Group = groupIdentities[n2.getId()];

            if (n1Group != n2Group) {
                if (n1Group < n2Group) {
                    replace(groupIdentities, n2Group, n1Group);
                } else {
                    replace(groupIdentities, n1Group, n2Group);
                }

                n1.addConnection(n2);
                n2.addConnection(n1);

                if (isZeroes(groupIdentities)) {
//                    System.out.println((long) n1.getBox().getX() * (long) n2.getBox().getX());

                    break;
                }
            }

            i++;

//            System.out.println();
        }

        double totalDist = calcLength(null, nodes.getFirst());

        System.out.println(totalDist);
    }

    public static void main(String[] args) {
        part1();
        part2();
        cableLength();
    }
}

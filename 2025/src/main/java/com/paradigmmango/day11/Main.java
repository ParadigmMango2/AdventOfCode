package com.paradigmmango.day11;

import com.paradigmmango.util.Cache;
import com.paradigmmango.util.Pair;
import com.paradigmmango.util.parse.ParseMatches;
import com.paradigmmango.util.parse.Parser;
import lombok.Cleanup;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Pattern;

@Data
class Node {
    private String name;
    private List<Node> outgoingConnections;
    public static HashMap<String, Node> identities = new HashMap<>();

    public Node(String name) {
        this.name = name;
        outgoingConnections = new ArrayList<>();

        identities.put(name, this);
    }

    @Override
    public String toString() {
        return "Node(" + name + ", outgoingConnections=[" + String.join(", ", outgoingConnections.stream().map(Node::getName).toList()) + "])";
    }
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
    private static Node parse1(String path) {
        var lines = Parser.parseLines(path).getMatches(Pattern.compile("\\w{3}")).getMatchesList().stream()
                .map(ParseMatches::toStrs)
                .toList();

        Node[] nodes = new Node[lines.size()];
        Node out = new Node("out");

        // add nodes
        for (int i = 0; i < lines.size(); i++) {
            nodes[i] = new Node(lines.get(i).getFirst());
        }

        // add connections
        for (int i = 0; i < lines.size(); i++) {
            var line = lines.get(i);
            Node node = Node.identities.get(line.getFirst());

            for (int j = 1; j < line.size(); j++) {
                Node connection = Node.identities.get(line.get(j));
                node.getOutgoingConnections().add(connection);
            }
        }

        return nodes[0];
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

    private static Map<String, Long> cache = new HashMap<>();
    public static long cachedDfs (Node curNode, String tgt) {
        String key = curNode.getName() + tgt;

        if (cache.containsKey(key)) return cache.get(key);

        long result = dfs(curNode, tgt);
        cache.put(key, result);
        return result;
    }

    public static long dfs(Node curNode, String tgt) {
        if (curNode.getName().equals(tgt)) {
            return 1;
        }

        long total = 0;

        for (Node connection : curNode.getOutgoingConnections()) {
            total += cachedDfs(connection, tgt);
        }

        return total;
    }

    public static void part1() {
        Node you = parse1(getInputsPath() + "real.txt");

        long paths = dfs(you, "out");

        System.out.println(paths);
    }

    public static void part2() {
        parse1(getInputsPath() + "real.txt");

        Node svr = Node.identities.get("svr");
        Node fft = Node.identities.get("fft");
        Node dac = Node.identities.get("dac");

        long svrToFft = dfs(svr, "fft");
        long fftToDac = dfs(fft, "dac");
        long dacToOut = dfs(dac, "out");
        long path1 = svrToFft * fftToDac * dacToOut;

        long svrToDac = dfs(svr, "dac");
        long dacToFft = dfs(dac, "fft");
        long fftToOut = dfs(fft, "out");
        long path2 = svrToDac * dacToFft * fftToOut;

        long total = path1 + path2;

        System.out.println(total);
    }

    public static void main(String[] args) {
        part1();
        part2();
    }
}

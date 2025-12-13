package com.paradigmmango.day11;

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

    public Node(String name) {
        this.name = name;
        outgoingConnections = new ArrayList<>();
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
    private static HashMap<String, Node> parse1(String path) {
        var lines = Parser.parseLines(path).getMatches(Pattern.compile("\\w{3}")).getMatchesList().stream()
                .map(ParseMatches::toStrs)
                .toList();

        Node[] nodes = new Node[lines.size()];
        HashMap<String, Node> nodesMap = new HashMap<>();
        nodesMap.put("out", new Node("out"));

        // add nodes
        for (int i = 0; i < lines.size(); i++) {
            Node node = new Node(lines.get(i).getFirst());
            nodes[i] = node;
            nodesMap.put(node.getName(), node);
        }

        // add connections
        for (List<String> line : lines) {
            Node node = nodesMap.get(line.getFirst());

            for (int j = 1; j < line.size(); j++) {
                Node connection = nodesMap.get(line.get(j));
                node.getOutgoingConnections().add(connection);
            }
        }

        return nodesMap;
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
    public static long cachedDfs(Node curNode, String tgt) {
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
        Node you = parse1(getInputsPath() + "real.txt").get("you");

        long paths = cachedDfs(you, "out");

        System.out.println(paths);
    }

    public static void part2() {
        HashMap<String, Node> nodeMap = parse1(getInputsPath() + "real.txt");

        Node svr = nodeMap.get("svr");
        Node fft = nodeMap.get("fft");
        Node dac = nodeMap.get("dac");

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
//        part1();
        part2();
    }
}

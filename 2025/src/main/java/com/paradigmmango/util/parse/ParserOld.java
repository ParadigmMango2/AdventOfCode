package com.paradigmmango.util.parse;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class ParserOld {
    @SneakyThrows
    static public List<String> parseLines(String path) {
        List<String> lines = new ArrayList<>();

        @Cleanup BufferedReader br = new BufferedReader(new FileReader(path));

        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }

        return lines;
    }

    @SneakyThrows
    static public List<List<Character>> parseChars(String path) {
        List<List<Character>> grid = new ArrayList<>();

        @Cleanup BufferedReader br = new BufferedReader(new FileReader(path));

        String line;
        while ((line = br.readLine()) != null) {
            List<Character> row = new ArrayList<>();

            for (char ch : line.toCharArray()) {
                row.add(ch);
            }

            grid.add(row);
        }

        return grid;
    }

    public static List<MatchResult> getMatches(Pattern pattern, String str) {
        return pattern.matcher(str)
                .results()
                .toList();
    }

    public static List<String> matchesToStrs(List<MatchResult> matches) {
        return matches.stream()
                .map(MatchResult::group)
                .toList();
    }

    public static List<List<String>> matchesToGroups(List<MatchResult> matches) {
        List<List<String>> matchGroupsList = new ArrayList<>();

        for (MatchResult match : matches) {
            List<String> matchGroups = new ArrayList<>();

            for (int groupIdx = 1; groupIdx <= match.groupCount(); groupIdx++) {
                matchGroups.add(match.group(groupIdx));
            }

            matchGroupsList.add(matchGroups);
        }

        return matchGroupsList;
    }

    public static <T> List<T> parseMatches(List<String> matches, Function<String, T> parseFn) {
        return matches.stream()
                .map(parseFn)
                .toList();
    }

    public static <T> List<List<T>> parseGroups(List<List<String>> matches, Function<String, T> parseFn) {
        return matches.stream()
                .map(groups -> groups.stream()
                        .map(parseFn)
                        .toList())
                .toList();
    }

    // Here, C is a custom object to store diverse strongly typed return variables
    public static <C> List<C> parseGroupsByGroups(List<List<String>> matches, Function<List<String>, C> parseFn) {
        return matches.stream()
                .map(parseFn)
                .toList();
    }
}

package com.paradigmmango.util.parse;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/*
 TODO:
 1. X SKIP X Streams for non-terminal operations (!!! may drastically change code character !!!)
 2. DONE Lists operations
 3. DONE Parse mapping on ParseChars
 4. Move ParseLines.getMatches(???)
*/

public class Parser {
    @SneakyThrows
    static public ParseLines parseLines(String path) {
        List<String> lines = new ArrayList<>();

        @Cleanup BufferedReader br = new BufferedReader(new FileReader(path));

        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }

        return new ParseLines(lines);
    }

    @SneakyThrows
    static public ParseChars parseChars(String path) {
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

        return new ParseChars(grid);
    }

    static public ParseMatches getMatches(Pattern pattern, String str) {
        List<MatchResult> matches = pattern.matcher(str).results()
                .toList();

        return new ParseMatches(matches);
    }
}

package com.paradigmmango.util.parse;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/*
 TODO:
 1`. X SKIP X Streams for non-terminal operations (!!! may drastically change code character !!!)
 2. Lists operations
 3. Parse mapping on ParseChars
 4. Move ParseLines.getMatches(???)
*/

public class Parser2 {
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
}

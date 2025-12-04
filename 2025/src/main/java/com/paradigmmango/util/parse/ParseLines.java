package com.paradigmmango.util.parse;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.regex.Pattern;

@AllArgsConstructor
public class ParseLines {
    @Getter private List<String> lines;

    public ParseMatchesList getMatches(Pattern pattern) {
        List<ParseMatches> parseMatchesList = lines.stream()
                .map(line -> Parser.getMatches(pattern, line))
                .toList();

        return new ParseMatchesList(parseMatchesList);
    }
}

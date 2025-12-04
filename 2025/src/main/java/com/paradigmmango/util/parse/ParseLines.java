package com.paradigmmango.util.parse;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@AllArgsConstructor
public class ParseLines {
    @Getter private List<String> lines;

//    public ParseMatches matchLines(Pattern pattern) {
//        return new ParseMatches(lines.stream()
//                .map(line -> matchLine(pattern, line))
//                .toList())
//    }

    static public ParseMatches getMatches(Pattern pattern, String str) {
        List<MatchResult> matches = pattern.matcher(str).results()
                .toList();

        return new ParseMatches(matches);
    }
}

package com.paradigmmango.util.parse;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.MatchResult;

@AllArgsConstructor
public class ParseMatches {
    @Getter private List<MatchResult> matches;

    public List<String> toStrs() {
        return matches.stream()
                .map(MatchResult::group)
                .toList();
    }

    public <T> List<T> parseStrs(Function<String, T> parseFn) {
        return matches.stream()
                .map(MatchResult::group)
                .map(parseFn)
                .toList();
    }

    public ParseGroups toGroups() {
        List<List<String>> matchGroupsList = new ArrayList<>();

        for (MatchResult match : matches) {
            List<String> matchGroups = new ArrayList<>();

            for (int groupIdx = 1; groupIdx <= match.groupCount(); groupIdx++) {
                matchGroups.add(match.group(groupIdx));
            }

            matchGroupsList.add(matchGroups);
        }

        return new ParseGroups(matchGroupsList);
    }
}

package com.paradigmmango.util.parse;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.MatchResult;

@AllArgsConstructor
public class ParseMatchesList {
    @Getter private List<ParseMatches> matchesList;

    public List<List<String>> toStrs() {
        return matchesList.stream()
                .map(ParseMatches::toStrs)
                .toList();
    }

    public <T> List<List<T>> parseStrs(Function<String, T> parseFn) {
        return matchesList.stream()
                .map(matches -> matches.parseStrs(parseFn))
                .toList();
    }

    public ParseGroupsList toGroups() {
        List<ParseGroups> parseGroupsList = matchesList.stream()
                .map(ParseMatches::toGroups)
                .toList();

        return new ParseGroupsList(parseGroupsList);
    }
}

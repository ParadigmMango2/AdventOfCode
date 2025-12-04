package com.paradigmmango.util.parse;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
public class ParseGroups {
    @Getter private List<List<String>> groups;

    public <T> List<List<T>> parse(Function<String, T> parseFn) {
        return groups.stream()
                .map(groups -> groups.stream()
                        .map(parseFn)
                        .toList())
                .toList();
    }

    // Here, C is a custom object to store diverse typed return variables
    public <C> List<C> parseByGroupSet(Function<List<String>, C> parseFn) {
        return groups.stream()
                .map(parseFn)
                .toList();
    }
}

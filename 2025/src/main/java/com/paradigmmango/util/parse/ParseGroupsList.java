package com.paradigmmango.util.parse;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
public class ParseGroupsList {
    @Getter
    List<ParseGroups> groupsList;

    public <T> List<List<List<T>>> parse(Function<String, T> parseFn) {
        return groupsList.stream()
                .map(groups -> groups.parse(parseFn))
                .toList();
    }

    // Here, C is a custom object to store diverse typed return variables
    public <C> List<List<C>> parseByGroupSet(Function<List<String>, C> parseFn) {
        return groupsList.stream()
                .map(groups -> groups.parseByGroupSet(parseFn))
                .toList();
    }
}

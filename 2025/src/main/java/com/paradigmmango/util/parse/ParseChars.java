package com.paradigmmango.util.parse;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
public class ParseChars {
    @Getter private List<List<Character>> chars;

    public <T> List<List<T>> parse(Function<Character, T> parseFn) {
        return chars.stream()
                .map(charList -> charList.stream()
                        .map(parseFn)
                        .toList())
                .toList();
    }
}

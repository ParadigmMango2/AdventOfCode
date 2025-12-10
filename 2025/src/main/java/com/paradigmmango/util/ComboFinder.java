package com.paradigmmango.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ComboFinder {
    public static <T> List<List<T>> findCombosN(T[] pool, int n, int pos, Stack<T> stack) {
        List<List<T>> combos = new ArrayList<>();

        if (stack.size() == n) {
            combos.add(stack.stream().toList());

            return combos;
        }

        for (int i = pos; i < pool.length; i++) {
            stack.push(pool[i]);
            combos.addAll(findCombosN(pool, n, i+1, stack));
            stack.pop();
        }

        return combos;
    }
}

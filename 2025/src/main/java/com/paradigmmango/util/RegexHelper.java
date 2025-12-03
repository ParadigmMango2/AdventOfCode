package com.paradigmmango.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHelper {
    public static List<String> getAll(Matcher matcher) {
        return RegexHelper.getAll(matcher, 0);
    }

    public static List<String> getAll(Matcher matcher, int group) {
        return matcher
                .results()
                .map(matchResult -> matchResult.group(group))
                .toList();
    }

    public static Matcher match(Pattern pattern, String str) {
        Matcher matcher = pattern.matcher(str);

        if (matcher.find()) {
            return matcher;
        } else {
            System.out.println("No match on str: " + str);
            return null;
        }
    }

    public static Matcher matchFull(Pattern pattern, String str) {
        Matcher matcher = pattern.matcher(str);

        if (matcher.matches()) {
            return matcher;
        } else {
            System.out.println("No match on str: " + str);
            return null;
        }
    }
}

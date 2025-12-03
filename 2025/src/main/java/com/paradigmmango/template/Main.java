package com.paradigmmango.template;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;

public class Main {
    private static final String SOURCE_PATH = "src/main/java/";

    private static String getInputsPath() {
        String[] packageArr = Main.class.getName().split("\\.");
        packageArr = Arrays.copyOfRange(packageArr, 0, packageArr.length - 1);
        String packagePath = String.join("/", packageArr);

        return SOURCE_PATH + packagePath + "/inputs/";
    }

    @SneakyThrows
    private static void parse1(String path) {
        @Cleanup BufferedReader br = new BufferedReader(new FileReader(path));

        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    @SneakyThrows
    private static void parse2(String path) {
        @Cleanup BufferedReader br = new BufferedReader(new FileReader(path));

        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static void part1() {
        Main.parse1(Main.getInputsPath() + "test1.txt");
    }

    public static void part2() {
        Main.parse2(Main.getInputsPath() + "test1.txt");
    }

    public static void main(String[] args) {
        Main.part1();
    }
}

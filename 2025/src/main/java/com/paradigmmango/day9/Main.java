package com.paradigmmango.day9;

import com.paradigmmango.util.Vector2;
import com.paradigmmango.util.Vector2Double;
import com.paradigmmango.util.parse.Parser;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Data
class Rectangle {
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private Vector2Double midpoint;

    public Rectangle(Vector2 corner1, Vector2 corner2) {
        minX = Math.min(corner1.getX(), corner2.getX());
        maxX = Math.max(corner1.getX(), corner2.getX());
        minY = Math.min(corner1.getY(), corner2.getY());
        maxY = Math.max(corner1.getY(), corner2.getY());
        midpoint = new Vector2Double((minX + maxX) / 2.0, (minY + maxY) / 2.0);
    }

    // NOTE: continuous exclusive
    // NOTE: does not account for lines which fully cross the entire rectangle, fine for this data
    // the second note is a dangerous lie! i'm keeping it around for the funnies
    public boolean contains(Line line) {
        if (line.getOrientation() == 'v') {
            if (line.getMinV().getX() > minX && line.getMinV().getX() < maxX &&
                    ((line.getMinV().getY() > minY && line.getMinV().getY() < maxY) ||
                            (line.getMaxV().getY() > minY && line.getMaxV().getY() < maxY) ||
                            (line.getMinV().getY() >= minY && line.getMaxV().getY() <= maxY) ||
                            line.containsExclusive(minY) || line.containsExclusive(maxY))) {
                return true;
            }
        } else {
            if (line.getMinV().getY() > minY && line.getMinV().getY() < maxY &&
                    ((line.getMinV().getX() > minX && line.getMinV().getX() < maxX) ||
                            (line.getMaxV().getX() > minX && line.getMaxV().getX() < maxX) ||
                            (line.getMinV().getX() >= minX && line.getMaxV().getX() <= maxX) ||
                            line.containsExclusive(minX) || line.containsExclusive(maxX))) {
                return true;
            }
        }

        return false;
    }
}

@Data
class Line {
    private Vector2 minV; // this coord has the lower ordinate values
    private Vector2 maxV;
    private char orientation; // v=vertical, h=horizontal

    public Line(Vector2 from, Vector2 to) {
        if (from.getX() == to.getX()) {
            orientation = 'v';

            if (from.getY() < to.getY()) {
                minV = from;
                maxV = to;
            } else {
                minV = to;
                maxV = from;
            }
        } else {
            orientation = 'h';

            if (from.getX() < to.getX()) {
                minV = from;
                maxV = to;
            } else {
                minV = to;
                maxV = from;
            }
        }
    }

    public boolean contains(Vector2 point) {
        if (orientation == 'v') {
            if (point.getX() != minV.getX()) return false;
            if (point.getY() < minV.getY()) return false;
            if (point.getY() > maxV.getY()) return false;
        } else {
            if (point.getY() != minV.getY()) return false;
            if (point.getX() < minV.getX()) return false;
            if (point.getX() > maxV.getX()) return false;
        }

        return true;
    }

    // do not include endpoints
    public boolean containsExclusive(Vector2 point) {
        if (orientation == 'v') {
            if (point.getX() != minV.getX()) return false;
            if (point.getY() <= minV.getY()) return false;
            if (point.getY() >= maxV.getY()) return false;
        } else {
            if (point.getY() != minV.getY()) return false;
            if (point.getX() <= minV.getX()) return false;
            if (point.getX() >= maxV.getX()) return false;
        }

        return true;
    }

    public boolean containsExclusive(double axisVal) {
        if (orientation == 'v') {
            if (axisVal <= minV.getY()) return false;
            if (axisVal >= maxV.getY()) return false;
        } else {
            if (axisVal <= minV.getX()) return false;
            if (axisVal >= maxV.getX()) return false;
        }

        return true;
    }

    public static boolean intersects(Line l1, Line l2) {
        if (l1.getOrientation() == 'v' && l2.getOrientation() == 'v') {
            if (l1.getMinV().getX() == l2.getMinV().getX() && l1.getMinV().getY() >= l2.getMinV().getY()) return true;
        } else if (l1.getOrientation() == 'h' && l2.getOrientation() == 'h') {
            if (l1.getMinV().getY() == l2.getMinV().getY() && l1.getMinV().getX() >= l2.getMinV().getX()) return true;
        } else if (l1.getOrientation() == 'v' && l2.getOrientation() == 'h') {
            Vector2 point = new Vector2(l1.getMinV().getX(), l2.getMinV().getY());

            if (l1.containsExclusive(point) && l2.containsExclusive(point)) return true;
        } else if (l1.getOrientation() == 'h' && l2.getOrientation() == 'v') {
            Vector2 point = new Vector2(l1.getMinV().getY(), l2.getMinV().getX());

            if (l1.containsExclusive(point) && l2.containsExclusive(point)) return true;
        }

        return false;
    }
}

public class Main {
    private static final String SOURCE_PATH = "src/main/java/";

    private static String getInputsPath() {
        String[] packageArr = Main.class.getName().split("\\.");
        packageArr = Arrays.copyOfRange(packageArr, 0, packageArr.length - 1);
        String packagePath = String.join("/", packageArr);

        return SOURCE_PATH + packagePath + "/inputs/";
    }

    @SneakyThrows
    private static List<Vector2> parse1(String path) {
        var coords = Parser.parseLines(path)
                .getMatches(Pattern.compile("(\\d+),(\\d+)"))
                .getFirstMatches()
                .toGroups()
                .parseByGroupSet(coord -> new Vector2(Integer.parseInt(coord.get(0)), Integer.parseInt(coord.get(1))));

        return coords;
    }

//    @SneakyThrows
//    private static void parse2(String path) {
//        @Cleanup BufferedReader br = new BufferedReader(new FileReader(path));
//
//        String line;
//        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//        }
//    }

    public static void part1() {
        List<Vector2> coords = parse1(getInputsPath() + "real.txt");

        long largestRectangle = 0;

        for (int i = 0; i < coords.size() - 1; i++) {
            Vector2 coord1 = coords.get(i);

            for (int j = i + 1; j < coords.size(); j++) {
                Vector2 coord2 = coords.get(j);

                long diffX = Math.abs(coord1.getX() - coord2.getX());
                long diffY = Math.abs(coord1.getY() - coord2.getY());

                long area = (diffX + 1) * (diffY + 1);

                if (largestRectangle < area)
                    largestRectangle = area;
            }
        }

        System.out.println(largestRectangle);
    }

    public static boolean isInALine(List<Line> lines, Vector2 point) {
        for (Line line : lines) {
            if (line.contains(point)) {
                return true;
            }
        }

        return false;
    }

    public static void part2() {
        List<Vector2> coords = parse1(getInputsPath() + "test1.txt");

        // BUILD FLOOR GRID
        ArrayList<Line> lines = new ArrayList<>();
        for (int i = 0; i < coords.size() - 1; i++) {
            Vector2 coord1 = coords.get(i);
            Vector2 coord2 = coords.get(i+1);

            lines.add(new Line(coord1, coord2));
        }
        lines.add(new Line(coords.getLast(), coords.getFirst()));

//        var line = lines.getFirst();
//        var bool = isInALine(lines, new Vector2(2,1));

        long largestRectangle = 0;

        for (int i = 0; i < coords.size() - 1; i++) {
            Vector2 coord1 = coords.get(i);

            for (int j = i + 1; j < coords.size(); j++) {
                Vector2 coord2 = coords.get(j);

                long diffX = Math.abs(coord1.getX() - coord2.getX());
                long diffY = Math.abs(coord1.getY() - coord2.getY());

                long area = (diffX + 1) * (diffY + 1);

                if (largestRectangle < area) {
                    Vector2 coord3 = new Vector2(coord1.getX(), coord2.getY());
                    Vector2 coord4 = new Vector2(coord2.getX(), coord1.getY());

                    if (isInALine(lines, coord1) &&
                            isInALine(lines, coord2) &&
                            isInALine(lines, coord3) &&
                            isInALine(lines, coord4)) {
                        largestRectangle = area;
                    }
                }
            }
        }

        System.out.println();
    }

    public static void checkIntersections() {
        List<Vector2> coords = parse1(getInputsPath() + "real.txt");

        // BUILD FLOOR GRID
        ArrayList<Line> lines = new ArrayList<>();
        for (int i = 0; i < coords.size() - 1; i++) {
            Vector2 coord1 = coords.get(i);
            Vector2 coord2 = coords.get(i+1);

            lines.add(new Line(coord1, coord2));
        }
        lines.add(new Line(coords.getLast(), coords.getFirst()));

        for (int i = 0; i < lines.size() - 1; i++) {
            Line line1 = lines.get(i);

            for (int j = i + 1; j < lines.size(); j++) {
                Line line2 = lines.get(j);

                if (Line.intersects(line1, line2)) {
                    System.out.println("INTERSECTION!");
                }
            }
        }

        char prevOrientation = 'h';
        for (int i = 0; i < lines.size(); i++) {
            char curOrientation = lines.get(i).getOrientation();

            if (curOrientation == prevOrientation)
                System.out.println("STRAIGHT");

            prevOrientation = curOrientation;
        }

        System.out.println();
    }

    public static boolean raycastIn(List<Line> vLines, Vector2Double point) {
        int intersectionCount = 0;

        double rayY = point.getY();

        List<Line> preceedingVLines = vLines.stream()
                .filter(line -> line.getMinV().getX() < point.getX())
                .toList();

        for (Line line : preceedingVLines) {
            if (line.containsExclusive(rayY)) {
                intersectionCount++;
            }
        }

        return intersectionCount % 2 == 1;
    }

    public static void nudgeToNonWhole(Vector2Double vec) {
        // nudge vector components to only intersect lines and never touch ends
        // I use thresholds below to ensure floating point shenanigans don't mess with determining whole numbers
        if (vec.getX() % 1.0 < 0.001) vec.setX(vec.getX() + 0.5);
        if (vec.getY() % 1.0 < 0.001) vec.setY(vec.getY() + 0.5);
    }

    public static boolean noneContained(List<Line> lines, Rectangle rect) {
        for (Line line : lines) {
            if (rect.contains(line)) {
                return false;
            }
        }

        return true;
    }

    public static void part2RetryRetry() {
        List<Vector2> coords = parse1(getInputsPath() + "real.txt");

        // BUILD FLOOR GRID
        ArrayList<Line> lines = new ArrayList<>();
        for (int i = 0; i < coords.size() - 1; i++) {
            Vector2 coord1 = coords.get(i);
            Vector2 coord2 = coords.get(i+1);

            lines.add(new Line(coord1, coord2));
        }
        lines.add(new Line(coords.getLast(), coords.getFirst()));

        var vLines = new ArrayList<>(lines.stream().filter(line -> line.getOrientation() == 'v').toList());
//        var hLines = new ArrayList<>(lines.stream().filter(line -> line.getOrientation() == 'h').toList());




//        Rectangle rect = new Rectangle(new Vector2(2,5), new Vector2(11,1));
//        var tLine = new Line(new Vector2(1,3), new Vector2(2,3));
//        nudgeToNonWhole(rect.getMidpoint());
//
//        boolean lol = rect.contains(tLine);
//        boolean hecc = raycastIn(vLines, rect.getMidpoint());

        long largestRectangle = 0;

        for (int i = 0; i < coords.size() - 1; i++) {
            Vector2 coord1 = coords.get(i);

            for (int j = i + 1; j < coords.size(); j++) {
                Vector2 coord2 = coords.get(j);

                long diffX = Math.abs(coord1.getX() - coord2.getX());
                long diffY = Math.abs(coord1.getY() - coord2.getY());

                long area = (diffX + 1) * (diffY + 1);

                if (largestRectangle < area) {
                    Rectangle rect = new Rectangle(coord1, coord2);
                    nudgeToNonWhole(rect.getMidpoint());

                    if (noneContained(lines, rect) && raycastIn(vLines, rect.getMidpoint())) {
                        largestRectangle = area;
                    }
                }
            }
        }

        System.out.println(largestRectangle);
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        part2RetryRetry();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.

        System.out.println(duration);
    }
}

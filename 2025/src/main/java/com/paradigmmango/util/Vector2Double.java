package com.paradigmmango.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vector2Double {
    private double x;
    private double y;

    public static double distance(Vector2Double v1, Vector2Double v2) {
        double diffX = v1.getX() - v2.getX();
        double diffY = v1.getY() - v2.getY();

        return Math.sqrt(diffX * diffX + diffY * diffY);
    }
}

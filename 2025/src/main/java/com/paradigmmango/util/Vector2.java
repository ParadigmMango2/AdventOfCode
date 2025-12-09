package com.paradigmmango.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vector2 {
    private int x;
    private int y;

    public static double distance(Vector2 v1, Vector2 v2) {
        double diffX = v1.getX() - v2.getX();
        double diffY = v1.getY() - v2.getY();

        return Math.sqrt(diffX * diffX + diffY * diffY);
    }
}

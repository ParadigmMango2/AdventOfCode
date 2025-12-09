package com.paradigmmango.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vector3 {
    private int x;
    private int y;
    private int z;

    public static double distance(Vector3 v1, Vector3 v2) {
        double diffX = v1.getX() - v2.getX();
        double diffY = v1.getY() - v2.getY();
        double diffZ = v1.getZ() - v2.getZ();

        return Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
    }
}

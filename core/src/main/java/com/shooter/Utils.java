package com.shooter;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

public class Utils {
    public static boolean overlaps(Circle circle, Rectangle rectangle) {

        float closestX = Math.max(rectangle.x, Math.min(circle.x, rectangle.x + rectangle.width));
        float closestY = Math.max(rectangle.y, Math.min(circle.y, rectangle.y + rectangle.height));

        // Calculate the distance from the circle's center to this closest point
        float distanceX = circle.x - closestX;
        float distanceY = circle.y - closestY;

        // If the distance is less than the circle's radius, there is an overlap
        return (distanceX * distanceX + distanceY * distanceY) <= (circle.radius * circle.radius);
    }
}

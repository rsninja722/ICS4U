package game.drawing;

import game.GameJava;
import game.physics.Point;

/** object to offset drawing by translations, rotation, and scaling */
public class Camera {
    /** x position of the center of the camera */
    public static int x = 0;
    /** y position of the center of the camera */
    public static int y = 0;
    /** rotation in radians */
    public static float angle = 0;
    /** how many pixels squared every pixel should render at */
    public static float zoom = 1;

    /**
     * centers the camera on xPosition,yPosition
     * @param xPosition
     * @param yPosition
     */
    public static void centerOn(int xPosition, int yPosition) {
        x = -xPosition + GameJava.gw / 2;
        y = -yPosition + GameJava.gh / 2;
    }

    /**
     * centers the camera on the point position
     * @param position {@link game.physics.Point#Point Point}
     */
    public static void centerOn(Point position) {
        x = -(int)position.x + GameJava.gw / 2;
        y = -(int)position.y + GameJava.gh / 2;
    }

    /**
     * moves the camera x,y taking into account the camera angle
     * @param xMovement
     * @param yMovement
     */
    public static void move(int xMovement, int yMovement) {
        x -= yMovement * Math.sin(angle);
        y -= yMovement * Math.cos(angle);
        x -= xMovement * Math.sin(angle + 1.57079632);
        y -= xMovement * Math.cos(angle + 1.57079632);
    }
}

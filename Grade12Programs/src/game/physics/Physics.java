package game.physics;

/** collision methods */
public class Physics {

	/**
     * calculates the distance between x1,y1 and x2,y2
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return distance
     */
	public static double dist(double x1, double y1, double x2, double y2) {
		final double one = (x2 - x1);
		final double two = (y2 - y1);
		return Math.sqrt((one * one) + (two * two));
    }
    /**
     * calculates the distance between point1 and point2
     * @param point1 {@link game.physics.Point#Point physics point}
     * @param point2 {@link game.physics.Point#Point physics point}
     * @return
     */
    public static double dist(Point point1, Point point2) {
		final double one = (point2.x - point1.x);
		final double two = (point2.y - point1.y);
		return Math.sqrt((one * one) + (two * two));
	}

	/**
     * collision between two circles
     * @param circle1 {@link game.physics.Circle#Circle physics cirlce}
     * @param circle2 {@link game.physics.Circle#Circle physics cirlce}
     * @return if the circles are colliding
     */
	public static boolean circlecircle(Circle circle1, Circle circle2) {
		if (dist(circle1.x, circle1.y, circle2.x, circle2.y) < (circle1.r + circle2.r)) {
			return true;
		} else {
			return false;
		}
	}

	/**
     * collision between two rectangles
     * @param rect1 {@link game.physics.Rect#Rect physics rect}
     * @param rect2 {@link game.physics.Rect#Rect physics rect}
     * @return if the rectangles are colliding
     */
	public static boolean rectrect(Rect rect1, Rect rect2) {
		if (rect1.x + rect1.halfW >= rect2.x - rect2.halfW && rect1.x - rect1.halfW <= rect2.x + rect2.halfW
            && rect1.y + rect1.halfH >= rect2.y - rect2.halfH && rect1.y - rect1.halfH <= rect2.y + rect2.halfH) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
     * collision between a rectangle and a point
     * @param rect {@link game.physics.Rect#Rect physics rect}
     * @param point {@link game.physics.Point#Point physics point}
     * @return if the rectangle and point are colliding
     */
	public static boolean rectpoint(Rect rect, Point point) {
	    if(rect.x + rect.w/2 >= point.x &&
	       rect.x - rect.w/2 <= point.x &&
	       rect.y + rect.h/2 >= point.y &&
	       rect.y - rect.h/2 <= point.y ) {
	        return true;
	    } else {
	        return false;
	    }
    }
    
    /**
     * collision between a circle and a point
     * @param circle {@link game.physics.Circle#Circle physics cirlce}
     * @param point {@link game.physics.Point#Point physics point}
     * @return if the circle and point are colliding
     */
    public static boolean circlepoint(Circle circle, Point point) {
		if (dist(circle.x, circle.y, point.x, point.y) < circle.r) {
			return true;
		} else {
			return false;
		}
	}

	
	/**
     * collision between a circle and a rectangle
     * @param circle {@link game.physics.Circle#Circle physics cirlce}
     * @param rect {@link game.physics.Rect#Rect physics rect}
     * @return if the circle and rectangle are colliding
     */
	public static boolean circlerect(Circle circle, Rect rect) { //credit: https://yal.cc/rectangle-circle-intersection-test/
	    final double rectHalfWidth  = rect.w/2;
	    final double rectHalfHeight = rect.h/2;
	    final double deltaX = circle.x - Math.max(rect.x - rectHalfWidth, Math.min(circle.x, rect.x + rectHalfWidth));
	    final double deltaY = circle.y - Math.max(rect.y - rectHalfHeight, Math.min(circle.y, rect.y + rectHalfHeight));
	    return (deltaX * deltaX + deltaY * deltaY) < (circle.r * circle.r);
    }
    
    // credit: http://www.jeffreythompson.org/collision-detection/line-rect.php
    // public static boolean lineRect(int x1, int y1, int x2, int y2, int rx, int ry, int rw, int rh) {

    //     // check if the line has hit any of the rectangle's sides
    //     // uses the Line/Line function below
    //     boolean left = lineLine(x1, y1, x2, y2, rx - rw/2, ry- rw/2, rx - rw/2, ry + rh/2);
    //     boolean right = lineLine(x1, y1, x2, y2, rx + rw/2, ry- rw/2, rx + rw/2, ry + rh/2);
    //     boolean top = lineLine(x1, y1, x2, y2, rx - rw/2, ry- rw/2, rx + rw/2, ry- rw/2);
    //     boolean bottom = lineLine(x1, y1, x2, y2, rx - rw/2, ry + rh/2, rx + rw/2, ry + rh/2);

    //     // if ANY of the above are true, the line
    //     // has hit the rectangle
    //     if (left || right || top || bottom) {
    //         return true;
    //     }
    //     return false;
    // }

    // // LINE/LINE
    // public static boolean lineLine(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {

    //     // calculate the direction of the lines
    //     int uA = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));
    //     int uB = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));

    //     // if uA and uB are between 0-1, lines are colliding
    //     if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
    //         return true;
    //     }
    //     return false;
    // }
}

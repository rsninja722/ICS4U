package game;

import game.physics.Point;

/** useful stuff I have found/figured out as I make games */
public class Utils {

    public static StringBuilder debugString = new StringBuilder();

    public static boolean debugMode = false;

    /**
     * Supported operating systems
     */
    public enum OperatingSystem {
        WINDOWS, LINUX, OSX, SOLARIS
    }

    /**
     * the angle something at point needs to be at to be facing target point
     * 
     * @param point       {@link game.physics.Point#Point physics point}
     * @param targetPoint {@link game.physics.Point#Point physics point}
     * @return angle in radians
     */
    public static double pointTo(Point point, Point targetPoint) {
        double adjacent = (targetPoint.x - point.x);
        double opposite = (targetPoint.y - point.y);
        double h = Math.atan2(opposite, adjacent);
        return h;
    }

    /**
     * the angle something at x1,y1 needs to be at to be facing x2,y2
     * 
     * @param x1 x of object to determine angle of
     * @param y1 y of object to determine angle of
     * @param x2 x of object to face
     * @param y2 y of object to face
     * @return angle in radians
     */
    public static double pointTo(double x1, double y1, double x2, double y2) {
        double adjacent = (x2 - x1);
        double opposite = (y2 - y1);
        double h = Math.atan2(opposite, adjacent);
        return h;
    }

    /**
     * changes an angle to another angle a certain amount
     * 
     * @param currentAngle
     * @param targetAngle
     * @param turnSpeed
     * @return the new currentAngle
     */
    public static double turnTo(double currentAngle, double targetAngle, double turnSpeed) {
        double pi = Math.PI;
        double tau = pi * 2;
        if (targetAngle < 0) {
            targetAngle = tau + targetAngle;
        }
        if ((currentAngle % tau) > targetAngle) {
            if ((currentAngle % tau) - targetAngle > pi) {
                currentAngle += turnSpeed;
            } else {
                currentAngle -= turnSpeed;
            }
        } else {
            if (targetAngle - (currentAngle % tau) > pi) {
                currentAngle -= turnSpeed;
            } else {
                currentAngle += turnSpeed;
            }
        }
        if (Math.abs(currentAngle - targetAngle) < turnSpeed * 1.1) {
            currentAngle = targetAngle;
        }
        if (currentAngle > tau) {
            currentAngle = currentAngle - tau;
        }
        if (currentAngle < 0) {
            currentAngle = tau + currentAngle;
        }
        return currentAngle;
    }

    /**
     * @param min minimum value inclusive
     * @param max maximum value inclusive
     * @return random number between min and max
     */
    public static int rand(int min, int max) {
        return (int) (Math.floor(Math.random() * (max - min + 1)) + min);
    }

    /**
     * linear interpilation, used to slowly move a value towards another value
     * 
     * @param start  current value
     * @param end    target value
     * @param amount value from 0 to 1, higher values moves faster
     * @return new current value
     */
    public static double lerp(double start, double end, double amount) {
        return (1 - amount) * start + amount * end;
    }

    /**
     * moves a value towards 0 by frictionAmount, once close to 0 the value will
     * snap to 0
     * 
     * @param value          current value
     * @param frictionAmount value reduction
     * @return new value
     */
    public static double friction(double value, double frictionAmount) {
        if (value > 0) {
            value -= frictionAmount;
        }
        if (value < 0) {
            value += frictionAmount;
        }
        if (Math.abs(value) < frictionAmount * 2) {
            value = 0;
        }
        return value;
    }

    /**
     * limits a number between a max and min
     * 
     * @param value number to limit
     * @param max   maximum value
     * @param min   minimum value
     * @return limited value
     */
    public static double limit(double value, double max, double min) {
        return value < min ? min : (value > max ? max : value);
    }

    /**
     * takes a range of numbers and maps it to another range of numbers
     * 
     * @param value        current value
     * @param valueLow     current value minimum
     * @param valueHigh    current value maximum
     * @param remappedLow  return value minimum
     * @param remappedHigh return value maximum
     * @return number between remappedLow and remappedHigh
     */
    public static double mapRange(double value, double valueLow, double valueHigh, double remappedLow,
            double remappedHigh) {
        return remappedLow + (remappedHigh - remappedLow) * (value - valueLow) / (valueHigh - valueLow);
    }

    /**
     * puts a number into the debug screen
     * 
     * @param lable label of the value
     * @param value value to show
     */
    public static void putInDebugMenu(String lable, double value) {
        debugString.append("[" + lable + "] " + Math.round(value * 1000.0) / 1000.0 + "\n");
    }

    /**
     * puts a string into the debug screen
     * 
     * @param lable label of the sting
     * @param text  string to show
     */
    public static void putInDebugMenu(String lable, String text) {
        debugString.append("[" + lable + "] " + text + "\n");
    }

    /**
     * Get the host operating system type
     * 
     * @return Operating system
     */
    public static OperatingSystem getOperatingSystem() {

        // Read OS property
        String osStr = System.getProperty("os.name").toLowerCase();

        // OS value (default to linux because most systems support linux paths)
        OperatingSystem os = OperatingSystem.LINUX;

        // Find actual OS type
        if (osStr.contains("win")) {
            os = OperatingSystem.WINDOWS;
        } else if (osStr.contains("nix") || osStr.contains("nux") || osStr.contains("aix")) {
            os = OperatingSystem.LINUX;
        } else if (osStr.contains("mac")) {
            os = OperatingSystem.OSX;
        } else if (osStr.contains("sunos")) {
            os = OperatingSystem.SOLARIS;
        }
        return os;
    }
}
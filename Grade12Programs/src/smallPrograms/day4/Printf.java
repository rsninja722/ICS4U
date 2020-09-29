package smallPrograms.day4;

/**
 * James N 
 * 2020.09.28 
 * Printf 
 * prints out a formatted table of values
 */

public class Printf {
    public static void main(String[] args) {
        System.out.printf("  x |   y    \n");
        System.out.printf("----+--------\n");
        for (int x = -2; x < 13; x++) {
            System.out.printf(" %2d | %6.3f \n", x, f(x));
        }
    }

    public static double f(int x) {
        return 8 * Math.sqrt(x) - 10 * Math.sin(x) - 0.003 * Math.pow(x, 3) - 5;
    }
}

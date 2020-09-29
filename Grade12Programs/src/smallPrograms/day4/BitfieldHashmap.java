package smallPrograms.day4;

/**
 * James N 
 * 2020.09.28 
 * BitfieldHashmap
 * 
 * represents printer statuses as bitfield integers and stores them in a hashmap, then prints information on them. 
 * bitfield representation:
 * monochrome, single sided, portrait oriented, letter paper
 */

import java.util.HashMap;

public class BitfieldHashmap {

    static HashMap<String, Integer> printers = new HashMap<String, Integer>();

    static final String SPECIFIC_PRINTER_NAME = "Math";

    public static void main(String[] args) {
        // create printers
        printers.put("Basement", bool4ToInt(true, false, false, true));
        printers.put("Math", bool4ToInt(false, true, true, true));
        printers.put("English", bool4ToInt(false, false, false, true));
        printers.put("History", bool4ToInt(true, true, false, false));

        // print all printer names and status numbers
        for (String s : printers.keySet()) {
            System.out.printf("name: %-15s status: %d\n", s, printers.get(s));
        }

        // print specific
        boolean[] bools = intToBool4(printers.get(SPECIFIC_PRINTER_NAME));
        System.out.printf("\n%s's status: %s, %s, %s, %s\n\n", SPECIFIC_PRINTER_NAME, (bools[3] ? "mono" : "color"), (bools[2] ? "single" : "double"), (bools[1] ? "portrait" : "landscape"), (bools[0] ? "letter" : "legal"));

        // find all printers set to letter size
        System.out.println("printers set to letter size:");
        for (String s : printers.keySet()) {
            if ((printers.get(s) & 0b0001) == 1) {
                System.out.println(s);
            }
        }
    }

    public static int bool4ToInt(boolean a, boolean b, boolean c, boolean d) {
        return (a ? 0b1000 : 0) + (b ? 0b0100 : 0) + (c ? 0b0010 : 0) + (d ? 0b0001 : 0);
    }

    public static boolean[] intToBool4(int i) {
        boolean[] values = { (i & 0b1000) == 1, (i & 0b0100) == 1, (i & 0b0010) == 1, (i & 0b0001) == 1 };
        return values;
    }
}

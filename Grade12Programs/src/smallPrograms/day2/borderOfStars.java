package smallPrograms.day2;

import java.util.Arrays;
import java.util.Scanner;

public class borderOfStars {
    static final int MAX_MESSAGE_LENGTH = 20;

    public static void main(String[] Args) {
        // get text from user
        Scanner scan = new Scanner(System.in);
        System.out.println("enter text to format: ");
        String text = scan.nextLine();

        // get list of words
        String[] words = text.split(" ");

        StringBuilder output = new StringBuilder();

        // top line
        for (int i = 0; i < MAX_MESSAGE_LENGTH + 4; i++) {
            output.append("*");
        }

        int lineLength = 0;

        output.append("\n* ");
        for (int i = 0; i < words.length; i++) {

            if (lineLength + words[i].length() + 1 > MAX_MESSAGE_LENGTH) {
                // end line
                for (int j = lineLength; j < MAX_MESSAGE_LENGTH + 1; j++) {
                    output.append(" ");
                }
                output.append("*\n* ");
                // put word on next line
                lineLength = 0;
                output.append(words[i] + " ");
            } else {
                // add word in line
                output.append(words[i] + " ");
            }

            lineLength += words[i].length() + 1;
        }

        // end of last line
        for (int j = lineLength; j < MAX_MESSAGE_LENGTH + 1; j++) {
            output.append(" ");
        }
        output.append("*\n");

        // bottom line
        for (int i = 0; i < MAX_MESSAGE_LENGTH + 4; i++) {
            output.append("*");
        }

        System.out.println(output);

    }
}
package smallPrograms.day2;

public class lettersInCommon {
    public static void main(String[] Args) {
        // words to look through
        String[] words = { "rampur", "mingus", "tither", "podite", "omelet", "skewer", "reside", "snuffy", "sateen",
                "butter", "rerent", "filtre", "stoker", "marino", "zonule", "teniae", "niobid", "miguel", "largen",
                "arliss", "getter", "corves", "kilted", "djilas", "wonted", "uropod", "galeus", "gidgee", "vienne",
                "nimrod", "ninety", "seller", "basify", "kaunda", "ardour", "nablus", "dynast", "forger", "sigher",
                "carlow", "hatred", "cunaxa", "olivet", "bundle", "fondle", "rebury", "intuit", "tamest", "tanguy",
                "ropery" };

        // words to choose from for the other word
        String[] wordsToMatch = { "bougie", "carius", "mosaic", "rhodic", "jayvee", "fergus", "trajan", "tromso",
                "blight", "picaro", "situla", "coking", "galatz", "ilford", "nudger", "naiant", "strops", "plauen",
                "fortis" };

        // counts of letters in words that are being looked through
        int[] searchLetters = new int[26];

        // counts of letter in word to match with
        int[] matchLetters = new int[26];

        // chose a random word to be matching with
        String wordToMatch = wordsToMatch[(int) Math.round(Math.random() * 18)];

        String bestString = "";
        int bestMatchCount = 0;

        // count letters in word
        for (int i = 0; i < 26; i++) {
            matchLetters[i] = 0;
        }
        for (int i = 0; i < 6; i++) {
            matchLetters[wordToMatch.charAt(i) - 97] += 1;
        }

        for (int i = 0; i < words.length; i++) {
            // count letters in word being looked through
            for (int j = 0; j < 26; j++) {
                searchLetters[j] = 0;
            }
            for (int j = 0; j < 6; j++) {
                searchLetters[words[i].charAt(j) - 97] += 1;
            }

            // count matching letters
            int matchCount = 0;
            for (int j = 0; j < 26; j++) {
                matchCount += Math.min(matchLetters[j], searchLetters[j]);
            }

            // see if this word has the most matches
            if (matchCount > bestMatchCount || i == 0) {
                bestMatchCount = matchCount;
                bestString = words[i];
            }
        }

        System.out.println("the word " + bestString + " had " + bestMatchCount + " letters in common with " + wordToMatch);
    }
}
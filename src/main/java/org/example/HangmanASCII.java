package org.example;

public class HangmanASCII {
    public static String getHangmanPicture(int misses) {
        String[] picture = new String[7];
        picture[0] = "  +---+";
        picture[1] = "  |   |";

        if (misses > 0) {
            picture[2] = "  O   |";
        } else {
            picture[2] = "      |";
        }

        if (misses == 2) {
            picture[3] = "      |";
        } else if (misses == 3) {
            picture[3] = " /    |";
        } else if (misses >= 4) {
            picture[3] = " /|\\  |";
        } else {
            picture[3] = "      |";
        }

        if (misses == 5) {
            picture[4] = " /    |";
        } else if (misses >= 6) {
            picture[4] = " / \\  |";
        } else {
            picture[4] = "      |";
        }

        picture[5] = "      |";
        picture[6] = "=========";

        StringBuilder sb = new StringBuilder();
        for (String line : picture) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}

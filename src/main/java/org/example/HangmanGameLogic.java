package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class HangmanGameLogic {
    public static final int MAX_MISSES = 6;

    private final String secretWord;
    private final char[] guessedWord;
    private int misses;
    private final Set<Character> guessedLetters;

    public HangmanGameLogic() {
        this.secretWord = getRandomWordFromAPI();
        guessedWord = new char[secretWord.length()];
        Arrays.fill(guessedWord, '_');
        misses = 0;
        guessedLetters = new HashSet<>();
    }

    public String processLetterGuess(char letter) {
        letter = Character.toLowerCase(letter);
        if (guessedLetters.contains(letter)) {
            return "Вы уже вводили букву \"" + letter + "\".";
        }
        guessedLetters.add(letter);

        if (secretWord.indexOf(letter) >= 0) {
            for (int i = 0; i < secretWord.length(); i++) {
                if (secretWord.charAt(i) == letter) {
                    guessedWord[i] = letter;
                }
            }
            return "Правильно!";
        } else {
            misses++;
            return "Неправильно!";
        }
    }

    public boolean processWordGuess(String guess) {
        guess = guess.toLowerCase();
        if (guess.equals(secretWord)) {
            for (int i = 0; i < secretWord.length(); i++) {
                guessedWord[i] = secretWord.charAt(i);
            }
            return true;
        } else {
            endGameAsLost();
            return false;
        }
    }

    public void endGameAsLost() {
        misses = MAX_MISSES;
    }

    public boolean isGameWon() {
        for (char c : guessedWord) {
            if (c == '_') {
                return false;
            }
        }
        return true;
    }

    public boolean isGameOver() {
        return (misses >= MAX_MISSES) || isGameWon();
    }

    public int getMisses() {
        return misses;
    }

    public String getGuessedWord() {
        return new String(guessedWord);
    }

    public String getSecretWord() {
        return secretWord;
    }

    private String getRandomWordFromAPI() {
        int attempts = 100;

        while (attempts-- > 0) {
            try {
                String urlStr = "https://ru.wiktionary.org/w/api.php"
                        + "?action=query"
                        + "&format=json"
                        + "&generator=random"
                        + "&namespace=0"
                        + "&limit=1";

                URL url = new URL(urlStr);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", "Mozilla/5.0");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();

                JSONObject json = new JSONObject(content.toString());

                if (!json.has("query")) continue;

                JSONObject query = json.getJSONObject("query");
                JSONObject pages = query.getJSONObject("pages");

                for (String key : pages.keySet()) {
                    JSONObject page = pages.getJSONObject(key);
                    String title = page.getString("title").toLowerCase();

                    if (!title.contains(":") && title.matches("[а-яё]{4,7}$")) {
                        return title;
                    }
                }
            } catch (Exception e) {
                System.err.println("Ошибка при получении случайного слова из API: " + e.getMessage());
            }
        }
        return "виселица";
    }
}

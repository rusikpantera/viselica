package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Виселица ===");
            System.out.println("1. Начать новую игру");
            System.out.println("2. Выйти");
            System.out.print("Выберите пункт меню: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                startNewGame(scanner);
            } else if (choice.equals("2")) {
                System.out.println("Выход из приложения. До встречи!");
                break;
            } else {
                System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
        scanner.close();
    }

    private static void startNewGame(Scanner scanner) {
        System.out.println("\nВыберите уровень сложности:");
        System.out.println("1. Лёгкий (встроенный словарь)");
        System.out.println("2. Сложный (слово через API Wiktionary)");
        System.out.print("Ваш выбор: ");
        String diff = scanner.nextLine().trim();

        boolean isEasy = diff.equals("1");

        HangmanGameLogic game = new HangmanGameLogic(isEasy);
        System.out.println("\nНовая игра началась!");

        while (!game.isGameOver()) {
            displayGameState(game);

            System.out.print("Введите букву или слово целиком: ");
            String input = scanner.nextLine().toLowerCase().trim();

            if (input.isEmpty()) {
                System.out.println("Пустой ввод! Пожалуйста, введите букву или слово.");
                continue;
            }

            if (input.length() == 1) {
                char letter = input.charAt(0);
                if (!Character.isLetter(letter)) {
                    System.out.println("Пожалуйста, введите букву, а не другие символы.");
                    continue;
                }
                String response = game.processLetterGuess(letter);
                System.out.println(response);
            } else {
                boolean correct = game.processWordGuess(input);
                if (correct) {
                    System.out.println("Поздравляем! Вы угадали слово целиком!");
                } else {
                    System.out.println("Неверное слово! Вы проиграли.");
                    game.endGameAsLost();
                }
                break;
            }
        }
        displayFinalResult(game);
    }

    private static void displayGameState(HangmanGameLogic game) {
        System.out.println("\nСлово: " + game.getGuessedWord());
        System.out.println("Ошибки: " + game.getMisses() + " из " + HangmanGameLogic.MAX_MISSES);
        System.out.println(HangmanASCII.getHangmanPicture(game.getMisses()));
    }

    private static void displayFinalResult(HangmanGameLogic game) {
        if (game.isGameWon()) {
            System.out.println("Вы выиграли! Загаданное слово: " + game.getSecretWord());
        } else {
            System.out.println(HangmanASCII.getHangmanPicture(game.getMisses()));
            System.out.println("Вы проиграли. Загаданное слово: " + game.getSecretWord());
        }
    }

}

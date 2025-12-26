package ru.vsu.vinyukov.utils;

import ru.vsu.vinyukov.game.core.Position;

import java.util.Scanner;

public class ConsoleInput {
    private static final Scanner scanner = new Scanner(System.in);

    public static String readLine() {
        return scanner.nextLine().trim();
    }

    public static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите целое число:");
            }
        }
    }

    public static Position readPosition() {
        while (true) {
            System.out.print("Введите позицию (например, e2): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.length() == 2) {
                char colChar = input.charAt(0);
                char rowChar = input.charAt(1);

                if (colChar >= 'a' && colChar <= 'i' &&
                        rowChar >= '1' && rowChar <= '9') {

                    int col = colChar - 'a';
                    int row = 9 - (rowChar - '1');

                    return new Position(row, col);
                }
            }
            System.out.println("Неверный формат. Попробуйте снова.");
        }
    }

    public static void close() {
        scanner.close();
    }
}
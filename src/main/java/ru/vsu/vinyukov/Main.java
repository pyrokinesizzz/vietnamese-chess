package ru.vsu.vinyukov;

import ru.vsu.vinyukov.config.GameConfig;
import ru.vsu.vinyukov.game.core.*;
import ru.vsu.vinyukov.game.bot.RandomBot;
import ru.vsu.vinyukov.game.rules.GameTimer;
import ru.vsu.vinyukov.ui.GameView;
import ru.vsu.vinyukov.utils.ConsoleInput;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Добро пожаловать во Вьетнамские шахматы!");
        System.out.println("Введите 'Хочу играть!' для интерактивного режима");
        System.out.println("Введите 'Я наблюдатель' для неинтерактивного режима");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("Хочу играть!")) {
            startInteractiveMode(scanner);
        } else if (input.equalsIgnoreCase("Я наблюдатель")) {
            startNonInteractiveMode();
        } else {
            System.out.println("Неизвестная команда. Завершение программы.");
        }

        scanner.close();
    }

    private static void startInteractiveMode(Scanner scanner) {
        System.out.println("Выберите цвет (красный/черный) или нажмите Enter для случайного выбора:");
        String colorInput = scanner.nextLine().trim();

        PieceColor playerColor;
        if (colorInput.equalsIgnoreCase("красный")) {
            playerColor = PieceColor.RED;
        } else if (colorInput.equalsIgnoreCase("черный")) {
            playerColor = PieceColor.BLACK;
        } else {
            playerColor = Math.random() > 0.5 ? PieceColor.RED : PieceColor.BLACK;
            System.out.println("Случайный выбор: " + playerColor.getDisplayName());
        }

        GameConfig config = GameConfig.getInstance();
        System.out.println("Запуск интерактивного режима...");

        GameView.launchGame(playerColor);
    }

    private static void startNonInteractiveMode() {
        System.out.println("Запуск неинтерактивного режима...");

        Board board = new Board();
        RandomBot redBot = new RandomBot(PieceColor.RED);
        RandomBot blackBot = new RandomBot(PieceColor.BLACK);
        GameTimer timer = new GameTimer(GameConfig.getInstance().getGameTimeoutMinutes());

        timer.start();

        int moveCount = 0;
        while (moveCount < 100 && timer.isRunning()) { // Максимум 100 ходов
            // Ход красных
            System.out.println("\nХод " + (moveCount + 1) + ": Красные");
            Move redMove = redBot.makeMove(board);
            if (redMove != null) {
                board.movePiece(redMove);
                System.out.println("Красные: " + redMove);
                printBoard(board);
            }

            if (checkGameOver(board, timer)) break;

            // Ход черных
            System.out.println("\nХод " + (moveCount + 2) + ": Черные");
            Move blackMove = blackBot.makeMove(board);
            if (blackMove != null) {
                board.movePiece(blackMove);
                System.out.println("Черные: " + blackMove);
                printBoard(board);
            }

            if (checkGameOver(board, timer)) break;

            moveCount += 2;

            try {
                Thread.sleep(1000); // Пауза между ходами
            } catch (InterruptedException e) {
                break;
            }
        }

        timer.stop();
        System.out.println("\nИгра завершена!");
    }

    private static boolean checkGameOver(Board board, GameTimer timer) {
        if (!timer.isRunning()) {
            System.out.println("Время вышло! Ничья.");
            return true;
        }

        // Проверка на мат (упрощенная)
        Position redGeneral = board.findGeneral(PieceColor.RED);
        Position blackGeneral = board.findGeneral(PieceColor.BLACK);

        if (redGeneral == null) {
            System.out.println("Черные выиграли! Генерал красных захвачен.");
            return true;
        }

        if (blackGeneral == null) {
            System.out.println("Красные выиграли! Генерал черных захвачен.");
            return true;
        }

        return false;
    }

    private static void printBoard(Board board) {
        System.out.println("  a b c d e f g h i");
        for (int row = 0; row < board.getHeight(); row++) {
            System.out.print((9 - row) + " ");
            for (int col = 0; col < board.getWidth(); col++) {
                Piece piece = board.getPiece(new Position(row, col));
                if (piece == null) {
                    System.out.print(". ");
                } else {
                    String symbol = piece.getSymbol();
                    System.out.print((piece.getColor() == PieceColor.RED ?
                            symbol.toUpperCase() : symbol.toLowerCase()) + " ");
                }
            }
            System.out.println((9 - row));
        }
        System.out.println("  a b c d e f g h i");
    }
}
package ru.vsu.vinyukov.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ru.vsu.vinyukov.config.GameConfig;
import ru.vsu.vinyukov.game.core.*;
import ru.vsu.vinyukov.game.bot.RandomBot;
import ru.vsu.vinyukov.game.rules.GameTimer;
import java.util.function.Consumer;
import java.util.Timer;
import java.util.TimerTask;

public class GameView extends Application {
    private static PieceColor playerColor;
    private GameController controller;
    private Stage primaryStage;

    public static void launchGame(PieceColor playerColor) {
        GameView.playerColor = playerColor;
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        try {
            GameConfig config = GameConfig.getInstance();
            Board board = new Board();
            RandomBot bot = new RandomBot(playerColor.getOpponent());
            GameTimer timer = new GameTimer(config.getGameTimeoutMinutes());

            // Создаем контроллер
            controller = new GameController(board, playerColor, bot, timer);

            // Создаем UI
            ChessBoardUI boardUI = new ChessBoardUI(controller);
            controller.setBoardUI(boardUI);

            Scene scene = new Scene(boardUI.createBoard(), 900, 700);

            // Пробуем загрузить стили
            try {
                String cssPath = getClass().getResource("/styles/styles.css").toExternalForm();
                scene.getStylesheets().add(cssPath);
                System.out.println("CSS файл загружен успешно");
            } catch (Exception e) {
                System.out.println("CSS файл не найден, используется стандартный стиль");
            }

            primaryStage.setTitle("Вьетнамские шахматы - Играете за " + playerColor);
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e -> {
                controller.stopGame();
                Platform.exit();
                System.exit(0);
            });

            // Создаем callback для завершения игры
            Consumer<String> gameOverCallback = (result) -> {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Игра завершена");
                    alert.setHeaderText(null);
                    alert.setContentText(result);
                    alert.showAndWait();
                    primaryStage.close();
                });
            };

            // Устанавливаем callback в контроллер
            controller.setGameOverCallback(gameOverCallback);

            // Запускаем таймер
            timer.start();

            // Если игрок черными, бот должен ходить первым
            if (playerColor == PieceColor.BLACK) {
                System.out.println("Вы играете черными. Бот делает первый ход...");

                // Делаем первый ход бота с задержкой, чтобы UI успел отрисоваться
                Timer startTimer = new Timer();
                startTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            controller.makeBotMove();
                            System.out.println("Бот сделал первый ход");
                        });
                    }
                }, 1000); // Задержка 1 секунда для начала игры
            } else {
                System.out.println("Вы играете красными. Ваш ход первый.");
            }

            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Ошибка запуска игры: " + e.getMessage());
        }
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
            Platform.exit();
            System.exit(1);
        });
    }
}
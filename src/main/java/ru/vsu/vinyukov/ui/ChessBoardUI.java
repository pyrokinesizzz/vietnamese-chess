package ru.vsu.vinyukov.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ru.vsu.vinyukov.config.GameConfig;
import ru.vsu.vinyukov.game.core.*;

public class ChessBoardUI {
    private final GameController controller;
    private final int cellSize;
    private GridPane boardGrid;
    private PieceUI selectedPiece = null;
    private Label timerLabel;
    private Label statusLabel;
    private Label playerLabel;

    public ChessBoardUI(GameController controller) {
        this.controller = controller;
        this.cellSize = GameConfig.getInstance().getCellSize();
    }

    public BorderPane createBoard() {
        BorderPane mainPane = new BorderPane();
        mainPane.getStyleClass().add("root");

        // Левая панель - доска
        boardGrid = createBoardGrid();
        BorderPane.setAlignment(boardGrid, Pos.CENTER);
        mainPane.setCenter(boardGrid);

        // Правая панель - информация
        VBox infoPanel = createInfoPanel();
        mainPane.setRight(infoPanel);

        // Нижняя панель - статус
        HBox statusPanel = createStatusPanel();
        mainPane.setBottom(statusPanel);

        return mainPane;
    }

    private GridPane createBoardGrid() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("board");

        Board board = controller.getBoard();
        int width = board.getWidth();
        int height = board.getHeight();

        // Создаем клетки доски
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                StackPane cell = createCell(row, col);
                grid.add(cell, col, row);

                // Добавляем фигуру если есть
                Position pos = new Position(row, col);
                Piece piece = board.getPiece(pos);
                if (piece != null) {
                    PieceUI pieceUI = new PieceUI(piece, cellSize);
                    cell.getChildren().add(pieceUI);

                    // Назначаем обработчик клика только для фигур игрока
                    if (piece.getColor() == controller.getPlayerColor()) {
                        pieceUI.setOnMouseClicked(e -> handlePieceClick(pieceUI, pos));
                    }
                }
            }
        }

        return grid;
    }

    private StackPane createCell(int row, int col) {
        StackPane cell = new StackPane();
        cell.setPrefSize(cellSize, cellSize);

        // Определяем цвет клетки
        boolean isLight = (row + col) % 2 == 0;
        Rectangle background = new Rectangle(cellSize, cellSize);
        if (isLight) {
            background.setFill(Color.web("#F0D9B5"));
        } else {
            background.setFill(Color.web("#B58863"));
        }

        // Добавляем реку (середина доски)
        if (row == 4 || row == 5) {
            background.setStroke(Color.BLUE);
            background.setStrokeWidth(1);
        }

        // Добавляем дворцы
        if (isInPalace(row, col)) {
            Circle palaceMarker = new Circle(5);
            palaceMarker.setFill(Color.RED);
            palaceMarker.setOpacity(0.3);
            cell.getChildren().add(palaceMarker);
        }

        cell.getChildren().add(0, background);

        // Обработчик клика по клетке
        cell.setOnMouseClicked(e -> handleCellClick(row, col));

        return cell;
    }

    private boolean isInPalace(int row, int col) {
        return (row >= 0 && row <= 2 && col >= 3 && col <= 5) ||  // Красный дворец
                (row >= 7 && row <= 9 && col >= 3 && col <= 5);    // Черный дворец
    }

    private VBox createInfoPanel() {
        VBox infoPanel = new VBox(20);
        infoPanel.setStyle("-fx-background-color: #3C3C3C; -fx-padding: 15; -fx-border-color: #4A90E2; -fx-border-width: 2; -fx-border-radius: 5;");
        infoPanel.setPrefWidth(250);
        infoPanel.setPadding(new Insets(20));

        // Заголовок
        Label title = new Label("ВЬЕТНАМСКИЕ ШАХМАТЫ");
        title.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 18; -fx-font-weight: bold;");

        // Игрок
        playerLabel = new Label("Вы играете: " +
                (controller.getPlayerColor() == PieceColor.RED ? "Красные" : "Черные"));
        playerLabel.setStyle("-fx-text-fill: #CCCCCC; -fx-font-size: 14;");

        // Таймер
        timerLabel = new Label("05:00");
        timerLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 24; -fx-font-weight: bold;");

        // Кнопка сдачи
        Button resignButton = new Button("Сдаться");
        resignButton.setStyle("-fx-background-color: #4A90E2; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-background-radius: 5;");
        resignButton.setOnMouseEntered(e -> resignButton.setStyle("-fx-background-color: #357ABD; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-background-radius: 5;"));
        resignButton.setOnMouseExited(e -> resignButton.setStyle("-fx-background-color: #4A90E2; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-background-radius: 5;"));
        resignButton.setOnAction(e -> controller.resign());

        // Кнопка новой игры
        Button newGameButton = new Button("Новая игра");
        newGameButton.setStyle("-fx-background-color: #4A90E2; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-background-radius: 5;");
        newGameButton.setOnMouseEntered(e -> newGameButton.setStyle("-fx-background-color: #357ABD; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-background-radius: 5;"));
        newGameButton.setOnMouseExited(e -> newGameButton.setStyle("-fx-background-color: #4A90E2; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-background-radius: 5;"));
        newGameButton.setOnAction(e -> controller.newGame());

        // Разделитель
        Separator separator = new Separator();
        separator.setPrefWidth(200);

        infoPanel.getChildren().addAll(
                title, playerLabel, timerLabel,
                separator, resignButton, newGameButton
        );

        return infoPanel;
    }

    private HBox createStatusPanel() {
        HBox statusPanel = new HBox(10);
        statusPanel.setStyle("-fx-background-color: #4A4A4A; -fx-padding: 10; -fx-border-color: #666666; -fx-border-width: 1;");
        statusPanel.setPadding(new Insets(10));
        statusPanel.setAlignment(Pos.CENTER_LEFT);

        statusLabel = new Label("Ваш ход. Выберите фигуру.");
        statusLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");

        statusPanel.getChildren().add(statusLabel);

        return statusPanel;
    }

    private void handlePieceClick(PieceUI pieceUI, Position pos) {
        if (controller.isGameOver()) {
            return;
        }

        if (!controller.isPlayerTurn()) {
            updateStatus("Сейчас ход противника. Подождите...");
            return;
        }

        // Снимаем выделение с предыдущей фигуры
        if (selectedPiece != null) {
            selectedPiece.setSelected(false);
        }

        // Выделяем новую фигуру
        pieceUI.setSelected(true);
        selectedPiece = pieceUI;

        // Показываем возможные ходы
        showValidMoves(pos);

        updateStatus("Фигура выбрана. Выберите клетку для хода.");
    }

    private void handleCellClick(int row, int col) {
        if (controller.isGameOver() || selectedPiece == null) {
            return;
        }

        if (!controller.isPlayerTurn()) {
            updateStatus("Сейчас не ваш ход. Подождите...");
            return;
        }

        Position targetPos = new Position(row, col);

        if (controller.makePlayerMove(selectedPiece.getPiece().getPosition(), targetPos)) {
            clearHighlights();
            selectedPiece.setSelected(false);
            selectedPiece = null;

            // Доска обновится автоматически через Platform.runLater
            // в методах makePlayerMove и makeBotMove
        } else {
            updateStatus("Недопустимый ход. Попробуйте снова.");
        }
    }

    private void showValidMoves(Position from) {
        clearHighlights();

        // Подсвечиваем выбранную фигуру
        Node cell = getCellAt(from.getRow(), from.getCol());
        if (cell instanceof StackPane) {
            Rectangle bg = (Rectangle) ((StackPane) cell).getChildren().get(0);
            bg.setStroke(Color.YELLOW);
            bg.setStrokeWidth(3);
        }

        // Показываем допустимые ходы
        for (Position move : controller.getValidMoves(from)) {
            Node targetCell = getCellAt(move.getRow(), move.getCol());
            if (targetCell instanceof StackPane) {
                Rectangle bg = (Rectangle) ((StackPane) targetCell).getChildren().get(0);
                bg.setStroke(Color.GREEN);
                bg.setStrokeWidth(2);
            }
        }
    }

    private void clearHighlights() {
        for (Node node : boardGrid.getChildren()) {
            if (node instanceof StackPane) {
                Rectangle bg = (Rectangle) ((StackPane) node).getChildren().get(0);
                bg.setStroke(null);
                bg.setStrokeWidth(0);
            }
        }
    }

    private Node getCellAt(int row, int col) {
        for (Node node : boardGrid.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return node;
            }
        }
        return null;
    }

    public void updateBoard() {
        boardGrid.getChildren().clear();

        Board board = controller.getBoard();
        int width = board.getWidth();
        int height = board.getHeight();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                StackPane cell = createCell(row, col);
                boardGrid.add(cell, col, row);

                Position pos = new Position(row, col);
                Piece piece = board.getPiece(pos);
                if (piece != null) {
                    PieceUI pieceUI = new PieceUI(piece, cellSize);
                    cell.getChildren().add(pieceUI);

                    if (piece.getColor() == controller.getPlayerColor()) {
                        pieceUI.setOnMouseClicked(e -> handlePieceClick(pieceUI, pos));
                    }
                }
            }
        }

        // Обновляем статус
        if (controller.isGameOver()) {
            updateStatus("Игра завершена. " + controller.getGameResult());
        } else if (controller.isPlayerTurn()) {
            updateStatus("Ваш ход. Выберите фигуру.");
        } else {
            updateStatus("Ход противника...");
        }
    }

    public void updateTimer(String time) {
        timerLabel.setText(time);
    }

    public void updateStatus(String message) {
        statusLabel.setText(message);

        // Меняем цвет в зависимости от статуса
        if (message.contains("ошибка") || message.contains("недопустимый")) {
            statusLabel.setStyle("-fx-text-fill: #F44336; -fx-font-weight: bold;");
        } else if (message.contains("ход противника")) {
            statusLabel.setStyle("-fx-text-fill: #FF9800; -fx-font-weight: bold;");
        } else {
            statusLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
        }
    }

    public void updateGameInfo() {
        playerLabel.setText("Вы играете: " +
                (controller.getPlayerColor() == PieceColor.RED ? "Красные" : "Черные"));
    }
}
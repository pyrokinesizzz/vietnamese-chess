package ru.vsu.vinyukov.ui;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import ru.vsu.vinyukov.config.ColorTheme;
import ru.vsu.vinyukov.game.core.Piece;
import ru.vsu.vinyukov.game.core.PieceColor;

public class PieceUI extends StackPane {
    private final Piece piece;
    private final Circle background;
    private final Text symbol;
    private boolean selected = false;

    public PieceUI(Piece piece, int cellSize) {
        this.piece = piece;

        int radius = cellSize / 2 - 5;

        // Фон фигуры
        background = new Circle(radius);
        background.setFill(piece.getColor() == PieceColor.RED ?
                ColorTheme.RED_PIECE : ColorTheme.BLACK_PIECE);
        background.setStroke(piece.getColor() == PieceColor.RED ?
                Color.DARKRED : Color.DARKGRAY);
        background.setStrokeWidth(2);

        // Символ фигуры
        symbol = new Text(piece.getSymbol());
        symbol.setFont(Font.font("Arial", FontWeight.BOLD, radius));
        symbol.setFill(Color.WHITE);

        getChildren().addAll(background, symbol);

        // Эффект при наведении (только для фигур игрока)
        if (piece.getColor() == PieceColor.RED) {
            setOnMouseEntered(e -> {
                if (!selected) {
                    background.setStroke(Color.YELLOW);
                }
            });

            setOnMouseExited(e -> {
                if (!selected) {
                    background.setStroke(Color.DARKRED);
                }
            });
        } else {
            setOnMouseEntered(e -> {
                if (!selected) {
                    background.setStroke(Color.YELLOW);
                }
            });

            setOnMouseExited(e -> {
                if (!selected) {
                    background.setStroke(Color.DARKGRAY);
                }
            });
        }
    }

    public Piece getPiece() {
        return piece;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            background.setStroke(Color.YELLOW);
            background.setStrokeWidth(3);
        } else {
            background.setStroke(piece.getColor() == PieceColor.RED ?
                    Color.DARKRED : Color.DARKGRAY);
            background.setStrokeWidth(2);
        }
    }

    public boolean isSelected() {
        return selected;
    }
}
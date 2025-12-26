package ru.vsu.vinyukov.game.core;

import java.util.List;

public abstract class Piece {
    protected PieceColor color;
    protected Position position;
    protected String symbol;

    public Piece(PieceColor color, Position position) {
        this.color = color;
        this.position = position;
    }

    public abstract boolean isValidMove(Position to, Board board);
    public abstract Piece copy();
    public abstract List<Position> getPossibleMoves(Board board);

    // Проверка что путь свободен
    protected boolean isPathClear(Position from, Position to, Board board) {
        int rowDiff = to.getRow() - from.getRow();
        int colDiff = to.getCol() - from.getCol();

        // Должна быть прямая линия
        if (rowDiff != 0 && colDiff != 0 && Math.abs(rowDiff) != Math.abs(colDiff)) {
            return false;
        }

        int rowStep = Integer.compare(rowDiff, 0);
        int colStep = Integer.compare(colDiff, 0);

        int currentRow = from.getRow() + rowStep;
        int currentCol = from.getCol() + colStep;

        while (currentRow != to.getRow() || currentCol != to.getCol()) {
            if (board.getPiece(new Position(currentRow, currentCol)) != null) {
                return false;
            }
            currentRow += rowStep;
            currentCol += colStep;
        }
        return true;
    }

    protected boolean isWithinPalace(Position pos, PieceColor pieceColor) {
        if (pieceColor == PieceColor.RED) {
            return pos.getRow() >= 0 && pos.getRow() <= 2 &&
                    pos.getCol() >= 3 && pos.getCol() <= 5;
        } else {
            return pos.getRow() >= 7 && pos.getRow() <= 9 &&
                    pos.getCol() >= 3 && pos.getCol() <= 5;
        }
    }

    // Геттеры сеттеры
    public PieceColor getColor() { return color; }
    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }
    public String getSymbol() { return symbol; }
}
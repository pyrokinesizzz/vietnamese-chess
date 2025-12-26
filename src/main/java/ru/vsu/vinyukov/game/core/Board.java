package ru.vsu.vinyukov.game.core;

import ru.vsu.vinyukov.config.GameConfig;
import ru.vsu.vinyukov.game.pieces.*;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Piece[][] grid;
    private int width;
    private int height;

    public Board() {
        GameConfig config = GameConfig.getInstance();
        this.width = config.getBoardWidth();
        this.height = config.getBoardHeight();
        this.grid = new Piece[height][width];
        initializeBoard();
    }

    private void initializeBoard() {
        // Инициализация красных
        grid[0][0] = new Chariot(PieceColor.RED, new Position(0, 0));
        grid[0][1] = new Horse(PieceColor.RED, new Position(0, 1));
        grid[0][2] = new Elephant(PieceColor.RED, new Position(0, 2));
        grid[0][3] = new Advisor(PieceColor.RED, new Position(0, 3));
        grid[0][4] = new General(PieceColor.RED, new Position(0, 4));
        grid[0][5] = new Advisor(PieceColor.RED, new Position(0, 5));
        grid[0][6] = new Elephant(PieceColor.RED, new Position(0, 6));
        grid[0][7] = new Horse(PieceColor.RED, new Position(0, 7));
        grid[0][8] = new Chariot(PieceColor.RED, new Position(0, 8));

        // Второй ряд
        grid[2][1] = new Cannon(PieceColor.RED, new Position(2, 1));
        grid[2][7] = new Cannon(PieceColor.RED, new Position(2, 7));

        // Третий ряд
        grid[3][0] = new Soldier(PieceColor.RED, new Position(3, 0));
        grid[3][2] = new Soldier(PieceColor.RED, new Position(3, 2));
        grid[3][4] = new Soldier(PieceColor.RED, new Position(3, 4));
        grid[3][6] = new Soldier(PieceColor.RED, new Position(3, 6));
        grid[3][8] = new Soldier(PieceColor.RED, new Position(3, 8));

        // Инициализация черных фигур
        // Девятый ряд
        grid[9][0] = new Chariot(PieceColor.BLACK, new Position(9, 0));
        grid[9][1] = new Horse(PieceColor.BLACK, new Position(9, 1));
        grid[9][2] = new Elephant(PieceColor.BLACK, new Position(9, 2));
        grid[9][3] = new Advisor(PieceColor.BLACK, new Position(9, 3));
        grid[9][4] = new General(PieceColor.BLACK, new Position(9, 4));
        grid[9][5] = new Advisor(PieceColor.BLACK, new Position(9, 5));
        grid[9][6] = new Elephant(PieceColor.BLACK, new Position(9, 6));
        grid[9][7] = new Horse(PieceColor.BLACK, new Position(9, 7));
        grid[9][8] = new Chariot(PieceColor.BLACK, new Position(9, 8));

        // Седьмой ряд
        grid[7][1] = new Cannon(PieceColor.BLACK, new Position(7, 1));
        grid[7][7] = new Cannon(PieceColor.BLACK, new Position(7, 7));

        // Шестой ряд
        grid[6][0] = new Soldier(PieceColor.BLACK, new Position(6, 0));
        grid[6][2] = new Soldier(PieceColor.BLACK, new Position(6, 2));
        grid[6][4] = new Soldier(PieceColor.BLACK, new Position(6, 4));
        grid[6][6] = new Soldier(PieceColor.BLACK, new Position(6, 6));
        grid[6][8] = new Soldier(PieceColor.BLACK, new Position(6, 8));
    }

    public Piece getPiece(Position pos) {
        if (isValidPosition(pos)) {
            return grid[pos.getRow()][pos.getCol()];
        }
        return null;
    }

    public void setPiece(Position pos, Piece piece) {
        if (isValidPosition(pos)) {
            grid[pos.getRow()][pos.getCol()] = piece;
            if (piece != null) {
                piece.setPosition(pos);
            }
        }
    }

    public void movePiece(Move move) {
        Piece piece = getPiece(move.getFrom());
        if (piece != null) {
            setPiece(move.getTo(), piece);
            setPiece(move.getFrom(), null);
        }
    }

    public boolean isValidPosition(Position pos) {
        return pos != null &&
                pos.getRow() >= 0 && pos.getRow() < height &&
                pos.getCol() >= 0 && pos.getCol() < width;
    }

    public List<Piece> getPiecesByColor(PieceColor color) {
        List<Piece> pieces = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Piece piece = grid[row][col];
                if (piece != null && piece.getColor() == color) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    public Position findGeneral(PieceColor color) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Piece piece = grid[row][col];
                if (piece instanceof General && piece.getColor() == color) {
                    return new Position(row, col);
                }
            }
        }
        return null;
    }

    public Board copy() {
        Board copy = new EmptyBoard();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Piece piece = this.grid[row][col];
                copy.grid[row][col] = (piece != null) ? piece.copy() : null;
            }
        }
        return copy;
    }

    // Вспомогательный метод для отладки
    public void printBoard() {
        System.out.println("  a b c d e f g h i");
        for (int row = 0; row < height; row++) {
            System.out.print((9 - row) + " ");
            for (int col = 0; col < width; col++) {
                Piece piece = grid[row][col];
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

    // Геттеры
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Piece[][] getGrid() { return grid; }

    private static class EmptyBoard extends Board {
        public EmptyBoard() {
            super();
            // Очищаем доску
            for (int row = 0; row < getHeight(); row++) {
                for (int col = 0; col < getWidth(); col++) {
                    this.getGrid()[row][col] = null;
                }
            }
        }
    }
}
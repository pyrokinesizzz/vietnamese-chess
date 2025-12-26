package ru.vsu.vinyukov.game.core;

public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    public Position add(Position other) {
        return new Position(this.row + other.row, this.col + other.col);
    }

    public Position subtract(Position other) {
        return new Position(this.row - other.row, this.col - other.col);
    }

    public int distanceTo(Position other) {
        return Math.abs(this.row - other.row) + Math.abs(this.col - other.col);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    }

    @Override
    public String toString() {
        char colChar = (char) ('a' + col);
        return "" + colChar + (9 - row);
    }
}
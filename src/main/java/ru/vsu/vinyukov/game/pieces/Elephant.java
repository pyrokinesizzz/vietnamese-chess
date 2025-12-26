package ru.vsu.vinyukov.game.pieces;

import ru.vsu.vinyukov.game.core.*;

import java.util.ArrayList;
import java.util.List;

public class Elephant extends Piece {

    public Elephant(PieceColor color, Position position) {
        super(color, position);
        this.symbol = "相"; // Для красных
        if (color == PieceColor.BLACK) {
            this.symbol = "象"; // Для черных
        }
    }

    @Override
    public boolean isValidMove(Position to, Board board) {
        if (!board.isValidPosition(to)) {
            return false;
        }

        int rowDiff = to.getRow() - position.getRow();
        int colDiff = to.getCol() - position.getCol();

        // Ходит на 2 клетки по диагонали
        if (Math.abs(rowDiff) == 2 && Math.abs(colDiff) == 2) {
            // Проверяем, не переходит ли реку
            if (color == PieceColor.RED && to.getRow() > 4) return false;
            if (color == PieceColor.BLACK && to.getRow() < 5) return false;

            // Проверяем, не занята ли промежуточная клетка
            int middleRow = position.getRow() + rowDiff / 2;
            int middleCol = position.getCol() + colDiff / 2;
            if (board.getPiece(new Position(middleRow, middleCol)) != null) {
                return false;
            }

            Piece target = board.getPiece(to);
            return target == null || target.getColor() != color;
        }

        return false;
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();

        int[][] directions = {{-2, -2}, {-2, 2}, {2, -2}, {2, 2}};

        for (int[] dir : directions) {
            Position newPos = new Position(position.getRow() + dir[0],
                    position.getCol() + dir[1]);
            if (isValidMove(newPos, board)) {
                moves.add(newPos);
            }
        }

        return moves;
    }

    @Override
    public Piece copy() {
        return new Elephant(color, new Position(position.getRow(), position.getCol()));
    }
}
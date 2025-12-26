package ru.vsu.vinyukov.game.pieces;

import ru.vsu.vinyukov.game.core.*;

import java.util.ArrayList;
import java.util.List;

public class Horse extends Piece {

    public Horse(PieceColor color, Position position) {
        super(color, position);
        this.symbol = "傌"; // Для красных
        if (color == PieceColor.BLACK) {
            this.symbol = "馬"; // Для черных
        }
    }

    @Override
    public boolean isValidMove(Position to, Board board) {
        if (!board.isValidPosition(to)) {
            return false;
        }

        int rowDiff = to.getRow() - position.getRow();
        int colDiff = to.getCol() - position.getCol();

        // Конь ходит буквой "Г": 2 клетки в одном направлении и 1 в перпендикулярном
        if ((Math.abs(rowDiff) == 2 && Math.abs(colDiff) == 1) ||
                (Math.abs(rowDiff) == 1 && Math.abs(colDiff) == 2)) {

            // Проверяем, не заблокирована ли первая клетка
            int blockRow = position.getRow();
            int blockCol = position.getCol();

            if (Math.abs(rowDiff) == 2) {
                blockRow += rowDiff / 2;
            } else {
                blockCol += colDiff / 2;
            }

            if (board.getPiece(new Position(blockRow, blockCol)) != null) {
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

        int[][] directions = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
                {1, -2}, {1, 2}, {2, -1}, {2, 1}};

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
        return new Horse(color, new Position(position.getRow(), position.getCol()));
    }
}
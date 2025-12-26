package ru.vsu.vinyukov.game.pieces;

import ru.vsu.vinyukov.game.core.*;

import java.util.ArrayList;
import java.util.List;

public class Advisor extends Piece {

    public Advisor(PieceColor color, Position position) {
        super(color, position);
        this.symbol = "仕"; // Для красных
        if (color == PieceColor.BLACK) {
            this.symbol = "士"; // Для черных
        }
    }

    @Override
    public boolean isValidMove(Position to, Board board) {
        if (!board.isValidPosition(to)) {
            return false;
        }

        // Должен оставаться во дворце
        if (!isWithinPalace(to, color)) {
            return false;
        }

        int rowDiff = Math.abs(to.getRow() - position.getRow());
        int colDiff = Math.abs(to.getCol() - position.getCol());

        // Может ходить только по диагонали на одну клетку
        if (rowDiff == 1 && colDiff == 1) {
            Piece target = board.getPiece(to);
            return target == null || target.getColor() != color;
        }

        return false;
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();

        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

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
        return new Advisor(color, new Position(position.getRow(), position.getCol()));
    }
}
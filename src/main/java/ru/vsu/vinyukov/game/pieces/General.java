package ru.vsu.vinyukov.game.pieces;

import ru.vsu.vinyukov.game.core.*;

import java.util.ArrayList;
import java.util.List;

public class General extends Piece {

    public General(PieceColor color, Position position) {
        super(color, position);
        this.symbol = "帥"; // Для красных
        if (color == PieceColor.BLACK) {
            this.symbol = "將"; // Для черных
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

        // Может ходить на одну клетку ортогонально
        if ((rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1)) {
            Piece target = board.getPiece(to);
            return target == null || target.getColor() != color;
        }

        return false;
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

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
        return new General(color, new Position(position.getRow(), position.getCol()));
    }
}
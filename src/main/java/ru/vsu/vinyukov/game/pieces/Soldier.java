package ru.vsu.vinyukov.game.pieces;

import ru.vsu.vinyukov.game.core.*;

import java.util.ArrayList;
import java.util.List;

public class Soldier extends Piece {

    public Soldier(PieceColor color, Position position) {
        super(color, position);
        this.symbol = "兵"; // Для красных
        if (color == PieceColor.BLACK) {
            this.symbol = "卒"; // Для черных
        }
    }

    @Override
    public boolean isValidMove(Position to, Board board) {
        if (!board.isValidPosition(to)) {
            return false;
        }

        int rowDiff = to.getRow() - position.getRow();
        int colDiff = to.getCol() - position.getCol();

        // Солдат всегда движется вперед (для красных - вниз, для черных - вверх)
        if (color == PieceColor.RED) {
            if (rowDiff <= 0) return false; // Красные двигаются вниз
        } else {
            if (rowDiff >= 0) return false; // Черные двигаются вверх
        }

        // До переправы через реку может ходить только вперед
        boolean crossedRiver = hasCrossedRiver(position);
        if (!crossedRiver) {
            return Math.abs(rowDiff) == 1 && colDiff == 0;
        }
        // После переправы может ходить также в стороны
        else {
            return (Math.abs(rowDiff) == 1 && colDiff == 0) ||
                    (rowDiff == 0 && Math.abs(colDiff) == 1);
        }
    }

    private boolean hasCrossedRiver(Position pos) {
        if (color == PieceColor.RED) {
            return pos.getRow() >= 5; // Красные переправились через реку
        } else {
            return pos.getRow() <= 4; // Черные переправились через реку
        }
    }



    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();

        int forwardDir = (color == PieceColor.RED) ? 1 : -1;
        boolean crossedRiver = hasCrossedRiver(position);

        // Вперед
        Position forwardPos = new Position(position.getRow() + forwardDir, position.getCol());
        if (isValidMove(forwardPos, board)) {
            moves.add(forwardPos);
        }

        // В стороны (только после переправы)
        if (crossedRiver) {
            Position leftPos = new Position(position.getRow(), position.getCol() - 1);
            Position rightPos = new Position(position.getRow(), position.getCol() + 1);

            if (isValidMove(leftPos, board)) {
                moves.add(leftPos);
            }
            if (isValidMove(rightPos, board)) {
                moves.add(rightPos);
            }
        }

        return moves;
    }

    @Override
    public Piece copy() {
        return new Soldier(color, new Position(position.getRow(), position.getCol()));
    }
}
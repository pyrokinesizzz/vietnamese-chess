package ru.vsu.vinyukov.game.pieces;

import ru.vsu.vinyukov.game.core.*;

import java.util.ArrayList;
import java.util.List;

public class Chariot extends Piece {

    public Chariot(PieceColor color, Position position) {
        super(color, position);
        this.symbol = "俥"; // Для красных
        if (color == PieceColor.BLACK) {
            this.symbol = "車"; // Для черных
        }
    }

    @Override
    public boolean isValidMove(Position to, Board board) {
        if (!board.isValidPosition(to)) {
            return false;
        }

        int rowDiff = to.getRow() - position.getRow();
        int colDiff = to.getCol() - position.getCol();

        // Ладья ходит только по прямой
        if (rowDiff != 0 && colDiff != 0) {
            return false;
        }

        // Проверяем, свободен ли путь
        if (!isPathClear(position, to, board)) {
            return false;
        }

        Piece target = board.getPiece(to);
        return target == null || target.getColor() != color;
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();

        // Проверяем все клетки по горизонтали и вертикали
        for (int dir = 0; dir < 4; dir++) {
            int rowStep = 0, colStep = 0;
            switch (dir) {
                case 0: rowStep = -1; break;  // вверх
                case 1: rowStep = 1; break;   // вниз
                case 2: colStep = -1; break;  // влево
                case 3: colStep = 1; break;   // вправо
            }

            int currentRow = position.getRow() + rowStep;
            int currentCol = position.getCol() + colStep;

            while (board.isValidPosition(new Position(currentRow, currentCol))) {
                Position newPos = new Position(currentRow, currentCol);
                Piece target = board.getPiece(newPos);

                if (target == null) {
                    moves.add(newPos);
                } else {
                    if (target.getColor() != color) {
                        moves.add(newPos);
                    }
                    break;
                }

                currentRow += rowStep;
                currentCol += colStep;
            }
        }

        return moves;
    }

    @Override
    public Piece copy() {
        return new Chariot(color, new Position(position.getRow(), position.getCol()));
    }
}
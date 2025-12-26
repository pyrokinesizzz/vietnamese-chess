package ru.vsu.vinyukov.game.pieces;

import ru.vsu.vinyukov.game.core.*;

import java.util.ArrayList;
import java.util.List;

public class Cannon extends Piece {

    public Cannon(PieceColor color, Position position) {
        super(color, position);
        this.symbol = "炮"; // Одинаково для обоих цветов
    }

    @Override
    public boolean isValidMove(Position to, Board board) {
        if (!board.isValidPosition(to)) {
            return false;
        }

        int rowDiff = to.getRow() - position.getRow();
        int colDiff = to.getCol() - position.getCol();

        // Пушка ходит только по прямой
        if (rowDiff != 0 && colDiff != 0) {
            return false;
        }

        Piece target = board.getPiece(to);
        int piecesBetween = countPiecesBetween(position, to, board);

        // Для пустого хода не должно быть фигур на пути
        if (target == null) {
            return piecesBetween == 0;
        }
        // Для захвата должна быть ровно одна фигура между
        else {
            return piecesBetween == 1 && target.getColor() != color;
        }
    }

    private int countPiecesBetween(Position from, Position to, Board board) {
        int count = 0;

        int rowStep = Integer.compare(to.getRow() - from.getRow(), 0);
        int colStep = Integer.compare(to.getCol() - from.getCol(), 0);

        int currentRow = from.getRow() + rowStep;
        int currentCol = from.getCol() + colStep;

        while (currentRow != to.getRow() || currentCol != to.getCol()) {
            if (board.getPiece(new Position(currentRow, currentCol)) != null) {
                count++;
            }
            currentRow += rowStep;
            currentCol += colStep;
        }

        return count;
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
            boolean foundPiece = false;

            while (board.isValidPosition(new Position(currentRow, currentCol))) {
                Position newPos = new Position(currentRow, currentCol);
                Piece target = board.getPiece(newPos);

                if (!foundPiece) {
                    if (target == null) {
                        moves.add(newPos);
                    } else {
                        foundPiece = true;
                    }
                } else {
                    if (target != null) {
                        if (target.getColor() != color) {
                            moves.add(newPos);
                        }
                        break;
                    }
                }

                currentRow += rowStep;
                currentCol += colStep;
            }
        }

        return moves;
    }

    @Override
    public Piece copy() {
        return new Cannon(color, new Position(position.getRow(), position.getCol()));
    }
}
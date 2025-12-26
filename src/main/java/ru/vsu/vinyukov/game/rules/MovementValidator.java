package ru.vsu.vinyukov.game.rules;

import ru.vsu.vinyukov.game.core.*;

public class MovementValidator {

    public static boolean isValidMove(Move move, Board board) {
        if (move == null || move.getPiece() == null) {
            return false;
        }

        Position from = move.getFrom();
        Position to = move.getTo();
        Piece piece = move.getPiece();

        // Проверяем, что начальная позиция содержит эту фигуру
        if (board.getPiece(from) != piece) {
            return false;
        }

        // Проверяем, что фигура может так ходить
        if (!piece.isValidMove(to, board)) {
            return false;
        }

        // Проверяем, не ставит ли ход своего генерала под шах
        Board testBoard = board.copy();
        testBoard.movePiece(move);

        if (isKingInCheck(piece.getColor(), testBoard)) {
            return false;
        }

        return true;
    }

    public static boolean isKingInCheck(PieceColor color, Board board) {
        Position kingPos = board.findGeneral(color);
        if (kingPos == null) {
            return false; // Генерал уже захвачен
        }

        // Проверяем все фигуры противника
        PieceColor opponentColor = color.getOpponent();

        for (Piece piece : board.getPiecesByColor(opponentColor)) {
            if (piece.isValidMove(kingPos, board)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isCheckmate(PieceColor color, Board board) {
        if (!isKingInCheck(color, board)) {
            return false;
        }

        // Проверяем, есть ли хоть один допустимый ход
        for (Piece piece : board.getPiecesByColor(color)) {
            for (Position move : piece.getPossibleMoves(board)) {
                Move testMove = new Move(piece.getPosition(), move, piece);
                if (isValidMove(testMove, board)) {
                    return false; // Есть хотя бы один ход
                }
            }
        }

        return true; // Нет допустимых ходов
    }
}
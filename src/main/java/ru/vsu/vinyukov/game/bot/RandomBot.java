package ru.vsu.vinyukov.game.bot;

import ru.vsu.vinyukov.game.core.*;
import ru.vsu.vinyukov.game.rules.MovementValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomBot implements BotPlayer {
    private final PieceColor color;
    private final Random random;

    public RandomBot(PieceColor color) {
        this.color = color;
        this.random = new Random();
    }

    @Override
    public Move makeMove(Board board) {
        List<Move> validMoves = getAllValidMoves(board);

        if (validMoves.isEmpty()) {
            return null;
        }

        // Выбираем случайный допустимый ход
        return validMoves.get(random.nextInt(validMoves.size()));
    }

    @Override
    public PieceColor getColor() {
        return color;
    }

    private List<Move> getAllValidMoves(Board board) {
        List<Move> validMoves = new ArrayList<>();
        List<Piece> pieces = board.getPiecesByColor(color);

        for (Piece piece : pieces) {
            List<Position> possibleMoves = piece.getPossibleMoves(board);

            for (Position target : possibleMoves) {
                Move move = new Move(piece.getPosition(), target, piece);
                if (MovementValidator.isValidMove(move, board)) {
                    validMoves.add(move);
                }
            }
        }

        return validMoves;
    }
}
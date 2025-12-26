package ru.vsu.vinyukov.game.core;

public class Move {
    private final Position from;
    private final Position to;
    private final Piece piece;

    public Move(Position from, Position to, Piece piece) {
        this.from = from;
        this.to = to;
        this.piece = piece;
    }

    public Position getFrom() { return from; }
    public Position getTo() { return to; }
    public Piece getPiece() { return piece; }

    @Override
    public String toString() {
        return piece.getSymbol() + " " + from + " -> " + to;
    }
}
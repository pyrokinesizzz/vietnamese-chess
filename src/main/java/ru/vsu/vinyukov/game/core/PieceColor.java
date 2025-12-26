package ru.vsu.vinyukov.game.core;

public enum PieceColor {
    RED("Красные"),
    BLACK("Черные");

    private final String displayName;

    PieceColor(String displayName) {
        this.displayName = displayName;
    }

    public PieceColor getOpponent() {
        return this == RED ? BLACK : RED;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
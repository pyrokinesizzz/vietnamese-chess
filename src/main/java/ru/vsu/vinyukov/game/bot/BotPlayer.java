package ru.vsu.vinyukov.game.bot;

import ru.vsu.vinyukov.game.core.*;

public interface BotPlayer {
    Move makeMove(Board board);
    PieceColor getColor();
}
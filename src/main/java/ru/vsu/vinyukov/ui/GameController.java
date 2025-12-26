package ru.vsu.vinyukov.ui;

import javafx.application.Platform;
import ru.vsu.vinyukov.config.GameConfig;
import ru.vsu.vinyukov.game.core.*;
import ru.vsu.vinyukov.game.bot.BotPlayer;
import ru.vsu.vinyukov.game.rules.GameTimer;
import ru.vsu.vinyukov.game.rules.MovementValidator;

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class GameController {
    private Board board;
    private final PieceColor playerColor;
    private final BotPlayer bot;
    private final GameTimer gameTimer;
    private boolean gameOver = false;
    private String gameResult = "";
    private ChessBoardUI boardUI;
    private Timer uiTimer;
    private Consumer<String> gameOverCallback;
    private boolean isPlayerTurn = true;

    public GameController(Board board, PieceColor playerColor, BotPlayer bot, GameTimer gameTimer) {
        this.board = board;
        this.playerColor = playerColor;
        this.bot = bot;
        this.gameTimer = gameTimer;
        this.isPlayerTurn = (playerColor == PieceColor.RED);
        setupTimer();
    }

    public void setBoardUI(ChessBoardUI boardUI) {
        this.boardUI = boardUI;
    }

    private void setupTimer() {
        uiTimer = new Timer(true);
        uiTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (boardUI != null) {
                        boardUI.updateTimer(gameTimer.getFormattedTime());
                    }

                    if (!gameTimer.isRunning() && !gameOver) {
                        endGame("Время вышло! " +
                                (playerColor == PieceColor.RED ? "Черные" : "Красные") +
                                " выиграли.");
                    }
                });
            }
        }, 0, 1000);
    }

    public boolean makePlayerMove(Position from, Position to) {
        if (gameOver || !isPlayerTurn) {
            return false;
        }

        Piece piece = board.getPiece(from);
        if (piece == null || piece.getColor() != playerColor) {
            return false;
        }

        Move move = new Move(from, to, piece);
        if (!MovementValidator.isValidMove(move, board)) {
            return false;
        }

        board.movePiece(move);
        isPlayerTurn = false;

        if (boardUI != null) {
            Platform.runLater(() -> {
                boardUI.updateBoard();
                boardUI.updateStatus("Ход сделан. Ход противника...");
            });
        }

        checkGameEnd();

        if (!gameOver) {
            makeBotMoveWithDelay();
        }

        return true;
    }

    private void makeBotMoveWithDelay() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    makeBotMove(); // Теперь это приватный метод
                });
            }
        }, 1000);
    }

    // Изменено на public, чтобы можно было вызывать из ChessBoardUI
    public void makeBotMove() {
        if (gameOver || isPlayerTurn) {
            return;
        }

        Move botMove = bot.makeMove(board);
        if (botMove != null) {
            board.movePiece(botMove);
            isPlayerTurn = true;

            if (boardUI != null) {
                Platform.runLater(() -> {
                    boardUI.updateBoard();
                    boardUI.updateStatus("Бот сделал ход: " + botMove + ". Ваш ход.");
                });
            }

            checkGameEnd();
        } else {
            isPlayerTurn = true;
            if (boardUI != null) {
                Platform.runLater(() -> {
                    boardUI.updateStatus("У противника нет допустимых ходов. Ваш ход.");
                });
            }
        }
    }

    private void checkGameEnd() {
        if (MovementValidator.isCheckmate(PieceColor.RED, board)) {
            endGame("Мат! Черные выиграли!");
            return;
        } else if (MovementValidator.isCheckmate(PieceColor.BLACK, board)) {
            endGame("Мат! Красные выиграли!");
            return;
        }

        if (isStalemate()) {
            endGame("Пат! Ничья!");
            return;
        }

        Position redGeneral = board.findGeneral(PieceColor.RED);
        Position blackGeneral = board.findGeneral(PieceColor.BLACK);

        if (redGeneral == null) {
            endGame("Черные выиграли! Генерал красных захвачен.");
            return;
        } else if (blackGeneral == null) {
            endGame("Красные выиграли! Генерал черных захвачен.");
            return;
        }

        PieceColor currentPlayer = isPlayerTurn ? playerColor : bot.getColor();
        boolean hasValidMoves = false;
        for (Piece piece : board.getPiecesByColor(currentPlayer)) {
            if (!piece.getPossibleMoves(board).isEmpty()) {
                hasValidMoves = true;
                break;
            }
        }

        if (!hasValidMoves) {
            endGame("У " + currentPlayer + " нет допустимых ходов. " +
                    currentPlayer.getOpponent() + " выиграли!");
        }
    }

    private boolean isStalemate() {
        boolean redHasMoves = false;
        boolean blackHasMoves = false;

        for (Piece piece : board.getPiecesByColor(PieceColor.RED)) {
            if (!piece.getPossibleMoves(board).isEmpty()) {
                redHasMoves = true;
                break;
            }
        }

        for (Piece piece : board.getPiecesByColor(PieceColor.BLACK)) {
            if (!piece.getPossibleMoves(board).isEmpty()) {
                blackHasMoves = true;
                break;
            }
        }

        return !redHasMoves && !blackHasMoves;
    }

    private void endGame(String result) {
        gameOver = true;
        gameResult = result;
        gameTimer.stop();

        if (uiTimer != null) {
            uiTimer.cancel();
        }

        if (gameOverCallback != null) {
            Platform.runLater(() -> {
                gameOverCallback.accept(result);
            });
        }

        if (boardUI != null) {
            Platform.runLater(() -> {
                boardUI.updateStatus(result);
            });
        }
    }

    public List<Position> getValidMoves(Position from) {
        List<Position> validMoves = new ArrayList<>();
        Piece piece = board.getPiece(from);

        if (piece == null || piece.getColor() != playerColor) {
            return validMoves;
        }

        for (Position move : piece.getPossibleMoves(board)) {
            Move testMove = new Move(from, move, piece);
            if (MovementValidator.isValidMove(testMove, board)) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    public void resign() {
        if (!gameOver) {
            endGame("Игрок сдался. " +
                    (playerColor == PieceColor.RED ? "Черные" : "Красные") +
                    " выиграли!");
        }
    }

    public void newGame() {
        board = new Board();
        gameOver = false;
        gameResult = "";
        isPlayerTurn = (playerColor == PieceColor.RED);

        gameTimer.stop();
        GameConfig config = GameConfig.getInstance();
        GameTimer newTimer = new GameTimer(config.getGameTimeoutMinutes());
        newTimer.start();

        if (boardUI != null) {
            Platform.runLater(() -> {
                boardUI.updateBoard();
                boardUI.updateStatus("Новая игра началась. " +
                        (isPlayerTurn ? "Ваш ход." : "Ход противника..."));
                boardUI.updateGameInfo();
            });
        }

        if (uiTimer != null) {
            uiTimer.cancel();
        }
        setupTimer();

        if (!isPlayerTurn) {
            makeBotMoveWithDelay();
        }
    }

    public void stopGame() {
        gameTimer.stop();
        if (uiTimer != null) {
            uiTimer.cancel();
        }
    }

    public void setGameOverCallback(Consumer<String> callback) {
        this.gameOverCallback = callback;
    }

    // Геттеры
    public Board getBoard() { return board; }
    public PieceColor getPlayerColor() { return playerColor; }
    public boolean isGameOver() { return gameOver; }
    public String getGameResult() { return gameResult; }
    public boolean isPlayerTurn() { return isPlayerTurn && !gameOver; }
}
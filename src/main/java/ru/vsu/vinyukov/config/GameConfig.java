package ru.vsu.vinyukov.config;

import java.io.InputStream;
import java.util.Properties;

public class GameConfig {
    private static final String CONFIG_FILE = "/config/game.properties";
    private static GameConfig instance;
    private final Properties properties;

    private GameConfig() {
        properties = new Properties();
        try (InputStream input = getClass().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            } else {
                setDefaults();
            }
        } catch (Exception e) {
            setDefaults();
        }
    }

    public static GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }

    private void setDefaults() {
        properties.setProperty("board.width", "9");
        properties.setProperty("board.height", "10");
        properties.setProperty("game.timeout.minutes", "5");
        properties.setProperty("bot.move.delay", "1000");
        properties.setProperty("sound.enabled", "true");
        properties.setProperty("cell.size", "60");
    }

    public int getBoardWidth() {
        return Integer.parseInt(properties.getProperty("board.width", "9"));
    }

    public int getBoardHeight() {
        return Integer.parseInt(properties.getProperty("board.height", "10"));
    }

    public int getGameTimeoutMinutes() {
        return Integer.parseInt(properties.getProperty("game.timeout.minutes", "5"));
    }

    public int getBotMoveDelay() {
        return Integer.parseInt(properties.getProperty("bot.move.delay", "1000"));
    }

    public boolean isSoundEnabled() {
        return Boolean.parseBoolean(properties.getProperty("sound.enabled", "true"));
    }

    public int getCellSize() {
        return Integer.parseInt(properties.getProperty("cell.size", "60"));
    }
}
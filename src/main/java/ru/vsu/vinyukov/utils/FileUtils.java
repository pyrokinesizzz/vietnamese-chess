package ru.vsu.vinyukov.utils;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;

public class FileUtils {

    public static Properties loadProperties(String filePath) {
        Properties properties = new Properties();
        try (InputStream input = FileUtils.class.getResourceAsStream(filePath)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки свойств: " + e.getMessage());
        }
        return properties;
    }

    public static void saveGameState(String gameState, String fileName) {
        try {
            Path path = Paths.get("saves", fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, gameState.getBytes());
        } catch (IOException e) {
            System.err.println("Ошибка сохранения игры: " + e.getMessage());
        }
    }

    public static String loadGameState(String fileName) {
        try {
            Path path = Paths.get("saves", fileName);
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            System.err.println("Ошибка загрузки игры: " + e.getMessage());
            return null;
        }
    }
}
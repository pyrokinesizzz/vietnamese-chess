package ru.vsu.vinyukov.game.rules;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {
    private final int totalMinutes;
    private int remainingSeconds;
    private Timer timer;
    private boolean running;

    public GameTimer(int minutes) {
        this.totalMinutes = minutes;
        this.remainingSeconds = minutes * 60;
        this.running = false;
    }

    public void start() {
        if (timer != null) {
            timer.cancel();
        }

        running = true;
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (remainingSeconds > 0) {
                    remainingSeconds--;
                    if (remainingSeconds % 60 == 0) {
                        System.out.println("Осталось времени: " + (remainingSeconds / 60) + " минут");
                    }
                } else {
                    stop();
                    System.out.println("Время вышло!");
                }
            }
        }, 1000, 1000);
    }

    public void stop() {
        running = false;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public boolean isRunning() {
        return running;
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    public String getFormattedTime() {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
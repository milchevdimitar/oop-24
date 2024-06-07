package com.example.mining.models;

public class Clock extends Thread {
    private volatile boolean running = true;

    public void run() {
        int days = 0;
        while (running) {
            try {
                Thread.sleep(500); // Спим за 0.5 секунда (един ден)
                days++;
                System.out.println("Day " + days);
                synchronized (this) {
                    notifyAll();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stopClock() {
        running = false;
    }
}

package com.example.mining;
import java.util.concurrent.Semaphore;

public class Lumberjack extends Worker {
    private static int totalWood = 0;

    public Lumberjack(Clock clock, Semaphore semaphore) {
        super(clock, semaphore);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                semaphore.acquire();
                synchronized (clock) {
                    clock.wait();
                }
                synchronized (lock) {
                    totalWood += work();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                semaphore.release();
            }
        }
    }

    @Override
    protected int work() {
        return 1; // Симулираме количество изсечено дърво
    }

    public static synchronized int getTotalResource() {
        return totalWood;
    }
}

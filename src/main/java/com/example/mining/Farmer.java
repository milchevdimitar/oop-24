package com.example.mining;
import java.util.concurrent.Semaphore;

public class Farmer extends Worker {
    private static int totalGrain = 0;

    public Farmer(Clock clock, Semaphore semaphore) {
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
                    totalGrain += work();
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
        return 1; // Симулираме количество произведено жито
    }

    public static synchronized int getTotalResource() {
        return totalGrain;
    }
}

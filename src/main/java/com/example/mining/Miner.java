package com.example.mining;
import java.util.concurrent.Semaphore;

public class Miner extends Worker {
    private static int totalGold = 0;

    public Miner(Clock clock, Semaphore semaphore) {
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
                    totalGold += work();
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
        return 1; // Симулираме количество изкопано злато
    }

    public static synchronized int getTotalResource() {
        return totalGold;
    }
}

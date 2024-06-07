package com.example.mining.models.single_workers;

import java.util.concurrent.Semaphore;

import com.example.mining.models.Clock;
import com.example.mining.models.Worker;
import com.example.mining.models.treasuries.Treasury;

public class Miner extends Worker {

    public Miner(Clock clock, Semaphore semaphore, Treasury treasury) {
        super(treasury, clock, semaphore);
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
                    getTreasury().addMetal(work());
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
        return 2; // Симулираме количество изкопан метал
    }
}

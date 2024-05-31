package com.example.mining;

import java.util.concurrent.Semaphore;

public abstract class Worker extends Thread {
    protected static final Object lock = new Object();
    protected static Semaphore semaphore;
    protected Clock clock;

    public Worker(Clock clock, Semaphore semaphore) {
        this.clock = clock;
        Worker.semaphore = semaphore;
    }

    protected abstract int work();

    public static synchronized int getTotalResource() {
        return 0;
    }
}

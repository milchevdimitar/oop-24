package com.example.mining.models;

import com.example.mining.models.treasuries.Treasury;

import java.util.concurrent.Semaphore;

public abstract class Worker extends Thread {
    private final Treasury treasury;
    protected static final Object lock = new Object();
    protected static Semaphore semaphore;
    protected final Clock clock;

    public Worker(Treasury treasury, Clock clock, Semaphore semaphore) {
        this.treasury = treasury;
        this.clock = clock;
        Worker.semaphore = semaphore;
    }

    public Treasury getTreasury() {
        return treasury;
    }

    protected abstract int work();
}

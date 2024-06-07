package com.example.mining.models.productions;

import com.example.mining.models.Clock;
import com.example.mining.models.Worker;
import com.example.mining.models.treasuries.ProductionsTreasury;
import com.example.mining.models.treasuries.Treasury;

import java.util.concurrent.Semaphore;

public class MetalProduction extends Worker {
    private final ProductionsTreasury productionsTreasury;
    public MetalProduction(Clock clock, Semaphore semaphore, Treasury treasury, ProductionsTreasury productionsTreasury) {
        super(treasury, clock, semaphore);
        this.productionsTreasury = productionsTreasury;
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
                    productionsTreasury.addMetalStructures(work());
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
        return makeMetalStructures();
    }

    private synchronized int makeMetalStructures() {
//        Needs 50 metal and 30 grain
        if(getTreasury().getMetal() < 50 || getTreasury().getGrain() < 30) {
            return 0;
        } else {
            getTreasury().setMetal(getTreasury().getMetal() - 50);
            getTreasury().setGrain(getTreasury().getGrain() - 30);
            return 1;
        }
    }
}
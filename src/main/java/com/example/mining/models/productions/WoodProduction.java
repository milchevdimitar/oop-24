package com.example.mining.models.productions;

import com.example.mining.models.Clock;
import com.example.mining.models.Worker;
import com.example.mining.models.treasuries.ProductionsTreasury;
import com.example.mining.models.treasuries.Treasury;

import java.util.concurrent.Semaphore;

public class WoodProduction extends Worker {
    private final ProductionsTreasury productionsTreasury;
    public WoodProduction(Clock clock, Semaphore semaphore, Treasury treasury, ProductionsTreasury productionsTreasury) {
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
                    if(work() == 2) {
                        productionsTreasury.addBigWood(1);
                    } else if(work() == 1) {
                        productionsTreasury.addSmallWood(1);
                    }
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
        if(productionsTreasury.getSmallWood() >= 50) {
            if(makeBigWood() == 1) {
                return 2;
            }
        } else {
            if(makeSmallWood() == 1) {
                return 1;
            }
        }
        return 0;
    }

    private int makeSmallWood() {
//        Needs 20 wood and 10 grain
        if(getTreasury().getWood() < 20 || getTreasury().getGrain() < 10) {
            return 0;
        } else {
            getTreasury().setWood(getTreasury().getWood() - 20);
            getTreasury().setGrain(getTreasury().getGrain() - 10);
            return 1;
        }
    }

    private int makeBigWood() {
//        Needs 50 wood and 20 grain
        if(getTreasury().getWood() < 50 || getTreasury().getGrain() < 20) {
            return 0;
        } else {
            getTreasury().setWood(getTreasury().getWood() - 50);
            getTreasury().setGrain(getTreasury().getGrain() - 20);
            return 1;
        }
    }
}

package com.example.mining;

import com.example.mining.models.*;
import com.example.mining.models.productions.MetalProduction;
import com.example.mining.models.treasuries.ProductionsTreasury;
import com.example.mining.models.productions.WoodProduction;
import com.example.mining.models.single_workers.Farmer;
import com.example.mining.models.single_workers.Lumberjack;
import com.example.mining.models.single_workers.Miner;
import com.example.mining.models.treasuries.Treasury;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Semaphore;

public class MiningSimulation {
    private static final String REPORTS_DIR = "reports";
    private static int dayIndex = 1;
    private static final Treasury treasury = new Treasury();
    private static final ProductionsTreasury productionsTreasury = new ProductionsTreasury();

    public static void main(String[] args) {
        File reportsDir = new File(REPORTS_DIR);
        if (!reportsDir.exists()) {
            reportsDir.mkdir();
        }

        dayIndex = getStartingDayIndex();

        Clock clock = new Clock();
        clock.start();

        int numberOfWorkers = 70; // Example number of workers
        int maxActiveWorkers = 40; // Maximum number of active workers
        Semaphore semaphore = new Semaphore(maxActiveWorkers);
        Worker[] workers = new Worker[numberOfWorkers];

        for(int i = 0; i < numberOfWorkers - 4; i += 5) {
            workers[i] = new Miner(clock, semaphore, treasury);
            workers[i + 1] = new Lumberjack(clock, semaphore, treasury);
            workers[i + 2] = new Farmer(clock, semaphore, treasury);
            workers[i + 3] = new MetalProduction(clock, semaphore, treasury, productionsTreasury);
            workers[i + 4] = new WoodProduction(clock, semaphore, treasury, productionsTreasury);
        }

        for(Worker worker : workers) {
            worker.start();
        }

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                generateDailyReport();
                updateConsole(maxActiveWorkers, semaphore);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 500, TimeUnit.MILLISECONDS);

        while (!buildBuilding()) {
            try {
                Thread.sleep(500); // Check every 0.5 second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("Building has been built");

        clock.stopClock();
        try {
            clock.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for (Worker worker : workers) {
            worker.interrupt();
        }

        for (Worker worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }

        nullAllResources();
    }

    private static int getStartingDayIndex() {
        File reportsDir = new File(REPORTS_DIR);
        String[] reportFiles = reportsDir.list((dir, name) -> name.startsWith("day_") && name.endsWith(".txt"));
        if (reportFiles != null && reportFiles.length > 0) {
            return reportFiles.length + 1;
        }
        return 1;
    }

    private static void generateDailyReport() throws IOException {
        int totalGold = treasury.getMetal();
        int totalWood = treasury.getWood();
        int totalGrain = treasury.getGrain();
        int totalSmallWood = productionsTreasury.getSmallWood();
        int totalBigWood = productionsTreasury.getBigWood();
        int totalMetal = productionsTreasury.getMetalStructures();

        String reportContent = String.format("Day %d Report:%n", dayIndex);
        reportContent += String.format("Gold: %d kg%n", totalGold);
        reportContent += String.format("Wood: %d cubic meters%n", totalWood);
        reportContent += String.format("Grain: %d kg%n", totalGrain);
        reportContent += String.format("Small Wood: %d number%n", totalSmallWood);
        reportContent += String.format("Big Wood: %d number%n", totalBigWood);
        reportContent += String.format("Metal constructions: %d number%n", totalMetal);

        File reportFile = new File(REPORTS_DIR + "/day_" + dayIndex + ".txt");
        try (FileWriter writer = new FileWriter(reportFile)) {
            writer.write(reportContent);
        }

        dayIndex++;
    }

    private static void updateConsole(int maxActiveWorkers, Semaphore semaphore) {
        int activeMiners = countActiveWorkers(Miner.class);
        int activeLumberjacks = countActiveWorkers(Lumberjack.class);
        int activeFarmers = countActiveWorkers(Farmer.class);
        int activeWood = countActiveWorkers(WoodProduction.class);
        int activeMetal = countActiveWorkers(MetalProduction.class);

        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("Mining Simulation");
        System.out.println("-----------------");
        System.out.println("Metal (kg): " + treasury.getMetal());
        System.out.println("Wood (cubic meters): " + treasury.getWood());
        System.out.println("Grain (kg): " + treasury.getGrain() + "\n");
        System.out.println("Small Wood (number): " + productionsTreasury.getSmallWood());
        System.out.println("Big Wood (number): " + productionsTreasury.getBigWood());
        System.out.println("Metal Structures (number): " + productionsTreasury.getMetalStructures());
        System.out.println();
        System.out.println("Active Miners: " + activeMiners);
        System.out.println("Active Lumberjacks: " + activeLumberjacks);
        System.out.println("Active Farmers: " + activeFarmers);
        System.out.println("Active Wood Producers: " + activeFarmers);
        System.out.println("Active Metal Producers: " + activeFarmers);
        System.out.println();
        System.out.println("Total Active Workers: " + (activeMiners + activeLumberjacks + activeFarmers + activeWood + activeMetal) + "/" + maxActiveWorkers);
        System.out.println("Total Workers: " + semaphore.getQueueLength());
    }

    private static int countActiveWorkers(Class<? extends Worker> workerClass) {
        int count = 0;
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (workerClass.isInstance(thread) && thread.isAlive()) {
                count++;
            }
        }
        return count;
    }

    private static boolean buildBuilding() {
        return productionsTreasury.getBigWood() >= 30
                && productionsTreasury.getSmallWood() >= 50
                && productionsTreasury.getMetalStructures() >= 60;
    }

    private static void nullAllResources() {
        productionsTreasury.setBigWood(0);
        productionsTreasury.setSmallWood(0);
        productionsTreasury.setMetalStructures(0);
        treasury.setWood(0);
        treasury.setGrain(0);
        treasury.setMetal(0);
    }
}
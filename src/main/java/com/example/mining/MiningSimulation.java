package com.example.mining;

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

    public static void main(String[] args) {
        File reportsDir = new File(REPORTS_DIR);
        if (!reportsDir.exists()) {
            reportsDir.mkdir();
        }

        dayIndex = getStartingDayIndex();

        Clock clock = new Clock();
        clock.start();

        int numberOfWorkers = 30; // Примерен брой работници
        int maxActiveWorkers = 10; // Максимален брой активни работници
        Semaphore semaphore = new Semaphore(maxActiveWorkers);
        Worker[] workers = new Worker[numberOfWorkers];

        for (int i = 0; i < numberOfWorkers / 3; i++) {
            workers[i] = new Miner(clock, semaphore);
            workers[i].start();
        }

        for (int i = numberOfWorkers / 3; i < 2 * numberOfWorkers / 3; i++) {
            workers[i] = new Lumberjack(clock, semaphore);
            workers[i].start();
        }

        for (int i = 2 * numberOfWorkers / 3; i < numberOfWorkers; i++) {
            workers[i] = new Farmer(clock, semaphore);
            workers[i].start();
        }

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                generateDailyReport();
                updateConsole(maxActiveWorkers, semaphore);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);

        // Спираме симулацията след 10 секунди (10 дни)
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        clock.stopClock();
        for (Worker worker : workers) {
            worker.interrupt();
        }

        scheduler.shutdown();
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
        int totalGold = Miner.getTotalResource();
        int totalWood = Lumberjack.getTotalResource();
        int totalGrain = Farmer.getTotalResource();

        String reportContent = String.format("Day %d Report:%n", dayIndex);
        reportContent += String.format("Gold: %d kg%n", totalGold);
        reportContent += String.format("Wood: %d cubic meters%n", totalWood);
        reportContent += String.format("Grain: %d kg%n", totalGrain);

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

        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("Mining Simulation");
        System.out.println("-----------------");
        System.out.println("Gold (kg): " + Miner.getTotalResource());
        System.out.println("Wood (cubic meters): " + Lumberjack.getTotalResource());
        System.out.println("Grain (kg): " + Farmer.getTotalResource());
        System.out.println();
        System.out.println("Active Miners: " + activeMiners);
        System.out.println("Active Lumberjacks: " + activeLumberjacks);
        System.out.println("Active Farmers: " + activeFarmers);
        System.out.println();
        System.out.println("Total Active Workers: " + (activeMiners + activeLumberjacks + activeFarmers) + "/" + maxActiveWorkers);
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
}

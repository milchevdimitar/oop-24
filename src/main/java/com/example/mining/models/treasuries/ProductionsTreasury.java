package com.example.mining.models.treasuries;

import java.io.*;

public class ProductionsTreasury {
    private static final String FILE_PATH = "saved_production_resources.txt";
    private int smallWood; // Depends on wood + grain
    private int bigWood; // Depends on wood + grain
    private int metalStructures; // Depends on metal + grain

    public ProductionsTreasury() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            if ((line = reader.readLine()) != null) {
                this.smallWood = Integer.parseInt(line.split(": ")[1]);
            } else {
                this.smallWood = 0;
            }
            if ((line = reader.readLine()) != null) {
                this.bigWood = Integer.parseInt(line.split(": ")[1]);
            } else {
                this.bigWood = 0;
            }
            if ((line = reader.readLine()) != null) {
                this.metalStructures = Integer.parseInt(line.split(": ")[1]);
            } else {
                this.metalStructures = 0;
            }
        } catch (IOException | NumberFormatException e) {
            this.smallWood = 0;
            this.bigWood = 0;
            this.metalStructures = 0;
        }
    }

    private void saveResources() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("Small Wood: " + smallWood);
            writer.newLine();
            writer.write("Big Wood: " + bigWood);
            writer.newLine();
            writer.write("Metal Structures: " + metalStructures);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addSmallWood(int amount) {
        smallWood += amount;
        saveResources();
    }

    public synchronized void addBigWood(int amount) {
        bigWood += amount;
        saveResources();
    }

    public synchronized void addMetalStructures(int amount) {
        metalStructures += amount;
        saveResources();
    }

    public int getSmallWood() {
        return smallWood;
    }

    public int getBigWood() {
        return bigWood;
    }

    public int getMetalStructures() {
        return metalStructures;
    }

    public void setSmallWood(int smallWood) {
        this.smallWood = smallWood;
        saveResources();
    }

    public void setBigWood(int bigWood) {
        this.bigWood = bigWood;
        saveResources();
    }

    public void setMetalStructures(int metalStructures) {
        this.metalStructures = metalStructures;
        saveResources();
    }
}
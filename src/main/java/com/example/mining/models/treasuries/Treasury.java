package com.example.mining.models.treasuries;

import java.io.*;

public class Treasury {
    private static final String FILE_PATH = "saved_resources.txt";
    private int grain;
    private int metal;
    private int wood;

    public Treasury() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            if ((line = reader.readLine()) != null) {
                this.grain = Integer.parseInt(line.split(": ")[1]);
            } else {
                this.grain = 0;
            }
            if ((line = reader.readLine()) != null) {
                this.metal = Integer.parseInt(line.split(": ")[1]);
            } else {
                this.metal = 0;
            }
            if ((line = reader.readLine()) != null) {
                this.wood = Integer.parseInt(line.split(": ")[1]);
            } else {
                this.wood = 0;
            }
        } catch (IOException | NumberFormatException e) {
            this.grain = 0;
            this.metal = 0;
            this.wood = 0;
        }
    }

    private void saveResources() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("Grain: " + grain);
            writer.newLine();
            writer.write("Metal: " + metal);
            writer.newLine();
            writer.write("Wood: " + wood);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addGrain(int amount) {
        grain += amount;
        saveResources();
    }

    public synchronized void addMetal(int amount) {
        metal += amount;
        saveResources();
    }

    public synchronized void addWood(int amount) {
        wood += amount;
        saveResources();
    }

    public synchronized int getGrain() {
        return grain;
    }

    public synchronized int getMetal() {
        return metal;
    }

    public synchronized int getWood() {
        return wood;
    }

    public void setGrain(int grain) {
        this.grain = grain;
        saveResources();
    }

    public void setMetal(int metal) {
        this.metal = metal;
        saveResources();
    }

    public void setWood(int wood) {
        this.wood = wood;
        saveResources();
    }
}

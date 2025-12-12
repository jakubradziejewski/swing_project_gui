package config;

import java.io.*;
import java.util.Properties;

public class GameConfig {
    private static GameConfig instance;
    private static final String CONFIG_FILE = "config/game.properties";
    
    private int carrotGrowthTime = 1000;
    private int farmerRepairTime = 3000;
    private int farmerPlantTime = 2000;
    private double rabbitSpawnRate = 0.3;
    private int dogDetectionRange = 5;
    private int cellSize = 40;
    private int refreshRate = 100;
    
    private GameConfig() {
        loadFromFile();
    }
    
    public static GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }
    
    private void loadFromFile() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
            carrotGrowthTime = Integer.parseInt(props.getProperty("carrotGrowthTime", "1000"));
            farmerRepairTime = Integer.parseInt(props.getProperty("farmerRepairTime", "3000"));
            farmerPlantTime = Integer.parseInt(props.getProperty("farmerPlantTime", "2000"));
            rabbitSpawnRate = Double.parseDouble(props.getProperty("rabbitSpawnRate", "0.3"));
            dogDetectionRange = Integer.parseInt(props.getProperty("dogDetectionRange", "5"));
            cellSize = Integer.parseInt(props.getProperty("cellSize", "40"));
            refreshRate = Integer.parseInt(props.getProperty("refreshRate", "100"));
            System.out.println("Config loaded from file");
        } catch (IOException e) {
            System.out.println("Config file not found, using defaults");
        }
    }
    
    public void saveToFile() {
        Properties props = new Properties();
        props.setProperty("carrotGrowthTime", String.valueOf(carrotGrowthTime));
        props.setProperty("farmerRepairTime", String.valueOf(farmerRepairTime));
        props.setProperty("farmerPlantTime", String.valueOf(farmerPlantTime));
        props.setProperty("rabbitSpawnRate", String.valueOf(rabbitSpawnRate));
        props.setProperty("dogDetectionRange", String.valueOf(dogDetectionRange));
        props.setProperty("cellSize", String.valueOf(cellSize));
        props.setProperty("refreshRate", String.valueOf(refreshRate));
        
        try {
            new File("config").mkdirs();
            try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
                props.store(fos, "Game Configuration");
                System.out.println("Config saved to file");
            }
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }
    
    // Getters
    public int getCarrotGrowthTime() { return carrotGrowthTime; }
    public int getFarmerRepairTime() { return farmerRepairTime; }
    public int getFarmerPlantTime() { return farmerPlantTime; }
    public double getRabbitSpawnRate() { return rabbitSpawnRate; }
    public int getDogDetectionRange() { return dogDetectionRange; }
    public int getCellSize() { return cellSize; }
    public int getRefreshRate() { return refreshRate; }
    
    // Setters with validation
    public void setCarrotGrowthTime(int time) {
        if (time > 0) carrotGrowthTime = time;
    }
    
    public void setFarmerRepairTime(int time) {
        if (time > 0) farmerRepairTime = time;
    }
    
    public void setFarmerPlantTime(int time) {
        if (time > 0) farmerPlantTime = time;
    }
    
    public void setRabbitSpawnRate(double rate) {
        if (rate >= 0.0 && rate <= 1.0) rabbitSpawnRate = rate;
    }
    
    public void setDogDetectionRange(int range) {
        if (range > 0) dogDetectionRange = range;
    }
}
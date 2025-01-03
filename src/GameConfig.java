// GameConfig.java
import java.io.Serializable;
public class GameConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    // Game parameters with default values
    private double rabbitSpawnRate;
    private int carrotGrowthTime;
    private int dogDetectionRange;
    private int farmerRepairTime;
    private int farmerPlantTime;

    // Singleton instance
    private static GameConfig instance;

    // Constructor with default values
    private GameConfig() {
        setDefaultValues();
    }

    private void setDefaultValues() {
        rabbitSpawnRate = 0.3;
        carrotGrowthTime = 1000; // milliseconds
        dogDetectionRange = 5;
        farmerRepairTime = 3000; // milliseconds
        farmerPlantTime = 2000; // milliseconds
    }

    public static GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }

    // Getters and setters with validation
    public double getRabbitSpawnRate() {
        return rabbitSpawnRate;
    }

    public void setRabbitSpawnRate(double rate) {
        if (rate >= 0.0 && rate <= 1.0) {
            this.rabbitSpawnRate = rate;
        } else {
            throw new IllegalArgumentException("Spawn rate must be between 0.0 and 1.0");
        }
    }

    public int getCarrotGrowthTime() {
        return carrotGrowthTime;
    }

    public void setCarrotGrowthTime(int time) {
        if (time > 0) {
            this.carrotGrowthTime = time;
        } else {
            throw new IllegalArgumentException("Growth time must be positive");
        }
    }

    public int getDogDetectionRange() {
        return dogDetectionRange;
    }

    public void setDogDetectionRange(int range) {
        if (range > 0) {
            this.dogDetectionRange = range;
        } else {
            throw new IllegalArgumentException("Detection range must be positive");
        }
    }

    public int getFarmerRepairTime() {
        return farmerRepairTime;
    }

    public void setFarmerRepairTime(int time) {
        if (time > 0) {
            this.farmerRepairTime = time;
        } else {
            throw new IllegalArgumentException("Repair time must be positive");
        }
    }

    public int getFarmerPlantTime() {
        return farmerPlantTime;
    }

    public void setFarmerPlantTime(int time) {
        if (time > 0) {
            this.farmerPlantTime = time;
        } else {
            throw new IllegalArgumentException("Plant time must be positive");
        }
    }
}


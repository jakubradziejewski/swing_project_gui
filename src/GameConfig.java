import java.io.Serializable;

// Singleton class for game configuration
public class GameConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    private static GameConfig instance;

    private int carrotGrowthTime;
    private int farmerRepairTime;
    private int farmerPlantTime;

    private double rabbitSpawnRate;
    private int dogDetectionRange;

    // Constructor with default values
    private GameConfig() {
        setDefaultValues();
    }

    // Set default configuration values
    private void setDefaultValues() {
        carrotGrowthTime = 1000;
        farmerRepairTime = 3000;
        farmerPlantTime = 2000;

        rabbitSpawnRate = 0.3;
        dogDetectionRange = 5;
    }

    // Get singleton instance
    public static GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }

    // Getters and setters with validation
    private void validatePositiveValue(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive");
        }
    }

    private void validateSpawnRate(double rate) {
        if (rate < 0.0 || rate > 1.0) {
            throw new IllegalArgumentException("Spawn rate must be between 0.0 and 1.0");
        }
    }

    // Time-related getters and setters
    public int getCarrotGrowthTime() { return carrotGrowthTime; }
    public void setCarrotGrowthTime(int time) {
        validatePositiveValue(time, "Growth time");
        carrotGrowthTime = time;
    }

    public int getFarmerRepairTime() { return farmerRepairTime; }
    public void setFarmerRepairTime(int time) {
        validatePositiveValue(time, "Repair time");
        farmerRepairTime = time;
    }

    public int getFarmerPlantTime() { return farmerPlantTime; }
    public void setFarmerPlantTime(int time) {
        validatePositiveValue(time, "Plant time");
        farmerPlantTime = time;
    }

    // Game mechanics getters and setters
    public double getRabbitSpawnRate() { return rabbitSpawnRate; }
    public void setRabbitSpawnRate(double rate) {
        validateSpawnRate(rate);
        rabbitSpawnRate = rate;
    }

    public int getDogDetectionRange() { return dogDetectionRange; }
    public void setDogDetectionRange(int range) {
        validatePositiveValue(range, "Detection range");
        dogDetectionRange = range;
    }
}


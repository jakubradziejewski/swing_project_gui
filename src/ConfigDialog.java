// ConfigDialog.java
import javax.swing.*;
import java.awt.*;

public class ConfigDialog extends JDialog {
    private final JSpinner spawnRateSpinner;
    private final JSpinner growthTimeSpinner;
    private final JSpinner detectionRangeSpinner;
    private final JSpinner repairTimeSpinner;
    private final JSpinner plantTimeSpinner;

    public ConfigDialog(Frame owner) {
        super(owner, "Game Configuration", true);

        GameConfig config = GameConfig.getInstance();

        // Create spinners with current values
        SpinnerNumberModel spawnModel = new SpinnerNumberModel(
                config.getRabbitSpawnRate(), 0.0, 1.0, 0.05);
        spawnRateSpinner = new JSpinner(spawnModel);

        SpinnerNumberModel growthModel = new SpinnerNumberModel(
                config.getCarrotGrowthTime(), 100, 5000, 100);
        growthTimeSpinner = new JSpinner(growthModel);

        SpinnerNumberModel rangeModel = new SpinnerNumberModel(
                config.getDogDetectionRange(), 1, 10, 1);
        detectionRangeSpinner = new JSpinner(rangeModel);

        SpinnerNumberModel repairModel = new SpinnerNumberModel(
                config.getFarmerRepairTime(), 500, 10000, 500);
        repairTimeSpinner = new JSpinner(repairModel);

        SpinnerNumberModel plantModel = new SpinnerNumberModel(
                config.getFarmerPlantTime(), 500, 10000, 500);
        plantTimeSpinner = new JSpinner(plantModel);

        // Layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        addConfigField("Rabbit Spawn Rate:", spawnRateSpinner, 0, gbc);
        addConfigField("Carrot Growth Time (ms):", growthTimeSpinner, 1, gbc);
        addConfigField("Dog Detection Range:", detectionRangeSpinner, 2, gbc);
        addConfigField("Farmer Repair Time (ms):", repairTimeSpinner, 3, gbc);
        addConfigField("Farmer Plant Time (ms):", plantTimeSpinner, 4, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            saveConfig();
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        pack();
        setLocationRelativeTo(owner);
    }

    private void addConfigField(String label, JComponent component, int row, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        add(new JLabel(label), gbc);

        gbc.gridx = 1;
        add(component, gbc);
    }

    private void saveConfig() {
        GameConfig config = GameConfig.getInstance();
        try {
            config.setRabbitSpawnRate((Double) spawnRateSpinner.getValue());
            config.setCarrotGrowthTime((Integer) growthTimeSpinner.getValue());
            config.setDogDetectionRange((Integer) detectionRangeSpinner.getValue());
            config.setFarmerRepairTime((Integer) repairTimeSpinner.getValue());
            config.setFarmerPlantTime((Integer) plantTimeSpinner.getValue());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid configuration value: " + e.getMessage(),
                    "Configuration Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}